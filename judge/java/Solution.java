package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class Solution{
    public boolean canWin(int[] array, int start){
        if(array == null || array.length == 0 || start < 0 || start >= array.length){
            return false;
        }
        
        Map<Integer> visited = new HashMap<Integer>();
        return helper(array, start, visited);
    }
    
    public boolean helper(int[] array, int start, Map<Integer> visited){
        if(array[start] == 0){
            true;
        }
        
        visited.put(start, false);
        boolean canWin = false;
        if(start - array[start] >= 0){
            if(visited.contains(start - array[start])){
                canWin = canWin || visited.get(start - array[start]);
            }else{
                canWin = canWin || helper(array, start - array[start], visited);
            }
        }
        
        if(start + array[start] < array.length){
            if(visited.contains(start + array[start])){
                canWin = canWin || visited.get(start + array[start]);
            }else{
                canWin = canWin || helper(array, start + array[start], visited);
            }
        }
        
        return canWin;
    }
}