
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
	
	public int step;
	public int phase;
	public int turn;
	public int round;
	
	private boolean isOver = false;
	private Player currentPlayer;
	
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
		
		step = 0;
		
		round = 1;
		
		phase = 1;
		
		turn = 0;
		
		setupGame();
		
		setupCities();
		
		setupPlayers();
		
		setupResources();
		
		updateGameState();
		
<<<<<<< HEAD
//		play();
=======
		//play();
>>>>>>> branch 'master' of https://github.com/dewd69/POWERGRID
	}
	
	public void play() 
	{		
		while (!isOver)
		{
			if (phase == 1 && step != 0) //Determining Player Order
			{
<<<<<<< HEAD
				calculatePlayerOrder();
=======
				//calculatePlayerOrder();
>>>>>>> branch 'master' of https://github.com/dewd69/POWERGRID
			}
			
			else if (step == 0) //First time phase 1 is being played
			{
				Collections.shuffle(players);
				step = 1;
			}
			
			else if (phase == 2) //Auctioning Power Plants
			{
				boolean allPassed = true;
				
				for (Player x : players)
				{
					PowerPlant purchase = null; //This will be set to the power plant the player chooses to buy
					
					int cost = 0; //This will be set to the final cost of the power plant after bidding
					
					if (x.balance() >= cost)
					{
						x.buyPowerPlant(purchase, cost);
						
						market.remove(purchase);
						
						market.add(deck.remove(0));
						
						Collections.sort(market, Collections.reverseOrder());
						
						allPassed = false;
					}
				}
				
				if (allPassed)
				{
					updateMarket();
					
					for (PowerPlant x : market)
					{
						if (x.getName().equals("Step 3"))
						{
							step = 3;
							market.remove(x);
							market.remove(0);
						}
					}
				}
			}
			
			else if (phase == 3) //Buying Resources
			{
				Collections.sort(players, Collections.reverseOrder());
				
				for (Player x : players)
				{
					ArrayList <Resource> purchase = new ArrayList <Resource> (); //This will be filled with all of the resources the player chooses to buy
					
					int cost = 0; //This will be set to the final cost of the resources
					
					if (x.balance() >= cost)
					{
						for (Resource y : purchase)
						{
							x.buyResources(y);
							
							resources.get(y.toString()).pop();
						}
						
						x.pay(cost);
					}
				}
			}
			
			else if (phase == 4) //Building Cities
			{
				int highestCityCount = Integer.MIN_VALUE;
				
				for (Player x : players)
				{
					ArrayList <City> purchase = new ArrayList <City> (); //This will be filled with all of the cities the player chooses to buy
					
					int cost = 0; //This will be set to the final cost of all the cities the player wants to buy
					
					if (x.balance() >= cost)
					{
						for (City y : purchase)
						{
							x.buyCity(y, calculateCost(y));
						}
					}
					
					if (x.getCities().size() >= 7 && step == 1)
						endStep();
					
					else if (x.getCities().size() == 17 && step == 3)
						endGame();
					
					if (x.getCities().size() > highestCityCount)
						highestCityCount = x.getCities().size();
				}
				
				while (highestCityCount >= market.get(0).getID())
				{
					market.remove(0);
					market.add(deck.remove(0));
					Collections.sort(market, Collections.reverseOrder());
				}
				
				for (PowerPlant x : market)
				{
					if (x.getName().equals("Step 3"))
					{
						step = 3;
						market.remove(x);
						market.remove(0);
					}
				}
			}
			
			else if (phase == 5)
			{
				endRound();
				
				for (PowerPlant x : market)
				{
					if (x.getName().equals("Step 3"))
					{
						step = 3;
						market.remove(x);
						market.remove(0);
					}
				}
			}
		}
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
		
		deck.add(new PowerPlant ("Step 3", Integer.MAX_VALUE));
		
		for (PowerPlant x : deck)
		{
			System.out.println(x);
		}
		
		for (int x = 0; x < 8; x++)
			market.add(deck.remove(x));
		
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
	
	public void start()
	{
		Collections.shuffle(players);
		step = 1;
		phase = 2;
		currentPlayer = players.get(0);
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
		currentPlayer = players.get(0);
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
		market.remove(0);
		
		market.add(deck.remove(0));
		
		Collections.sort(market, Collections.reverseOrder());
	}
	
	public int calculateCost (City buy) //Shortest Path Algorithm
	{	
		return 0; 
	}
	
	public void nextTurn()
	{
		turn++;
		
		currentPlayer = players.get(turn);
	}
	public void endPhase ()
	{
		if (phase == 5)
			endRound();
		
		else
			phase++;
	}
	
	public int getRound()
	{
		return round;
	}
	public void endRound ()
	{
		round++;
		phase = 1;
		
		updateMarket();
		
		distributeCash();
		
		refillResources();
	}
	
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	public int getTurn()
	{
		return turn;
	}
	
	public void endStep ()
	{
		step++;
		round = 1;
		phase = 1;
		if (step == 2)
		{
			updateMarket();
			
//			for (City x : cities)
//				x.incrementCost(); 
		}
	}
	
	public int getStep()
	{
		return step;
	}
	
	public int getPhase()
	{
		return phase;
	}
	
	public void endGame ()
	{		
		isOver = true;
		
		Player winner = null;
		Player temp = null;
		
		boolean tie = false;
		
		int mostPowered = Integer.MIN_VALUE;
		
		for (Player x : players)
		{
			int current = 0;
			
			for (PowerPlant y : x.getPowerPlants())
			{
				if (y.isPowered())
					current += y.getNumPowered();
			}
			
			if (current == mostPowered)
			{
				temp = x;
				tie = true;
			}
			
			else if (current > mostPowered)
			{
				mostPowered = current;
				winner = x;
				tie = false;
				temp = null;
			}
		}
		
		if (tie)
		{	
			if (temp.balance() > winner.balance())
			{
				winner = temp;
			}
			
			else if (temp.balance() == winner.balance())
			{
				if (winner.getCities().size() < temp.getCities().size())
					winner = temp;
			}
		}
		
		for (Player x : players)
		{
			if (x != winner)
				players.remove(x);
		}
	}
	
	public boolean isOver()
	{
		return isOver;
	}
	
	public ArrayList <Object> updateGameState ()
	{
		ArrayList <Object> elements = new ArrayList <Object> ();
		
		elements.add(deck);
		elements.add(resources);
		elements.add(players);
		elements.add(market);
		
		return elements;
	}
}
