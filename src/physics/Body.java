package physics;

import java.util.ArrayList;
import java.util.List;

public class Body {
    
    public List<Vector2d> points;
    public List<Edge> edges;
    
    public Body() {
        points = new ArrayList<Vector2d>();
        edges = new ArrayList<Edge>();
    }

}
