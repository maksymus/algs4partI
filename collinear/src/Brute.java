import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Brute {
    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();

        Point[] arr = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            arr[i] = new Point(x, y);
            arr[i].draw();
        }

        Arrays.sort(arr, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.compareTo(p2);
            }
        });

        List<Point> points = Arrays.asList(arr);

        for (int i = 0; i < points.size() - 3; i++) {
            Point p1 = points.get(i);

            for (int j = i + 1; j < points.size(); j++) {
                Point p2 = points.get(j);
                double slopeTo2 = p1.slopeTo(p2);

                for (int a = j + 1; a < points.size(); a++) {
                    Point p3 = points.get(a);
                    double slopeTo3 = p1.slopeTo(p3);

                    if (slopeTo2 != slopeTo3)
                        continue;

                    for (int b = a + 1; b < points.size(); b++) {
                        Point p4 = points.get(b);
                        double slopeTo4 = p1.slopeTo(p4);

                        if (slopeTo3 != slopeTo4)
                            continue;

                        printLine(p1, p2, p3, p4);
                    }
                }
            }
        }
    }

    private static void printLine(Point p1, Point p2, Point p3, Point p4) {
        p1.drawTo(p4);
        StdOut.printf("%s -> %s -> %s -> %s\n", p1.toString(), p2.toString(),
                p3.toString(), p4.toString());
    }
}
