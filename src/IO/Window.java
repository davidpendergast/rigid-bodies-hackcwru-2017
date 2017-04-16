package IO;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JButton;
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
                Vector2d pos = new Vector2d(e.getX(), e.getY());
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
        });
        
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Edge selE = state.selectedEdge;
                Vector2d selP = state.selectedPoint;
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (selE != null) {
                        state.body.stretch(selE, 1.0);
                    } else if (selP != null) {
                        selP.y = (int)(selP.y - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (selE != null) {
                        state.body.stretch(selE, -1.0);
                    } else if (selP != null) {
                        selP.y = (int)(selP.y - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (selP != null) {
                        selP.x = (int)(selP.x + 1);
                    }
                }  else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (selP != null) {
                        selP.x = (int)(selP.x - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (selP != null) {
                        selP.fixed = !selP.fixed;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
    }

}
