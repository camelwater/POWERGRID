
public class City 
{
	private String name;
	private String region;
	private int numOccupants;
	private int cost;
	
	public City (String n)
	{
	    region=n.substring(0,n.indexOf(" "));
		name = n.substring(n.indexOf(" "));
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
	
	public String getName ()
	{
		return name;
	}
	
	public String getRegion()
	{
	    return region;
	}
	public void incrementNumOccupants ()
	{
		numOccupants++;
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
	
	public String toString ()
	{
		return name + " ";
	}
}















