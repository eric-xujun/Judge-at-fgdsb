package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class solution{
    private Map<String, TreeMap<Double, String>> table = new HashMap<String, TreeMap<Double, String>>();
    
    public void put(double t, String k, String v) {
        if (!table.containsKey(k))
            table.put(k, new TreeMap<Double, String>());
        TreeMap<Double, String> bst = table.get(k);
        bst.put((t, v);
    }
   
    public String get(double t, String k) {
        Double key = getKey(t, k);
        if (key == null)
            return null;
        else
            return table.get(k).get(key);
    }

    public void remove(double t, String k) {
        Double key = getKey(t, k);
        if (key != null)
            table.get(k).remove(key);
    }
   
    private Double getKey(double t, String k) {
        if (!table.containsKey(k))
            return null;
        if(table.get(k).floorKey(t) != null && table.get(k).ceilingKey(t) != null){
            return table.get(k).floorKey(t) - t > t - table.get(k).ceilingKey(t) ? table.get(k).ceilingKey(t) : table.get(k).floorKey(t);
        }else if(table.get(k).floorKey(t) != null){
            return table.get(k).floorKey(t);
        }else if(table.get(k).ceilingKey(t) != null){
            return table.get(k).ceilingKey(t);
        }else{
            return null;
        }
    }
}
