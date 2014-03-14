import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Small fixes @sredmond
 * Changes fleet data type to hashset from linkedlist
 * fixed arithmetic errors (math.sqrt)
 * sped up int to string conversion
 * Fixes game-breaking cheats (send negative numbers to sendFleet)
 */

public class Tournament 
{
	private final static Player TEAM_1 = new SamplePlayer();
	private final static Player TEAM_2 = new SamplePlayer();
	private final static int NUM_GAMES = 2;
	
	private Player[] ais = {TEAM_1, TEAM_2};
	private int[] results; //Index of arrays that 
	private int numMatches;
	private boolean done = false;
	private int firstPlayerIndex = 0, secondPlayerIndex = 1;
	private Player cp1 = null, cp2 = null;
	
	public static void main(String args[])
	{
		new TournamentManager(); //Begin the tournament!
	}
	
	public TournamentManager(Player[] ais, int numMatchesEachWay)
	{
		if (ais.length < 2)
		{
			System.err.println("You need at least 2 ais to play.");
		}
		this.ais = ais;
		numMatches = numMatchesEachWay;
		results = new int[ais.length];
		cp1 = ais[firstPlayerIndex];
		cp2 = ais[secondPlayerIndex];
	}
	
	public Player[] getCurrentPlayers()
	{
		Player[] returnVar =  new Player[2];
		returnVar[0] = cp1;
		returnVar[1] = cp2;
		return returnVar;
	}
	
	public Player[] getNextMatch()
	{

		if (numMatches > 0)
		{
			Player[] returnVar =  new Player[2];
			returnVar[0] = ais[firstPlayerIndex];
			returnVar[1] = ais[secondPlayerIndex];
			cp1 = returnVar[0];
			cp2 = returnVar[1];
			if (firstPlayerIndex == ais.length - 2)//last case
			{
				firstPlayerIndex = 0;
				secondPlayerIndex = 1;
				numMatches--;
				if(numMatches ==0 )
				{
					done = true;
				}
			}
			else
			{
				if (secondPlayerIndex == ais.length - 1)//last case of each loop
				{
					firstPlayerIndex++;
					secondPlayerIndex = firstPlayerIndex+1;
				}
				else//all is good
				{
					secondPlayerIndex++;
				}
			}
			return returnVar;
		}
		else
		{
			cp1 = null;
			cp2 = null;
			done = true;
			return null;
		}
	}
	
	public Player[] getPlayers()
	{
		return ais;
	}
	
	public boolean isDone()
	{
		return Planet.isGameOver() != null && done;
	}
	
	public void reportResult(Player winner)
	{
		for (int i=0; i<ais.length; i++)
		{
			if (ais[i] == winner)
			{
				results[i]++;
				System.out.println("Win for: "+ winner.getName());
				return;
			}
		}
		System.out.println("Error in reporting winner. Player is nonexistent");
	}
	
	public String reportRankings()//This one actually works!
	{
		Rank[] ranking = getRanks();
		String s = "";
		for (int i=0; i<ranking.length; i++)
		{
			s = s + ranking[i].name + "\t" + ranking[i].result + "\n";
		}
		return s.substring(0, s.length()-1);
	}
	
	public String[] reportRankingsArray()//a graphics-friendly version of reportRankings
	{
		Rank[] ranking = getRanks();
		String[] str = new String[ranking.length];
		
		for (int i=0; i<ranking.length; i++)
		{
			str[i] = ranking[i].name + "\t\t\t\t" + ranking[i].result;
		}
		return str;
	}
	
	private Rank[] getRanks()
	{
		Rank[] ranking = new Rank[ais.length];
		for (int i=0; i< ais.length; i++)
		{
			ranking[i] = new Rank(ais[i].getName(), results[i]);
		}
		Arrays.sort(ranking);
		return ranking;
	}

	//Get a player's color, or return DARK_GRAY if player is null
	public static Color getPlayerColor(Player player)
	{
		return (player != null) ? player.getColor() : Color.DARK_GRAY;
	}
}
class Rank implements Comparable <Rank>
{
	String name;
	int result;

	public Rank(String n, int r)
	{
		name = n;
		result = r;
	}
	public int compareTo(Rank r) 
	{
		return r.result - result;
	}
}