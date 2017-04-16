package rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import IO.State;
import physics.Body;
import physics.Edge;
import physics.Vector2d;

public class Renderer {
    
    public static Color SELECTED_COLOR = Color.RED;
    
    public static void render(Graphics2D g, RenderOptions options, State state) {
        Body body = state.body;
        g.setColor(options.bgColor);
        g.fillRect(0, 0, 
                options.drawRect.width, options.drawRect.height);
  
        
        int xOffs = options.drawRect.x;
        int yOffs = options.drawRect.y;
        
        for (Edge e : body.edges) {
            if (state.selectedEdge == e) {
                g.setColor(SELECTED_COLOR);
            } else {
                g.setColor(e.color);
            }
            g.setStroke(new BasicStroke(e.thickness));
            g.draw(new Line2D.Double(e.p1.x - xOffs, e.p1.y - yOffs, 
                    e.p2.x - xOffs, e.p2.y - yOffs));
        }
        
        for (Vector2d p : body.adj.keySet()) {
            if (state.selectedPoint == p) {
                g.setColor(SELECTED_COLOR);
            } else {
                g.setColor(p.color);
            }
            
            int diam = p.thickness;
            g.fillOval((int)(p.x - diam/2) - xOffs, 
                    (int)(p.y - diam/2) - yOffs, diam, diam);
        }
    }

}
