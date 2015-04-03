
/**
 * Percolation implementation using union/find.
 */
public class Percolation {
    
    /** Neighbour search direction */
    private static enum Pos { LEFT, RIGHT, UP, DOWN }
    
    /** Weighted union/find */
    private WeightedQuickUnionUF uf;
    
    /** Solve backwash problem */
    private WeightedQuickUnionUF backwash;
    
    /** Opened/blocked state grid packed to bytes */
    private byte[] grid;
    
    /** Grid dimension */
    private final int n;
    
    /** Num of grid elements */
    private final int nn;  
    
    /**
     * Create N-by-N grid, with all sites blocked.
     * @param N grid dimention
     */
    public Percolation(int N) {
        if (N <= 0) 
            throw new IllegalArgumentException();
        
        n = N;
        nn = N * N;
        grid = new byte[nn / 8 + 1];
        uf = new WeightedQuickUnionUF(nn - n + 1);
        backwash = new WeightedQuickUnionUF(nn - n + 2);
    }

    /**
     * Open site (row i, column j) if it is not open already.
     * @param i row
     * @param j column
     */
    public void open(int i, int j) {
        assertBounds(i, j);
        int gridIndex = toGridIndex(i - 1, j - 1);
        
        if (isOpen(gridIndex)) 
            return;
        
        open(gridIndex);
        
        union(gridIndex, neighbour(gridIndex, Pos.LEFT));
        union(gridIndex, neighbour(gridIndex, Pos.RIGHT));
        union(gridIndex, neighbour(gridIndex, Pos.UP));
        union(gridIndex, neighbour(gridIndex, Pos.DOWN));
        
        if (isBottomRow(gridIndex)) {
            backwash.union(toUFIndex(gridIndex), nn - n + 1);
        }
    }

    /**
     * Is site (row i, column j) open?
     * @param i row
     * @param j column
     * @return true if site open
     */
    public boolean isOpen(int i, int j) {
        assertBounds(i, j);
        return isOpen(toGridIndex(i - 1, j - 1));
    }

    /**
     * Is site (row i, column j) full?
     * @param i row
     * @param j column
     * @return true if site full
     */
    public boolean isFull(int i, int j) {
        assertBounds(i, j);
        
        int index = toGridIndex(i - 1, j - 1);
        
        boolean isFull = isFull(index);
        return isFull;
    }

    /**
     * Does the system percolate?
     * @return true if perlocates
     */
    public boolean percolates() {
        return backwash.connected(0, nn - n + 1); 
    }
    
    // test client (described below)
    public static void main(String[] args) {
    }
    
    // Private methods
    // ====================================================
    private boolean isFull(int index) {
        if (!isOpen(index))
            return false;
        
        if (isTopRow(index))
            return true;
        
        int upper = neighbour(index, Pos.UP);
        if (isTopRow(upper) && isOpen(upper))
            return true;
        
        boolean isFull = (isNeighbourFull(index, Pos.LEFT)
                        || isNeighbourFull(index, Pos.RIGHT)
                        || isNeighbourFull(index, Pos.UP) 
                        || isNeighbourFull(index, Pos.DOWN));

        return isFull;
    }
    
    private void union(int gridIndex, int neighbour) {
        if (neighbour != -1 && isOpen(neighbour)) {
            int p = toUFIndex(gridIndex);
            int q = toUFIndex(neighbour);

            if (!uf.connected(p, q)) {
                uf.union(p, q);
                backwash.union(p, q);
            }
        }
    }
    
    private boolean isNeighbourFull(int index, Pos pos) {
        int neightbour = neighbour(index, pos);
        return (neightbour != -1 && isOpen(neightbour) && uf.connected(0,
                toUFIndex(neightbour)));
    }
    
    private void open(int index) {
        int seg = index / 8;
        int pos = index % 8;
        
        grid[seg] |= 1 << pos;
    }
    
    private boolean isOpen(int index) {
        int seg = index / 8;
        int pos = index % 8;
        
        return (grid[seg] & 1 << pos) > 0;
    }
    
    private void assertBounds(int i, int j) {
        if (i <= 0 || i > n || j <= 0 || j > n) 
            throw new IndexOutOfBoundsException();
    }
    
    private int toGridIndex(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n)
            return -1;
        
        return i * n + j;
    }
    
    private int neighbour(int gridIndex, Pos pos) {
        int i = gridIndex / n;
        int j = gridIndex % n;
        
        switch (pos) {
        case LEFT:
            return toGridIndex(i, j - 1);
        case RIGHT:
            return toGridIndex(i, j + 1);
        case UP:
            return toGridIndex(i - 1, j);
        case DOWN:
            return toGridIndex(i + 1, j);
        default:
            return -1;
        }
    }
    
    private int toUFIndex(int index) {
        if (index < n)
            return 0;
        
        return index - n + 1;
    }
    
    private boolean isTopRow(int index) {
        return index >= 0 && index < n;
    }
    
    private boolean isBottomRow(int index) {
        return index >= nn - n && index < nn;
    }
}
