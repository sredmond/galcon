import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.awt.Toolkit;
/***************************************************************************
 * ATCS AI Challenge: Main Class
 * 
 * Author: Christopher Sauer, Alexander Lazar, <insert other names here>
 * 
 * Description:  This class is my standard way to set up the program and manage
 * all of the other classes.  It tells all of the other classes to draw and update.
 * Uses double buffering.  Please see SamplePlayer and Galcon REAMDE to write an AI.
 * 
 * This comment was written on: 9/30/11, edited on 2/26/14
 **************************************************************************/
public class Game extends JFrame implements ActionListener
{	
	//Window setup data	
	public static final int TOP_BAR_HEIGHT = 22; //24 for windows, 22 for mac
	public static final int WIN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int WIN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

	//double Buffering stuff
	private static Graphics bufferGraphics;
	private static Image bufferImage;

	private static Game game;

	public static final int FRAME_TIME = 1;

	private static final Image STAR_BACKGROUND = new ImageIcon("SpacePic.jpg").getImage();

	private static Set<Fleet> fleets = new HashSet<Fleet>();
	public static Set<Planet> planets = new HashSet<Planet>();

	private static final int NUM_PLANETS = 16;

	private static Timer clock;

	//	public static void main(String args[])
	//	{	
	//		Player[] ais = {TEAM1, TEAM2};
	//		manager = new TournamentManager(ais, NUM_GAMES);
	//		mc = new MainClass();
	//	}

	private Game()
	{
		setName("Galcon AI Challenge");
		//set up window
		setSize(WIN_WIDTH, WIN_HEIGHT + TOP_BAR_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.GRAY);
		setResizable(false);
		setVisible(true);	
		generateNewMap();		


		Player[] players = tournament.getPlayers();
		for (int i=0; i<players.length; i++)
		{
			if(!players[i].isAlive())
			{
				players[i].start();
			}

		}

		clock = new Timer(FRAME_TIME, this);
		clock.start();
	}

	/**
	 * Double buffered graphics
	 */
	public void paint(Graphics g)
	{
		if (bufferImage == null)
		{
			bufferImage = createImage(this.getSize().width,this.getSize().height);
			bufferGraphics = bufferImage.getGraphics();

		}

		clearScreen(bufferGraphics);
		Planet.drawAll(bufferGraphics);
		Fleet.drawAll(bufferGraphics);
		drawCurrentPlayerInfo(bufferGraphics);

		if (manager.isDone())
		{
			drawRankings(bufferGraphics);
		}

		g.drawImage(bufferImage,0,0,this);
	}

	private void drawCurrentPlayerInfo(Graphics g)
	{
		Player[] players =  manager.getCurrentPlayers();
		Player cp1 = players[0];
		Player cp2 = players[1];
		final int FONT_HEIGHT = 40;
		Font font = new Font("Monospaced", Font.PLAIN, FONT_HEIGHT);
		g.setFont (font);    
		g.setColor(cp1.getColor());
		g.drawString(cp1.getPlayerName(), 10, TOP_BAR_HEIGHT + FONT_HEIGHT);
		g.setColor(cp2.getColor());
		g.drawString(cp2.getPlayerName(), WIN_WIDTH/2, TOP_BAR_HEIGHT + FONT_HEIGHT);
	}

	private void drawRankings(Graphics g)
	{
		String[] str = manager.reportRankingsArray();
		final int FONT_HEIGHT = 40;
		Font font = new Font("Monospaced", Font.PLAIN, FONT_HEIGHT);
		g.setFont(font);
		g.setColor(Color.white);
		int x = 400;
		int y = 250;
		g.drawString("Name\n\n\n\n\n\n\n\n\n\n\n\nWins", x, y);
		y += FONT_HEIGHT+5;
		for (String s : str)
		{
			g.drawString(s, x, y);
			y += FONT_HEIGHT + 5;
		}
	}

	/**
	 * Clears the screen.
	 * @param g The ability to draw.
	 */
	private void clearScreen(Graphics g)
	{
		g.clearRect(0, 0, WIN_WIDTH, WIN_HEIGHT);
		g.drawImage(STAR_BACKGROUND, 0, TOP_BAR_HEIGHT, WIN_WIDTH, WIN_HEIGHT, this);
	}

	//Draw all planets
	public static void drawAllPlanets(Graphics g)
	{
		for(int i = 0; i < planets.size(); i++)
		{
			planets.get(i).draw(g);
		}
	}

	public static void drawAllFleets(Graphics g)
	{
		for(Fleet f: fleets)
		{
			f.draw(g);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Player winner = Planet.isGameOver();
		if(winner == null)
		{
			Player[] players = manager.getPlayers();
			for (int i=0; i<players.length; i++)
			{

				players[i].makeMove();
			}
			Fleet.updateAll();
			Planet.updateAll();
		}
		else
		{
			if(manager.isDone()){
				manager.reportResult(winner);
				System.out.println(manager.reportRankings());
				clock.stop();
			}
			else
			{
				manager.reportResult(winner);
				generateNewMap();
			}
		}
		mc.repaint();
	}

	private void generateNewMap(){
		Player players[] = manager.getNextMatch();
		Player p1 = players[0];
		Player p2 = players[1];
		
		//Reset the fleets and planets
		clearFleets();
		clearPlanets();
		planets.add(new Planet(p1)); //Give player 1 a starting planet
		planets.add(new Planet(p2)); //Give player 2 a starting planet
		for (int i = 0 ; i < NUM_PLANETS - 2; i++) //Add the rest of the planets as neutral
		{
			planets.add(new Planet());
		}
	}
	
	public static void clearFleets()
	{
		fleets.clear();
	}
	public static void clearPlanets()
	{
		planets.clear();
	}
	public static void updateAllFleets()
	{
		Set<Fleet> toBeRemoved = new HashSet<Fleet>();
		for(Fleet f: fleets)
		{
			if (f.update()) //Did we hit the planet?
			{
				toBeRemoved.add(f); //Mark it for removal
			}
		}
		for (Fleet f: toBeRemoved) //Remove marked fleets
		{
			fleets.remove(f);
		}
		
	}
	public static void updateAllPlanets()
	{
		for(Planet p: planets)
		{
			p.update();
		}
	}
}