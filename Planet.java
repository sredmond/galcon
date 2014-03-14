//3/13/14

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Planet 
{
	public static final int MAX_SIZE = 50;
	public static final int MIN_SIZE = 12;
	public static final int MAX_NEUTRAL_UNITS = 50;
	public static final int MIN_PRODUCE_TIME = 10;
	public static final int MAX_PRODUCE_TIME = 40;

	private static final Image PLANET_IMAGE = new ImageIcon("planetGray.png").getImage();

	public final int X, Y, RADIUS, PRODUCTION_TIME;
	private int numUnits, updateCnt = 0;
	private Player owner;

	//Construct a planet owned by a particular person
	public Planet(Player player)
	{
		owner = player;
		numUnits = 100;
		RADIUS = MAX_SIZE;
		PRODUCTION_TIME = MIN_PRODUCE_TIME;
		//    	setupImage();
		int x = 0, y = 0;
		boolean overlapping = true;
		while (overlapping)
		{
			x = (int) (Math.random() * (MainClass.WIN_WIDTH - RADIUS * 2) + RADIUS);
			y = (int) (Math.random() * (MainClass.WIN_HEIGHT - RADIUS * 2 - MainClass.TOP_BAR_HEIGHT)
					+ RADIUS + MainClass.TOP_BAR_HEIGHT);

			overlapping = checkOtherPlanets(x, y);
		}
		X = x;
		Y = y;
	}

	public Planet()
	{
		owner = null;
		numUnits = (int) (Math.random() * MAX_NEUTRAL_UNITS);
		RADIUS = (int) (Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE);
		PRODUCTION_TIME = (int) ((1 - ((double) RADIUS - MIN_SIZE) / (MAX_SIZE - MIN_SIZE)) * 
				(MAX_PRODUCE_TIME - MIN_PRODUCE_TIME) + MIN_PRODUCE_TIME); //Calculate the time between productions
		setupImage();
		int x = 0, y = 0;
		boolean overlapping = true;
		while (overlapping)
		{
			x = (int) (Math.random() * (MainClass.WIN_WIDTH - RADIUS * 2) + RADIUS);
			y = (int) (Math.random() * (MainClass.WIN_HEIGHT - RADIUS * 2) + MainClass.TOP_BAR_HEIGHT
					+ RADIUS);

			overlapping = checkOtherPlanets(x, y);
		}
		X = x;
		Y = y;
	}

	private boolean checkOtherPlanets(int x, int y)
	{
		for(int i = 0; i < planets.size(); i++)
		{
			int pX = planets.get(i).getX();
			int pY = planets.get(i).getY();
			int pR = planets.get(i).getRadius();

			double distance = Math.sqrt((pX - x) * (pX - x) + (pY - y) * (pY - y));
			if(distance <= RADIUS + pR + 10)
			{
				return true;
			}
		}
		return false;
	}

	//Get this planet's information
	public PlanetInfo getPlanetInfo()
	{
		return new PlanetInfo(owner, numUnits, X, Y, RADIUS, PRODUCTION_TIME);
	}

	public int getRadius()
	{
		return RADIUS;
	}

	private void draw(Graphics g, Color bodyColor, Color textColor)
	{
		g.drawImage(PLANET_IMAGE, X-RADIUS, Y-RADIUS, RADIUS*2, RADIUS*2, null);
		g.setColor(bodyColor);
		Color c = Tournament.getPlayerColor(owner);

		g.setColor(new Color(c.getRed(),
				c.getGreen(),
				c.getBlue(),
				Math.min(50 + numUnits, 255)));
		g.fillOval(X - RADIUS, Y - RADIUS, RADIUS * 2, RADIUS * 2);

		g.setColor(invertColor(MainClass.getPlayerColor(owner)));
		Font font = new Font("Monospaced", Font.BOLD, (int) (0.7 * MIN_SIZE * 2));
		g.setFont (font);
		g.drawString("" + numUnits, (int)(X - 0.7 * MIN_SIZE), (int)(Y + 0.7 * MIN_SIZE));
	}

	private Color invertColor(Color c)
	{
		Color newColor = new Color(255 - c.getRed(),
				255 - c.getGreen(),
				255 - c.getBlue());
		return newColor;
	}

	public void update()
	{
		if(owner != null && updateCnt % PRODUCTION_TIME == 0)
		{
			numUnits++;
		}
		updateCnt++;
	}

	public static ArrayList <Planet> getAllPlanets()
	{
		return planets;
	}

	public Player getOwner()
	{
		return owner;
	}

	public static Player isGameOver()
	{
		Player winner = planets.get(0).getOwner();
		for(int i = 1; i < planets.size(); i++)
		{
			if(winner == null)
			{
				if (planets.get(i).getOwner() != null)
				{
					winner = planets.get(i).getOwner();
				}
			}
			else if(planets.get(i).getOwner() != winner && planets.get(i).getOwner() != null)
			{
				return null;
			}
		}
		if (winner != null && (Fleet.getAllFleets().size() == 0 || winner == Fleet.checkWinner()))
		{
			return winner;
		}
		return null;
	}

	public void invade(Player player, int numUnitsIn)
	{
		if(owner == player)
		{
			numUnits = numUnits + numUnitsIn;
		}
		else
		{
			numUnits = numUnits - numUnitsIn;
			if(numUnits < 0)
			{
				owner = player;
				numUnits = numUnits * -1;
			}
		}
	}

	public int getX()
	{
		return X;
	}

	public int getY()
	{
		return Y;
	}

	public void sendFleet(int numSent, Planet target)
	{
		if (target == this)
		{
			return;
		}
		if(numSent > numUnits)
		{
			numSent = numUnits;
		}

		if (numSent > 0)
		{
			new Fleet(X, Y, numSent, owner, target);
		}
		numUnits -= numSent;
	}
}

/*
 * Wrapper class for `Planet` which allows a player to grab all of a Planet's info at once
 */
class PlanetInfo
{	
	public final int NUM_UNITS, X, Y, RADIUS, PRODUCTION_TIME;
	public final Player OWNER;

	public PlanetInfo (Player ownerIn, int numUnitsIn, int xIn, int yIn, int radiusIn, int production)
	{
		OWNER = ownerIn;
		NUM_UNITS = numUnitsIn;
		X = xIn;
		Y = yIn;
		RADIUS = radiusIn;
		PRODUCTION_TIME = production;
	}

	//Getters
	public Player getOwner()
	{
		return OWNER;
	}
	public int getNumUnits()
	{
		return NUM_UNITS;
	}
	public int getX()
	{
		return X;
	}
	public int getY()
	{
		return Y;
	}
	public int getRadius()
	{
		return RADIUS;
	}
	public int getProductionTime()
	{
		return PRODUCTION_TIME;
	}
	public double getProductionRate()
	{
		return 1.0/PRODUCTION_TIME;
	}

	//Test to see if this PlanetInfo is the same as some other PlanetInfo
	public boolean equals(PlanetInfo other)
	{
		return (OWNER == other.OWNER &&
				NUM_UNITS == other.NUM_UNITS &&
				X == other.X &&
				Y == other.Y &&
				RADIUS == other.RADIUS);
	}
}
