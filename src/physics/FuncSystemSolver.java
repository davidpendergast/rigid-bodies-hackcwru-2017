package physics;

import java.util.Arrays;

import Jama.Matrix;

public class FuncSystemSolver {
    
    public static double[] solve(Func[] f, double[] x0) {
        return solve(f, x0, 200, 0.1);
    }
    
    /**
     * Returns the vector x that minimizes the magnitude of 
     * [f1(x), f2(x), ..., fn(x)]  while also minimizing |x - x0|.
     */
    public static double[] solve(Func[] f, double[] x0, int maxIters, double errThreshold) {
        double err = err(f, x0);
        
        // adding closeness to x0 constraints.
        Func[] closeToX0 = getCloseToXConstraints(x0, 0.00001);
        int n = f.length;
        Func[] f_copy = new Func[n + closeToX0.length];
        for (int i = 0; i < f_copy.length; i++) {
            if (i < n) {
                f_copy[i] = f[i];
            } else {
                f_copy[i] = closeToX0[i - n];
            }
        }
        f = f_copy;
        
        // building partial derivative matrix (jacobian).
        Func[][] Df = D(f, x0.length, 0.0001);
        
        double[] x = Arrays.copyOf(x0, x0.length);
        for (int i = 0; i < x.length; i++) {
            x[i] += (Math.random()-0.5)*0.001;
        }
        
        for (int i = 0; i < maxIters && err > errThreshold; i++) {
            Matrix Df_mat = eval(Df, x);
            Matrix f_mat = eval(f, x);
            f_mat = f_mat.times(-1);
            
            Matrix x_delta = Df_mat.solve(f_mat);
            
            for (int j = 0; j < x.length; j++) {
                x[j] += x_delta.get(j, 0);
            }
            
            err = err(f, n, x);
        }
        
        err = err(f, n, x); // for breakpointing
        
        if (err <= errThreshold) {
            return x;
        } else {
            System.out.println("Solver failed! err = "+err);
            return null;
        }
    }
    
    private static Func[] getCloseToXConstraints(final double[] x0, final double weight) {
          int n = x0.length;
          Func[] f = new Func[n];

          for (int i = 0; i < n; i++) {
              final int j = i;
              f[i] = x -> {
                          return weight*(x[j] - x0[j]);
                      };
          }
          
          return f;
    }
    
    public static Matrix eval(Func[][] functs, double[] vars) {
        double[][] vals = new double[functs.length][functs[0].length];
        for (int fi = 0; fi < functs.length; fi++) {
            for (int vari = 0; vari < functs[0].length; vari++) {
                vals[fi][vari] = functs[fi][vari].eval(vars);
            }
        }
        return new Matrix(vals);
    }
    
    public static Matrix eval(Func[] functs, double[] vars) {
        double[][] vals = new double[functs.length][1];

        for (int i = 0; i < functs.length; i++) {
            try {
                vals[i][0] = functs[i].eval(vars);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return new Matrix(vals);
    }
    
    public static double err(Func[] f, double[] x) {
        return err(f, f.length, x);
    }
    
    public static double err(Func[] f, int n, double[] x) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            Func function = f[i];
            double error = function.eval(x);
            sum += error * error;
        }
        return Math.sqrt(sum);
    } 
    
    public static Func d(Func f, int i, double h, double[] buffer) {
        return x -> {
            for (int j = 0; j < buffer.length; j++) {
                buffer[j] = x[j];
            }
            buffer[i] = x[i] + h;
            
            return (f.eval(buffer) - f.eval(x)) / h;
        };
    }
    
    public static Func[][] D(Func[] f, int n, double h) {
        double[] buffer = new double[n];
        Func[][] Df = new Func[f.length][n];
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < n; j++) {
                Df[i][j] = d(f[i], j, h, buffer);
            }
        }
        return Df;
    }
    
}
