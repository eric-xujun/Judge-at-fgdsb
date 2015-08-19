package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class Solution{
    public static int findLongestChain(String[] words){
        if(null == words || 0 == words.length){
                return 0;
        }
        //把所有的不同的字符长度都求出来，然后从小到大排好序
        HashSet<Integer> lengthSet = new HashSet<Integer>();
        HashMap<Integer, HashSet<String>> stringMap = new HashMap<Integer, HashSet<String>>();
        HashMap<String, Integer> lengthMap = new HashMap<String, Integer>();
        int max = 1;
        for(int i = 0; i < words.length; i++){
            if(null != words[i]){
                lengthSet.add(words[i].length());
                lengthMap.put(words[i], 1);
                if(!stringMap.containsKey(words[i].length())){
                    stringMap.put(words[i].length(), new HashSet<String>());
                }
                HashSet<String> stringSet = stringMap.get(words[i].length());
                stringSet.add(words[i]);
            }                        
        }
        
        int[] lengths = new int[lengthSet.size()];
        Iterator<Integer> lenIter = lengthSet.iterator();
        int j = 0;
        while(lenIter.hasNext()){
                lengths[j++] = lenIter.next();
        }
        Arrays.sort(lengths);
        
        //从长度最短的字符串开始找
        for(int i = 0; i < lengths.length; i++){
            HashSet<String> stringSet = stringMap.get(lengths[i]);
            HashSet<String> lessStringSet = stringMap.get(lengths[i] - 1);
            if(null != lessStringSet){
                Iterator<String> iter = stringSet.iterator();
                while(iter.hasNext()){
                    String str = iter.next();
                    for(int k = 0; k < str.length(); k++){
                        String newStr = str.substring(0, k) + str.substring(k + 1);
                        if(lessStringSet.contains(newStr)){
                            int maxLen = Math.max(lengthMap.get(newStr) + 1, lengthMap.get(str)); 
                            lengthMap.put(str, maxLen);
                            max = Math.max(maxLen, max);
                        }
                    }
                }
            }
        }
        //In case that all of the strings are null
        return lengthSet.size() == 0 ? 0 : max;
    }
}