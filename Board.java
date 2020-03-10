
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

import Enums.Type;

@SuppressWarnings({"unused", "resource"})
public class Board 
{
	private static final String DATA_PATH = "Data\\";
	
	private ArrayList <PowerPlant> deck;
	private TreeMap <String, Stack <Resource>> resources;
	private ArrayList <Player> players;
	private ArrayList <PowerPlant> market;
	private Map graph;
	
	public Board () throws IOException
	{
		deck = new ArrayList <PowerPlant> ();
		
		resources = new TreeMap <String, Stack <Resource>> ();
		
		players = new ArrayList <Player> ();
		
		market = new ArrayList <PowerPlant> ();
		
		setupGame();
		
		setupPlayers();
		
		distributeCash();
		
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
		
		System.out.println(deck.toString()); 
	}
	
	public void setupPlayers ()
	{
		for (int x = 0; x < 4; x++)
		{
			players.add(new Player());
		}
	}
	
	public void distributeCash ()
	{
		
	}
	
	public void calculatePlayerOrder ()
	{
		
	}
	
	public void refillResources ()
	{
		
	}
	
	public void updateMarket ()
	{
		
	}
	
	public void endPhase ()
	{
		
	}
	
	public void endRound ()
	{
		
	}
	
	public void endStep ()
	{
		
	}
	
	public Player endGame ()
	{
		return null;
	}
	
	public void updateGameState ()
	{
		
	}
}



























