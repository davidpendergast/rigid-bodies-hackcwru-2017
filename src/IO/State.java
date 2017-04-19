package IO;

import physics.Body;
import physics.Edge;
import physics.Vector2d;

public class State {
    
    public Body body = null;
    public Edge selectedEdge = null;
    public Vector2d selectedPoint = null;
    
    public void selectEdge(Edge e) {
        selectedEdge = e;
        selectedPoint = null;
    }
    
    public void selectPoint(Vector2d p) {
        selectedEdge = null;
        selectedPoint = p;
    }
    
}
