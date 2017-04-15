package physics;

import java.awt.Color;

public class Vector2d {
    
    public double x;
    public double y;
    
    // render stuff
    public Color color = Color.RED;
    public int thickness = 10;
    
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
