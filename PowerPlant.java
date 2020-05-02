
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
	private ArrayList <Resource> cost;
	
	private String name;
	
	private boolean powered;
	
	public PowerPlant (int num, int id, ArrayList <Resource> c)
	{
		ID = id;
		numPowered = num;
		cost = c;
		name = "";
	}
	
	public PowerPlant (String n, int id)
	{
		name = n;
		ID = id;
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
	
	public ArrayList <Resource> getCost ()
	{
		return cost;
	}
	
	public String toString ()
	{
		return ID + " " + numPowered + " " + cost.toString();
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
