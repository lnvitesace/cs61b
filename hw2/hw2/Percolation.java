package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] gird;
    private final int N;
    private int numberOfOpenSites;
    private  WeightedQuickUnionUF dsWithVirtualBottomSite;
    private  WeightedQuickUnionUF dsWithoutVirtualBottomSite;
    private final int virtualTopSite;
    private final int virtualBottomSite;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must greater than 0");
        }

        gird = new boolean[N][N];
        this.N = N;
        numberOfOpenSites = 0;

        // allocate two more spaces for virtualTopSite and virtualBottomSite
        dsWithVirtualBottomSite = new WeightedQuickUnionUF(N * N + 2);
        dsWithoutVirtualBottomSite = new WeightedQuickUnionUF(N * N + 1);

        virtualTopSite = N * N;
        virtualBottomSite = N * N + 1;
    }

    private int xyTo1D(int x, int y) {
        return x * N + y;
    }

    private void check(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException("row and col must between 0 and N-1");
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        check(row, col);
        if (isOpen(row, col)) {
            return;
        }

        // if the site is at the top or the bottom, connect it with virtual site
        if (row == 0) {
            dsWithVirtualBottomSite.union(xyTo1D(row, col), virtualTopSite);
            dsWithoutVirtualBottomSite.union(xyTo1D(row, col), virtualTopSite);
        }
        if (row == N - 1) {
            dsWithVirtualBottomSite.union(xyTo1D(row, col), virtualBottomSite);
        }

        gird[row][col] = true;
        numberOfOpenSites++;

        // if there is any surrounded site opened, union it with this site
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            dsWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            dsWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if (row + 1 < N && isOpen(row + 1, col)) {
            dsWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            dsWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            dsWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            dsWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
        if (col + 1 < N && isOpen(row, col + 1)) {
            dsWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            dsWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check(row, col);
        return gird[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        check(row, col);
        return isOpen(row, col) && dsWithoutVirtualBottomSite.connected(
                xyTo1D(row, col), virtualTopSite);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return dsWithVirtualBottomSite.connected(virtualTopSite, virtualBottomSite);
    }

    public static void main(String[] args) {
        // I prefer to use JUnit to test
    }
}
