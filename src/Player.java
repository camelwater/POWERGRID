
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.imageio.ImageIO;

@SuppressWarnings("rawtypes")
public class Player implements Comparable
{
	private ArrayList <PowerPlant> PowerPlants;
	private ArrayList <City> cities;
	private HashMap <Type, Stack <Resource>> resources;
	
	private int tCash = 0;
	
	private HashMap<Type, Stack<Resource>> tempRes = new HashMap<Type, Stack<Resource>>() ;
	private String name;
	
	private String house;
	
	private int cash;
	public boolean finished = false;
	
	public Player (String n)
	{
		PowerPlants = new ArrayList <PowerPlant> ();
		
		cities = new ArrayList <City> ();
		
		resources = new HashMap <Type, Stack <Resource>> ();
		
		resources.put(Type.Coal,  new Stack<Resource>());
		
		resources.put(Type.Oil,  new Stack<Resource>());
		
		resources.put(Type.Trash,  new Stack<Resource>());
		
		resources.put(Type.Uranium,  new Stack<Resource>());
		
		cash = 50;
		
		name = n;
		
		tempRes = resources;
		
		setHouse();
	}
	
	public void setHouse()
	{
		if(name.equals("1"))
			house = "red";
		if(name.equals("2"))
			house = "blue";
		if(name.equals("3"))
			house = "green";
		if(name.equals("4"))
			house = "yellow";
	}
	
	public void addTCash(int x)
	{
		//tCash = 0;
		tCash=x;
		cash+=x;
	}
	
	public int getTCash()
	{
		return tCash;
	}
	
	public BufferedImage getPic()
	{
		if(name.equals("1"))
			try {
				return ImageIO.read(getClass().getResource("red_house.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(name.equals("2"))
			try {
				return ImageIO.read(getClass().getResource("blue_house.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(name.equals("3"))
			try {
				return ImageIO.read(getClass().getResource("green_house.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(name.equals("4"))
			try {
				return ImageIO.read(getClass().getResource("yellow_house.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	public String getHouse()
	{
		return house;
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
	
	public void finished()
	{
		finished = true;
	}
	public boolean maxPP()
	{
		if(PowerPlants.size()==4)
			return true;
		return false;
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
	
	public boolean bid(int amount)
	{
		if(amount<=cash)
		{
			//pay(amount);
			return true;
		}
		return false;
	}
	
	public boolean buyCity (City c, int cost)
	{
		if(cost<=cash)
		{
			return true;
		}
		return false;
		
	}
	
	public void buyResources (Resource r)
	{
		resources.get(r.getType()).push(r);
		tempRes = resources;
	}
	
	public void buyPowerPlant (PowerPlant x, int cost)
	{
		if (PowerPlants.size() <4)
		{
			PowerPlants.add(x);
			pay(cost);
		}
	}
	
	public HashMap <Type, Stack <Resource>> getTempRes()
	{
		return tempRes;
	}
	
	public int compareTo (Object obj)
	{       
        int x = getCities().size();
		int y = ((Player) obj).getCities().size();
		
		if (x != y)
			return x - y;
		x = getPowerPlants().size();
		y = ((Player)obj).getPowerPlants().size();
		
		if(x!=y)
			return x-y;
		
		x = getPowerPlants().get(0).getID();
		y = ((Player) obj).getPowerPlants().get(0).getID();
		
		return x - y;
	}

	public String toString ()
	{
		return "Player " + name;
	}
}
