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

//http://codereview.stackexchange.com/questions/32827/flatten-iterator-for-nested-list
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

//http://baozitraining.org/blog/design-and-implement-a-deep-iterator/
public class DeepIterator implements Iterator {
    private Stack iteratorStack = new Stack();
    private Integer top = null;

    public DeepIterator(Iterable iterable){
        this.iteratorStack.push(iterable.iterator());
    }

    @Override
    public boolean hasNext() {
        if(this.top != null) return true;

        while(!this.iteratorStack.isEmpty()) {
            Iterator tmpIterator = this.iteratorStack.peek();

            if(tmpIterator.hasNext()){
                Object tmp = tmpIterator.next();
                if(tmp instanceof Integer){
                    this.top = (Integer) tmp;
                    return true;
                } else if(tmp instanceof Iterable){
                    this.iteratorStack.push(((Iterable) tmp).iterator());
                } else {
                    throw new RuntimeException("Unsupported data type. ");
                }
            } else {
                this.iteratorStack.pop();
            }
        }
        return false;
    }

    @Override
    public Integer next() throws NoSuchElementException {
        if(hasNext()){
            Integer tmp = this.top;
            this.top = null;
            return tmp;
        }

        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("This is not supported right now.");
    }
}