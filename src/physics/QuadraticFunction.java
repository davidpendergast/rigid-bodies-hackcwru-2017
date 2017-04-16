package physics;

public class QuadraticFunction {
    public int n;
    public double[][] quadTerms;
    public double constantTerm = 0;
    public double[] linearTerms;
    
    public QuadraticFunction(int n) {
        this.n = n;
        linearTerms = new double[n];
        quadTerms = new double[n][n];
    }
    
    public void set(double val) {
        constantTerm = val;
    }
    
    public void set(int i, double val) {
        linearTerms[i] = val;
    }
    
    /**
     * bigger one first
     */
    public void set(int i, int j, double val) {
        checkIJ(i, j);
        quadTerms[i][j] = val;
    }
    
    private void checkIJ(int i, int j) {
        if (j > i) {
            throw new IllegalArgumentException("j > i");
        }
    }
    
    public double eval(double[] variables) {
        double sum = constantTerm;
        
        for (int i = 0; i < n; i++) {
            sum += linearTerms[i]*variables[i];
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                sum += quadTerms[i][j]*variables[i]*variables[j];
            }
        }
        
        return sum;
    }
    
    public QuadraticFunction partial(int i) {
        QuadraticFunction f = new QuadraticFunction(n);
        f.set(linearTerms[i]);
        for (int j = 0; j <= i; j++) {
            f.set(j, quadTerms[i][j]);
        }
        for (int j = i+1; j < n; j++) {
            f.set(j, quadTerms[j][i]);
        }
        f.set(i,i, quadTerms[i][i]*2);
        
        return f;
    }
}
