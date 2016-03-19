import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.*;

public class SeamCarver {
    private static final int MAX_ENERGY = 1000;

    private int[] pictureBuf;
    private double[] energyBuf;
    private int height;
    private int width;
    private boolean vertical = true;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new NullPointerException();

        this.width = picture.width();
        this.height = picture.height();

        this.pictureBuf = new int[picture.width() * picture.height()];
        this.energyBuf = new double[picture.width() * picture.height()];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.pictureBuf[j * width + i] = picture.get(i, j).getRGB();
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.energyBuf[j * width + i] = energy(i, j);
            }
        }
    }

    public int height() {
        if (vertical)
            return height;
        else
            return width;
    }

    public int width() {
        if (vertical)
            return width;
        else
            return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            throw new IndexOutOfBoundsException(String.format(
                    "x = %d, y = %d, width = %d, height = %d", x, y, width, height));

        if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
            return MAX_ENERGY;

        int leftColor = pictureBuf[y * width + (x - 1)];
        int rightColor = pictureBuf[y * width + (x + 1)];

        int topColor = pictureBuf[(y - 1) * width + x];
        int bottomColor = pictureBuf[(y + 1) * width + x];

        int deltax = getDelta(leftColor, rightColor);
        int deltay = getDelta(topColor, bottomColor);

        return Math.sqrt(deltax + deltay);
    }

    public int[] findVerticalSeam() {
        if (!vertical) transpose();
        return findSeam();
    }

    public int[] findHorizontalSeam() {
        if (vertical) transpose();
        return findSeam();
    }

    public void removeHorizontalSeam(int[] horizontalSeam) {
        if (vertical) transpose();
        removeSeam(horizontalSeam);
    }

    public void removeVerticalSeam(int[] verticalSeam) {
        if (!vertical) transpose();
        removeSeam(verticalSeam);
    }

    public Picture picture() {
        if (!vertical) transpose();

        Picture pic = new Picture(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pic.set(i, j, new Color(pictureBuf[j * width + i]));
            }
        }

        return pic;
    }

    // Private ========================================================================================================
    private int getDelta(int color1, int color2) {
        int red = getRed(color1) - getRed(color2);
        int green = getGreen(color1) - getGreen(color2);
        int blue = getBlue(color1) - getBlue(color2);

        return red * red + green * green + blue * blue;
    }

    private int getRed(int color) {
        return color >> 16 & 255;
    }

    private int getGreen(int color) {
        return color >> 8 & 255;
    }

    private int getBlue(int color) {
        return color >> 0 & 255;
    }

    private int[] findSeam() {
        if (width == 1) return new int[height];

        double[] distTo = new double[width * height];
        int[] edgeTo = new int[width * height];

        for (int v = 0; v < width * height; v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        for (int i = 0; i < width; i++)
            distTo[i] = 0.0;

        shortestPath(distTo, edgeTo);

        int shortest = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < width; i++) {
            int v = i + (height - 1) * width;
            double dist = distTo[v];
            if (dist < minDist) {
                minDist = dist;
                shortest = v;
            }
        }

        int[] path = new int[height];
        int cnt = height - 1;
        while (distTo[shortest] != 0) {
            path[cnt--] = shortest % width;
            shortest = edgeTo[shortest];
        }

        if (height > 1)
            path[0] = path[1] - 1;

        return path;
    }

    private void removeSeam(int[] seam) {
        if (seam == null)
            throw new NullPointerException();

        if (width <= 1 || seam.length != height)
            throw new IllegalArgumentException();

        int[] sequence = new int[seam.length];

        int previous = seam[0];
        for (int i = 0; i < seam.length; i++) {
            int current = seam[i];
            if (current < 0 || current >= width || Math.abs(previous - current) > 1) {
                throw new IllegalArgumentException();
            }
            previous = current;
            sequence[i] = i * width + current;
        }
        sequence = removeSeam1(sequence);
        updateEnergy(sequence);
    }

    private int[] removeSeam1(int[] seam) {
        int[] newPath = new int[seam.length];

        for (int i = 0, lag = 1; i < seam.length; i++, lag++) {
            int pos = seam[i] + 1;
            int length = 0;

            if (i < seam.length - 1) {
                length = seam[i + 1] - seam[i] - 1;
            } else {
                length = width * height - seam[i] - 1;
            }

            System.arraycopy(energyBuf, pos, energyBuf, pos - lag, length);
            System.arraycopy(pictureBuf, pos, pictureBuf, pos - lag, length);

            newPath[i] = pos - lag;
        }

        width--;

        return newPath;
    }

//    private void updateEnergy1(int[] seam) {
//        System.out.println(Arrays.toString(seam));
//        for (int i = 0; i < seam.length; i++) {
//            int x = seam[i] % width;
//            int y = seam[i] / width;
//
//
//        }
//    }

    private int[] getAdjacencies(int i) {
        // bottom
        if (i / width == height - 1)
            return new int[]{};

        int directAdj = (i / width + 1) * width + i % width;
        int rightAdj = directAdj + 1;
        int leftAdj = directAdj - 1;

        if (width == 1)
            return new int[] { directAdj };

        // left border
        if (i % width == 0)
            return new int[]{ directAdj, rightAdj };

        // right border
        if (i % width == width - 1)
            return new int[] { leftAdj, directAdj };

        return new int[] { leftAdj, directAdj, rightAdj };
    }
//
//    private int[] getVerticalAdjacencies(int i) {
//        // bottom
//        if (i / width == height - 1)
//            return new int[]{};
//
//        int directAdj = (i / width + 1) * width + i % width;
//        int rightAdj = directAdj + 1;
//        int leftAdj = directAdj - 1;
//
//        if (width == 1)
//            return new int[] { directAdj };
//
//        // left border
//        if (i % width == 0)
//            return new int[]{ directAdj, rightAdj };
//
//        // right border
//        if (i % width == width - 1)
//            return new int[] { leftAdj, directAdj };
//
//        return new int[] { leftAdj, directAdj, rightAdj };
//    }
//
//    private int[] getHorizontalAdjacencies(int i) {
//        if (i % width == width - 1)
//            return new int[]{};
//
//        int directAdj = i + 1;
//        int topAdj = i + 1 - width;
//        int bottomAdj = i + 1 + width;
//
//        if (height == 1)
//            return new int[] { directAdj };
//
//        if (i / width == 0)
//            return new int[]{ directAdj, bottomAdj };
//
//        if (i / width == height - 1)
//            return new int[]{ topAdj, directAdj };
//
//        return new int[]{ topAdj, directAdj, bottomAdj };
//    }

    private Iterable<Integer> topological() {
        Stack<Integer> order = new Stack<Integer>();
        boolean[] marked = new boolean[width * height];
        for (int v = 0; v < width * height; v++) {
            if (!marked[v])
                dfs(v, marked, order);
        }

        return order;
    }

    private void dfs(int v, boolean[] marked, Stack<Integer> order) {
        marked[v] = true;
        for (int adj : getAdjacencies(v)) {
            if (!marked[adj]) {
                dfs(adj, marked, order);
            }
        }
        order.push(v);
    }

    private void shortestPath(double[] distTo, int[] edgeTo) {
        for (int v : topological()) {
            for (int adj : getAdjacencies(v)) {
                relax(v, adj, distTo, edgeTo);
            }
        }
    }

    private void relax(int v, int w, double[] distTo, int[] edgeTo) {
        if (distTo[w] > distTo[v] + energyBuf[w]) {
            distTo[w] = distTo[v] + energyBuf[w];
            edgeTo[w] = v;
        }
    }

    private void transpose() {
        int[] pictureBufTmp = new int[pictureBuf.length];
        double[] energyBufTmp = new double[energyBuf.length];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                pictureBufTmp[i * height + j] = pictureBuf[j * width + i];
                energyBufTmp[i * height + j] = energyBuf[j * width + i];
            }
        }

        pictureBuf = pictureBufTmp;
        energyBuf = energyBufTmp;

        vertical = !vertical;

        int tmp = width;
        width = height;
        height = tmp;
    }

    private void updateEnergy(int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            int x = seam[i] % width;
            int y = seam[i] / width;

            if (y < height) {
                energyBuf[seam[i]] = energy(x, y);
                if (x > 0)
                    energyBuf[seam[i] - 1] = energy(x - 1, y);
            }
        }
    }

    public static void main(String[] args) {
//        Picture picture = new Picture("/home/maksym/Projects/home/algs4partI/seam-carving/resources/12x10.png");
//        SeamCarver sc = new SeamCarver(picture);
//
//        System.out.println(Arrays.toString(sc.findVerticalSeam()));
//        System.out.println(Arrays.toString(sc.findHorizontalSeam()));
//        System.out.println(Arrays.toString(sc.findVerticalSeam()));
//        System.out.println(Arrays.toString(sc.findHorizontalSeam()));


//        Arrays.equals()
//        Picture picture = new Picture("/home/maksym/Projects/home/algs4partI/seam-carving/resources/6x5.png");
//        SeamCarver sc = new SeamCarver(picture);
//
//        sc.removeVerticalSeam(sc.findVerticalSeam());
//
//        for (int j = 0; j < sc.height(); j++) {
//            for (int i = 0; i < sc.width(); i++)
//                StdOut.printf("%9.2f ", sc.energyBuf[sc.width * j + i]);
//            StdOut.println();
//        }
    }
}
