
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

@SuppressWarnings({ "rawtypes", "unused" })
public class PowerPlant implements Comparable
{
	private BufferedImage img;
	private int ID;
	private int numPowered;
	private ArrayList <String> cost;
	
	private String name;
	
	private boolean powered;
	
	private ArrayList<Resource> storage = new ArrayList<Resource>();
	public int maxCapacity = -1;
	public String capType = "not set";
	
	public PowerPlant (int num, int id, ArrayList <String> c)
	{
		ID = id;
		numPowered = num;
		cost = c;
		name = "";
		setCapacity();
		//System.out.println("power plant "+ id+ " costs " + cost.toString());
	}
	
	public PowerPlant (String n, int id)
	{
		name = n;
		ID = id;
		numPowered = 0;
		cost = null;
	}
	public ArrayList<Resource> getStorage()
	{
		return storage;
	}
	public int getCapacity()
	{
		return maxCapacity;
	}
	
	public String capType()
	{
//		System.out.println("cost: "+cost+", type: "+capType);
//		System.out.println("capacity: "+maxCapacity);
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
	public boolean extract(Player p, String x)
	{
		
		if(capType.equals("Oil"))
		{
			if(storage.size()!=0)
			{
				p.getResources().get(Type.Oil).push(storage.remove(storage.size()-1));
				return true;
			}
		}
		else if(capType.equals("Coal"))
		{
			if(storage.size()!=0)
			{
				p.getResources().get(Type.Coal).push(storage.remove(storage.size()-1));
				return true;
			}
		}
		else if(capType.equals("Uran"))
		{
			if(storage.size()!=0)
			{
				p.getResources().get(Type.Uranium).push(storage.remove(storage.size()-1));
				return true;
			}
		}
		else if(capType.equals("Trash"))
		{
			if(storage.size()!=0)
			{
				p.getResources().get(Type.Trash).push(storage.remove(storage.size()-1));
				return true;
			}
		}
		else if(capType.equals("Hybrid"))
		{
			if(storage.size()!=0)
			{
				if(x.equals("oil"))
				{
					for(int i = storage.size()-1;i>-1;i--)
					{
						if(storage.get(i).getType().equals(Type.Oil))
						{
							p.getResources().get(Type.Oil).push(storage.remove(i));
							return true;
						}
					}
				}
				else if(x.equals("coal"))
				{
					for(int i = storage.size()-1;i>-1;i--)
					{
						if(storage.get(i).getType().equals(Type.Coal))
						{
							p.getResources().get(Type.Coal).push(storage.remove(i));
							return true;
						}
					}
				}
				//return true;
			}
		}
		
		return false;
	}
	public boolean insert(Player p, String x)
	{
		if(capType.equals("Oil") && x.equals("oil"))
		{
			if(p.getOil()+storage.size()  >= maxCapacity/2 && storage.size() <cost.size())
			{
				storage.add(p.getResources().get(Type.Oil).pop());
				return true;
			}
		}
		else if(capType.equals("Coal")&&x.equals("coal"))
		{
			if(p.getCoal()+storage.size() >= maxCapacity/2 && storage.size()<cost.size())
			{
				storage.add(p.getResources().get(Type.Coal).pop());
				return true;
			}
		}
		else if(capType.equals("Uran")&&x.equals("uranium"))
		{
			if(p.getUranium()+storage.size()  >= maxCapacity/2 && storage.size()<cost.size())
			{
				storage.add(p.getResources().get(Type.Uranium).pop());
				return true;
			}
		}
		else if(capType.equals("Trash")&&x.equals("trash"))
		{
			if(p.getTrash()+storage.size() >= maxCapacity/2 && storage.size()<cost.size())
			{
				storage.add(p.getResources().get(Type.Trash).pop());
				return true;
			}
		}
		else if(capType.equals("Hybrid"))
		{
			if(p.getOil()+p.getCoal()+storage.size() >= maxCapacity/2 && storage.size()<cost.size()/2)
			{
				if(x.equals("oil"))
					storage.add(p.getResources().get(Type.Oil).pop());
				else if(x.equals("coal"))
					storage.add(p.getResources().get(Type.Coal).pop());
				return true;
			}
		}
		
		return false;
	}
	public int getNumPowered()
	{
		return numPowered;
	}
	
	public void setPoweredStatus (boolean status)
	{
		powered = status;
	}
	
	public boolean isPowered ()
	{
		powered = false;
		if(capType.equals("Hybrid"))
		{
			if(storage.size() == cost.size()/2)
				powered = true;
		}
		else if(storage.size() == cost.size())
			powered = true;
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
		img = ImageIO.read(getClass().getResource("resources/"+ID+".png"));
	}
	
	public int compareTo (Object obj)
	{
		int x = getID();
		int y = ((PowerPlant) obj).getID();
		
		return x - y;
	}
}
