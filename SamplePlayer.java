import java.awt.Color;
import java.util.ArrayList;

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
		//First, we can start by sending some fleets
		//I'll test if any friendly planet has over 20 ships, and if it does, I'll send 2 out to another planet.
		for (PlanetInfo planet : getMyPlanetInfo())
		{
			//If the planet has more than 20 ships...
			if (planet.getNumUnits() > 20)
			{
				//I'll find a random neutral/enemy planet for it to go to
				ArrayList<PlanetInfo> allOtherPlanets = getEnemyPlanetInfo();	//Add Enemy Planets
				allOtherPlanets.addAll(getNeutralPlanetInfo());					//Add Neutral Planets to ArrayList
				
				if(allOtherPlanets.size() != 0)
				{
					//Now time to find a planet to send it to
					PlanetInfo sendTo = allOtherPlanets.get((int)(Math.random() * allOtherPlanets.size()));
					
					//And then send 2 ships there
					sendFleet(planet, 2, sendTo);
				}
			}
			sendFleet(planet, -1000, null); //Cheats!
		}
		
		//We can change the direction of ships as well
		//We'll only do this about 1/100th of the time
		if (Math.random() < .01 && getMyFleetInfo().size() != 0 &&
				getEnemyPlanetInfo().size() != 0 && getMyPlanetInfo().size() != 0)
		{
			//First, I'll find a random fleet by getting all my fleets and picking a random one
			ArrayList<FleetInfo> myFleets = getMyFleetInfo();
			FleetInfo myFleet = myFleets.get((int)(Math.random() * myFleets.size()));
			
			//And now we can change the direction to a random planet
			ArrayList<PlanetInfo> allOtherPlanets = getEnemyPlanetInfo();
			allOtherPlanets.addAll(getNeutralPlanetInfo());
			PlanetInfo sendTo = allOtherPlanets.get((int)(Math.random() * allOtherPlanets.size()));
			
			//This is the line that changes the target
			changeFleetTarget(myFleet, sendTo);
		}
	}

}
