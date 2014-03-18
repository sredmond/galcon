import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Small fixes @sredmond
 * Changes fleet data type to hashset from linkedlist
 * Fixed arithmetic errors (math.sqrt)
 * Sped up int to string conversion
 * Various inefficiencies
 * Fixes game-breaking cheats (send negative numbers to sendFleet)
 * 
 * NOTE: A tournament can only ever have two players, as of now.
 */

public class Tournament extends JFrame
{
	//Graphical Setup
	public static final int TOP_BAR_HEIGHT = 22;
	public static final int WIN_WIDTH = 800;//Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int WIN_HEIGHT = 400;//Toolkit.getDefaultToolkit().getScreenSize().height - TOP_BAR_HEIGHT;
	
	//Double buffering
	private static Image bufferedImage = null;
	private static Graphics bufferedGraphics = null;
	private static final Image STAR_BACKGROUND = new ImageIcon("SpacePic.jpg").getImage();
	
	private final static Player[] AIs = {new SamplePlayer(), new SamplePlayer()};
	private final static int NUM_GAMES = 2; //The number of games in this tournament
	
	private Player[] winners; //An array holding the list of winners
	private Game currentGame;
	private boolean completed;
	
	public static void main(String args[])
	{
		if (AIs.length != 2)
		{
			System.err.println("Error! Two AIs are required for Galcon.");
			System.exit(1);
		}
		new Tournament(AIs[0], AIs[1]);
	}
	public Tournament(Player player1, Player player2)
	{
		//Set up window
		setName("Galcon AI Challenge");
		setSize(WIN_WIDTH, WIN_HEIGHT + TOP_BAR_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.GRAY);
		setResizable(false);
		setVisible(true);
		
		bufferedImage = createImage(WIN_WIDTH, WIN_HEIGHT);
		bufferedGraphics = bufferedImage.getGraphics();
		
		winners = new Player[NUM_GAMES];
		for (int i = 0; i < NUM_GAMES; i++) //Play the games
		{
			currentGame = new Game(player1, player2, this);
			Player.currentGame = currentGame;
			Player winner = currentGame.play(); //Note: winner could be null
			winners[i] = winner;
			System.out.println("Done with a game, winner is " + winner.getPlayerName());	
		}
	}
	public void paint(Graphics g)
	{
		g.translate(0, TOP_BAR_HEIGHT);
		g.drawImage(bufferedImage, 0, 0, this);
	}
	public void clear(Graphics g)
	{
		g.clearRect(0, 0, WIN_WIDTH, WIN_HEIGHT);
		g.setColor(Color.WHITE);
//		g.drawImage(STAR_BACKGROUND, 0, TOP_BAR_HEIGHT, WIN_WIDTH, WIN_HEIGHT, this);
		g.fillRect(0, 0, WIN_WIDTH, WIN_HEIGHT);
	}
//	private static void recordResult(Player winner)
//	{
//		if (numWins.containsKey(winner))
//		{
//			numWins.put(winner, numWins.get(winner) + 1); //Add 1 to the player's win count
//			System.out.println("Win for "+ winner.getPlayerName()); //Print to stdout
//		}
//		else
//		{
//			System.out.println("Unable to find specified player in tournament.");	
//		}
//	}

	//Print the player ranks in order
	public void printRanks()
	{
//		for (Player p: numWins.keySet())
//		{
//			System.out.println(p.getPlayerName() + ": \t" + numWins.get(p));
//		}
	}

	private void drawRankings(Graphics g)
	{
//		final int FONT_HEIGHT = 40;
//		Font font = new Font("Monospaced", Font.PLAIN, FONT_HEIGHT);
//		g.setFont(font);
//
//		g.setColor(Color.white);
//		int x = 400;
//		int y = 250;
//		g.drawString("Name\n\n\n\n\n\n\n\n\n\n\n\nWins", x, y);
//		y += FONT_HEIGHT+5;
//		for (Player p: numWins.keySet())
//		{
//			g.drawString(p.getPlayerName() + "\t\t\t\t" + numWins.get(p), x, y);
//			y += FONT_HEIGHT + 5;
//		}
	}
	public boolean isCompleted()
	{
		return completed;
	}
	public Graphics getBufferedGraphics()
	{
		return bufferedGraphics;
	}

	//Generate a value of x between rad and WIN_WIDTH - rad
	public static int genX(int rad)
	{
		return (int) (Math.random() * (WIN_WIDTH - rad * 2) + rad);
	}
	//Generate a value of y between rad and WIN_HEIGHT - rad
	public static int genY(int rad)
	{
		return (int) (Math.random() * (WIN_HEIGHT - rad * 2) + rad);
	}
	//Get a player's color, or return DARK_GRAY if player is null
	public static Color getPlayerColor(Player player)
	{
		return (player != null) ? player.getColor() : Color.DARK_GRAY;
	}
}