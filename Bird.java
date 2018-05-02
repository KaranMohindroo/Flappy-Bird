    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */
    package flappybird;

    import java.awt.Color;
    import java.awt.Graphics;
    import java.awt.Image;
    import java.net.*;
    import java.awt.*;
    import javax.swing.*;

    public class Bird {
        public static float x, y, vx, vy;
        public static float fall=0.3f;
        public static int j=8;

        public static final int RAD = 40;
        private Image img;
        public Bird() {
            x = FlappyBird.WIDTH/2-100;
            y = FlappyBird.HEIGHT/2-30;
               img = getImage("Dragon.gif");
        }
        private static Image getImage(String filename) {

            // to read from file
            ImageIcon icon = new ImageIcon(filename);

            // try to read from URL
            if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
                try {
                    URL url = new URL(filename);
                    icon = new ImageIcon(url);
                } catch (Exception e) { /* not a url */ }
            }

            // in case file is inside a .jar
            if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
                URL url = FlappyBird.class.getResource(filename);
                if (url == null) throw new IllegalArgumentException("image " + filename + " not found");
                icon = new ImageIcon(url);
            }

            return icon.getImage();
        }
        public static void fall1(float f)
        {
            fall=f;
        }
        public void physics() {
            x+=vx;
            y+=vy;
            vy+=fall;
        }
        public void update(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawImage(img, Math.round(x-RAD),Math.round(y-RAD),160,180, null);
        }

        public static void jump(int j1) {
            j=j1;
        }
        public static void jump() {
            vy = -j;
        }
        public void reset() {
            x = FlappyBird.WIDTH/2-100;
            y = FlappyBird.HEIGHT/2-30;
            vx = vy = 0;
        }
    }
