
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
	
	public BufferedImage getImage()
	{
		return img;
	}
	public void setImage() throws IOException
	{
		//System.out.println(name+".png");
		img = ImageIO.read(getClass().getResource(ID+".png"));
	}
	public int compareTo (Object obj)
	{
		int x = getID();
		int y = ((PowerPlant) obj).getID();
		
		return x - y;
	}
}
