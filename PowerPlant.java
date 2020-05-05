
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Enums.Type;

@SuppressWarnings({ "rawtypes", "unused" })
public class PowerPlant implements Comparable
{
	private BufferedImage img;
	private int ID;
	private int numPowered;
	private ArrayList <String> cost;
	
	private String name;
	
	private boolean powered;
	
	public int maxCapacity = -1;
	public String capType = "not set";
	
	public PowerPlant (int num, int id, ArrayList <String> c)
	{
		ID = id;
		numPowered = num;
		cost = c;
		name = "";
		setCapacity();
		System.out.println("power plant "+ id+ " costs " + cost.toString());
	}
	
	public PowerPlant (String n, int id)
	{
		name = n;
		ID = id;
		numPowered = 0;
		cost = null;
	}
	public int getCapacity()
	{
		return maxCapacity;
	}
	public String capType()
	{
		System.out.println("cost: "+cost+", type: "+capType);
		System.out.println("capacity: "+maxCapacity);
		return capType;
	}
	public void setCapacity()
	{
		if(cost.size()!=0)
		{
			if(cost.contains("COAL") && cost.contains("OIL"))
			{
				capType = "Hybrid";
				maxCapacity = cost.size();
			}
			else if (cost.contains("COAL"))
			{
				capType = "Coal";
				maxCapacity = cost.size()*2;
			}
			else if (cost.contains("OIL"))
			{
				capType = "Oil";
				maxCapacity = cost.size()*2;
			}
			else if (cost.contains("TRASH"))
			{
				capType = "Trash";
				maxCapacity = cost.size()*2;
			}
			else if (cost.contains("URANIUM"))
			{
				capType = "Uran";
				maxCapacity = cost.size()*2;
			}
		}
		else
		{
			capType = "Free";
			maxCapacity = 0;
		}
	}
	public String getName ()
	{
		return name;
	}
	
	public int getID ()
	{
		return ID;
	}
	
	public int getNumPowered ()
	{
		return numPowered;
	}
	
	public void setPoweredStatus (boolean status)
	{
		powered = status;
	}
	
	public boolean isPowered ()
	{
		return powered;
	}
	
	public ArrayList <String> getCost ()
	{
		return cost;
	}
	
	public String toString ()
	{
		if (numPowered != 0 && cost != null)
			return ID + " " + numPowered + " " + cost.toString();
		
		return "";
	}
	
	public BufferedImage getImage()
	{
		return img;
	}
	
	public void setImage() throws IOException
	{
		img = ImageIO.read(getClass().getResource(ID+".png"));
	}
	
	public int compareTo (Object obj)
	{
		int x = getID();
		int y = ((PowerPlant) obj).getID();
		
		return x - y;
	}
}
