import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PointSET {
    
    private final SET<Point2D> points = new SET<Point2D>();
    
    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        
        if (!points.contains(p))
            points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2d : points) {
            point2d.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();
        
        Set<Point2D> innerPoints = new HashSet<Point2D>();
        
        for (Point2D point2d : points)
            if (rect.contains(point2d))
                    innerPoints.add(point2d);
        
        return innerPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(final Point2D p) {
        if (p == null)
            throw new NullPointerException();
        
        ArrayList<Point2D> sortedPoints = new ArrayList<Point2D>();
        
        for (Point2D point2d : points) {
            sortedPoints.add(point2d);
        }
        
        if (sortedPoints.isEmpty())
            return null;
        
        Collections.sort(sortedPoints, new Comparator<Point2D>() {

            @Override
            public int compare(Point2D o1, Point2D o2) {
                return Double.compare(p.distanceTo(o1), p.distanceTo(o2));
            }        
        });
        
        return sortedPoints.get(0);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}