/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybird;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;
import java.io.IOException;
import java.io.*;
import java.net.*;
import sun.audio.*;
import java.awt.Rectangle;
import javax.swing.JOptionPane;

public class FlappyBird implements ActionListener, KeyListener {
    
    public static final int WIDTH = 1000, HEIGHT = 680;
    int FPS = 60;
    
    private Bird bird;
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Rectangle> rects;
    private int time, scroll,hs;
    private Timer t;
    
    private boolean paused;
    static AudioPlayer MGP = AudioPlayer.player;
    static AudioStream BGM;
    static AudioData MD;
    public void go() {
        music();
        frame = new JFrame("Flappy Dragon");

        bird = new Bird();
        rects = new ArrayList<Rectangle>();
        panel = new GamePanel(this, bird, rects);
        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);
        
        paused = true;
        
        t = new Timer(1000/FPS, this);
        t.start();
    }
    public static void main(String[] args) {
        new FlappyBird().go();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();
        if(!paused) {
            bird.physics();
            if(scroll % 90 == 0) {
                Rectangle r = new Rectangle(WIDTH, 0, GamePanel.PIPE_W, (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT));
                int h2 = (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT);
                Rectangle r2 = new Rectangle(WIDTH, HEIGHT - h2, GamePanel.PIPE_W, h2);
                rects.add(r);
                rects.add(r2);
            }
            ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
            boolean game = true;
            for(Rectangle r : rects) {
                r.x-=3;
                if(r.x + r.width <= 0) {
                    toRemove.add(r);
                }
                if(r.contains(bird.x, bird.y)) {
                    JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your score was: "+time/60+".");
                    game = false;
                    if(hs<time/60)
                    {
                        hs=time/60;
                    }
                }
            }
            rects.removeAll(toRemove);
            time++;
            if((time/60)<5)FPS=60;
            else if((time/60)<10){FPS=80; Bird.fall1(0.2f);Bird.jump(7); }
            else if((time/60)<15){FPS=100;Bird.fall1(0.15f);Bird.jump(6); }
            else {FPS=120;Bird.fall1(0.1f);Bird.jump(5);}
            t.setDelay(1000/FPS);
            scroll++;

            if(bird.y > HEIGHT || bird.y+bird.RAD < 0) {
                JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your score was: "+time/60+".");
                game = false;
                if(hs<time/60)
                {
                    hs=time/60;
                }
            }

            if(!game) {
                rects.clear();
                bird.reset();
                time = 0;
                scroll = 0;
                paused = true;
            }
        }
        else {
            
        }
    }
    
    public int getScore() {
        return time/60;
    }
    public int getHScore() {

        return hs;
    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            paused = false;
            if(hs<time/60)
            {
                hs=time/60;
            }
            bird.jump();
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            paused = true;
        }
    }
    public void keyReleased(KeyEvent e) {
        
    }
    public void keyTyped(KeyEvent e) {
        
    }
    
    public boolean paused() {
        return paused;
    }
    public static void music()
    {


        ContinuousAudioDataStream loop = null;

        try
        {
            InputStream test = new FileInputStream("E:\\Movies\\Batman\\S3\\b.wav");
            BGM = new AudioStream(test);
            AudioPlayer.player.start(BGM);
            //  AudioPlayer.player.stop(BGM);
        }
        catch(FileNotFoundException e){
            System.out.print(e.toString());
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
        MGP.start(loop);
    }
}
