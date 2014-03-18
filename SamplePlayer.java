import java.awt.Color;
import java.util.Set;

public class SamplePlayer extends Player
{
	/*
	 * DEFINE THESE FOR YOUR AI
	 */
	final String NAME = "Sample Player";
	final Color COLOR = new Color((int)(Math.random() * 255), (int)(Math.random() * 225), (int)(Math.random() * 225));
	//End definitions
	
	public Color getColor() 
	{
		return COLOR;
	}

	public String getPlayerName()
	{
		return NAME;
	}

	/*
	 * This is where you will make your move. This method is called every "tic", the unit of time between each repaint().
	 * The goal here is to call the sendFleet() and changeFleetTarget() methods. These will allow us to send and change the 
	 * direction of fleets.
	 */
	public void makeMove() 
	{
		try {
			sleep(100); //Stall
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//First, we can start by sending some fleets
		//I'll test if any friendly planet has over 20 ships, and if it does, I'll send 2 out to another planet.
		for (PlanetInfo planetInfo: getMyPlanetInfo())
		{
			//If the planet has more than 20 ships...
			if (planetInfo.getNumUnits() > 20)
			{
				//I'll find a random neutral/enemy planet for it to go to
				Set<PlanetInfo> notMyPlanets = getEnemyPlanetInfo();
				notMyPlanets.addAll(getNeutralPlanetInfo());	//Enemy planets and neutral planets
				
				sendFleet(planetInfo, 2, getRandomPlanetInfo(notMyPlanets));
//				System.out.println(getName() + " made a move!");
//				sendFleet(planetInfo, -1000, getRandomPlanetInfo(notMyPlanets));
			}
		}
		
		//We can change the direction of ships as well
		//We'll only do this about 1/100th of the time
//		if (Math.random() < .01 && getMyFleetInfo().size() != 0 &&
//				getEnemyPlanetInfo().size() != 0 && getMyPlanetInfo().size() != 0)
//		{
//			//First, I'll find a random fleet by getting all my fleets and picking a random one
//			FleetInfo myFleet = getRandomFleetInfo(getMyFleetInfo());
//			
//			//And now we can change the direction to a random planet
//			Set<PlanetInfo> allOtherPlanets = getEnemyPlanetInfo();
//			allOtherPlanets.addAll(getNeutralPlanetInfo()); //Enemy planets and neutral planets
//
//			PlanetInfo sendTo = getRandomPlanetInfo(allOtherPlanets);
//			
//			//This is the line that changes the target
//			changeFleetTarget(myFleet, sendTo);
//		}
	}
	
	//The one downside to using HashSets: no random access
	//Just some convenience function to replace the functionality of ArrayList
	//Note: it's OK if planetInfos is null
	private static PlanetInfo getRandomPlanetInfo(Set<PlanetInfo> planetInfos)
	{
		int n = (int) (Math.random() * planetInfos.size());
		int i = 0;
		for (PlanetInfo pi: planetInfos)
		{
			if (i == n)
			{
				return pi;
			}
			i++;
		}
		return null;
	}
	private static FleetInfo getRandomFleetInfo(Set<FleetInfo> fleetInfos)
	{
		int n = (int) (Math.random() * fleetInfos.size());
		int i = 0;
		for (FleetInfo fi: fleetInfos)
		{
			if (i == n)
			{
				return fi;
			}
			i++;
		}
		return null;
	}

}
