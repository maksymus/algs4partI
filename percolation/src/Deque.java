import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Dequeue implementation 
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {
    
    /**
     * Dequeue iterator 
     */
    private final class IteratorImplementation implements Iterator<Item> {
        private Node<Item> cur = first;

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Item next() {
            if (cur == null) 
                throw new NoSuchElementException();
            
            Item data = cur.data;
            cur = cur.next;
            
            return data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Dequeue node.  
     * @param <Item> node item.
     */
    private static class Node<Item> {
        private Item data;
        private Node<Item> prev;
        private Node<Item> next;

        public Node(Item data, Node<Item> prev, Node<Item> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
            
            if (next != null)
                next.prev = this;
            
            if (prev != null)
                prev.next = this;
        }
    }
    
    /** number of elements */
    private int size;
    
    /** first node */
    private Node<Item> first;
    
    /** last node */
    private Node<Item> last;
    
    /**
     * construct an empty deque
     */
    public Deque() {
    }

    /**
     * is the deque empty?
     * @return true if empty
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * return the number of items on the deque
     * @return number of elements
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front
     * @param item
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException();
        
        if (size == 0) { 
            addToEmpty(item);
        } else {
            Node<Item> node = new Node<Item>(item, null, first);
            first = node;
            size++;
        }
    }

    /**
     * add the item to the end
     * @param item
     */
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException();

        if (size == 0) {
            addToEmpty(item);
        } else {
            Node<Item> node = new Node<Item>(item, last, null);
            last = node;
            size++;
        }
    }

    /**
     * remove and return the item from the front 
     * @return remmoved element
     */
    public Item removeFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        
        Item data = first.data;
        
        if (first.next != null)
            first.next.prev = null;
        else
            last = null;
        
        first = first.next;
        size--;
        
        return data;
    }

    /**
     * remove and return the item from the end
     * @return removed element
     */
    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException();
        
        Item data = last.data;

        if (last.prev != null)
            last.prev.next = null;
        else
            first = null;
        
        last = last.prev;
        size--;
        
        return data;
    }

    /**
     * return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator() {
        return new IteratorImplementation();
    }
    
    private void addToEmpty(Item data) {
        Node<Item> node = new Node<Item>(data, null, null);
        first = node; 
        last = node;
        size++;
    }

    // unit testing
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<Integer>();

        for (int i = 0; i < 1000000; i++) {
            dq.addFirst(i);
            
        }

        for (int i = 0; i < 1000000 - 1; i++) {
            dq.removeLast();
        }
        
        System.out.println("size  = " + dq.size());
        
//        for (Integer integer : dq) {
//            System.out.println(integer);
//        }
    }
}
