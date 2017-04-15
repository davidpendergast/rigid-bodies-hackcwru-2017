package physics;

import java.awt.Color;

public class Edge {
    
    public Vector2d p1, p2;
    
    // rendering stuff
    public Color color = Color.BLACK;
    public int thickness = 5;
    
    public Edge(Vector2d p1, Vector2d p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    

}
