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
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window {

    public BufferedImage[] canvas = new BufferedImage[] { null }; // lel
    public JFrame frame;
    public JPanel panel;
    public JPanel imagePanel;
    public JPanel toolPanel;

    public Window(int width, int height) {

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

    public static void main(String[] args) {
        Window window = new Window(600, 400);
    }

}
