
import java.util.HashMap;

public class Map 
{
	private HashMap <City, HashMap <City, Integer>> cities;
	
	public Map (HashMap <City, HashMap <City, Integer>> connections)
	{
		cities = connections;
	}
	
	public int getCost (City start, City end)
	{
		return cities.get(start).get(end);
	}
	
	public HashMap <City, Integer> getConnections (City x)
	{
		return cities.get(x);
	}
}
