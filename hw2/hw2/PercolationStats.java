package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private Percolation p;
    private int[] results;
    private int T;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must greater than 0");
        }

        p = pf.make(N);
        results = new int[T];
        this.T = T;

        for (int i = 0; i < T; i++) {
            while (!p.percolates()) {
                int randomRow = StdRandom.uniform(N);
                int randomColumn = StdRandom.uniform(N);
                p.open(randomRow, randomColumn);
            }
            results[i] = p.numberOfOpenSites();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev()) / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev()) / Math.sqrt(T);
    }

}
