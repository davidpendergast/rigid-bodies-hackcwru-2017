package physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Body {
    
    public Set<Vector2d> points;
    public List<Edge> edges;
    public Map<Vector2d, List<Edge>> adj;
    
    public Body() {
        points = new HashSet<Vector2d>();
        edges = new ArrayList<Edge>();
        adj = new HashMap<Vector2d, List<Edge>>();
    }
    
    public void add(Edge edge) {
        edges.add(edge);
        add(edge.p1);
        add(edge.p2);
    }
    
    public void add(Vector2d point) {
        points.add(point);
        if (!adj.containsKey(point)) {
            adj.put(point, new ArrayList<Edge>());
        }
    }
    
    public List<Vector2d> neighbors(Vector2d point) {
        return null;
    }

}
