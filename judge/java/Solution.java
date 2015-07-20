package judge;import java.util.*;import java.lang.*;import java.io.*;import datastruct.*; public class NestedList {
 
    public static List<Object> flattenList(List<Object> inList) {
        List<Object> newList = new LinkedList<Object>();

        for (Object i : inList) {
                // If it's not a list, just add it to the return list.
                if (!(i instanceof List)) {
                        newList.add(i);
                } else {
                        // It's a list, so add each item to the return list.
                        newList.addAll(flattenList((List<Object>)i));
                }
        }

        return newList;
    }
}

public class NestedIterator<NestedListNode> implements Iterator<?>{
    
    private Stack<Iterator> stack = null;
    
    public NestedIterator(){
        if(in != null){
            stack = new Stack<Iterator>();
            stack.push(in.iterator);
        }
    }
    
    public boolean hasNext(){
        
    }
    
    public NestedListNode next(){
        
    }
}