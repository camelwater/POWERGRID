import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

@SuppressWarnings({"unused", "resource", "unchecked"})
public class Board 
{
	//private static final String DATA_PATH = "Data\\";
	
	private ArrayList <PowerPlant> deck;
	private TreeMap <String, Stack <Resource>> resources;
	private ArrayList <Player> players;
	private ArrayList <PowerPlant> market;
	private ArrayList <City> cities;
	private HashMap <Integer, Integer> costs;
	private ArrayList<String> regions = new ArrayList<String>();
	private Map graph;
	
	public int step;
	public int phase;
	public int turn;
	public int round;
	
	public boolean step3 = false;
	public boolean step3g = false;
	
	public int cost = 0;
	public int passC = 0;
	public int numFin = 0;
	public int allPass = 0;
	
	public boolean auctionDone = false;
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
	
//	public void play() 
//	{		
//		while (!isOver)
//		{
//			if (phase == 1 && step != 0) //Determining Player Order
//			{
//				calculatePlayerOrder();
//			}
//			
//			else if (step == 0) //First time phase 1 is being played
//			{
//				Collections.shuffle(players);
//				step = 1;
//			}
//			
//			else if (phase == 2) //Auctioning Power Plants
//			{
//				boolean allPassed = true;
//				
//				for (Player x : players)
//				{
//					PowerPlant purchase = null; //This will be set to the power plant the player chooses to buy
//					
//					int cost = 0; //This will be set to the final cost of the power plant after bidding
//					
//					if (x.balance() >= cost)
//					{
//						x.buyPowerPlant(purchase, cost);
//						
//						market.remove(purchase);
//						
//						market.add(deck.remove(0));
//						
//						Collections.sort(market, Collections.reverseOrder());
//						
//						allPassed = false;
//					}
//				}
//				
//				if (allPassed)
//				{
//					updateMarket();
//					
//					for (PowerPlant x : market)
//					{
//						if (x.getName().equals("Step 3"))
//						{
//							step = 3;
//							market.remove(x);
//							market.remove(0);
//						}
//					}
//				}
//			}
//			
//			else if (phase == 3) //Buying Resources
//			{
//				Collections.sort(players, Collections.reverseOrder());
//				
//				for (Player x : players)
//				{
//					ArrayList <Resource> purchase = new ArrayList <Resource> (); //This will be filled with all of the resources the player chooses to buy
//					
//					int cost = 0; //This will be set to the final cost of the resources
//					
//					if (x.balance() >= cost)
//					{
//						for (Resource y : purchase)
//						{
//							x.buyResources(y);
//							
//							resources.get(y.toString()).pop();
//						}
//						
//						x.pay(cost);
//					}
//				}
//			}
//			
//			else if (phase == 4) //Building Cities
//			{
//				int highestCityCount = Integer.MIN_VALUE;
//				
//				for (Player x : players)
//				{
//					ArrayList <City> purchase = new ArrayList <City> (); //This will be filled with all of the cities the player chooses to buy
//					
//					int cost = 0; //This will be set to the final cost of all the cities the player wants to buy
//					
//					if (x.balance() >= cost)
//					{
//						for (City y : purchase)
//						{
//						//	x.buyCity(y, graph.calculateCost());		//The second parameter will be determined by the shortest path method
//						}
//					}
//					
//					if (x.getCities().size() >= 7 && step == 1)
//						endStep();
//					
//					else if (x.getCities().size() == 17 && step == 3)
//						endGame();
//					
//					if (x.getCities().size() > highestCityCount)
//						highestCityCount = x.getCities().size();
//				}
//				
//				while (highestCityCount >= market.get(0).getID())
//				{
//					market.remove(0);
//					market.add(deck.remove(0));
//					Collections.sort(market, Collections.reverseOrder());
//				}
//				
//				for (PowerPlant x : market)
//				{
//					if (x.getName().equals("Step 3"))
//					{
//						step = 3;
//						market.remove(x);
//						market.remove(0);
//					}
//				}
//			}
//			
//			else if (phase == 5) //Bureaucracy 		We also need to prompt the players on what cities they want to power
//			{
//				endRound();
//				
//				for (PowerPlant x : market)
//				{
//					if (x.getName().equals("Step 3"))
//					{
//						step = 3;
//						market.remove(x);
//						market.remove(0);
//					}
//				}
//			}
//		}
//	}
	
	public ArrayList<PowerPlant> getDeck()
	{
		return deck;
	}
	public ArrayList<City> getCities()
	{
		return cities;
	}
	public void setupGame () throws IOException
	{
		InputStream in = Board.class.getResourceAsStream("PowerPlants.txt");
		Scanner input = new Scanner (in);
		
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
			//System.out.println(deck.toString());
		}
		
		for (int x = 0; x < 8; x++)
			market.add(deck.remove(0));
		
//		for (int x = 0; x < 7; x++)
//			market.add(deck.remove(0));
//		market.add(new PowerPlant("Step 3", Integer.MAX_VALUE));
		
		PowerPlant z = null;
		for(int i = 0; i<deck.size();i++)
			if(deck.get(i).getID() == 13)
				z = deck.remove(i);

		Collections.shuffle(deck);
		
		deck.add(0, z);
		
		deck.add(new PowerPlant ("Step 3", Integer.MAX_VALUE));
		
	}
	
	public void setupCities () throws IOException
	{
		InputStream in = Board.class.getResourceAsStream("Cities.txt");
		Scanner input = new Scanner (in);
		
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
		
		graph = new Map (map,cities);
		
		System.out.println("\n\n");
		
		for (City x : cities)
		{
			System.out.println(x.getName() + " " + graph.getConnections(x)); 
		}
	}
	
	public void setupPlayers ()
	{
		for (int x = 1; x < 5; x++)
		{
			players.add(new Player(""+x));
			
		}
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
	
	public void insert(PowerPlant p, String x)
	{
		boolean b  = false;
		
		b = p.insert(currentPlayer, x);
		
		
		if(!b)
		{
			System.out.println("CAN'T PLACE RESOURCE");
			return;
		}
		if(p.capType.equals("Hybrid"))
		{
			
			if(p.getStorage().size()== p.getCost().size()/2)
			{
				//System.out.println("HELLO AM HERE. HYBRID CHECKER");
				p.setPoweredStatus(true);
			}
		}
		else if(p.getStorage().size()== p.getCost().size())
			p.setPoweredStatus(true);
		
	}
	
	public void extract(PowerPlant p, String x)
	{
		boolean b = false;
		
		b = p.extract(currentPlayer, x);
		
		if(!b)
		{
			//System.out.println("CAN'T TAKE RESOURCE BACK");
			return;
		}
		if(p.capType().equals("Hybrid"))
		{
			if(p.getStorage().size()!= p.getCost().size()/2)
				p.setPoweredStatus(false);
		}
		else if(p.getStorage().size() != p.getCost().size())
			p.setPoweredStatus(false);
	}
	public boolean cityA(String x)
	{
		City c = null;
		
		for(City i: cities)
			if(i.getName().equals(x))
				c = i;
		//.out.println(x);
		//System.out.println(cities.toString());
		return c.isAvailable(step, currentPlayer);
	}
	
	public void buyCity(String x)
	{
		City c = null;
		
		for(City i: cities)
			if(i.getName().equals(x))
				c = i;
		
		int price = 0;
		price+= c.getCost();
		
		if(currentPlayer.getCities().size()!=0)
			price+=graph.getCost(currentPlayer, c);
		
		boolean h = currentPlayer.buyCity(c, price);
		
		if(!h || !c.isAvailable(step, currentPlayer) || currentPlayer.getCities().contains(c))
		{
			//System.out.println("CANT BUY CITY");
			return;
		}
		
		currentPlayer.getCities().add(c);
			
		currentPlayer.pay(price);
		
		c.incrementNumOccupants(step, currentPlayer);
		
		
		if(numFin == 4)
		{
			citiesDone = true;
		}
	}
	
	public void buyRes(Type t, int x)
	{
		//System.out.print(resources.get(t.toString().toUpperCase()).size());
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
		//System.out.println(" - "+resources.get(t.toString().toUpperCase()).size());
		if(numFin == 4)
			resourceDone = true;
	}
	public void takeResBack(Type t)
	{
		if(Type.Oil.equals(t))
		{

			resources.get("OIL").push(currentPlayer.getResources().get(t).pop());
		}
		if(Type.Coal.equals(t))
		{
			
			resources.get("COAL").push(currentPlayer.getResources().get(t).pop());
		}
		if(Type.Uranium.equals(t))
		{
			
			resources.get("URANIUM").push(currentPlayer.getResources().get(t).pop());
		}
		if(Type.Trash.equals(t))
		{
			
			resources.get("TRASH").push(currentPlayer.getResources().get(t).pop());
		}
	}
	public void pass (int i)
	{
		System.out.println("ALLPASS = "+allPass);
		if(step>0&&i==-1)
		{
			currentPlayer.finished();
			numFin++;
			allPass++;
			nextTurn();
			
			if(numFin ==4)
				auctionDone = true;
			return;
		}
		passC++;
		
		if(numFin==3)
		{
			currentPlayer.finished();
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
			
			if(deck.get(0).getName().equals("Step 3")&& step == 2)
				step3 = true;
			market.add(deck.remove(0));
			Collections.sort(market);
			if(currentPlayer.getPowerPlants().size()==4)
			{
				System.out.println("MAX PP");
				return;
			}
			currentPlayer.finished();
			numFin++;
			
		}
			
		
		nextTurn();
		
	}
	
	public boolean bid(int i, String x)
	{
		
		if(i == -1)
			return false;
		if(x.equals("first"))
			cost = market.get(i).getID();
		else
			cost++;
		
		allPass = 0;
		//System.out.println("cost :"+cost);
		passC = 0;
		currentPlayer = players.get(turn);
		
		boolean h = currentPlayer.bid(cost);
		
		if(!h || currentPlayer.getPowerPlants().size()==4)
		{
			System.out.println("CAN'T BID");
			if(x.equals("first"))
				cost = 0;
			else
				cost--;
			return false;
		}
		if(numFin == 3)
		{
			currentPlayer.buyPowerPlant(market.get(i), cost);
			market.remove(i);
			
			if(deck.get(0).getName().equals("Step 3")&& step == 2)
				step3 = true;
			market.add(deck.remove(0));
			Collections.sort(market);
			if(currentPlayer.getPowerPlants().size()==4)
			{
				System.out.println("MAX PP");
				return true;
			}
			
			currentPlayer.finished();
			auctionDone = true;
			numFin++;
//			if(step ==0)
//				step++;
			return true;
		}
		nextTurn();
		return true;
	}
	
	public void setRegions(ArrayList<String> list)
	{
		regions = list;
		
		for(City c: cities)
			if(regions.contains(c.getRegion()))
				c.regionTrue();
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
			int numPowered = 0;
			
			for (PowerPlant y : x.getPowerPlants())
			{
				if (y.isPowered())
					numPowered+=y.getNumPowered();
			}
			
			numPowered = Math.min(numPowered, x.getCities().size());
				
			if (numPowered == 0)
				x.addTCash(10);
			else if (numPowered == 1)
				x.addTCash(22);
			else if (numPowered == 2)
				x.addTCash(33);
			else if (numPowered == 3)
				x.addTCash(44);
			else if (numPowered == 4)
				x.addTCash(54);			
			else if (numPowered == 5)
				x.addTCash(64);			
			else if (numPowered == 6)
				x.addTCash(73);			
			else if (numPowered == 7)
				x.addTCash(82);			
			else if (numPowered == 8)
				x.addTCash(90);			
			else if (numPowered == 9)
				x.addTCash(98);			
			else if (numPowered == 10)
				x.addTCash(105);			
			else if (numPowered == 11)
				x.addTCash(112);
			else if (numPowered == 12)
				x.addTCash(118);
			else if (numPowered == 13)
				x.addTCash(124);
			else if (numPowered == 14)
				x.addCash(129);
			else if (numPowered == 15)
				x.addTCash(134);
			else if (numPowered == 16)
				x.addTCash(138);
			else if (numPowered == 17)
				x.addTCash(142);
			else if (numPowered == 18)
				x.addTCash(145);
			else if (numPowered == 19)
				x.addTCash(148);
			else if (numPowered == 20)
				x.addTCash(150);
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
			for (Player x : players)
			{
				Collections.sort(x.getPowerPlants());
			}

			Collections.sort(players);
			Collections.reverse(players);
			//System.out.println("DONE, PHASE 1, 2 SORT");
			currentPlayer = players.get(0);
		}
		else if(step!=0 && round!=1 && (phase == 3 || phase == 4))
		{
			for (Player x : players)
			{
				Collections.sort(x.getPowerPlants());
			}
			//System.out.println("DONE, PHASE 3, 4 SORT");
			Collections.sort(players);
			Collections.reverse(players);
			currentPlayer = players.get(3);
		}
		
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
			{
				if (resources.get("COAL").size() < 24)
					resources.get("COAL").push(new Resource("COAL"));
			}
			
			for (int x = 0; x < 3; x++)
			{
				if (resources.get("OIL").size() < 24)
					resources.get("OIL").push(new Resource("OIL"));
			}
	
			for (int x = 0; x < 2; x++)
			{
				if (resources.get("TRASH").size() < 24)
					resources.get("TRASH").push(new Resource("TRASH"));
			}
			
			if (resources.get("URANIUM").size() < 12)
				resources.get("URANIUM").push(new Resource("URANIUM"));
		}
		
		else if (step == 2)
		{
			for (int x = 0; x < 4; x++)
			{
				if (resources.get("COAL").size() < 24)
					resources.get("COAL").push(new Resource("COAL"));		
				
				if (resources.get("OIL").size() < 24)
					resources.get("OIL").push(new Resource("OIL"));	
			}
			
			for (int x = 0; x < 3; x++)
			{
				if (resources.get("TRASH").size() < 24)
					resources.get("TRASH").push(new Resource("TRASH"));
			}

			for (int x = 0; x < 2; x++)
			{
				if (resources.get("URANIUM").size() < 12)
					resources.get("URANIUM").push(new Resource("URANIUM")); 
			}
		}
		
		else
		{
			for (int x = 0; x < 4; x++)
			{
				if (resources.get("COAL").size() < 24)
					resources.get("COAL").push(new Resource("COAL"));		
				
				if (resources.get("OIL").size() < 24)
					resources.get("OIL").push(new Resource("OIL"));	
				
				if (resources.get("TRASH").size() < 24)
					resources.get("TRASH").push(new Resource("TRASH"));
			}
			
			for (int x = 0; x < 2; x++)
			{
				if (resources.get("URANIUM").size() < 12)
					resources.get("URANIUM").push(new Resource("URANIUM"));
			}
		}
	}
	
	public void updateMarket ()
	{
		
		if(step == 3)
		{
			//System.out.println("STEP3 = "+step3);
			if(step3)
			{
				System.out.println("MADE IT HERE");
				Collections.shuffle(deck);
				market.remove(7);
				market.remove(0);
				step3 = false;
			}
			else
			{
				System.out.println("AND I MADE IT HERE");
				if(market.size()>0)
					market.remove(0);
				if(deck.size()>0)
					market.add(deck.remove(0));
				
			}
		}
		else
		{
			//System.out.println("REMOVE LAST CARD");
			deck.add(market.remove(7));
			if(deck.get(0).getName().equals("Step 3"))
			{
				market.add(deck.remove(0));
				step3 = true;
				step3g = true;
				endStep();
			}
			else
				market.add(deck.remove(0));
		}
		
	
		Collections.sort(market);
	}
	
	public int calculateCost (Type type) //Calculates the cost of the given resource
	{				
		if (type == Type.Uranium)
		{
			if (resources.get(type.toString().toUpperCase()).size() > 4)
			{
				return costs.get((resources.get(type.toString().toUpperCase()).size() - 4));
			}
			
			else if (resources.get(type.toString().toUpperCase()).size() != 0)
			{
				return costs.get((resources.get(type.toString().toUpperCase()).size() + 10));
			}
		}
		
		else if (resources.get(type.toString().toUpperCase()).size() != 0)
		{		
			int index = 1;
			
			for (int x = 3; x <= 24; x += 3)
			{
				if (resources.get(type.toString().toUpperCase()).size() <= x)
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
		if(currentPlayer.isFinished()&& numFin!=4)
			nextTurn();
	}
	
	public void endPhase ()
	{
		if (phase == 4)		
			distributeCash();
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
		auctionDone = false;
		resourceDone = false;
		citiesDone = false;
		int x = 0;
		for(Player p: players)
			if(p.getCities().size()>=7)
				x++;
		if(step == 1 & x>0)
		{
			endStep();
			return;
		}
		endPhase();
		
		updateMarket();
		
		
		refillResources();
		
		for(Player p: players)
			for(PowerPlant g: p.getPowerPlants())
				g.getStorage().clear();
	}
	public void gameDone()
	{
		isOver = true;
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
		if(phase == 0)
		{
			round = 1;
			phase = 0;
			endPhase();
		}
		
		updateMarket();
			
		refillResources();
			//distributeCash();
			
//			for (City x : cities)
//				x.incrementCost(); 
		
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
		phase = 0;
		step = 3;
		
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
			
			current = Math.min(current, x.getCities().size());
			
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
		
		
		for(int i = players.size()-1;i>-1;i--)
		{
			if(players.get(i) != winner)
				players.remove(i);
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
