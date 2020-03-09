
import java.util.HashMap;

import Enums.Type;

public class Resource 
{
	private Type type;
	private int cost;
	public HashMap <String, Type> resources = new HashMap <String, Type> ();
	public HashMap <Type, String> reverseResources = new HashMap <Type, String> ();
	
	public Resource (String t, int c)
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
		cost = c;
	}
	
	public int getCost ()
	{
		return cost;
	}
	
	public Type getType ()
	{
		return type;
	}
}
