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
        // connect every top site with virtualTopSite, every bottom site with virtualBottomSite
        for (int i = 0; i < N; i++) {
            dsWithVirtualBottomSite.union(i, virtualTopSite);
            dsWithoutVirtualBottomSite.union(i, virtualTopSite);
        }
        for (int i = 0; i < N; i++) {
            dsWithVirtualBottomSite.union(xyTo1D(N - 1, i), virtualBottomSite);
        }
    }

    private int xyTo1D(int x, int y) {
        return x * N + y;
    }


    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException("row and col must between 0 and N-1");
        }
        if (isOpen(row, col)) {
            return;
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
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException("row and col must between 0 and N-1");
        }
        return gird[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw new IndexOutOfBoundsException("row and col must between 0 and N-1");
        }
        return isOpen(row, col) && dsWithoutVirtualBottomSite.connected(xyTo1D(row, col), virtualTopSite);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return dsWithVirtualBottomSite.connected(virtualTopSite, virtualBottomSite);
    }
}
