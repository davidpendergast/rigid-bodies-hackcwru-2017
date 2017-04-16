package physics;

import java.util.Arrays;

import org.junit.Test;

public class TestNewton {
    
    @Test
    public void testSolve() {
//        QuadraticFunction[] f1 = {new QuadraticFunction(1)};
//        f1[0].set(0, 0, 1);
//        f1[0].set(-4);
//        System.out.println(f1[0]);
//        
//        double[] x0 = new double[] {-3.0};
//        double[] x = Newton.solve(1000, f1, x0);
//        
//        System.out.println(Arrays.toString(x));
    }
    
    @Test
    public void testDerive() {
        QuadraticFunction f1 = new QuadraticFunction(4);
        f1.set(5);
        f1.set(1, 3);
        f1.set(3, 2);
        f1.set(2, 9);
        f1.set(1, 2, 7);
        f1.set(2, 2, 1);
        System.out.println("f="+f1);
        System.out.println("partial="+f1.partial(1));
        
    }

}
