
public class City 
{
	private String name;
	private String region;
	private int numOccupants;
	private int cost;
	private boolean available = true;;
	
	public City (String n)
	{
	    region=n.substring(0,n.indexOf(" "));
		name = n.substring(n.indexOf(" ") + 1);
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
	
	public void incrementNumOccupants (int step)
	{
		if(available)
		{
			numOccupants++;
			incrementCost();
			
		}
		else
		{
			available = false;
		}
	}
	
	public void incrementCost ()
	{
		cost += 5;
	}
	
	public boolean isAvailable (int step, Player p)
	{
		//add region checker
		
		if(numOccupants<step && !p.getCities().contains(this))
			available = true;
		else
			available = false;
		
		//System.out.println(available);
		
		return available;
	}
	
	public String toString ()
	{
		return name + " ";
	}
}















