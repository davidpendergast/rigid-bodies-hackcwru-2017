package simulation;

import IO.Window;
import physics.Body;
import physics.Edge;
import physics.Vector2d;

public class Simulation {
    
    public static void main(String[] mushrooms) {
        Body b = new Body();
        Vector2d v1 = new Vector2d(5, 2);
        Vector2d v2 = new Vector2d(3, 6);
        Edge e = new Edge(v1, v2);
        
        b.edges.add(e);
        
        Window window = new Window(600, 400);
    }

}
