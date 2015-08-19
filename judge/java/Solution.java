package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class N_Queen {
        static int[] nx = {1, 1};
        static int[] ny = {1, -1};
        
        //只有当前扫描的位置上有queen的时候才返回，否则一旦越界了，就返回-1，代表这个queen不受任何威胁
        public static int findThreatLine(int[] pos, int x, int y, int mov1, int mov2){
                x += mov1;
                y += mov2;
                while(y >= 0 && x < pos.length){
                        if(pos[x] == y){
                                return x;
                        }
                        x += mov1;
                        y += mov2;
                }
                return -1;
        }
        
        public static int findMaxThreat(int[] pos){
                if(null == pos || 0 == pos.length){
                        return 0;
                }
                //由于每一行每一列都只有一个queen，所以这里的map里面，只用行号来代表当前行上的那个queen了
                HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
                int max = 0;
                for(int i = 0; i < pos.length; i++){
                        map.put(i, 0);
                }
                for(int i = 0; i < pos.length; i++){
                        int threatLine1 = findThreatLine(pos, i, pos[i], nx[0], ny[0]);
                        int threatLine2 = findThreatLine(pos, i, pos[i], nx[1], ny[1]);
                        if(threatLine1 != -1){
                                map.put(i, map.get(i) + 1);
                                map.put(threatLine1, map.get(threatLine1) + 1);
                        }
                        if(threatLine2 != -1){
                                map.put(i, map.get(i) + 1);
                                map.put(threatLine2, map.get(threatLine2) + 1);
                        }
                        max = Math.max(max, map.get(i));
                }
                return max;
        }

}