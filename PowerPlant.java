
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class PowerPlant implements Comparable
{
	private int ID;
	private int numPowered;
	private ArrayList <Resource> cost;
	
	public PowerPlant (int num, int id, ArrayList <Resource> c)
	{
		ID = id;
		numPowered = num;
		cost = c;
	}
	
	public int getID ()
	{
		return ID;
	}
	
	public int getNumPowered ()
	{
		return numPowered;
	}
	
	public ArrayList <Resource> getCost ()
	{
		return cost;
	}
	
	public String toString ()
	{
		return ID + " " + numPowered + " " + cost.toString();
	}
	
	public int compareTo (Object obj)
	{
		int x = getID();
		int y = ((PowerPlant) obj).getID();
		
		return x - y;
	}
}