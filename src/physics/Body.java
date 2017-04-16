package physics;

import java.util.ArrayList;
import java.util.Arrays;
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
    public Map<Vector2d, Integer> id;
    public Map<Edge, Double> preferedLengths;
    
    public Body() {
        edges = new ArrayList<Edge>();
        toEdge = new HashMap<Vector2d, Set<Edge>>();
        adj = new HashMap<Vector2d, Set<Vector2d>>();
        id = new HashMap<Vector2d, Integer>();
        preferedLengths = new HashMap<Edge, Double>();
    }
    
    public Map<Edge, Double> getLengths() {
        Map<Edge, Double> res = new HashMap<Edge, Double>();
        for (Edge e : edges) {
            res.put(e, e.length());
        }
        return res;
    }
    
    public Set<Vector2d> points() {
        return adj.keySet();
    }
    
    public void add(Edge edge) {
        edges.add(edge);
        preferedLengths.put(edge, edge.length());
        add(edge.p1);
        add(edge.p2);
        adj.get(edge.p1).add(edge.p2);
        adj.get(edge.p2).add(edge.p1);
    }
    
    private void refreshPreferedLengths() {
        for (Edge e : edges) {
            preferedLengths.put(e, e.length());
        }
    }

    public void add(Vector2d point) {
        if (!adj.containsKey(point)) {
            adj.put(point, new HashSet<Vector2d>());
        }
        if (!toEdge.containsKey(point)) {
            toEdge.put(point, new HashSet<Edge>());
        }
        if (!id.containsKey(point)) {
            id.put(point, id.size());
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
            double currLength = e.length();

            refreshPreferedLengths();
            preferedLengths.put(e, currLength + delta);
            QuadraticFunction[] f = getConstraintFunctions();
            int n = points().size();
            double[] x0 = new double[n * 2];
            for (Vector2d p : points()) {
                int vari = id.get(p);
                x0[vari] = p.x;
                x0[vari + n] = p.y;
            }
            double[] x = Newton.solve(50, f, x0);
            System.out.println("x0 = " + Arrays.toString(x0));
            System.out.println("x = " + Arrays.toString(x));
            for (Vector2d p : points()) {
                int vari = id.get(p);
                p.x = x[vari];
                p.y = x[vari + n];
            }
            
            return true;
        }
    }
    
    public Set<Vector2d> getFixedPoints() {
        return adj.keySet().stream()
                .filter(p -> p.fixed)
                .collect(Collectors.toSet());
    }
    
    private QuadraticFunction[] getConstraintFunctions() {
        List<QuadraticFunction> res = new ArrayList<QuadraticFunction>();
        int n = adj.keySet().size();
        for (Edge e : edges) {
            int xi = Math.max(id.get(e.p1), id.get(e.p2));
            int xj = Math.min(id.get(e.p1), id.get(e.p2));
            int yi = xi + n;
            int yj = xj + n;
            double lij = preferedLengths.get(e);
            
            QuadraticFunction f = new QuadraticFunction(n * 2);
            f.set(xi, xi, 1);
            f.set(xi, xj, -2);
            f.set(xj, xj, 1);
            f.set(yi, yi, 1);
            f.set(yi, yj, -2);
            f.set(yj, yj, 1);
            f.set(-lij*lij);
            res.add(f);
        }
        
        for (Vector2d p : adj.keySet()) {
            if (p.fixed) {
                QuadraticFunction f1 = new QuadraticFunction(n * 2);
                QuadraticFunction f2 = new QuadraticFunction(n * 2);
                int xi = id.get(p);
                f1.set(xi, -p.x);
                f2.set(xi + n, -p.y);
                res.add(f1);
                res.add(f2);
            }
        }
        
        return res.toArray(new QuadraticFunction[0]);
    }
}
