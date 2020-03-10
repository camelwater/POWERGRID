
import java.util.ArrayList;
import java.util.HashMap;
import Enums.Type;

public class Player 
{
	private ArrayList <PowerPlant> PowerPlants;
	private ArrayList <City> cities;
	private HashMap <Type, Resource> resources;
	private int cash;
	
	public Player ()
	{
		PowerPlants = new ArrayList <PowerPlant> ();
		
		cities = new ArrayList <City> ();
		
		resources = new HashMap <Type, Resource> ();
		
		cash = 50;
	}
	
	public void addCash (int x)
	{
		cash += x;
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
	
	public void buyPowerPlant (PowerPlant x)
	{
		if (PowerPlants.size() != 3)
		{
			PowerPlants.add(x);
			pay(x.getID());
		}
	}
}











































