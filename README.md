#Galcon

##Installation:
1. Create a Project in Eclipse called ?Galcon?
2. Drag all of the .jpg images from the Galcon/Images/ folder into the main folder of the ?Galcon? project in Eclipse.
3. Drag all of the .java files from the Galcon/Code/ folder into the Galcon/src/ folder in Eclipse.

###How to Start:
Just select the MainClass class and press run. You should see many planets on the screen with numbers on each of them. There should, at the start, be two planets of random color sending out small circles with numbers in them. The small circles flying around are called ?fleets,? which are comprised of ships.

When you create an AI, you can sub it in for one of the SamplePlayers in the first assignment lines (as TEAM1 or TEAM2). You can also change the number of games that are run at a time by changing ?NUM_GAMES?.

###What is Galcon and How Do I Play?
Glacon is an RTS (Real Time Strategy) game that is similar to risk in that the goal is to capture all of your enemy?s planets.

###From Wikipedia:
Players start with 1 large "home planet" and send off ships to conquer other planets around them. The numbers on each planet indicate how many ships it will take to conquer them. The numbers on a player's own planet indicate the amount of ships that their planet holds. Each planet a player owns produces ships for that player with more ships at a faster rate produced depending on the planet's size. Players can select what percentage of the ships to send from a planet and players can redirect ships in mid-flight. The aim of the game is to defeat the other player by eliminating all of the enemy planets.

###To play for yourself
* PC/Mac: http://www.galcon.com/flash/
* iPhone/iPad: Search 'Galcon' in the App Store

##Usable Methods:
###Player Class
  ArrayList<PlanetInfo> getAllPlanetInfo()
  ArrayList<FleetInfo>getAllFleetInfo()
  ArrayList<PlanetInfo> getEnemyPlanetInfo()
  ArrayList<FleetInfo> getEnemyFleetInfo()
  ArrayList<PlanetInfo> getMyPlanetInfo()
  ArrayList<FleetInfo> getMyFleetInfo()
  ArrayList<PlanetInfo> getNeutralPlanets()
  void sendFleet(PlanetInfo origin, int numUnits, PlanetInfo target)
  void changeFleetTarget(FleetInfo f, PlanetInfo target)

###PlanetInfo Class
* Player getOwner()
* int getNumUnits()
* int getX()
* int getY()
* int getRadius()
* int getProductionTime() <- number of tics you have to wait to produce 1 unit
* double getProductionRate() <- units per tic
* boolean equals(PlanetInfo p)

###FleetInfo Class
* Player getOwner()
* int getNumUnits()
* double getX()
* double getY()
* PlanetInfo getTarget()
* boolean equals(FleetInfo p)
  
###For More Information:
To see an example of an AI, open the ?SamplePlayer? class. It will cover what you need to do to create an AI.