
/**
 * Calulate percolation statistics. 
 */
public class PercolationStats {
    private int numOfExperiments;
    private double[] results;
    
    /**
     * Perform T independent experiments on an N-by-N grid
     * @param N grid dimension
     * @param T number of experiments
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) 
            throw new IllegalArgumentException();
        numOfExperiments = T;
        results = new double[numOfExperiments];
        
        int nn = N * N;
        
        for (int t = 0; t < numOfExperiments; t++) {
            Percolation percolation = new Percolation(N);
            double numToPercolate = 0;
            
            while (true) {
                int rand = StdRandom.uniform(nn);
                int i = rand / N + 1, j = rand % N + 1;

                if (!percolation.isOpen(i, j)) {
                    percolation.open(i, j);
                    percolation.isFull(i, j);
                    numToPercolate++;
                    if (percolation.percolates()) 
                        break;
                }
            }
            
            results[t] = numToPercolate / (double) nn; 
        }
    }
    
    /**
     * Sample mean of percolation threshold
     * @return mean
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * Sample standard deviation of percolation threshold
     * @return std dev
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * Low endpoint of 95% confidence interval
     * @return low endpoint.
     */
    public double confidenceLo() {
        double mean = StdStats.mean(results);
        double stddev = StdStats.stddev(results);
        return mean - 1.96 * stddev / java.lang.Math.sqrt(numOfExperiments);
    }

    /**
     * High endpoint of 95% confidence interval
     * @return High endpoint 
     */
    public double confidenceHi() {
        double mean = StdStats.mean(results);
        double stddev = StdStats.stddev(results);
        return mean + 1.96 * stddev / java.lang.Math.sqrt(numOfExperiments);
    }

    // test client (described below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(200, 100);
        
        System.out.printf("mean\t\t\t\t\t= %f\n", stats.mean());
        System.out.printf("stddev\t\t\t\t\t= %f\n", stats.stddev());
        System.out.printf("95%% confidence interval\t\t= %f, %f\n", 
                stats.confidenceLo(), stats.confidenceHi()); 
    }
}
