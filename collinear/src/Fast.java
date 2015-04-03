import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Fast {
    public static void main(String[] args) {
         String filename = args[0];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        In in = new In(filename);
        int N = in.readInt();

        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }

        List<List<Point>> pointList = new ArrayList<List<Point>>();
        Set<List<Point>> pointSet = new HashSet<List<Point>>();

        for (Point point : points) {
            Point[] copy = Arrays.copyOf(points, points.length);
            Arrays.sort(copy, point.SLOPE_ORDER);

            List<Point> collinear = null;
            double curSlope = 0;

            for (Point point2 : copy) {
                if (point == point2)
                    continue;

                double slope = point.slopeTo(point2);

                if (slope == curSlope) {
                    if (collinear == null)
                        collinear = new ArrayList<Point>();

                    collinear.add(point2);
                } else {
                    if (collinear != null && collinear.size() > 2) {
                        collinear.add(point);
                        pointList.add(collinear);
                    }

                    collinear = new ArrayList<Point>();
                    collinear.add(point2);
                    curSlope = slope;
                }
            }

            if (collinear != null && collinear.size() > 2) {
                collinear.add(point);
                pointList.add(collinear);
            }
        }

        for (List<Point> list : pointList) {
            Collections.sort(list);
            pointSet.add(list);
        }

        for (List<Point> list : pointSet) {
            printLine(list);
        }
    }

    private static void printLine(List<Point> points) {
        Collections.min(points).drawTo(Collections.max(points));

        for (int i = 0; i < points.size(); i++) {
            StdOut.printf("%s", points.get(i));

            if (i < points.size() - 1) {
                StdOut.print(" -> ");
            }
        }

        StdOut.println();
    }
}
