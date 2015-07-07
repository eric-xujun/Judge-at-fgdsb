package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class Solution extends Reader4 {
    /**
     * @param buf Destination buffer
     * @param n   Maximum number of characters to read
     * @return    The number of characters read
     */
    public int read(char[] buf, int n) {
        int readCount = 0;
        char[] buf4 = new char[4];
        
        int count = 0;
        while(count < n){
            int read4Count = read4(buf4);
            
            for(int i = 0; i < Math.min(read4Count, n - readCount); i++){
                buf[readCount + i] = buf4[i];
            }
            
            readCount += Math.min(read4Count, n - readCount);
            
            if(read4Count < 4){
                break;
            }
        }
        
        return count;
    }
}