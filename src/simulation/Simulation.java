package simulation;

import java.awt.Graphics;
import java.awt.Graphics2D;

import IO.State;
import IO.Window;
import physics.Body;
import physics.Edge;
import physics.Vector2d;
import rendering.RenderOptions;
import rendering.Renderer;

public class Simulation {
    
    public static void main(String[] mushrooms) {
        State state = new State();
        
        state.body = new Body();
        Vector2d v1 = new Vector2d(90, 300);
        Vector2d v2 = new Vector2d(200, 300);
        Vector2d v3 = new Vector2d(90, 250);
        Vector2d v4 = new Vector2d(150, 250);
        
        state.body.add(new Edge(v1, v2));
        state.body.add(new Edge(v1, v3));
        //state.body.add(new Edge(v1, v4));
        state.body.add(new Edge(v2, v4));
        state.body.add(new Edge(v3, v4));
        
        Window window = new Window(state, 600, 400);
        RenderOptions opts = new RenderOptions();
        
        while (true) {
            sleep(100);
            Graphics2D g = window.getGraphics();
            Renderer.render(g, opts, state);
            window.repaint();
            state.body.update(1.0);
        }
    }
    
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            
        }
    }

}
