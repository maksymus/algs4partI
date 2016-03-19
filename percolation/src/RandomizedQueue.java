import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Randomized queue.
 * @param <Item> generic parameter.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    
    /**
     * Randomized iterator implementation. 
     */
    private final class IteratorImplementation implements Iterator<Item> {
        private int index;
        private int[] arr = new int[size];
        {
            shuffle();
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Item next() {
            if (index == size)
                throw new NoSuchElementException();
            return items[arr[index++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void shuffle() {
            for (int i = 0; i < size; i++)
                arr[i] = i;

            for (int i = 0; i < size; i++)
                swap(i, StdRandom.uniform(i + 1));
        }

        private void swap(int i, int r) {
            int tmp = arr[r];
            arr[r] = arr[i];
            arr[i] = tmp;
        }
    }

    private int size;
    private Item[] items;
    
    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    /**
     * is the queue empty?
     * @return true if empty
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * return the nu mber of items on the queue
     * @return number of elements
     */
    public int size() {
        return size;
    }

    /**
     * add the item
     * @param item
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException();
        
        if (size == items.length)
            resize(2 * items.length);
        
        items[size++] = item; 
    }

    /**
     * remove and return a random item
     * @return removed item
     */
    public Item dequeue() {
        if (size <= 0)
            throw new NoSuchElementException();
        
        int r = StdRandom.uniform(size);

        Item item = items[r];
        items[r] = items[--size];
        items[size] = null;
        
        if (size > 0 && size == items.length / 4) {
            resize(items.length / 2);
        }
        
        return item;
    }

    /**
     * return (but do not remove) a random item
     * @return random item
     */
    public Item sample() {
        if (size <= 0)
            throw new NoSuchElementException();
        
        return items[StdRandom.uniform(size)];
    }

    /**
     * return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new IteratorImplementation();
    }

    private void resize(int capacity) {
        Item[] dest = (Item[]) new Object[capacity];
        
        for (int i = 0; i < size; i++) 
            dest[i] = items[i];
        
        items = dest;
    }
    
    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7);
        queue.enqueue(8);
        queue.enqueue(9);
        queue.enqueue(10);
        
//        for (Integer integer : queue) {
//            System.out.println(integer);
//        }
    }
}