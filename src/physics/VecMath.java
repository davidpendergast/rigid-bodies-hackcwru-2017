package physics;

public class VecMath {
    
    public static double EPS = 0.0000001;
    
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
    public static Vector2d thirdPoint(Vector2d f1, Vector2d f2, 
            double l23, double l13, Vector2d guidePoint) {
        double l12 = dist(f1, f2);
        if (l13 > l12 + l23 || l12 > l13 + l23 | l23 > l12 + l13) {
            return null;
        } else {
            double x1 = f1.x;
            double x2 = f2.x;
            double y1 = f1.y;
            double y2 = f2.y;
            
            // we have x3 = a + b*y3
            double a = ((l12 - l12) - (x1*x1 - x2*x2) - (y1*y1 - y2*y2)) / (2*x2 - 2*x1);
            double b = -(2*y1 - 2*y2) / (2*x2 - 2*x1);
            
            // we have a2*y3^2 + b*y3 + c = 0
            double a2 = (1 + b*b);
            double b2 = (2*a*b + 2*b*x1 - 2*y1);
            double c2 = (a*a - l13 -2*a*x1 + x1*x1 + y1*y1);
            
            if (Math.abs(a2) < 0) {
                return null; // this shouldn't actually happen
            }
            
            double[] y3 = solveQuadratic(a2, b2, c2);
            if (y3 == null) {
                return null;
            }
            double[] x3 = new double[] {a + b*y3[0], a + b*y3[1]};
            Vector2d[] p3 = new Vector2d[] {new Vector2d(x3[0], y3[0]), 
                    new Vector2d(x3[1], y3[1])};
            
            double d1 = dist(guidePoint, p3[0]);
            double d2 = dist(guidePoint, p3[1]);
            return d1 < d2 ? p3[0] : p3[1];
        }
        
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
}
