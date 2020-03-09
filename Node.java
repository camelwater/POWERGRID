
import Enums.NodeType;

public class Node 
{
	private int cost;
	private NodeType type;
	
	public Node (int c)
	{
		cost = c;
	}
	
	public int getCost ()
	{
		return cost;
	}
	
	public NodeType getType ()
	{
		return type;
	}
}
