package Flappy-Bird;
package flappyBird;

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
import sun.audio.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
	static AudioPlayer MGP = AudioPlayer.player;
	static AudioStream BGM;
	static AudioData MD;
  public static FlappyBird flappyBird;

	public final int WIDTH = 800, HEIGHT = 800;
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList<Rectangle> columns;
	public int ticks, yMotion, score, highScore;
	public boolean gameOver, started;
	public Random rand;
	
  public FlappyBird()  //Frame of game
  {
    		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();
	  music();
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

  public void addColumn(boolean start) 			//obstacle
	{
		int space = 300; // it is the space between two obstacle vertically
		int width = 100; // it is the width of rectangular obstacle
		int height = 50 + rand.nextInt(300); //height of rectangular obstacle will be decided randomly

		if (start) // at the start of game
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
	public void jump()  // responsible for jumps of box/bird
	{
		
		if (gameOver) 		// game over then jump restarts the game
		{	
			music();
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}
		if (!started)
		{
			started = true;
		}
		else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 12;
		}
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{int speed = 10;

		ticks++;

		if (started)
		{
			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0)
				{
					columns.remove(column);

					if (column.y == 0)
					{
						addColumn(false);
					}
				}
			}

			bird.y += yMotion;

			for (Rectangle column : columns)
			{
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
				{
					score++;
				}

				if (column.intersects(bird))
				{
					gameOver = true;

					if (bird.x <= column.x)
					{
						bird.x = column.x - bird.width;

					}
					else
					{
						if (column.y != 0)
						{
							bird.y = column.y - bird.height;
						}
						else if (bird.y < column.height)
						{
							bird.y = column.height;
						}
					}
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0)
			{
				gameOver = true;
			}

			if (bird.y + yMotion >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			}
		}

		renderer.repaint();
	}
	public void repaint(Graphics g) 	//responsible for handling graphics whenrestarting game
	{
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for (Rectangle column : columns)
		{
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		if (!started)
		{
			g.setFont(new Font("Arial", 1, 100));
			g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("High score", 25, HEIGHT / 2 - 350);
			g.drawString(String.valueOf(highScore), 25, HEIGHT / 2 - 310);
			g.drawString("Score", WIDTH-200, HEIGHT / 2 - 350);
			g.drawString(String.valueOf(score), WIDTH  - 100, HEIGHT / 2 - 310);
		}

		if (gameOver)
		{
			AudioPlayer.player.stop(BGM);
			g.setFont(new Font("Arial", 1, 100));
			g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
			
			if(score>highScore)
				highScore=score;
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("High score", 25, HEIGHT / 2 - 350);
			g.drawString(String.valueOf(highScore), 25, HEIGHT / 2 - 310);
			g.drawString("Score", WIDTH-200, HEIGHT / 2 - 350);
			g.drawString(String.valueOf(score), WIDTH  - 100, HEIGHT / 2 - 310);
		}

		if (!gameOver && started)
		{

			g.setFont(new Font("Arial", 1, 50));
			g.drawString("High score", 25, HEIGHT / 2 - 350);
			g.drawString(String.valueOf(highScore), 25, HEIGHT / 2 - 310);
			g.drawString("Score", WIDTH-200, HEIGHT / 2 - 350);
			g.drawString(String.valueOf(score), WIDTH  - 100, HEIGHT / 2 - 310);
		}
	}
	public static void music()
	{


		ContinuousAudioDataStream loop = null;

		try
		{
			InputStream test = new FileInputStream("E:\\Movies\\Batman\\S3\\fl.wav");
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
	public static void main(String[] args)
	{
		flappyBird = new FlappyBird();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		jump();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}
}
