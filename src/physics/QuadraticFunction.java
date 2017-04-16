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
        if (j > i) {
            int temp = i;
            i = j;
            j = temp;
        }
        quadTerms[i][j] = val;
    }
    
    private double get(int i, int j) {
        if (j > i) {
            int temp = i;
            i = j;
            j = temp;
        }
        
        return quadTerms[i][j];
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
        for (int j = 0; j < n ; j++) {
            f.set(j, get(i, j));
        }
        
        f.set(i, quadTerms[i][i]*2);
        
        return f;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (quadTerms[i][i] != 0) {
                if (sb.length() > 0) {
                    sb.append(" + ");
                }
                sb.append(String.format("%.1f", quadTerms[i][i])+"x_"+i+"^2");
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (quadTerms[i][i] != 0) {
                    if (sb.length() > 0) {
                        sb.append(" + ");
                    }
                    sb.append(String.format("%.1f", quadTerms[i][j])+"x_"+i+"x_"+j);
                }
            }
        }
        
        for (int i = 0; i < n; i++) {
            if (linearTerms[i] != 0) {
                if (sb.length() > 0) {
                    sb.append(" + ");
                }
                sb.append(String.format("%.1f", linearTerms[i])+"x_"+i);
            }
        }
        
        if (constantTerm != 0) {
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append(String.format("%.1f", constantTerm));
        }
        
        if (sb.length() == 0) {
            sb.append("0");
        }
        
        return sb.toString();
    }
    
    public static void main(String[] args) {
        QuadraticFunction f = new QuadraticFunction(8);
//        f.set(45);
//        f.set(4, 1);
        f.set(0,0, 5);
        System.out.println(f.eval(new double[]{1,2,3,4,5,6,7,8}));
    }
}
