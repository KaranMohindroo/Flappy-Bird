package Flappy-Bird;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
  
  public static FlappyBird flappyBird;

	public final int WIDTH = 800, HEIGHT = 800;
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList<Rectangle> columns;
	public int ticks, yMotion, score;
	public boolean gameOver, started;
	public Random rand;
	
  public FlappyBird()  //Frame of game
  {
    		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Flappy Box");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);
		
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
  }
  public void addColumn(boolean start)
	{
		int space = 300; // it is the space between two obstacle vertically
		int width = 100; // it is the width of rectangular obstacle
		int height = 50 + rand.nextInt(300); //height of rectangular obstacle will be decided randomly

		if (start) // at the arart of game
		{
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else
		{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}
  public void paintColumn(Graphics g, Rectangle column)
	{
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
}
