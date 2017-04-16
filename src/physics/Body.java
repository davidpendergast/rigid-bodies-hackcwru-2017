package physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Body {
    
    public List<Edge> edges;
    public Map<Vector2d, List<Edge>> adj;
    
    public Body() {
        edges = new ArrayList<Edge>();
        adj = new HashMap<Vector2d, List<Edge>>();
    }
    
    public Map<Edge, Double> getLengths() {
        Map<Edge, Double> res = new HashMap<Edge, Double>();
        for (Edge e : edges) {
            res.put(e, e.length());
        }
        return res;
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
    
    public List<Edge> edgesAt(Vector2d pos, double dist) {
        return edges.stream()
                .filter(e -> VecMath.dist(pos, e) <= dist)
                .collect(Collectors.toList());   
    }
    
    public boolean stretch(Edge e, double delta) {
        if (e.length() + delta <= 0) {
            return false;
        } else {
            double currLength = e.length();
            Vector2d midpoint = e.midpoint();
            Vector2d newP1 = VecMath.extend(midpoint, e.p1, (currLength + delta) / 2);
            Vector2d newP2 = VecMath.extend(midpoint, e.p2, (currLength + delta) / 2);
            e.p1.x = newP1.x;
            e.p1.y = newP1.y;
            e.p2.x = newP2.x;
            e.p2.y = newP2.y;
            return true;
        }
    }
    
}
