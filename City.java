


public class City 
{
	private int numOccupants;
	private int cost;
	
	public City ()
	{
		numOccupants = 0;
		cost = 10;
	}
	
	public int getCost ()
	{
		return cost;
	}
	
	public int getNumOccupants ()
	{
		return numOccupants;
	}
	
	public void incrementCost ()
	{
		cost += 5;
	}
	
	public boolean isAvailable (int step)
	{
		if (step == numOccupants)
			return false;
		
		return true;
	}
}
