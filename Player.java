//3/13/14
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * The abstract class that represents an AI.
 */
public abstract class Player extends Thread
{
	public abstract Color getColor(); //Get the AI's color
	public abstract void makeMove(); //The AI's main brain
	public abstract String getPlayerName(); //Get the AI's name
	
	public ArrayList<PlanetInfo> getAllPlanetInfo()
	{
		ArrayList<Planet> planets = Planet.getAllPlanets();
		ArrayList<PlanetInfo> info = new ArrayList<PlanetInfo>();
		for (Planet p : planets)
		{
			info.add(p.getPlanetInfo());
		}
		return info;
	}
	
	public ArrayList<FleetInfo>getAllFleetInfo()
	{
		LinkedList<Fleet> fleets = Fleet.getAllFleets();
		ArrayList<FleetInfo> info = new ArrayList<FleetInfo>();
		for(Fleet f : fleets)
		{
			info.add(f.getFleetInfo());
		}
		return info;
	}
	
	public ArrayList<PlanetInfo> getEnemyPlanetInfo()
	{
		ArrayList<PlanetInfo> enemyInfo = new ArrayList<PlanetInfo>();
		for (Planet p : Planet.getAllPlanets())
		{
			if (p.getOwner() != null && p.getOwner() != this)
			{
				enemyInfo.add(p.getPlanetInfo());
			}
		}
		return enemyInfo;
	}
	
	public ArrayList<FleetInfo> getEnemyFleetInfo()
	{
		ArrayList<FleetInfo> enemyFleets = new ArrayList<FleetInfo>();
		for (Fleet f : Fleet.getAllFleets())
		{
			if (f.getOwner() != this)
			{
				enemyFleets.add(f.getFleetInfo());
			}
		}
		return enemyFleets;
	}
	
	public ArrayList<PlanetInfo> getMyPlanetInfo()
	{
		ArrayList<PlanetInfo> myInfo = new ArrayList<PlanetInfo>();
		for (Planet p : Planet.getAllPlanets())
		{
			if (p.getOwner() == this)
			{
				myInfo.add(p.getPlanetInfo());
			}
		}
		return myInfo;
	}
	
	public ArrayList<FleetInfo> getMyFleetInfo()
	{
		ArrayList<FleetInfo> myFleets = new ArrayList<FleetInfo>();
		for (Fleet f : Fleet.getAllFleets())
		{
			if (f.getOwner() == this)
			{
				myFleets.add(f.getFleetInfo());
			}
		}
		return myFleets;
	}
	
	public ArrayList<PlanetInfo> getNeutralPlanetInfo()
	{
		ArrayList<PlanetInfo> myInfo = new ArrayList<PlanetInfo>();
		for (Planet p : Planet.getAllPlanets())
		{
			if (p.getOwner() == null)
			{
				myInfo.add(p.getPlanetInfo());
			}
		}
		return myInfo;
	}
	
	public void sendFleet(PlanetInfo origin, int numUnits, PlanetInfo target)
	{
		if (origin.getOwner() == this)
			getPlanet(origin).sendFleet(numUnits, getPlanet(target));
	}
	
	public void changeFleetTarget(FleetInfo f, PlanetInfo target)
	{
		if (f.getOwner() == this)
			getFleet(f).setDestination(getPlanet(target));
	}
	
	
	
	private Planet getPlanet(PlanetInfo pInfo)
	{
		int index = 0;
		while (true)
		{
			if (index == Planet.getAllPlanets().size())
			{
				return null;
			}
			Planet p = Planet.getAllPlanets().get(index);
			if (p.getPlanetInfo().equals(pInfo))
			{
				return p;
			}
			index++;
		}
	}
	
	private Fleet getFleet(FleetInfo fInfo)
	{
		int index = 0;
		while (true)
		{
			if (index == Fleet.getAllFleets().size())
			{
				return null;
			}
			Fleet f = Fleet.getAllFleets().get(index);
			if (f.getFleetInfo().equals(fInfo))
			{
				return f;
			}
			index++;
		}
	}
}
