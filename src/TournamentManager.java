import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;


public class TournamentManager 
{
	private Player[] ais;
	private int[] results;
	private int numMatches;
	private boolean done = false;
	private int firstPlayerIndex = 0, secondPlayerIndex = 1;
	private Player cp1 = null, cp2 = null;
	
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
	
	public Color getColor(Player player)
	{
		if(player != null)
		{
			return player.getColor();
		}
		return Color.DARK_GRAY;
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
	
	
//	public String reportRankings2()
//	{
//		ArrayList<Player> arAis = new ArrayList<Player>(Arrays.asList(ais));
//		ArrayList<Integer> arRes = new ArrayList<Integer>();
//		for (int i : results)
//		{
//			arRes.add(i);
//		}
//		
//		String returnVal ="";
//		for(int i = 0 ; i < ais.length; i++)
//		{
//			//TODO
//			//System.out.println(arRes);
//			int max = 0;
//			for (int j = 1 ; j < arRes.size(); j++)
//			{
//				if (arRes.get(max) < arRes.get(j))
//				{
//					max = j;
//				}
//			}
//			returnVal = returnVal + arAis.get(max).getName() + ":\t"+ arRes.get(max) + "\n";
//			arRes.remove(max);
//			arAis.remove(max);
//		}
//		return returnVal.substring(0, returnVal.length()-1);
//	}
	
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
	
	class Rank implements Comparable <Rank>
	{
		String name;
		int result;

		public Rank(String n, int r)
		{
			name = n;
			result = r;
		}
		@Override
		public int compareTo(Rank r) {
			// TODO Auto-generated method stub
			return r.result - result;
		}
	}
}