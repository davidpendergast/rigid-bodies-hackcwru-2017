package rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import physics.Body;
import physics.Edge;

public class Renderer {
    
    public static void render(Graphics2D g, RenderOptions options, Body body) {
        g.setColor(options.bgColor);
        g.fillRect(options.drawRect.x, options.drawRect.y, 
                options.drawRect.width, options.drawRect.height);
  
        for (Edge e : body.edges) {
            g.setColor(e.color);
            g.setStroke(new BasicStroke(e.thickness));
            g.draw(new Line2D.Double(e.p1.x, e.p1.y, e.p2.x, e.p2.y));
        }
    }

}
