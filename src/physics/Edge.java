package physics;

import java.awt.Color;

public class Edge {
    
    public Vector2d p1, p2;
    
    // rendering stuff
    public Color color = Color.BLACK;
    public int thickness = 3;
    
    public Edge(Vector2d p1, Vector2d p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public double length() {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public double maxX() {
        return Math.max(p1.x, p2.x);
    }
    
    public double minX() {
        return Math.min(p1.x, p2.x);
    }
    
    public double maxY() {
        return Math.max(p1.y, p2.y);
    }
    
    public double minY() {
        return Math.min(p1.y, p2.y);
    }
    
    public Vector2d other(Vector2d p) {
        if (p == p1) {
            return p2;
        } else if (p == p2) {
            return p1;
        }
        
        throw new IllegalArgumentException("p not in edge.");
    }
    
    public String toString() {
        return "Edge:" + p1 + " " + p2;
    }
}
