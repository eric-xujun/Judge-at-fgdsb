public class Solution {
    public boolean validUTF8(byte[] bytes) {
        int size = 0;
        for(byte b: bytes){
            if(size == 0){
                if((b >> 5) == 0x110){
                    size = 1;
                }else if((b >> 4) == 0x1110){
                    size = 2;
                }else if((b >> 3) == 0x11110){
                    size = 3;
                }else if((b >> 7) == 0x11110){
                    return false;
                }
            }else{
                if((b >> 6) != 0x10){
                    return false;
                }
                size--;
            }
        }
        return size == 0;
    }
}