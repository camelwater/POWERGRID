
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

import Enums.Type;

@SuppressWarnings({"unused", "resource", "unchecked"})
public class Board 
{
	private static final String DATA_PATH = "Data\\";
	
	private ArrayList <PowerPlant> deck;
	private TreeMap <String, Stack <Resource>> resources;
	private ArrayList <Player> players;
	private ArrayList <PowerPlant> market;
	private ArrayList <City> cities;
	private Map graph;
	
	private int step;
	private int phase;
	
	public Board () throws IOException
	{
		deck = new ArrayList <PowerPlant> ();
		
		resources = new TreeMap <String, Stack <Resource>> ();
		
		players = new ArrayList <Player> ();
		
		market = new ArrayList <PowerPlant> ();
		
		cities = new ArrayList <City> ();
		
		resources.put("COAL", new Stack <Resource> ());
		
		resources.put("OIL", new Stack <Resource> ());
		
		resources.put("TRASH", new Stack <Resource> ());
		
		resources.put("URANIUM", new Stack <Resource> ());
		
		step = 1;
		
		phase = 1;
		
		setupGame();
		
		setupCities();
		
		setupPlayers();
		
		setupResources();
		
		updateGameState();
	}
	
	public void setupGame () throws IOException
	{
		Scanner input = new Scanner (new File (DATA_PATH + "PowerPlants.txt"));
		
		while (input.hasNext())
		{
			String [] attributes = input.nextLine().split("\\s{0,}[\"|\"]{1}\\s{0,}");
			
			int id = Integer.parseInt(attributes[0]);
			int numPowered = Integer.parseInt(attributes[1]);
			
			String [] resources = attributes[2].split(", ");
			
			ArrayList <Resource> cost = new ArrayList <Resource> ();
			
			for (int x = 0; x < resources.length; x++)
			{
				String [] individualResource = resources[x].split(" ");
				
				if (!individualResource[0].equals("Free"))
				{
					for (int y = 0; y < Integer.parseInt(individualResource[0]); y++)
					{
						cost.add(new Resource(individualResource[1].toUpperCase()));
					}
				}
			}
			
			deck.add(new PowerPlant (id, numPowered, cost));
		}
		
		for (PowerPlant x : deck)
		{
			System.out.println(x);
		}
		
		Collections.shuffle(deck);
	}
	
	public void setupCities () throws IOException
	{
		Scanner input = new Scanner (new File (DATA_PATH + "cities.txt"));
		
		HashMap <City, HashMap <City, Integer>> map = new HashMap <City, HashMap <City, Integer>> ();
		
		while (input.hasNext())
		{
			String [] attributes = input.nextLine().split("\\s{0,}[\"|\"]{1}\\s{0,}");
			
			String name = attributes[0];
			
			City start = new City (name);
			
			HashMap <City, Integer> connections = new HashMap <City, Integer> ();
			
			cities.add(start);
			
			for (int x = 1; x <= attributes.length - 1; x++)
			{
				String [] connectingCity = attributes[x].split(", ");
				
				connections.put(new City (connectingCity[1]), Integer.parseInt(connectingCity[0]));
			}
			
			map.put(start, connections);
		}
		
		graph = new Map (map);
		
		System.out.println("\n\n");
		
		for (City x : cities)
		{
			System.out.println(x.getName() + " " + graph.getConnections(x)); 
		}
	}
	
	public void setupPlayers ()
	{
		for (int x = 1; x < 5; x++)
			players.add(new Player("" + x)); 
	}
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public void setupResources ()
	{
		for (int x = 0; x < 24; x++)
			resources.get("COAL").push(new Resource("COAL"));				
		for (int x = 0; x < 18; x++)
			resources.get("OIL").push(new Resource("OIL"));	
		for (int x = 0; x < 6; x++)
			resources.get("TRASH").push(new Resource("TRASH"));
		
		resources.get("URANIUM").push(new Resource("URANIUM"));
	}
	
	public void distributeCash ()
	{
		for (Player x : players)
		{
			if (x.getPowerPlants().size() == 0)
				x.pay(10);
			else if (x.getPowerPlants().size() == 1)
				x.pay(22);
			else if (x.getPowerPlants().size() == 2)
				x.pay(33);
			else if (x.getPowerPlants().size() == 3)
				x.pay(44);
			else if (x.getPowerPlants().size() == 4)
				x.pay(54);			
			else if (x.getPowerPlants().size() == 5)
				x.pay(64);			
			else if (x.getPowerPlants().size() == 6)
				x.pay(73);			
			else if (x.getPowerPlants().size() == 7)
				x.pay(82);			
			else if (x.getPowerPlants().size() == 8)
				x.pay(90);			
			else if (x.getPowerPlants().size() == 9)
				x.pay(98);			
			else if (x.getPowerPlants().size() == 10)
				x.pay(105);			
			else if (x.getPowerPlants().size() == 11)
				x.pay(112);
			else if (x.getPowerPlants().size() == 12)
				x.pay(118);
			else if (x.getPowerPlants().size() == 13)
				x.pay(124);
			else if (x.getPowerPlants().size() == 14)
				x.pay(129);
			else if (x.getPowerPlants().size() == 15)
				x.pay(134);
			else if (x.getPowerPlants().size() == 16)
				x.pay(138);
			else if (x.getPowerPlants().size() == 17)
				x.pay(142);
			else if (x.getPowerPlants().size() == 18)
				x.pay(145);
			else if (x.getPowerPlants().size() == 19)
				x.pay(148);
			else if (x.getPowerPlants().size() == 20)
				x.pay(150);
		}
	}
	
	public void calculatePlayerOrder ()
	{
		for (Player x : players)
		{
			Collections.sort(x.getPowerPlants());
		}
		
		Collections.sort(players);
	}
	
	public void refillResources ()
	{
		if (step == 1)
		{
			for (int x = 0; x < 5; x++)
				resources.get("COAL").push(new Resource("COAL"));			
			for (int x = 0; x < 3; x++)
				resources.get("OIL").push(new Resource("OIL"));	
			for (int x = 0; x < 2; x++)
				resources.get("TRASH").push(new Resource("TRASH"));
			
			resources.get("URANIUM").push(new Resource("URANIUM"));
		}
		
		else if (step == 2)
		{
			for (int x = 0; x < 4; x++)
			{
				resources.get("COAL").push(new Resource("COAL"));			
				resources.get("OIL").push(new Resource("OIL"));	
			}
			
			for (int x = 0; x < 3; x++)
				resources.get("TRASH").push(new Resource("TRASH"));
			for (int x = 0; x < 2; x++)
				resources.get("URANIUM").push(new Resource("URANIUM"));
		}
		
		else
		{
			for (int x = 0; x < 4; x++)
			{
				resources.get("COAL").push(new Resource("COAL"));			
				resources.get("OIL").push(new Resource("OIL"));	
				resources.get("TRASH").push(new Resource("TRASH"));
			}
			
			for (int x = 0; x < 2; x++)
				resources.get("URANIUM").push(new Resource("URANIUM"));
		}
	}
	
	public void updateMarket ()
	{
		Collections.sort(market, Collections.reverseOrder());
		
		market.remove(0);
		
		market.add(deck.remove(0));
	}
	
	public int calculateCost ()
	{	
		return 0; 
	}
	
	public void endPhase ()
	{
		if (phase == 5)
			endRound();
		
		else
			phase++;
	}
	
	public void endRound ()
	{
		phase = 1;
		
		updateMarket();
		
		distributeCash();
		
		refillResources();
	}
	
	public void endStep ()
	{
		step++;
		
		if (step == 2)
		{
			updateMarket();
			
			for (City x : cities)
				x.incrementCost(); 
		}
		
		else
		{
			market.remove(0);
			market.remove(0);
			
			for (City x : cities)
				x.incrementCost(); 
		}
	}
	
	public void endGame ()
	{
		
	}
	
	public void updateGameState ()
	{
		
	}
}



























