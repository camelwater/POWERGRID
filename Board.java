
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
		
		step = 1;
		
		phase = 1;
		
		setupGame();
		
		setupPlayers();
		
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
	
	public void setupPlayers ()
	{
		for (int x = 0; x < 4; x++)
			players.add(new Player()); 
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
		
	}
	
	public void updateMarket ()
	{
		Collections.sort(market, Collections.reverseOrder());
		
		market.remove(0);
		
		market.add(deck.remove(0));
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
			
		}
	}
	
	public void endGame ()
	{
		
	}
	
	public void updateGameState ()
	{
		
	}
}



























