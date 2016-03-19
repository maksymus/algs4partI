import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

/**
 * Board 
 */
public class Board {
    private final short size;

    private short[] blocks;

    private short hamming = -1;
    private short manhattan = -1;

    private short zeroPos;

    /**
     * construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     * @param blocks
     */
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new NullPointerException();

        this.size = (short) blocks.length;
        this.blocks = new short[size * size];

        for (int a = 0; a < size; a++) {
            for (int b = 0; b < size; b++) {
                this.blocks[a * size + b] = (short) blocks[a][b];

                if (blocks[a][b] == 0)
                    zeroPos = (short) (a * size + b);
            }
        }
    }

    /**
     * Private board
     * @param other
     */
    private Board(Board other) {
        this.size = other.size;
        this.zeroPos = other.zeroPos;
        this.blocks = Arrays.copyOf(other.blocks, other.blocks.length);
    }

    /**
     * board dimension N
     * @return
     */
    public int dimension() {
        return size;
    }

    /**
     * number of blocks out of place
     * @return
     */
    public int hamming() {
        if (this.hamming != -1)
            return this.hamming;

        short count = 1, ham = 0;

        for (int i = 0; i < size * size; i++)
            if (blocks[i] != count++ && blocks[i] != 0)
                ham++;

        this.hamming = ham;
        return ham;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     * @return
     */
    public int manhattan() {
        if (this.manhattan != -1)
            return this.manhattan;

        short manh = 0;

        for (int i = 0; i < size * size; i++) {
            int block = blocks[i];

            if (block == 0)
                continue;

            int x = (block - 1) / size;
            int y = (block - 1) % size;

            manh += Math.abs(x - i / size) + Math.abs(y - i % size);
        }

        this.manhattan = manh;
        return manh;
    }

    /**
     * is this board the goal board?
     * @return
     */
    public boolean isGoal() {
        for (int i = 0; i < size * size - 1; i++)
            if (this.blocks[i] != i + 1)
                return false;

        return true;
    }

    /**
     * a boadr that is obtained by exchanging two adjacent blocks in the same
     * row
     * @return
     */
    public Board twin() {
        Board board = new Board(this);

        short swapOffset = 0;
        if (board.blocks[0] == 0 || board.blocks[1] == 0)
            swapOffset = size;

        return board.swap(swapOffset, (short) (swapOffset + 1));
    }

    /**
     * does this board equal y?
     */
    public boolean equals(Object y) {
        if (!(y instanceof Board))
            return false;
        
        Board other = (Board) y;

        for (int i = 0; i < size * size; i++)
            if (this.blocks[i] != other.blocks[i])
                return false;

        return true;
    }

    /**
     * all neighboring boards
     * @return
     */
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();

        short row = (short) (zeroPos / size);
        short column = (short) (zeroPos % size);

        if (row - 1 >= 0)
            neighbors.push(new Board(this).swap(zeroPos, (short) ((row - 1)
                    * size + column)));

        if (row + 1 < size)
            neighbors.push(new Board(this).swap(zeroPos, (short) ((row + 1)
                    * size + column)));

        if (column - 1 >= 0)
            neighbors.push(new Board(this).swap(zeroPos, (short) (row * size
                    + column - 1)));

        if (column + 1 < size)
            neighbors.push(new Board(this).swap(zeroPos, (short) (row * size
                    + column + 1)));
        
        

        return neighbors;
    }
    
    /**
     * string representation of this board (in the output format specified
     * below)
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", blocks[i * size + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private Board swap(short i, short j) {
        if (blocks[i] == 0)
            zeroPos = j;

        if (blocks[j] == 0)
            zeroPos = i;

        short tmp = blocks[i];
        blocks[i] = blocks[j];
        blocks[j] = tmp;

        return this;
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Iterable<Board> neighbors = new Board(new int[][] { { 2, 1, 3 },
                { 4, 0, 6 }, { 7, 8, 5 }, }).neighbors();

        for (Board board : neighbors) {
            System.out.println(board);
        }
    }
}
