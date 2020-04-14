
import java.util.ArrayList;
import java.util.HashMap;
import Enums.Type;

@SuppressWarnings("rawtypes")
public class Player implements Comparable
{
	private ArrayList <PowerPlant> PowerPlants;
	private ArrayList <City> cities;
	private HashMap <Type, Resource> resources;
	private int cash;
	private String name;
	
	public Player (String n)
	{
		PowerPlants = new ArrayList <PowerPlant> ();
		
		cities = new ArrayList <City> ();
		
		resources = new HashMap <Type, Resource> ();
		
		cash = 50;
		name = n;
	}
	
	public void addCash (int x)
	{
		cash += x;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void pay (int x)
	{
		cash -= x;
	}
	
	public int balance ()
	{
		return cash;
	}
	
	public ArrayList <PowerPlant> getPowerPlants ()
	{
		return PowerPlants;
	}
	
	public HashMap <Type, Resource> getResources ()
	{
		return resources;
	}
	
	public ArrayList <City> getCities ()
	{
		return cities;
	}
	
	public PowerPlant discard (PowerPlant x)
	{
		PowerPlants.remove(x);
		
		return x;
	}
	
	public void buyCity (City x, int cost)
	{
		cities.add(x);
		
		pay(cost);
	}
	
	public void buyResources (Resource res, int cost)
	{
		resources.put(res.getType(), res);
		
		pay(cost);
	}
	
	public void buyPowerPlant (PowerPlant x, int cost)
	{
		if (PowerPlants.size() != 3)
		{
			PowerPlants.add(x);
			pay(cost);
		}
	}
	
	public int compareTo (Object obj)
	{       
        int x = getPowerPlants().size();
		int y = ((Player) obj).getPowerPlants().size();
		
		if (x != y)
			return x - y;
		
		x = getPowerPlants().get(0).getID();
		y = ((Player) obj).getPowerPlants().get(0).getID();
		
		return x - y;
	}
}
