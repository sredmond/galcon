//10/6/11 1:27pm

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Fleet 
{
	public static final double SPEED = 2;
	private double x, y;
	private int numUnits;
	private Player owner;
	private Planet target;
	
	private static LinkedList <Fleet> fleets = new LinkedList<Fleet>();
	
	public Fleet(double xIn, double yIn, int units, Player ownedBy, Planet destination)
	{
		x = xIn;
		y = yIn;
		numUnits = units;
		owner = ownedBy;
		target = destination;
		
		fleets.add(this);
	}
	
	public void setDestination(Planet newDest)
	{
		target = newDest;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public static LinkedList <Fleet> getAllFleets()
	{
		return fleets;
	}
	
	public void update()
	{
		double a = target.X - x; //Distance to the planet vertically
		double b = target.Y - y;
		double c = Math.sqrt(a * a + b * b);
		
		x += (a / c) * SPEED;
		y += (b / c) * SPEED;
		
		if(hasHit())
		{
			fleets.remove(this);
			target.invade(owner, numUnits);
		}
	}
	
    public static void updateAll()
    {
    	for(int i = 0; i < fleets.size(); i++)
    	{
    		fleets.get(i).update();
    	}
    }
	
	public static void drawAll(Graphics g)
	{
		for(Fleet f : fleets)
		{
			f.draw(g);
		}
	}
	
	public void draw(Graphics g)
	{
		int arbitraryRadius = 10 + numUnits / 5;
		g.setColor(MainClass.getPlayerColor(owner));
		g.fillOval((int)x - arbitraryRadius, (int)y - arbitraryRadius, arbitraryRadius *2, arbitraryRadius*2);
		g.setFont(new Font("Monospaced", Font.BOLD, 18));
		g.setColor(invertColor(MainClass.getPlayerColor(owner)));
		g.drawString("" +numUnits, (int)(x - 8), (int)(y + 5));
		
	}
	
    private Color invertColor(Color c)
    {
    	Color newColor = new Color(255 - c.getRed(),
    								255 - c.getGreen(),
    								255 - c.getBlue());
    	return newColor;
    }
	
	private boolean hasHit()
	{
		return (target.RADIUS > (Math.sqrt(Math.pow((target.X - x), 2)) + Math.pow((target.Y - y), 2)));
	}
	
	public FleetInfo getFleetInfo()
	{
		return new FleetInfo(owner, numUnits, x, y, SPEED, target.getPlanetInfo());
	}
	
	public static void clearFleets()
    {
    	fleets.clear();
    }
	
	public static Player checkWinner()
	{
		if (fleets.size() == 0)
		{
			return null;
		}
		Player winner = fleets.get(0).owner;
		Iterator<Fleet> it = fleets.iterator();
		while (it.hasNext())
		{
			Player p = it.next().owner;
			if (winner == null)
			{
				if (p != null)
				{
					winner = p;
				}
			}
			else if (p != winner && p != null)
			{
				return null;
			}
		}
		return winner;
	}
}


class FleetInfo
{
	public final int NUM_UNITS;
	public final Player OWNER;
	public final double X, Y, SPEED;
	public final PlanetInfo TARGET;
	
	public FleetInfo(Player ownerIn, int numUnitsIn, double xIn, double yIn, double speedIn, PlanetInfo targetIn)
	{
		OWNER = ownerIn;
		NUM_UNITS = numUnitsIn;
		X = xIn;
		Y = yIn;
		TARGET = targetIn;
		SPEED = speedIn;
	}
	
	public Player getOwner()
	{
		return OWNER;
	}
	
	public int getNumUnits()
	{
		return NUM_UNITS;
	}
	
	public double getX()
	{
		return X;
	}
	
	public double getY()
	{
		return Y;
	}
	
	public PlanetInfo getTarget()
	{
		return TARGET;
	}
	
	/*
	 * Do these need to be var.get()?
	 */
	public boolean equals(FleetInfo f)
	{
		if ( OWNER == f.OWNER &&
		 NUM_UNITS == f.NUM_UNITS && 
		         X == f.X && 
		         Y == f.Y &&
		     SPEED == f.SPEED &&
		   TARGET.equals(f.TARGET))
			
			return true;
		
		else return false;
	}

}

