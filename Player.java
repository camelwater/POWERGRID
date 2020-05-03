
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Enums.Type;

@SuppressWarnings("rawtypes")
public class Player implements Comparable
{
	private ArrayList <PowerPlant> PowerPlants;
	private ArrayList <City> cities;
	private HashMap <Type, Stack <Resource>> resources;
	private String name;
	private int cash;
	private boolean finished;
	
	public Player (String n)
	{
		PowerPlants = new ArrayList <PowerPlant> ();
		
		cities = new ArrayList <City> ();
		
		resources = new HashMap <Type, Stack <Resource>> ();
		
		cash = 50;
		
		name = n;
		
		resources.put(Type.Coal,  new Stack<Resource>());
		resources.put(Type.Oil,  new Stack<Resource>());
		resources.put(Type.Trash,  new Stack<Resource>());
		resources.put(Type.Uranium,  new Stack<Resource>());
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
	
	public boolean isFinished()
	{
		return finished;
	}
	public int balance ()
	{
		return cash;
	}
	
	public ArrayList <PowerPlant> getPowerPlants ()
	{
		return PowerPlants;
	}
	
	public HashMap <Type, Stack <Resource>> getResources ()
	{
		return resources;
	}

	public int getTrash() 
	{
		return resources.get(Type.Trash).size();
	}
	
	public int getUranium()
	{
		return resources.get(Type.Uranium).size();
	}
	
	public int getCoal()
	{
		return resources.get(Type.Coal).size();
	}
	
	public int getOil()
	{
		return resources.get(Type.Oil).size();
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
		
		x.incrementNumOccupants();
		
		x.incrementCost();
	}
	
	public void buyResources (Resource res)
	{
		resources.get(res.getType()).push(res);
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
