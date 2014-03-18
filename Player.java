//3/13/14
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * The abstract class that represents an AI.
 */
public abstract class Player extends Thread
{
	public abstract Color getColor(); //Get the AI's color
	public abstract void makeMove(); //The AI's main brain
	public abstract String getPlayerName(); //Get the AI's name
	
	public static Game currentGame;
	
	public void run()
	{
		while (true)
		{
			makeMove();
		}
	}
	public Set<PlanetInfo> getAllPlanetInfo()
	{
		Set<PlanetInfo> info = new HashSet<PlanetInfo>();
		for (Planet p: currentGame.getAllPlanets())
		{
			info.add(p.getPlanetInfo());
		}
		return info;
	}
	
	public Set<FleetInfo> getAllFleetInfo()
	{
		Set<FleetInfo> info = new HashSet<FleetInfo>();
		for(Fleet f: currentGame.getAllFleets())
		{
			info.add(f.getFleetInfo());
		}
		return info;
	}
	
	public Set<PlanetInfo> getEnemyPlanetInfo()
	{
		Set<PlanetInfo> enemyInfo = new HashSet<PlanetInfo>();
		for (Planet p: currentGame.getAllPlanets())
		{
			if (p.getOwner() != null && p.getOwner() != this)
			{
				enemyInfo.add(p.getPlanetInfo());
			}
		}
		return enemyInfo;
	}
	
	public Set<FleetInfo> getEnemyFleetInfo()
	{
		Set<FleetInfo> enemyFleets = new HashSet<FleetInfo>();
		for(Fleet f: currentGame.getAllFleets())
		{
			if (f.getOwner() != this)
			{
				enemyFleets.add(f.getFleetInfo());
			}
		}
		return enemyFleets;
	}
	
	public Set<PlanetInfo> getMyPlanetInfo()
	{
		Set<PlanetInfo> myInfo = new HashSet<PlanetInfo>();
		for (Planet p: currentGame.getAllPlanets())
		{
			if (p.getOwner() == this)
			{
				myInfo.add(p.getPlanetInfo());
			}
		}
		return myInfo;
	}
	
	public Set<FleetInfo> getMyFleetInfo()
	{
		Set<FleetInfo> myFleets = new HashSet<FleetInfo>();
		for(Fleet f: currentGame.getAllFleets())
		{
			if (f.getOwner() == this)
			{
				myFleets.add(f.getFleetInfo());
			}
		}
		return myFleets;
	}
	
	public Set<PlanetInfo> getNeutralPlanetInfo()
	{
		Set<PlanetInfo> myInfo = new HashSet<PlanetInfo>();
		for (Planet p: currentGame.getAllPlanets())
		{
			if (p.getOwner() == null)
			{
				myInfo.add(p.getPlanetInfo());
			}
		}
		return myInfo;
	}
	
	//Convenience functions to send fleets or change fleet target based on info
	public void sendFleet(PlanetInfo originInfo, int numUnits, PlanetInfo targetInfo)
	{
		Planet origin = getPlanetFromInfo(originInfo);
		Planet target = getPlanetFromInfo(targetInfo);
		if (origin != null && target != null)
		{
			if (origin.getOwner() == this)
			{
//				System.out.println("Sending fleet from " + origin + " to " + target);
				origin.sendFleet(numUnits, target);
			}
		}
	}
	
	public void changeFleetTarget(FleetInfo fi, PlanetInfo targetInfo)
	{
		if (fi.getOwner() == this)
		{
			Fleet f = getFleetFromInfo(fi);
			Planet p = getPlanetFromInfo(targetInfo);
			if (f != null && p != null)
			{
				f.setDestination(p);
			}
		}
	}
	
	//Subclasses can't use these
	private Planet getPlanetFromInfo(PlanetInfo pInfo)
	{
		for (Planet p: currentGame.getAllPlanets())
		{
			if (p.getPlanetInfo().equals(pInfo))
			{
				return p;
			}
		}
		return null;
	}
	
	private Fleet getFleetFromInfo(FleetInfo fInfo)
	{
		for (Fleet f: currentGame.getAllFleets())
		{
			if (f.getFleetInfo().equals(fInfo))
			{
				return f;
			}
		}
		return null;
	}
	
	//A string representation of a player
	public String toString()
	{
		return "Player [name=" + getPlayerName() + "]";
	}
}
