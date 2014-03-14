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
		int x = Game.genX(RADIUS);
		int y = Game.genY(RADIUS);
		while (Game.isPlanetOverlapping(x, y, RADIUS))
		{
			x = Game.genX(RADIUS);
			y = Game.genY(RADIUS);

		}
		
		//Assign our final variables
		X = x;
		Y = y;
	}

	//Construct a neutrally owned planet with a random starting size.
	public Planet()
	{
		owner = null;
		numUnits = (int) (Math.random() * MAX_NEUTRAL_UNITS);
		RADIUS = (int) (Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE);
		PRODUCTION_TIME = (int) ((1 - ((double) RADIUS - MIN_SIZE) / (MAX_SIZE - MIN_SIZE)) * 
				(MAX_PRODUCE_TIME - MIN_PRODUCE_TIME) + MIN_PRODUCE_TIME); //Calculate the time between productions
		//		setupImage();
		int x = Game.genX(RADIUS);
		int y = Game.genY(RADIUS);
		
		while (Game.isPlanetOverlapping(x, y, RADIUS))
		{
			x = Game.genX(RADIUS);
			y = Game.genY(RADIUS);
		}
		X = x;
		Y = y;
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

	private void draw(Graphics g, Color bodyColor, Color textColor, boolean drawImage)
	{
		if (drawImage)
		{
			g.drawImage(PLANET_IMAGE, X-RADIUS, Y-RADIUS, RADIUS*2, RADIUS*2, null);
		}
		
		//Change transparency depending on number of units
		g.setColor(new Color(bodyColor.getRed(),
				bodyColor.getGreen(),
				bodyColor.getBlue(),
				Math.min(50 + numUnits, 255)));
		g.fillOval(X - RADIUS, Y - RADIUS, RADIUS * 2, RADIUS * 2);

		g.setColor(textColor);
		Font font = new Font("Monospaced", Font.BOLD, (int) (0.7 * MIN_SIZE * 2));
		g.setFont(font);
		g.drawString("" + numUnits, (int)(X - 0.7 * MIN_SIZE), (int)(Y + 0.7 * MIN_SIZE));
	}

	public void update()
	{
		if(owner != null && updateCnt % PRODUCTION_TIME == 0)
		{
			numUnits++;
		}
		updateCnt++;
	}
	
	public Player getOwner()
	{
		return owner;
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
				owner = player; //Pass ownership of the planet to the invading player
				numUnits = -numUnits; //Make the number of units positive again
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

	//Send a fleet from this planet to some other planet
	public void sendFleet(int numSent, Planet target)
	{
		if (target != this)
		{
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
