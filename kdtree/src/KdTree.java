import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class KdTree {

    /**
     * Compares two points by x-coordinate.
     */
    private static final Comparator<Point2D> X_ORDER = new XOrder();

    /**
     * Compares two points by y-coordinate.
     */
    private static final Comparator<Point2D> Y_ORDER = new YOrder();
    
    private static class Node {
        private Point2D p; // the point
        private RectHV rect;
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private boolean horizontal;

        public Node(Point2D p, RectHV rect, boolean horizontal) {
            this.p = p;
            this.rect = rect;
            this.horizontal = horizontal;
        }
    }

    private Node root;
    private int count;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1), false);
        } else {
            Node parent = root;

            while (true) {
                Comparator<Point2D> comparator;
                if (parent.horizontal)
                    comparator = Y_ORDER;
                else
                    comparator = X_ORDER;

                RectHV rect = parent.rect;
                
                if (p.equals(parent.p))
                    return;

                if (comparator.compare(p, parent.p) <= 0) {
                    if (parent.lb == null) {
                        RectHV rectHV = null;

                        if (parent.horizontal)
                            rectHV = new RectHV(rect.xmin(), rect.ymin(),
                                    rect.xmax(), parent.p.y());
                        else
                            rectHV = new RectHV(rect.xmin(), rect.ymin(),
                                    parent.p.x(), rect.ymax());

                        parent.lb = new Node(p, rectHV, !parent.horizontal);
                        break;
                    } else {
                        parent = parent.lb;
                    }
                } else {
                    if (parent.rt == null) {
                        RectHV rectHV = null;

                        if (parent.horizontal)
                            rectHV = new RectHV(rect.xmin(), parent.p.y(),
                                    rect.xmax(), rect.ymax());
                        else
                            rectHV = new RectHV(parent.p.x(), rect.ymin(),
                                    rect.xmax(), rect.ymax());

                        parent.rt = new Node(p, rectHV, !parent.horizontal);
                        break;
                    } else {
                        parent = parent.rt;
                    }
                }
            }
        }

        count++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        Node node = root;

        while (node != null) {
            Comparator<Point2D> comparator;
            if (node.horizontal)
                comparator = Y_ORDER;
            else
                comparator = X_ORDER;

            if (p.equals(node.p))
                return true;

            if (comparator.compare(p, node.p) <= 0)
                node = node.lb;
            else
                node = node.rt;
        }

        return false;
    }

    public void draw() {
        Queue<Node> elements = new Queue<Node>();

        if (root != null)
            elements.enqueue(root);

        while (!elements.isEmpty()) {
            Node node = elements.dequeue();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.p.draw();

            StdDraw.setPenRadius();
            if (node.horizontal) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(),
                        node.p.y());
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(),
                        node.rect.ymax());
            }

            if (node.lb != null)
                elements.enqueue(node.lb);

            if (node.rt != null)
                elements.enqueue(node.rt);
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();

        Queue<Node> elements = new Queue<Node>();

        if (root != null)
            elements.enqueue(root);

        Set<Point2D> points = new HashSet<Point2D>();

        while (!elements.isEmpty()) {
            Node node = elements.dequeue();

            if (rect.contains(node.p))
                points.add(node.p);

            if (node.lb != null && node.lb.rect.intersects(rect))
                elements.enqueue(node.lb);

            if (node.rt != null && node.rt.rect.intersects(rect))
                elements.enqueue(node.rt);
        }

        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        Queue<Node> elements = new Queue<Node>();

        if (root != null)
            elements.enqueue(root);
        else
            return null;

        Node closest = root;

        while (!elements.isEmpty()) {
            Node node = elements.dequeue();

            if (node.p.distanceTo(p) < closest.p.distanceTo(p))
                closest = node;

            if (node.lb != null
                    && node.lb.rect.distanceTo(p) < closest.p.distanceTo(p))
                elements.enqueue(node.lb);

            if (node.rt != null
                    && node.rt.rect.distanceTo(p) < closest.p.distanceTo(p))
                elements.enqueue(node.rt);
        }

        return closest.p;
    }
    
    // compare points according to their x-coordinate
    private static class XOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            if (p.x() < q.x()) return -1;
            if (p.x() > q.x()) return +1;
            return 0;
        }
    }

    // compare points according to their y-coordinate
    private static class YOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            if (p.y() < q.y()) return -1;
            if (p.y() > q.y()) return +1;
            return 0;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
