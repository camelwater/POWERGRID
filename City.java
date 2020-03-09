


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
		if (step == 1 && numOccupants == 1)
			return false;
		
		else if (step == 2 && numOccupants == 2)
			return false;
		
		else if (step == 3 && numOccupants == 3)
			return false;
		
		return true;
	}
}
