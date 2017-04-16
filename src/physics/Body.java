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
    public Map<Vector2d, Set<Edge>> toEdge;
    public Map<Vector2d, Set<Vector2d>> adj;
    
    public Body() {
        edges = new ArrayList<Edge>();
        toEdge = new HashMap<Vector2d, Set<Edge>>();
        adj = new HashMap<Vector2d, Set<Vector2d>>();
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
        adj.get(edge.p1).add(edge.p2);
        adj.get(edge.p2).add(edge.p1);
    }
    
    
    
    public void add(Vector2d point) {
        if (!adj.containsKey(point)) {
            adj.put(point, new HashSet<Vector2d>());
        }
        if (!toEdge.containsKey(point)) {
            toEdge.put(point, new HashSet<Edge>());
        }
    }
    
    public List<Vector2d> neighbors(Vector2d point) {
        if (toEdge.containsKey(point)) {
            return toEdge.get(point).stream()
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
    
    public List<Vector2d> pointsAt(Vector2d pos, double dist) {
        return adj.keySet().stream()
        .filter(p -> VecMath.dist(pos, p) <= dist)
        .collect(Collectors.toList());   
    }
    
    public boolean stretch(Edge e, double delta) {
        if (e.length() + delta <= 0) {
            return false;
        } else {
            Set<Vector2d> fixedPoints = getDerivedFixedPoints();
            if (fixedPoints.contains(e.p1) && fixedPoints.contains(e.p2)) {
                return false;
            }
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
    
    public Set<Vector2d> getFixedPoints() {
        return adj.keySet().stream()
                .filter(p -> p.fixed)
                .collect(Collectors.toSet());
    }
    
    public Set<Vector2d> getDerivedFixedPoints() {
        Set<Vector2d> currentFixed = getFixedPoints();
        Set<Vector2d> nonFixed = adj.keySet().stream()
                .filter(p -> !currentFixed.contains(p))
                .collect(Collectors.toSet());
        Set<Vector2d> becomingFixed = new HashSet<Vector2d>();
        boolean addedNew = true;
        while(addedNew) {
            addedNew = false;
            for (Vector2d n : nonFixed) {
                for (Vector2d v1 : currentFixed) {
                    for (Vector2d v2 : currentFixed) {
                        if (fixedRelativeTo(n, v1, v2)) {
                            becomingFixed.add(n);
                        }
                    }
                }
            }
            
            if (!becomingFixed.isEmpty()) {
                addedNew = true;
                for(Vector2d v : becomingFixed) {
                    currentFixed.add(v);
                    nonFixed.remove(v);
                }
                becomingFixed.clear();
            }
        }
        
        return currentFixed;
    }
    
    public boolean fixedRelativeTo(Vector2d p, Vector2d v1, Vector2d v2) {
        return v1 != v2 && adj.get(p).contains(v1) && adj.get(p).contains(v2);
    }
    
}
