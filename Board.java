
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
	private HashMap <Integer, Integer> costs;
	private Map graph;
	
	public int step;
	public int phase;
	public int turn;
	public int round;
	
	public int origBal = 0;
	public int cost = 0;
	public int passC = 0;
	public int numFin = 0;
	
	private boolean auctionDone = false;
	private boolean resourceDone = false;
	private boolean citiesDone = false;
	private boolean isOver = false;
	
	private Player currentPlayer;
	
	public Board () throws IOException
	{
		deck = new ArrayList <PowerPlant> ();
		
		resources = new TreeMap <String, Stack <Resource>> ();
		
		players = new ArrayList <Player> ();
		
		market = new ArrayList <PowerPlant> ();
		
		cities = new ArrayList <City> ();
		
		costs = new HashMap <Integer, Integer> ();
		
		resources.put("COAL", new Stack <Resource> ());
		
		resources.put("OIL", new Stack <Resource> ());
		
		resources.put("TRASH", new Stack <Resource> ());
		
		resources.put("URANIUM", new Stack <Resource> ());
		
		costs.put(1, 8);
		costs.put(2, 7);
		costs.put(3, 6);
		costs.put(4, 5);
		costs.put(5, 4);
		costs.put(6, 3);
		costs.put(7, 2);
		costs.put(8, 1);
		costs.put(11, 16);
		costs.put(12, 14);
		costs.put(13, 12);
		costs.put(14, 10);
		
		step = 0;
		
		round = 1;
		
		phase = 1;
		
		turn = 0;
		
		setupGame();
		
		setupCities();
		
		setupPlayers();
		
		setupResources();
		
		updateGameState();
		
		//play();
	}
	
	public void play() 
	{		
		while (!isOver)
		{
			if (phase == 1 && step != 0) //Determining Player Order
			{
				calculatePlayerOrder();
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
						//	x.buyCity(y, graph.calculateCost());		//The second parameter will be determined by the shortest path method
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
			
			ArrayList <String> cost = new ArrayList <String> ();
			
			for (int x = 0; x < resources.length; x++)
			{
				String [] individualResource = resources[x].split(" ");
				
				if (!individualResource[0].equals("Free"))
				{
					for (int y = 0; y < Integer.parseInt(individualResource[0]); y++)
					{
						cost.add(individualResource[1].toUpperCase());
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
	
	public ArrayList<PowerPlant> getMarket()
	{
		return market;
	}
	
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public void start()
	{
		Collections.shuffle(players);
		//step = 1;
		//phase = 2;
		currentPlayer = players.get(0);
	}
	
	public boolean resourceDone()
	{
		return resourceDone;
	}
	
	public boolean citiesDone()
	{
		return citiesDone;
	}
	
	public boolean auctionDone()
	{
		return auctionDone;
	}
	public int numFin()
	{
		return numFin;
	}
	
	public void buyCity(String x)
	{
		City c = null;
		
		for(City i: cities)
			if(i.getName().equals(x))
				c = i;
		
		int price = 0;
		price+= c.getCost();
		//price+=c.roadCost();
		
		boolean h = currentPlayer.buyCity(c, price);
		
		if(!h || !c.isAvailable(step))
		{
			System.out.println("CANT BUY CITY");
			return;
		}
		
		currentPlayer.getCities().add(c);
			
		currentPlayer.pay(price);
		
		c.incrementNumOccupants(step);
		
		
		if(numFin == 4)
		{
			citiesDone = true;
		}
	}
	
	public void buyRes(Type t, int x)
	{
		System.out.print(resources.get(t.toString().toUpperCase()).size());
		if(Type.Oil.equals(t))
		{
			for(int i = 0;i<x;i++)
				currentPlayer.buyResources(new Resource("OIL"));
			for(int i = 0;i<x;i++)
				resources.get("OIL").pop();
		}
		if(Type.Coal.equals(t))
		{
			for(int i = 0;i<x;i++)
				currentPlayer.buyResources(new Resource("COAL"));
			for(int i = 0;i<x;i++)
				resources.get("COAL").pop();
		}
		if(Type.Uranium.equals(t))
		{
			for(int i = 0;i<x;i++)
				currentPlayer.buyResources(new Resource("URANIUM"));
			for(int i = 0;i<x;i++)
				resources.get("URANIUM").pop();
		}
		if(Type.Trash.equals(t))
		{
			for(int i = 0;i<x;i++)
				currentPlayer.buyResources(new Resource("TRASH"));
			for(int i = 0;i<x;i++)
				resources.get("TRASH").pop();
		}
		System.out.println(" - "+resources.get(t.toString().toUpperCase()).size());
		if(numFin == 4)
			resourceDone = true;
	}
	public void pass (int i)
	{
		if(step>0&&i==-1)
		{
			currentPlayer.isFinished();
			nextTurn();
			return;
		}
		passC++;
		
		if(numFin==3)
		{
			currentPlayer.isFinished();
			auctionDone = true;

			numFin++;
//			if(step == 0)
//				step++;
			return;
		}
		if(passC == 3-numFin)
		{
			nextTurn();
			currentPlayer.buyPowerPlant(market.get(i), cost);
			market.remove(i);
			currentPlayer.finished();
			numFin++;
		}
			
		
		nextTurn();
		
	}
	public void bid(int i, String x)
	{
		
		if(i == -1)
			return;
		if(x.equals("first"))
			cost = market.get(i).getID();
		else
			cost++;
		
		passC = 0;
		currentPlayer = players.get(turn);
		
		boolean h = currentPlayer.bid(cost);
		
		if(!h || currentPlayer.getPowerPlants().size()==3)
		{
			System.out.println("CAN'T BID");
			return;
		}
		if(numFin == 3)
		{
			currentPlayer.buyPowerPlant(market.get(i), cost);
			market.remove(i);
			currentPlayer.isFinished();
			auctionDone = true;
			numFin++;
//			if(step ==0)
//				step++;
			return;
		}
		nextTurn();
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
		if(step ==0 && phase == 3)
		{
			for (Player x : players)
			{
				Collections.sort(x.getPowerPlants());
			}
		
			Collections.sort(players);
			Collections.reverse(players);
			currentPlayer = players.get(3);
		}
		else if(step == 1 && round == 1 && phase == 4)
		{
			for (Player x : players)
			{
				Collections.sort(x.getPowerPlants());
			}
		
			Collections.sort(players);
			Collections.reverse(players);
			currentPlayer = players.get(3);
		}
		else if(step!=0 && (phase == 1 ||phase == 2))
		{
			citySort();
			currentPlayer = players.get(0);
		}
		else if(step!=0 && round!=0 && (phase == 3 || phase == 4))
		{
			citySort();
			currentPlayer = players.get(3);
		}
		
	}
	
	public void citySort()
	{
		int o0 = players.get(0).getCities().size(); 
		int o1= players.get(1).getCities().size(); 
		int o2= players.get(2).getCities().size(); 
		int o3= players.get(3).getCities().size();
		
		int[] x = new int[4];
		
		x[0] = o0;
		x[1] = o1;
		x[2] = o2;
		x[3] = o3;
		
		Arrays.sort(x);
		
		ArrayList<Player>temp = new ArrayList<Player>();
		
		if(x[3]==o0)
			temp.add(players.get(0));
		else if(x[3]==o1)
			temp.add(players.get(1));
		else if(x[3]==o2)
			temp.add(players.get(2));
		else if(x[3]==o3)
			temp.add(players.get(3));

		if(x[2]==o0)
			temp.add(players.get(0));
		else if(x[2]==o1)
			temp.add(players.get(1));
		else if(x[2]==o2)
			temp.add(players.get(2));
		else if(x[2]==o3)
			temp.add(players.get(3));
		
		if(x[1]==o0)
			temp.add(players.get(0));
		else if(x[1]==o1)
			temp.add(players.get(1));
		else if(x[1]==o2)
			temp.add(players.get(2));
		else if(x[1]==o3)
			temp.add(players.get(3));
		
		if(x[0]==o0)
			temp.add(players.get(0));
		else if(x[0]==o1)
			temp.add(players.get(1));
		else if(x[0]==o2)
			temp.add(players.get(2));
		else if(x[0]==o3)
			temp.add(players.get(3));
	
		
		players = temp;
		
	}
	public TreeMap <String, Stack <Resource>> getResources()
	{
		return resources;
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
	
	public int calculateCost (Type type) //Calculates the cost of the given resource
	{				
		if (type == Type.Uranium)
		{
			if (resources.get(type.toString()).size() > 4)
			{
				return costs.get((resources.get(type.toString()).size() - 4));
			}
			
			else if (resources.get(type.toString()).size() != 0)
			{
				return costs.get((resources.get(type.toString()).size() + 10));
			}
		}
		
		else if (resources.get(type.toString()).size() != 0)
		{		
			int index = 1;
			
			for (int x = 3; x <= 24; x += 3)
			{
				if (resources.get(type.toString()).size() <= x)
				{
					return costs.get(index);
				}
				
				index++;
			}
		}
		
		return 0;
	}
	
	public void nextTurn()
	{
		if(phase == 3)
		{
			if(turn == 0)
			{
				resourceDone = true;
				return;
			}
			else
				turn--;
		}
		else if(phase == 4)
		{
			if(turn == 0)
			{
				citiesDone = true;
				return;
			}
			else
				turn--;
		}
		else
		{
			if (turn == 3)
				turn = 0;
			
			else
				turn++;		
		}
		
		currentPlayer = players.get(turn);
		if(currentPlayer.isFinished())
			nextTurn();
	}
	
	public void endPhase ()
	{
		if (phase == 5)
			endRound();
		else
			phase++;
		
		if(phase == 1 || phase == 2 || phase == 5)
			turn = 0;
		else 
			turn = 3;
		
		numFin = 0;
		for(int i = 0;i<4;i++)
			players.get(i).finished = false;
		currentPlayer = players.get(turn);
	}
	
	public int getRound()
	{
		return round;
	}
	
	public void endRound ()
	{
		round++;
		
		phase = 0;
		
		endPhase();
		
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
		phase = 0;
		endPhase();
		
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
