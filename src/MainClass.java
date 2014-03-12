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
import java.util.Random;
import java.util.Scanner;
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
public class MainClass extends JFrame implements ActionListener
{
	private final static Player TEAM1 = new SamplePlayer();
	private final static Player TEAM2 = new BarrattAI();
	private final static int NUM_GAMES = 2;
	
	//Window setup data	
	public static final int TOP_BAR_HEIGHT = 21;//24 for windows 21 for mac
	public static final int WIN_WIDTH = 1000;
	public static final int WIN_HEIGHT = 600;
	
	//double Buffering stuff
	private static Graphics bufferGraphics;
	private static Image bufferImage;


	private static MainClass mc;

	public static final int FRAME_TIME = 1;

	private static final Image STAR_BACKGROUND = new ImageIcon("SpacePic.jpg").getImage();
	
	private static TournamentManager manager;
	
	private static final int NUM_PLANETS = 16;
	private static Timer clock;
	
	public static void main(String args[])
	{	
		Player[] ais = {TEAM1, TEAM2};
		manager = new TournamentManager(ais, NUM_GAMES);
		mc = new MainClass();
	}

	private MainClass()
	{
		setName("GalCon AI Challenge");
		//set up window
		setSize(WIN_WIDTH, WIN_HEIGHT + TOP_BAR_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.GRAY);
		setResizable(false);
		setVisible(true);	
		generateNewMap();		
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
        g.drawString(cp1.getName(), 10, TOP_BAR_HEIGHT + FONT_HEIGHT);
		g.setColor(cp2.getColor());
        g.drawString(cp2.getName(), WIN_WIDTH/2, TOP_BAR_HEIGHT + FONT_HEIGHT);
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
    
    public static Color getPlayerColor(Player player)
    {
    	return manager.getColor(player);
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
		Fleet.clearFleets();
		Planet.clearPlanets();
		new Planet(p1);
		new Planet(p2);
		for (int i = 0 ; i < NUM_PLANETS - 2; i++)
		{
			new Planet();			
		}
	}
}
