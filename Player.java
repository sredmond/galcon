//3/13/14
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

/*
 * The abstract class that represents an AI.
 */
public abstract class Player extends Thread
{
	public abstract Color getColor(); //Get the AI's color
	public abstract void makeMove(); //The AI's main brain
	public abstract String getPlayerName(); //Get the AI's name
	
	public Set<PlanetInfo> getAllPlanetInfo()
	{
		Set<PlanetInfo> info = new HashSet<PlanetInfo>();
		for (Planet p: Game.getAllPlanets())
		{
			info.add(p.getPlanetInfo());
		}
		return info;
	}
	
	public Set<FleetInfo> getAllFleetInfo()
	{
		Set<FleetInfo> info = new HashSet<FleetInfo>();
		for(Fleet f: Game.getAllFleets())
		{
			info.add(f.getFleetInfo());
		}
		return info;
	}
	
	public Set<PlanetInfo> getEnemyPlanetInfo()
	{
		Set<PlanetInfo> enemyInfo = new HashSet<PlanetInfo>();
		for (Planet p: Game.getAllPlanets())
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
		for (Fleet f: Game.getAllFleets())
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
		for (Planet p: Game.getAllPlanets())
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
		for (Fleet f : Game.getAllFleets())
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
		for (Planet p: Game.getAllPlanets())
		{
			if (p.getOwner() == null)
			{
				myInfo.add(p.getPlanetInfo());
			}
		}
		return myInfo;
	}
	
	//Convenience functions to send fleets or change fleet target based on info
	public void sendFleet(PlanetInfo origin, int numUnits, PlanetInfo target)
	{
		if (origin.getOwner() == this)
			getPlanetFromInfo(origin).sendFleet(numUnits, getPlanetFromInfo(target));
	}
	
	public void changeFleetTarget(FleetInfo f, PlanetInfo target)
	{
		if (f.getOwner() == this)
			getFleetFromInfo(f).setDestination(getPlanetFromInfo(target));
	}
	
	//Subclasses can't use these
	private Planet getPlanetFromInfo(PlanetInfo pInfo)
	{
		for (Planet p: Game.getAllPlanets())
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
		for (Fleet f: Game.getAllFleets())
		{
			if (f.getFleetInfo().equals(fInfo))
			{
				return f;
			}
		}
		return null;
	}
}
