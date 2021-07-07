
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Resource 
{
	private Type type;
	private HashMap <String, Type> resources = new HashMap <String, Type> ();
	private HashMap <Type, String> reverseResources = new HashMap <Type, String> ();

	public Resource (String t)
	{
		if (resources.size() == 0)
		{
			resources.put("COAL", Type.Coal);
			resources.put("OIL", Type.Oil);
			resources.put("TRASH", Type.Trash);
			resources.put("URANIUM", Type.Uranium);
		}

		if (reverseResources.size() == 0)
		{
			reverseResources.put(Type.Coal, "COAL");
			reverseResources.put(Type.Oil, "OIL");
			reverseResources.put(Type.Trash, "TRASH");
			reverseResources.put(Type.Uranium, "URANIUM");
		}
		
		type = resources.get(t);
	}
	
	public Type getType ()
	{
		return type;
	}
	
	public String toString ()
	{
		return reverseResources.get(type);
	}
	public BufferedImage getPic()
	{
		if(type.equals(Type.Oil))
			try {
				return ImageIO.read(getClass().getResource("resources/oil.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(type.equals(Type.Trash))
			try {
				return ImageIO.read(getClass().getResource("resources/trash.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(type.equals(Type.Coal))
			try {
				return ImageIO.read(getClass().getResource("resources/coal.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(type.equals(Type.Uranium))
			try {
				return ImageIO.read(getClass().getResource("resources/uranium.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
}
