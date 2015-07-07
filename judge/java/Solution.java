package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class Solution extends Reader4 {
    private char[] lastReadBuf = new char[4];
    private int lastReadIndex = 0;
    
    // read4多读出来的存起来， 下次先读这段剩余的
    public int read(char[] buf, int n) {
        int readCount = 0;
        
        while(lastReadIndex < 3){
            buf[readCount++] = lastReadBuf[++lastReadIndex];
        }
        
        while(readCount < n){
            int read4Count = read4(lastReadBuf);
            lastReadIndex = 0;
            
            for(int i = 0; i < Math.min(read4Count, n - readCount); i++){
                lastReadIndex++;
                buf[readCount + i] = buf4[i];
            }
            
            readCount += Math.min(read4Count, n - readCount);
            
            if(read4Count < 4){
                break;
            }
        }
        
        return readCount;
    }
}