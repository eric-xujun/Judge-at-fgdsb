package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class TinyUrl{
    private Map<String, String> mShortToLong = new HashMap<String, String>();
    private Map<String, String> mLongToShort = new HashMap<String, String>();
    private ArrayList<Character> encode = new ArrayList<Character>();
    
    public TinyUrl(){
        for(char i = '0'; i < '9'; i++){
            encode.add(i);
        }
        for(char c = 'a'; c <= 'z'; c++){
            encode.add(i);
        }
        for(char c = 'A'; c <= 'Z'; c++){
            encode.add(i);
        }
    }
    
    public String shorten(String longUrl){
        if(!mLongToShort.contains(longUrl)){
            String shortUrl = generateShortUrl();
            mLongToShort.put(longUrl, shortUrl);
            mShortToLong.put(shortUrl, longUrl);
            return shortUrl;
        }else{
            return mLongToShort.get(longUrl);
        }
    }
    
    public String retrieve(String shortUrl){
        return mShortToLong.get(shortUrl);
    }
    
    private String generateShortUrl(){
        encodeToBase62(mShortToLong.size());
    }
    
    private String encodeToBase62(int i){
        StringBuilder res = new StringBuilder();
        while(i > 0){
            int index = i % 62;
            res.append(encode[index]);
            i = i / 62;
        }
        while(res.length() < 6){
            res.append('0');
        }
        return res.reverse().toString();
    }
}