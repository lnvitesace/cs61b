import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    private static class SearchNode implements Comparable<SearchNode> {
        private final double energy;
        private final SearchNode prev;
        private final int x;

        public SearchNode(double energy, SearchNode prev, int x) {
            this.energy = energy;
            this.prev = prev;
            this.x = x;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Double.compare(this.energy, that.energy);
        }
    }

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        computeEnergy();
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        validateX(x);
        validateY(y);

        Color xPlus1 = picture.get(Math.floorMod(x + 1, width()), y);
        Color xMinus1 = picture.get(Math.floorMod(x - 1, width()), y);
        Color yPlus1 = picture.get(x, Math.floorMod(y + 1, height()));
        Color yMinus1 = picture.get(x, Math.floorMod(y - 1, height()));

        double rx = xPlus1.getRed() - xMinus1.getRed();
        double gx = xPlus1.getGreen() - xMinus1.getGreen();
        double bx = xPlus1.getBlue() - xMinus1.getBlue();
        double xGradient = rx * rx + gx * gx + bx * bx;

        double ry = yPlus1.getRed() - yMinus1.getRed();
        double gy = yPlus1.getGreen() - yMinus1.getGreen();
        double by = yPlus1.getBlue() - yMinus1.getBlue();
        double yGradient = ry * ry + gy * gy + by * by;

        return xGradient + yGradient;
    }

    public int[] findVerticalSeam() {
        SearchNode[] minPath = new SearchNode[width()];
        for (int i = 0; i < width(); i++) {
            minPath[i] = new SearchNode(energy[i][0],  null, i);
        }

        for (int y = 1; y < height(); y++) {
            SearchNode[] helper = new SearchNode[width()];
            for (int x = 0; x < width(); x++) {
                int xMinus1 = Math.max(x - 1, 0);
                int xPlus1 = Math.min(x + 1, width() - 1);
                SearchNode min = min(minPath[xMinus1], minPath[x], minPath[xPlus1]);
                helper[x] = new SearchNode(energy[x][y] + min.energy, min, x);
            }
            minPath = helper;
        }

        SearchNode smallest = findSmallest(minPath);
        int[] verticalSeam = new int[height()];
        int N = height();
        while (smallest != null) {
            verticalSeam[--N] = smallest.x;
            smallest = smallest.prev;
        }
        return verticalSeam;
    }

    public int[] findHorizontalSeam() {
        Picture originalPicture = picture();
        double[][] originalEnergy = energy.clone();

        transposeImage();
        int[] horizontalSeam = findVerticalSeam();
        for (int i = 0; i < height(); i++) {
            horizontalSeam[i] = width() - horizontalSeam[i] - 1;
        }

        picture = originalPicture;
        energy = originalEnergy;
        return horizontalSeam;
    }

    public void removeHorizontalSeam(int[] seam) {
        validateHorizontalSeam(seam);
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
        computeEnergy();
    }

    public void removeVerticalSeam(int[] seam) {
        validateVerticalSeam(seam);
        picture = SeamRemover.removeVerticalSeam(picture, seam);
        computeEnergy();
    }

    private void computeEnergy() {
        energy = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energy[i][j] = energy(i, j);
            }
        }
    }

    private SearchNode min(SearchNode a, SearchNode b, SearchNode c) {
        SearchNode smallest = a;
        if (smallest.compareTo(b) > 0) {
            smallest = b;
        }
        if (smallest.compareTo(c) > 0) {
            smallest = c;
        }
        return smallest;
    }

    private SearchNode findSmallest(SearchNode[] minPath) {
        SearchNode smallest = minPath[0];
        for (SearchNode node : minPath) {
            if (node.compareTo(smallest) < 0) {
                smallest = node;
            }
        }
        return smallest;
    }

    private void transposeImage() {
        int width = height();
        int height = width();
        Picture transposedPicture = new Picture(width, height);
        double[][] transposedEnergy = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                transposedPicture.set(x, y, picture.get(y, width - x - 1));
                transposedEnergy[x][y] = energy[y][width - x - 1];
            }
        }
        picture = transposedPicture;
        energy = transposedEnergy;
    }

    private void validateX(int x) {
        if (x < 0 || x >= width()) {
            throw new IndexOutOfBoundsException("x should between 0 and width - 1");
        }
    }

    private void validateY(int y) {
        if (y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("y should between 0 and height - 1");
        }
    }

    private void validateHorizontalSeam(int[] seam) {
        if (seam.length != width()) {
            throw new IllegalArgumentException("seam length should be equal to width");
        }
        for (int x = 0; x < width() - 1; x++) {
            int diff = seam[x] - seam[x + 1];
            if (diff < -1 || diff > 1) {
                throw new IllegalArgumentException("invalid seam");
            }
        }
    }

    private void validateVerticalSeam(int[] seam) {
        if (seam.length != height()) {
            throw new IllegalArgumentException("seam length should be equal to height");
        }
        for (int x = 0; x < height() - 1; x++) {
            int diff = seam[x] - seam[x + 1];
            if (diff < -1 || diff > 1) {
                throw new IllegalArgumentException("invalid seam");
            }
        }
    }
}
