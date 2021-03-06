//10/6/11 1:27pm

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//Note: Fleets are consider as points
public class Fleet
{
	public static final double SPEED = 2; //Good for quick and dirty scaling
	public static final Font TEXT_FONT = new Font("Monospaced", Font.BOLD, 18); //Font for drawing fleet numbers
	private double x, y; //Coordinates
	private int numUnits; //How many units are associated with that fleet
	private Player owner; //The owner of the fleet
	private Planet target; //The planet the fleet is headed to.

	//Construct a new fleet
	//Precondition: destination is not null
	public Fleet(double xIn, double yIn, int units, Player ownedBy, Planet destination)
	{
		x = xIn;
		y = yIn;
		numUnits = units;
		owner = ownedBy;
		target = destination;
	}

	//Redirect the fleet to a new destination
	public void setDestination(Planet newDest)
	{
		target = newDest;
	}

	public Player getOwner()
	{
		return owner;
	}

	//Move a fleet in the direction of its target planet
	//Returns true if the fleet has reached its target
	public boolean update()
	{
		double a = target.X - x; //Distance to the planet vertically
		double b = target.Y - y; //Distance to the planet horizontally
		double c = Math.sqrt(a * a + b * b); //Straight-line distance to planet

		//Move towards the planet
		x += (a / c) * SPEED;
		y += (b / c) * SPEED;

		if(hasHit())
		{
			target.invade(owner, numUnits);
			return true;
		}
		return false;
	}

	public void draw(Graphics g, Color planetColor, Color textColor)
	{
		int radius = 10 + numUnits / 5;
		int startX = (int) x - radius;
		int startY = (int) y - radius;
		int diam = radius * 2;
		g.setColor(Color.white);
		g.drawOval(startX, startY, diam, diam);
		g.setColor(planetColor);
		g.fillOval(startX, startY, diam, diam);
		g.setColor(textColor);
		g.drawString(String.valueOf(numUnits), (int) (x - 8), (int) (y + 5));
	}

	//Determines if this fleet has hit its target
	private boolean hasHit()
	{
		double dx = target.X - x;
		double dy = target.Y - y;
		return (target.RADIUS * target.RADIUS > dx * dx + dy * dy);
	}

	public FleetInfo getFleetInfo()
	{
		return new FleetInfo(owner, numUnits, x, y, SPEED, (target != null) ? target.getPlanetInfo() : null);
	}

	//A string representation of this fleet
	public String toString() 
	{
		return "Fleet [(" + x + ", " + y + "), numUnits=" + numUnits
		+ ", owner=" + owner + ", target=" + target + "]";
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

