package physics;

import java.util.Arrays;
import java.util.List;

import Jama.Matrix;

public class Newton {
    
    public static double[] solve(int iters, QuadraticFunction[] f, double[] x0) {
        QuadraticFunction[][] Df = new QuadraticFunction[f.length][x0.length];
        for (int fi = 0; fi < Df.length; fi++) {
            for (int vari = 0; vari < Df[0].length; vari++) {
                Df[fi][vari] = f[fi].partial(vari);
            }
        }
        
        double[] x = Arrays.copyOf(x0, x0.length);
        for (int i = 0; i < iters; i++) {
            Matrix Df_mat = eval(Df, x);
            Matrix f_mat = eval(f, x);
            f_mat = f_mat.times(-1);
            
            Matrix x_delta = Df_mat.solve(f_mat);
            System.out.println("x_delta = "+Arrays.deepToString(x_delta.getArray()));
            for (int j = 0; j < x.length; j++) {
                x[j] += x_delta.get(j, 0);
            }
        }
        
        return x;
    }
    
    public static Matrix eval(QuadraticFunction[][] functs, double[] vars) {
        double[][] vals = new double[functs.length][functs[0].length];
        for (int fi = 0; fi < functs.length; fi++) {
            for (int vari = 0; vari < functs[0].length; vari++) {
                vals[fi][vari] = functs[fi][vari].eval(vars);
            }
        }
        return new Matrix(vals);
    }
    
    public static Matrix eval(QuadraticFunction[] functs, double[] vars) {
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
}
