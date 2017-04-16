package physics;

import java.util.Arrays;

public class VecMath {
    
    public static final double EPS = 0.0000001;
    
    public static double dist(Vector2d v, Edge e) {
        if (!inBoundingBox(v, e, 2)) {
            return Math.min(dist(v, e.p1), dist(v, e.p2));
        } else {
            double x1 = e.p1.x;
            double x2 = e.p2.x;
            double y1 = e.p1.y;
            double y2 = e.p2.y;
            
            double num = Math.abs((y2 - y1)*v.x - (x2 - x1)*v.y + x2*y1 - y2*x1);
            double denom = Math.sqrt((y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1));
            
            if (denom < EPS) {
                return dist(v, e.p1);
            } else {
                return num / denom;
            }
        }
    }
    
    public static double dist(Vector2d v1, Vector2d v2) {
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public static boolean inBoundingBox(Vector2d v, Edge e, double icing) {
        return !(v.x < e.minX() - icing || v.x > e.maxX()  + icing
                || v.y < e.minY() - icing || v.y > e.maxY() + icing);
    }
    
    public static Vector2d midpoint(Vector2d p1, Vector2d p2) {
        return new Vector2d((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }
    
    public static Vector2d extend(Vector2d p1, Vector2d p2, double length) {
        Vector2d dir = sub(p2, p1);
        return add(p1, withLength(dir, length));
        
    }
    
    /**
     * 
     * @param f1 1st fixed point
     * @param f2 2nd fixed point
     * @param l12 length of edge between 1st and 2nd point
     * @param l23 length of edge between 2nd and 3rd point
     * @param l13 length of edge between 1st and 3rd point
     * @return position of 3rd point
     */
    public static Vector2d[] thirdPoint(Vector2d f1, Vector2d f2, 
            double r1, double r2) {

        double x1 = f1.x;
        double x2 = f2.x;
        double y1 = f1.y;
        double y2 = f2.y;

        double d = dist(f1, f2);
        if (d < EPS) {
            return null;
        }
        
        double a=(r1*r1 - r2*r2 + d*d)/(2*d);
        double h = Math.sqrt(r1*r1-a*a);
        double xt = x1 + a*(x2-x1)/d;   
        double yt = y1 + a*(y2-y1)/d;   
        
        double[] x3 = {xt+h*(y2-y1)/d, xt-h*(y2-y1)/d};
        double[] y3 = {yt-h*(x2-x1)/d, yt+h*(x2-x1)/d};
        
        return new Vector2d[] {new Vector2d(x3[0], y3[0]), new Vector2d(x3[1], y3[1])};
    }
    
    public static double[] solveQuadratic(double a, double b, double c) {
        double disc = b*b - 4*a*c;
        if (disc < 0 || Math.abs(a) < EPS) {
            return null;
        } else {
            double[] ans = new double[2];
            ans[0] = -b/a + Math.sqrt(disc)/a;
            ans[1] = -b/a - Math.sqrt(disc)/a;
            return ans;
        }
    }
    
    /**
     * returns p1 - p2
     */
    public static Vector2d sub(Vector2d p1, Vector2d p2) {
        return new Vector2d(p1.x - p2.x, p1.y - p2.y);
    }
    
    public static Vector2d add(Vector2d p1, Vector2d p2) {
        return new Vector2d(p2.x + p1.x, p2.y + p1.y);
    }
    
    public static Vector2d withLength(Vector2d v, double length) {
        double currLength = mag(v);
        if (currLength < EPS) {
            return new Vector2d(0,0);
        } else {
            return mult(v, length / currLength);
        }
    }
    
    public static double mag(Vector2d v) {
        return Math.sqrt(v.x*v.x + v.y*v.y);
    }
    
    public static Vector2d mult(Vector2d v, double scalar) {
        return new Vector2d(v.x * scalar, v.y * scalar); 
    }
    
    public static void main(String[] args) {
        Vector2d c1 = new Vector2d(0, 0);
        Vector2d c2 = new Vector2d(0, 0.00001);
        System.out.println(Arrays.toString(thirdPoint(c1, c2, 
                1, 1)));
    }
}
