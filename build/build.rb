require 'json'
require 'net/http'
require 'uri'
require 'ruby-progressbar'
require 'rubygems'
require 'nokogiri'
require 'open-uri'
require 'semantic'
require 'semantic/core_ext'
require 'fileutils'
require 'zip/zip'
require 'rubygems/package'
require 'zlib'

# configs
@release = false
@cache_dir = 'cache'
@build_dir = 'release'
@url = 'http://dl.nwjs.io/'
# @platforms = ['osx-x64', 'win-ia32', 'linux-x64', 'linux-ia32']
@platforms = []
@menifest = '../package.json'
@app_name = 'judge_fgdsb'
@update_branch_path = '../../fgdsb_updates'
@app_version = ''

# global variables
@latest_version = ''

# params
@release = true if ARGV.include? '-b'

if ARGV.include? '-osx64'
  puts 'Build to osx64'
  @platforms = ['osx-x64']
elsif ARGV.include? '-win32'
  puts 'Build to win32'
  @platforms = ['win-ia32']
elsif ARGV.include? '-linux32'
  puts 'Build to linux32'
  @platforms = ['linux-ia32']
elsif ARGV.include? '-linux64'
  puts 'Build to linux64'
  @platforms = ['linux-x64']
else
  puts 'Build to all platforms'
  @platforms = ['osx-x64', 'win-ia32', 'linux-x64', 'linux-ia32']
end

########################################################################
# download a URL to path
def download_file(url, path)  
  url_base, url_path = url.split('/')[2], '/'+url.split('/')[3..-1].join('/')
  @counter = 0
   
  Net::HTTP.start(url_base) do |http|
    response = http.request_head(URI.escape(url_path))
    length = response['content-length'].to_i
    bar = ProgressBar.create(:format => "#{File.basename(url)} (#{length/(1024**2)}MB) |%b>%i| %p%% done")

    FileUtils::mkdir_p path
    filename = "#{path}/#{File.basename(url)}"

    File.open(filename, 'w') do |f|
      http.get(URI.escape(url_path)) do |str|
        f.write str
        @counter += str.length
        bar.progress = @counter * 100 / length        
        print bar.to_s + "\r"
        $stdout.flush
      end
      return filename
    end  
  end
end

########################################################################
# get available nw.js versions
def get_ver_list
  Nokogiri::HTML(open(@url)).css('table tr td a').select do |el|
    el.text =~ /v?([0-9]+\.[0-9]+\.[0-9]+[^"]*)/ 
  end.map {|e| e.text.slice(1, e.text.length-2).to_version}
end

########################################################################
# download the latest version if necessary
def download_new_ver
  latest = get_ver_list.max
  local_latest = Dir.glob("./#{@cache_dir}/*").select{ |e| File.directory? e }.map { |v| File.basename(v).to_version }.max  
  if !local_latest.nil? && local_latest >= latest    
    @latest_version = local_latest
    return puts 'Already latest version of nw.js, let\'s build!'    
  end
  
  @latest_version = latest.to_s
  puts 'Latest version of nw.js found: ' + latest.to_s

  # do each platform
  @platforms.each do |p|
    is_linux = p.start_with?('linux')

    # ext name
    ext = is_linux ? 'tar.gz' : 'zip'

    # download
    file = download_file "#{@url}v#{latest.to_s}/nwjs-v#{latest.to_s}-#{p}.#{ext}", "cache/#{latest.to_s}/#{p}"
    
    # extract zip file
    puts
    $stdout.flush
    puts 'Extracting zip/tar files...'

    if is_linux      
      destination = "cache/#{latest.to_s}"
      Gem::Package::TarReader.new( Zlib::GzipReader.open file ) do |tar|
        dest = nil
        tar.each do |entry|
          if entry.full_name == '././@LongLink'
            dest = File.join destination, entry.read.strip
            next
          end
          dest ||= File.join destination, entry.full_name
          if entry.directory?
            File.delete dest if File.file? dest
            FileUtils.mkdir_p dest, :mode => entry.header.mode, :verbose => false
          elsif entry.file?
            FileUtils.rm_rf dest if File.directory? dest
            File.open dest, "wb" do |f|
              f.print entry.read
            end
            FileUtils.chmod entry.header.mode, dest, :verbose => false
          elsif entry.header.typeflag == '2' #Symlink!
            File.symlink entry.header.linkname, dest
          end
          dest = nil
        end
      end
    else
      Zip::ZipFile.open(file) do |zip_file|
        zip_file.each do |f|
          f_path = File.join("cache/#{latest.to_s}", f.name)
          FileUtils.mkdir_p(File.dirname(f_path))
          zip_file.extract(f, f_path) unless File.exist?(f_path)        
        end
      end
    end

    # remove temp files
    FileUtils.rm_rf "cache/#{latest.to_s}/#{p}"

    # rename extracted folder
    FileUtils.mv "cache/#{latest.to_s}/nwjs-v#{latest.to_s}-#{p}", "cache/#{latest.to_s}/#{p}"
  end
end

########################################################################
# modify some flags for release version
def modify_package_json
  # read and parse package.json
  content = File.read(@menifest)
  pkg = JSON.parse(content)

  # set dev flag to false
  pkg['dev'] = false
  pkg['window']['toolbar'] = false
  @app_version = pkg['version']

  f = File.new(@menifest, 'w')
  f.write JSON.pretty_generate(pkg)
  f.close

  # call build block
  yield

  # set dev flag back to true
  pkg['dev'] = true
  pkg['window']['toolbar'] = true
  f = File.new(@menifest, 'w')
  f.write JSON.pretty_generate(pkg)
  f.close
end

########################################################################
# compress the whole app folder to a zip file (app.nw)
def compress_app
  def write_entries(entries, path, io)
    excludes = ['run.command']
    entries.each do |e|
      zip_file_path = path.empty? ? e : File.join(path, e)
      diskFilePath = File.join '..', zip_file_path
      if File.directory? diskFilePath        
        subdir = Dir.entries(diskFilePath).reject! {|dir| dir == '.' or dir == '..'}
        write_entries subdir, zip_file_path, io
      elsif not excludes.include? File.basename(diskFilePath)
        io.get_output_stream(zip_file_path){ |f| f.print(File.open(diskFilePath, 'rb').read) }
      end
    end
  end

  output = @build_dir + '/app.nw'
  entries = Dir.entries('..').reject! do |dir|
    dir.start_with? '.' or dir == 'build'    
  end

  print 'Zipping the app...'

  io = Zip::File.open output, Zip::File::CREATE
  write_entries entries, '', io
  io.close
  puts ' Done!'
end

########################################################################
# release to github
def release_to_github
  # copy files
  FileUtils::mkdir_p "#{@update_branch_path}/#{@app_version}"
  FileUtils::cp "#{@build_dir}/app.nw", "#{@update_branch_path}/#{@app_version}/app.nw"
  FileUtils::cp @menifest, "#{@update_branch_path}/latest/package.json"

  # commit
  puts 'Commiting...'
  `./commit.sh #{@update_branch_path} "#{@app_version}"`
end

########################################################################
# generate the target app
def generate_app
  folder = "#{@build_dir}/#{@app_name}"
  FileUtils.rm_rf folder
  FileUtils::mkdir_p folder

  @platforms.each do |p|
    FileUtils::mkdir_p folder + "/#{p}"
    if p.start_with? 'osx'
      FileUtils::copy_entry "#{@cache_dir}/#{@latest_version}/#{p}/nwjs.app", "#{folder}/#{p}/#{@app_name}.app"
      FileUtils::cp "#{@build_dir}/app.nw", "#{folder}/#{p}/#{@app_name}.app/Contents/Resources/app.nw"
    elsif p.start_with? 'win'
      FileUtils::copy_entry "#{@cache_dir}/#{@latest_version}/#{p}", "#{folder}/#{p}/#{@app_name}"
      FileUtils::cp "#{@build_dir}/app.nw", "#{folder}/#{p}/#{@app_name}/package.nw"
    elsif p.start_with? 'linux'
      FileUtils::copy_entry "#{@cache_dir}/#{@latest_version}/#{p}", "#{folder}/#{p}/#{@app_name}"
      FileUtils::cp "#{@build_dir}/app.nw", "#{folder}/#{p}/#{@app_name}/package.nw"
    end
  end
end

########################################################################
# build a release
def build
  download_new_ver
  modify_package_json do
    puts 'Building...'

    # compress the whole folder
    compress_app

    # genrate
    generate_app

    # copy file to update branch folder and push to github
    release_to_github if @release

    # delete the temp file
    FileUtils::rm_rf "#{@build_dir}/app.nw"
  end
end

build


