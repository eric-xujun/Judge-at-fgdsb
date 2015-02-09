# Definition of Point
# class Point
#     attr_accessor :x, :y
#     def initialize(x = 0, y = 0)
#         @x, @y = x, y
#     end
# end

def search(i, j, origin, mat)
    n = mat.length
    @pac_ok[origin] = true if j == 0 or i == 0
    @atl_ok[origin] = true if j == n - 1 or i == n - 1
    return if @visited.length == n**2
    dirs = [[0, 1], [0, -1], [1, 0], [-1, 0]]
    dirs.each do |dir|
      newI, newJ = dir[0] + i, dir[1] + j

      next if newI < 0 or newI >= n or newJ < 0 or newJ >= n
      next if mat[newI][newJ] > mat[i][j] || @visited.has_key?([newI, newJ])

      @visited[[newI, newJ]] = true
      search(newI, newJ, origin, mat)
      @visited.delete [newI,newJ]
    end
  end

  def flowing_water(mat)
    ret = []
    @visited, @pac_ok, @atl_ok = {}, {}, {}

    (0...mat.length).each do |i|
      (0...mat.length).each do |j|
        @visited = {}
        @visited[[i,j]] = true
        search(i,j,[i,j],mat)
        ret << Point.new(i,j) if @pac_ok.has_key?([i,j]) and @atl_ok.has_key?([i,j])
      end
    end
    ret
  end