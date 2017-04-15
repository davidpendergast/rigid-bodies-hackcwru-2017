package physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Body {
    
    public Set<Vector2d> points;
    public List<Edge> edges;
    
    public Body() {
        points = new HashSet<Vector2d>();
        edges = new ArrayList<Edge>();
    }
    
    public void add(Edge edge) {
        edges.add(edge);
        points.add(edge.p1);
        points.add(edge.p2);
    }

}
