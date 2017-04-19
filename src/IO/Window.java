package IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import physics.Edge;
import physics.Vector2d;

public class Window {
    
    public State state;

    public BufferedImage[] canvas = new BufferedImage[] { null }; // lel
    public JFrame frame;
    public JPanel panel;
    public JPanel imagePanel;
    public JPanel toolPanel;
    
    public int moveSpeed = 3;
    public double stretchSpeed = 1.5;
    
    public Point mouseDownPos = null;
    public Point mouseCurrPos = null;
    
    private Object keyStateLock = new Object();
    public Set<Integer> heldKeys = new HashSet<Integer>();
    public Set<Integer> newlyPressedKeys = new HashSet<Integer>();

    public Window(State state, int width, int height) {
        this.state = state;
        
        frame = new JFrame("Rigid Body Simulator - HackCWRU 2017");
        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("This is a label!");
        panel.add(label, BorderLayout.SOUTH);
        
        canvas[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        imagePanel = new JPanel() {

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 600, 400);
                BufferedImage image = canvas[0];
                if (image != null) {
                    g.drawImage(image, 0, 0, null);
                }
            }
        };
        
        panel.add(imagePanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addListeners();
        
        frame.setVisible(true);
    }

    public Graphics2D getGraphics() {
        BufferedImage image = canvas[0];
        if (image != null) {
            return (Graphics2D) image.getGraphics();
        }
        return null;
    }
    
    public void repaint() {
        frame.repaint();
    }

    public Dimension imageSize() {
        return imagePanel.getSize();
    }
    
    private void addListeners() {
        imagePanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownPos = e.getPoint();
                Vector2d pos = new Vector2d(e.getX(), e.getY());
                if (e.getButton() == MouseEvent.BUTTON3) {
                    state.body.add(pos);
                }
                System.out.println("Woah! Clicked at: " +pos);
                List<Vector2d> clickedPoints = state.body.pointsAt(pos, 10);
                if (!clickedPoints.isEmpty() && !e.isControlDown()) {
                    System.out.println("Clicked on points: " +clickedPoints);
                    state.selectPoint(clickedPoints.get(0));
                    return;
                } else {
                    List<Edge> clickedEdges = state.body.edgesAt(pos, 10);
                    if (!clickedEdges.isEmpty()) {
                        System.out.println("Clicked on edges: " +clickedEdges);
                        state.selectEdge(clickedEdges.get(0));
                    } else {
                        state.selectEdge(null);
                    }
                }
            }    
            
            @Override
            public void mouseReleased(MouseEvent e) {
                Point downPos = mouseDownPos;
                mouseDownPos = null;
                Point releasePos = e.getPoint();
                
                if (downPos != null && e.getButton() == MouseEvent.BUTTON1) {
                    
                    List<Vector2d> clickedPoints = state.body.pointsAt(
                            new Vector2d(downPos.getX(), downPos.getY()), 10);
                    List<Vector2d> releasePoints = state.body.pointsAt(
                            new Vector2d(releasePos.getX(), releasePos.getY()), 10);
                    
                    if (!clickedPoints.isEmpty() && !releasePoints.isEmpty()) {
                        Vector2d p1 = clickedPoints.get(0);
                        Vector2d p2 = releasePoints.get(0);
                        if (p1 != p2) {
                            Edge edge = new Edge(p1, p2);
                            state.body.add(edge);
                        }
                    }
                }
            }
        });
        
        frame.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                synchronized (keyStateLock) {
                    if (!heldKeys.contains(key)) {
                        newlyPressedKeys.add(key);
                        heldKeys.add(key);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                synchronized (keyStateLock) {
                    if (heldKeys.contains(key)) {
                        heldKeys.remove(key);
                    }
                }
            }
            
        });
        
        imagePanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseCurrPos = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
    }
    
    public void handleInputs() {
        Edge selE = state.selectedEdge;
        Vector2d selP = state.selectedPoint;
        
        synchronized (keyStateLock) {
            if (heldKeys.contains(KeyEvent.VK_UP)) {
                if (selE != null) {
                    state.body.stretch(selE, stretchSpeed);
                } else if (selP != null) {
                    selP.y = (int)(selP.y - moveSpeed);
                }
            }
            
            if (heldKeys.contains(KeyEvent.VK_DOWN)) {
                if (selE != null) {
                    state.body.stretch(selE, -stretchSpeed);
                } else if (selP != null) {
                    selP.y = (int)(selP.y + moveSpeed);
                }
            }
            
            if (heldKeys.contains(KeyEvent.VK_RIGHT)) {
                if (selP != null) {
                    selP.x = (int)(selP.x + moveSpeed);
                }
            } 
            if (heldKeys.contains(KeyEvent.VK_LEFT)) {
                if (selP != null) {
                    selP.x = (int)(selP.x - moveSpeed);
                }
            } 
            
            if (newlyPressedKeys.contains(KeyEvent.VK_ENTER)) {
                if (selP != null) {
                    selP.fixed = !selP.fixed;
                }
            } 
            
            if (newlyPressedKeys.contains(KeyEvent.VK_R)) {
                state.body.resetPreferedLengths();
            }
            
            if (newlyPressedKeys.contains(KeyEvent.VK_DELETE) 
                    || newlyPressedKeys.contains(KeyEvent.VK_BACK_SPACE)) {
                if (selE != null) {
                    state.body.remove(selE);
                } else if (selP != null) {
                    state.body.remove(selP);
                }
            }
            
            newlyPressedKeys.clear();
        }
    }
}
