package physics;

public class VecMath {
    
    public static double EPS = 0.0000001;
    
    public static double dist(Vector2d v, Edge e) {
        if (!inBoundingBox(v, e)) {
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
    
    public static boolean inBoundingBox(Vector2d v, Edge e) {
        return !(v.x < e.minX() || v.x > e.maxX() 
                || v.y < e.minY() || v.y > e.maxY());
    }
}
