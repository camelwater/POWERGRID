
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

public class Map 
{
	private HashMap <City, HashMap <City, Integer>> connections;
	private HashMap <City, HashMap <City, Integer>> routes=new HashMap <City, HashMap <City, Integer>>();
	private ArrayList <City> cities;
	
	static final int VERTS = 42; 
	
	public Map (HashMap <City, HashMap <City, Integer>> conns, ArrayList <City> cits)
	{
		connections = conns;
		cities=cits;
	}
	
	/*public int getCost (City start, City end, String region)
	{
		return cities.get(start).get(end);
	}
	*/
	public HashMap <City, Integer> getConnections (City x)
	{
		return connections.get(x);
	}
	
	public int distance(HashMap <City,Integer> dist, Boolean sptSet[]) 
	{ 
	      int min = Integer.MAX_VALUE, min_index = -1; 
	  
	        for (int v = 0; v < VERTS; v++) 
	            if (sptSet[v] == false && dist.get(cities.get(v)) <= min) { 
	                min = dist.get(cities.get(v)); 
	                min_index = v; 
	            } 
	  
	        return min_index;
	 
	} 
	
	public void setUpRoutes()
	{
	    for(int a=0;a<cities.size();a++)
	    {
		HashMap <City, Integer> temp=new HashMap<City, Integer>();
		for(int b=0;b<cities.size();b++)
		{
		    if(has(connections.get(cities.get(a)),cities.get(b))!=null)
		    {
			int num=connections.get(cities.get(a)).get(has(connections.get(cities.get(a)),cities.get(b)));
			if(num==0)
			{
			    temp.put(cities.get(b),-1);
			}
			else
			    temp.put(cities.get(b),num);
		    }
		    /*if(connections.get(cities.get(a)).get(cities.get(b))!= null)
		    {
			temp.put(cities.get(b),connections.get(cities.get(a)).get(cities.get(b)));
		    }*/
		    else
			temp.put(cities.get(b), 0);
		}
		routes.put(cities.get(a), temp);
	    }
	}
	
	private City has(HashMap <City, Integer> temp,City cit)
	{
	    for(City c:temp.keySet())
	    {
		if(c.getName().equals(cit.getName()))
		    return c;
	    }
	    return null;
	}
	
	public Integer getCost(Player p, City end)
	{
	    ArrayList<Integer> paths=new ArrayList<Integer>();
	    for(City c:p.getCities())
	    {
	    	paths.add(getCost(c,end));
	    }
	    Collections.sort(paths);
	    return paths.get(0);
	}
	
	public Integer getCost(City start,City end) 
	{ 
	    	HashMap <City,Integer> dist = new HashMap <City,Integer>(42);
	    	
	    	setUpRoutes();
	  
	        Boolean isMin[] = new Boolean[VERTS]; 
	  
	        for (int i = 0; i < VERTS; i++) { 
	            dist.put(cities.get(i), Integer.MAX_VALUE); 
	            isMin[i] = false; 
	        } 
	  
	        dist.replace(start, 0); 
	  
	        for (int count = 0; count < VERTS - 1; count++)
	        { 
	            int u = distance(dist, isMin);
	  
	            isMin[u] = true; 
	            
	            for (int v = 0; v < VERTS; v++) 
	            {
	        	boolean isFree=false;
	        	Integer conn=routes.get(cities.get(u)).get(cities.get(v));
	        	if(conn==-1)
	        	{
	        	    //System.out.println("here");
	        	    isFree=true;
	        	} 
		        Integer distU=dist.get(cities.get(u));
	        	Integer distV=dist.get(cities.get(v));
	        	
	                if (!isMin[v] && conn != 0 && distU != Integer.MAX_VALUE && distU + conn < distV) 
	                {
	                    if(isFree)
	                	dist.replace(cities.get(v), distU+conn+1);
	                    else
	                	dist.replace(cities.get(v), distU+conn);
	                }
	            }
	        } 
	        return dist.get(end);
	        
	}
}
