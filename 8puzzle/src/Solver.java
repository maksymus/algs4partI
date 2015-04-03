import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Solver
 */
public class Solver {

    private final MinPQ<Node> mainQueue = new MinPQ<Node>(
            new PriorityComparator());
    private final MinPQ<Node> twinQueue = new MinPQ<Node>(
            new PriorityComparator());

    private Node solvingNode;

    /**
     * find a solution to the initial board (using the A* algorithm)
     * 
     * @param initial
     */
    public Solver(Board initial) {
        mainQueue.insert(new Node(initial, null, 0));
        twinQueue.insert(new Node(initial.twin(), null, 0));

        while (true)
            if (solve())
                break;
    }

    /**
     * is the initial board solvable?
     * 
     * @return
     */
    public boolean isSolvable() {
        return solvingNode != null;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * 
     * @return
     */
    public int moves() {
        if (solvingNode == null)
            return -1;

        return solvingNode.moves;
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * 
     * @return
     */
    public Iterable<Board> solution() {
        if (solvingNode == null)
            return null;

        Deque<Board> deque = new LinkedList<Board>();

        for (Node node = solvingNode; node != null; node = node.parent)
            deque.addFirst(node.board);

        return deque;
    }

    private boolean solve() {
        Node mainMin = mainQueue.delMin();
        Node twinMin = twinQueue.delMin();

        if (mainMin.board.isGoal()) {
            solvingNode = mainMin;
            return true;
        }

        if (twinMin.board.isGoal())
            return true;

        for (Board board : mainMin.board.neighbors()) {
            if (mainMin.parent != null && mainMin.parent.board.equals(board))
                continue;

            mainQueue.insert(new Node(board, mainMin, mainMin.moves + 1));
        }

        for (Board board : twinMin.board.neighbors()) {
            if (twinMin.parent != null && twinMin.parent.board.equals(board))
                continue;

            twinQueue.insert(new Node(board, twinMin, twinMin.moves + 1));
        }

        return false;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        System.out.println("Minimum number of moves = " + solver.moves());
        System.out.println();

        if (solver.isSolvable())
            for (Board board : solver.solution())
                System.out.println(board);
    }

    private static class Node {
        private final Board board;
        private final Node parent;
        private final int moves;

        public Node(Board board, Node parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
        }
    }

    private static class PriorityComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (o1.board.manhattan() + o1.moves)
                    - (o2.board.manhattan() + o2.moves);
        }
    }
}
