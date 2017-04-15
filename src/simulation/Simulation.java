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
        Vector2d v1 = new Vector2d(50, 20);
        Vector2d v2 = new Vector2d(30, 60);
        Edge e = new Edge(v1, v2);
        
        state.body.add(e);
        
        Window window = new Window(state, 600, 400);
        
        RenderOptions opts = new RenderOptions();
        
        while (true) {
            sleep(100);
            Graphics2D g = window.getGraphics();
            Renderer.render(g, opts, state);
            window.repaint();
        }
    }
    
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
