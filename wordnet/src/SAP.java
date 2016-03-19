import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {

    private Digraph wordnet;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException();

        wordnet = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateInput(v, w);

        int ancestor = ancestor(v, w);
        if (ancestor == -1)
            return -1;

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(wordnet, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(wordnet, w);

        return vPath.distTo(ancestor) + wPath.distTo(ancestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateInput(v, w);

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(wordnet, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(wordnet, w);

        int dist = -1;
        int ancestor = -1;
        for (int i = 0; i < wordnet.V(); i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int newDist = vPath.distTo(i) + wPath.distTo(i);
                if (dist == -1 || newDist < dist) {
                    dist = newDist;
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    private void validateInput(Iterable<Integer> v, Iterable<Integer> w) {
        for (int integer : v) {
            if (integer < 0 || integer > wordnet.V() - 1)
                throw new IndexOutOfBoundsException();
        }

        for (int integer : w) {
            if (integer < 0 || integer > wordnet.V() - 1)
                throw new IndexOutOfBoundsException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
