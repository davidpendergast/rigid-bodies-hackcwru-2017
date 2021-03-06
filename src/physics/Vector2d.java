package physics;

import java.awt.Color;

public class Vector2d {
    
    public double x;
    public double y;
    
    /**
     * Point is prevented from moving.
     */
    public boolean fixed = false;
    
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2d(Vector2d v) {
        this(v.x, v.y);
        this.fixed = v.fixed;
    }
    
    public void set(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public String toString() {
        return "(" + format(x) + ", " + format(y) + ")";
    }
    
    private static String format(double num) {
        return String.format("%.1f", num);
    }
    
    public Color color() {
        return Color.BLACK;
    }
    
    public int thickness() {
        if (fixed) {
            return 20;
        } else {
            return 10;
        }
    }

}
