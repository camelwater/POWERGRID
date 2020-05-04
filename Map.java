
import java.util.HashMap;

public class Map 
{
	private HashMap <City, HashMap <City, Integer>> cities;
	
	static final int V = 42; 
	
	public Map (HashMap <City, HashMap <City, Integer>> connections)
	{
		cities = connections;
	}
	
	public int getCost (City start, City end, String region)
	{
		return cities.get(start).get(end);
	}
	
	public HashMap <City, Integer> getConnections (City x)
	{
		return cities.get(x);
	}
	
	public int minDistance(int dist[], Boolean sptSet[]) 
	{ 
	  /*      int min = Integer.MAX_VALUE, min_index = -1; 
	  
	        for (int v = 0; v < V; v++) 
	            if (sptSet[v] == false && dist[v] <= min) { 
	                min = dist[v]; 
	                min_index = v; 
	            } 
	  
	        return min_index; */
	    return 0;
	} 
	
	public void dijkstra(int graph[][], int src) 
	{ 
	       /* int dist[] = new int[V];
	  
	        Boolean sptSet[] = new Boolean[V]; 
	  
	        for (int i = 0; i < V; i++) { 
	            dist[i] = Integer.MAX_VALUE; 
	            sptSet[i] = false; 
	        } 
	  
	        dist[src] = 0; 
	  
	        for (int count = 0; count < V - 1; count++) { 
	            int u = minDistance(dist, sptSet); 
	  
	            sptSet[u] = true; 
	  
	            for (int v = 0; v < V; v++) 
	                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) 
	                    dist[v] = dist[u] + graph[u][v]; 
	        } */
	        
	}
}
