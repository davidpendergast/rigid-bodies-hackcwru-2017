package physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Body {
    
    public static double errorThresh = 1.0;
    
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
    
    public void resetPreferedLengths() {
        for (Edge e : edges) {
            preferedLengths.put(e, e.length());
        }
    }
    
    public Set<Vector2d> points() {
        return adj.keySet();
    }
    
    public void add(Edge edge) {
        edges.add(edge);
        preferedLengths.put(edge, edge.length());
        add(edge.p1);
        add(edge.p2);
        toEdge.get(edge.p1).add(edge);
        toEdge.get(edge.p2).add(edge);
        adj.get(edge.p1).add(edge.p2);
        adj.get(edge.p2).add(edge.p1);
    }
    
    public void remove(Edge edge) {
        edges.remove(edge);
        preferedLengths.remove(edge);
        toEdge.get(edge.p1).remove(edge);
        toEdge.get(edge.p2).remove(edge);
        adj.get(edge.p1).remove(edge.p2);
        adj.get(edge.p2).remove(edge.p1);
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
    
    private void reassignPointIds() {
        List<Vector2d> allPoints = adj.keySet().stream().collect(Collectors.toList());
        id.clear();
        for (int i = 0; i < allPoints.size(); i++) {
            id.put(allPoints.get(i), i);
        }
    }
    
    public void remove(Vector2d point) {
        for (Edge e : new ArrayList<Edge>(toEdge.get(point))) {
            remove(e);
        }
        adj.remove(point);
        toEdge.remove(point);
        id.remove(point);
        
        reassignPointIds();
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
    
    public void stretch(Edge e, double delta) {
        double currLen = e.length();
        preferedLengths.put(e, currLen + delta);
    }
    
    public void update() {
        if (!needsUpdate()) {
            return;
        }
        Polynomial[] f = getConstraintFunctions();
        int n = points().size();
        double[] x0 = new double[n * 2];
        for (Vector2d p : points()) {
            int i = id.get(p);
            x0[i] = p.x;
            x0[i + n] = p.y;
        }
        
        double[] soln = FuncSystemSolver.solve(f, x0, 20, errorThresh);
        if (soln == null) {
            //System.out.println(FuncSystemSolver.err(f, soln));
        } else {
            for (Vector2d p : points()) {
                int i = id.get(p);
                p.x = soln[i];
                p.y = soln[i + n];
            }
        }
        
        resetPreferedLengths();
    }
    
    public boolean needsUpdate() {
        return getError() > errorThresh;
    }
    
    public double getError(Edge e) {
        return Math.abs(preferedLengths.get(e) - e.length());
    }
    
    public double getError() {
        double err = 0;
        for (Edge e : edges) {
            err += getError(e);
        }
        
        return err;
    }
    
    public Set<Vector2d> getFixedPoints() {
        return adj.keySet().stream()
                .filter(p -> p.fixed)
                .collect(Collectors.toSet());
    }
    
    private Polynomial[] getConstraintFunctions() {
        List<Polynomial> res = new ArrayList<Polynomial>();
        int n = adj.keySet().size();
        for (Edge e : edges) {
            int xi = Math.max(id.get(e.p1), id.get(e.p2));
            int xj = Math.min(id.get(e.p1), id.get(e.p2));
            int yi = xi + n;
            int yj = xj + n;
            double lij = preferedLengths.get(e);
            
            Polynomial f = new Polynomial();
            f.set( 1, xi, xi);
            f.set(-2, xi, xj);
            f.set( 1, xj, xj);
            f.set( 1, yi, yi);
            f.set(-2, yi, yj);
            f.set( 1, yj, yj);
            f.set(-lij*lij);
            res.add(f);
        }
        
//        for (Vector2d p : adj.keySet()) {
//            if (p.fixed) {
//                int xi = id.get(p);
//                Polynomial f1 = new Polynomial(n * 2);
//                f1.set(-p.x, xi);
//                res.add(f1);
//                
//                Polynomial f2 = new Polynomial(n * 2);
//                f2.set(-p.y, xi + n);
//                res.add(f2);
//            }
//        }
        
        return res.toArray(new Polynomial[0]);
    }
}
