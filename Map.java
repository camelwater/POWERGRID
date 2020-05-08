
import java.util.ArrayList;
import java.util.HashMap;

public class Map 
{
	private HashMap <City, HashMap <City, Integer>> connections;
	private ArrayList <City> cities;
	
	static final int V = 42; 
	
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
	
	public int minDistance(HashMap <City,Integer> dist, Boolean sptSet[]) 
	{ 
	      int min = Integer.MAX_VALUE, min_index = -1; 
	  
	        for (int v = 0; v < V; v++) 
	            if (sptSet[v] == false && dist.get(cities.get(v)) <= min) { 
	                min = dist.get(cities.get(v)); 
	                min_index = v; 
	            } 
	  
	        return min_index;
	 
	} 
	
	public Integer getCost(City start,City end,HashMap <City, HashMap <City, Integer>> routes) 
	{ 
	    	HashMap <City,Integer> dist = new HashMap <City,Integer>(42);
	  
	        Boolean sptSet[] = new Boolean[V]; 
	  
	        for (int i = 0; i < V; i++) { 
	            dist.put(cities.get(i), Integer.MAX_VALUE); 
	            sptSet[i] = false; 
	        } 
	  
	        dist.replace(start, 0); 
	  
	        for (int count = 0; count < V - 1; count++) { 
	            int u = minDistance(dist, sptSet); 
	  
	            sptSet[u] = true; 
	  
	            for (int v = 0; v < V; v++) 
	                if (!sptSet[v] && connections.get(cities.get(u)).get(cities.get(v)) != 0 && dist.get(cities.get(u)) != Integer.MAX_VALUE && dist.get(cities.get(u)) + connections.get(cities.get(u)).get(cities.get(v)) < dist.get(cities.get(v))) 
	                    //dist[v] = dist[u] + graph[u][v]; 
	                    dist.replace(cities.get(v), (dist.get(cities.get(u))+connections.get(cities.get(u)).get(cities.get(v))));
	        } 
	        return dist.get(end);
	        
	}
}
