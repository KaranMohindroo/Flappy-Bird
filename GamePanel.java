package flappybird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    
    private Bird bird;
    private ArrayList<Rectangle> rects;
    private FlappyBird fb;
    private Font scoreFont, pauseFont;
    public static final int PIPE_W = 50, PIPE_H = 30;
    private Image pipeHead, pipeLength,bgimage;

    public GamePanel(FlappyBird fb, Bird bird, ArrayList<Rectangle> rects) {
        this.fb = fb;
        this.bird = bird;
        this.rects = rects;
        scoreFont = new Font("Arial", Font.BOLD, 18);
        pauseFont = new Font("Arial", Font.BOLD, 48);
        
        try {
        	pipeHead = ImageIO.read(new File("78px-Pipe.png"));
        	pipeLength = ImageIO.read(new File("pipe_part.png"));
        	bgimage=ImageIO.read(new File("snowp.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void paintComponent(Graphics g) {
    	
        Dimension size = new Dimension(fb.frame.getWidth(), fb.frame.getHeight());
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size); 
        g.drawImage(bgimage,0,0,this);    
        bird.update(g);
        for(Rectangle r : rects) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);
            //g2d.fillRect(r.x, r.y, r.width, r.height);
            AffineTransform old = g2d.getTransform();
            g2d.translate(r.x+11+PIPE_W/2, r.y+11+PIPE_H/2);
            if(r.y < FlappyBird.HEIGHT/2) {
                g2d.translate(0, r.height);
                g2d.rotate(Math.PI);
            }
            g2d.drawImage(pipeHead, -PIPE_W/2, -PIPE_H/2, GamePanel.PIPE_W, GamePanel.PIPE_H, null);
            g2d.drawImage(pipeLength, -PIPE_W/2, PIPE_H/2, GamePanel.PIPE_W, r.height, null);
            g2d.setTransform(old);
        }
        setLayout(null);
        g.setFont(scoreFont);
        g.setColor(Color.red);
        g.drawString("Score: "+fb.getScore(), 10, 30);
        g.drawString("High Score: "+fb.getHScore(), 850, 30);
        if(fb.paused()) {
            g.setFont(pauseFont);
            g.setColor(new Color(0,0,0,170));
            g.drawString("PAUSED", FlappyBird.WIDTH/2-100, FlappyBird.HEIGHT/2-100);
            g.drawString("PRESS UP ARROW TO BEGIN", FlappyBird.WIDTH/2-300, FlappyBird.HEIGHT/2+150);
        }
    }
}
