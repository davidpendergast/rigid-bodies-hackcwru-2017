package physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Body {
    
    public List<Edge> edges;
    public Map<Vector2d, List<Edge>> adj;
    
    public Body() {
        edges = new ArrayList<Edge>();
        adj = new HashMap<Vector2d, List<Edge>>();
    }
    
    public void add(Edge edge) {
        edges.add(edge);
        add(edge.p1);
        add(edge.p2);
    }
    
    public void add(Vector2d point) {
        if (!adj.containsKey(point)) {
            adj.put(point, new ArrayList<Edge>());
        }
    }
    
    public List<Vector2d> neighbors(Vector2d point) {
        if (adj.containsKey(point)) {
            return adj.get(point).stream()
                    .map(e -> e.other(point))
                    .collect(Collectors.toList());
        }
        
        throw new IllegalArgumentException("point not in body");
    }

}
