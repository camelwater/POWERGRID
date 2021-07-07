import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings({"serial", "unused"})
public class GraphicsRunner extends JPanel implements MouseListener
{
	private static String path = "Misc\\";
	private Board game;
	private ArrayList<String>regions;
	private BufferedImage bg;
	BufferedImage cash;
	BufferedImage coal;
	BufferedImage oil;
	BufferedImage uranium;
	BufferedImage trash;
	
	BufferedImage blue;
	BufferedImage green;
	BufferedImage red;
	BufferedImage yellow;
	
	JButton b = null;
	private int page = 3;
	private int index = -1;
	
	private boolean ppVisible = false;
	private boolean first = true;
	boolean ppChosen = false;
	boolean maxPP = false;
	
	private int numFin = 0;
	
	private int coalC = 0;
	private int oilC = 0;
	private int trashC = 0;
	private int uranC = 0;
	
	private boolean paintC = false;
	private boolean paintO = false;
	private boolean paintT = false;
	private boolean paintU = false;
	
	private int rCost;
	
	private boolean free = false;
	private int pC = 0;
	private int pO = 0;
	private int pT = 0;
	private int pU = 0;
	private int pH = 0;
	private boolean fi = true;
	
	private boolean ppChosen2 = false;
	private int ppIndex = -1;
	private int jb = 1;
	private boolean buying = false;
	private String cityBuy = "";
	
	private boolean stepp3 = true;
	private HashMap <Type, Stack <Resource>> tempResources = new HashMap <Type, Stack <Resource>>(); 
	
	public GraphicsRunner(Board g, ArrayList<String> r) throws IOException
	{
		game = g;
		regions = r;
		game.setRegions(regions);
		 JFrame frame = new JFrame("POWERGRID");
		 frame.setBackground(Color.orange);
		 setVisible(true);

		 bg = ImageIO.read(getClass().getResource("resources/Powergridmap-Edited.png"));
		 
		 cash = ImageIO.read(getClass().getResource("resources/cash.png"));
		 coal = ImageIO.read(getClass().getResource("resources/coal.png"));
		 oil = ImageIO.read(getClass().getResource("resources/oil.png"));
		 trash = ImageIO.read(getClass().getResource("resources/trash.png"));
		 uranium = ImageIO.read(getClass().getResource("resources/uranium.png"));
		 
		 blue = ImageIO.read(getClass().getResource("resources/blue_house.png"));
		 red = ImageIO.read(getClass().getResource("resources/red_house.png"));
		 green = ImageIO.read(getClass().getResource("resources/green_house.png"));
		 yellow = ImageIO.read(getClass().getResource("resources/yellow_house.png"));
		 
		 addMouseListener(this);
		 
		 frame.add(this);
		 frame.setSize(1920,1080);
		 frame.setResizable(true);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setVisible(true);
		 
		
	}
	
	@SuppressWarnings("unchecked")
	public void paintComponent(Graphics g)
	{
		
		if(game.isOver())
		{
			g.setColor(new Color(0, 138, 138));
			g.fillRect(0, 0, 1920, 1080);
			
			g.setColor(Color.black);
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 100));
			g.drawString("Good job "+game.getPlayers().get(0), 400, 450);
		}
		else
		{    
			
			if (game.getPhase() == 1 && game.getStep() != 0)
			{
				//System.out.println("HELLO, step "+game.step);
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				if(game.getPhase()!= 4 && game.getStep()!=0)
				{
					
					g.setColor(Color.black);
					g.setFont(new Font("Arial", Font.BOLD, 50));
					g.drawString("Step: "+game.getStep(), 650, 50);
					g.drawString("Phase: "+game.getPhase(), 1000, 50);
				}
				
				game.calculatePlayerOrder();
				paintOrder(g);
				//game.endPhase();
				
			}
			
			
			if(game.getStep() == 0 && game.getPhase()==1)
			{
				game.start();
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				g.setColor(Color.black);
				g.setFont(new Font("Arial",Font.BOLD, 100));
				g.drawString("POWERGRID", 645, 125);
				paintOrder(g);
				
				//game.endPhase();
			}
			
			//POWERPLANT AUCTIONING
			if(game.getPhase() == 2)
			{
				if(game.getCurrentPlayer().maxPP())
				{
					index = -1;
				}
				if(game.auctionDone())
				{
					first = true;
					ppChosen = false;
					index = -1;
					game.cost = 0;
					numFin = 0;
					if(game.allPass == 4)
					{
						game.getMarket().remove(0);
						if(game.getDeck().get(0).getName().equals("Step 3"))
							game.step3 = true;
						game.getMarket().add(game.getDeck().remove(0));
						Collections.sort(game.getMarket());
					}
					game.allPass = 0;
					if(game.step3 && game.getStep()==2)
						game.endStep();
					
					game.endPhase();
					game.calculatePlayerOrder();
					repaint();
				}
				
				if(numFin<game.numFin())//next first bidder
				{

					first = true;
					ppChosen = false;
					index = -1;
					game.cost = 0;
					numFin++;	
				}
				
				//auctioning power plants
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				paintMarket(g);
				
				if(game.getPhase()!= 4 && game.getStep()!=0)
				{
					
					g.setColor(Color.black);
					g.setFont(new Font("Arial", Font.BOLD, 50));
					g.drawString("Step: "+game.getStep(), 650, 45);
					g.drawString("Phase: "+game.getPhase(), 1000, 45);
				}
				
				//which card is being auctioned
				BufferedImage check = null;
				
				try {
					check = ImageIO.read(getClass().getResource("resources/check.png"));
				} catch (IOException e) {}
				
				System.out.println("index " +index);
				if(index == 0)
					g.drawImage(check, 300, 50, 50, 50, null);
				else if(index == 1)
					g.drawImage(check, 650, 50, 50, 50, null);
				else if(index == 2)
					g.drawImage(check, 1000, 50, 50, 50, null);
				else if(index == 3)
					g.drawImage(check, 1350, 50, 50, 50, null);
				else if(index == 4)
					g.drawImage(check, 300, 280, 50, 50, null);
				else if(index == 5)
					g.drawImage(check, 650, 280, 50, 50, null);
				
				
				paintOrder(g); //player order thing
				g.setFont(new Font("Roboto", Font.BOLD, 75));
				g.setColor(Color.black);
				g.drawString("highest bid: "+game.cost, 700, 550);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				g.setColor(Color.white);

				g.drawString("bal: $"+game.getCurrentPlayer().balance(), 330, 550);
				
				g.setColor(Color.black);
				
				g.fillOval(600, 500, 75, 50);
				g.fillOval(1250, 500, 75, 50);
				g.setColor(Color.white);
				g.drawString("bid", 620, 533);
				g.drawString("pass", 1262, 533);
				
				if(game.getCurrentPlayer().getPowerPlants().size()>0)
					paintPlants(g);
				if(game.getCurrentPlayer().maxPP())
				{
					maxPP = true;
					g.setColor(Color.red);
					g.setFont(new Font("Arial", Font.BOLD, 50));
					g.drawString("CHOOSE A POWER PLANT TO REMOVE", 450, 650);
					g.setColor(Color.black);
				}
			}
			
			//RESOURCE BUYING
			if(game.getPhase() == 3)
			{
				
				if(game.resourceDone())
				{
					game.endPhase();
					
					numFin = 0;
					if(game.step == 0)
						game.step++;
					game.calculatePlayerOrder();
					page  = game.getPlayers().indexOf(game.getCurrentPlayer());
					tempResources = game.getCurrentPlayer().getResourcesCopy();
					repaint();
				}
				
				//buying resources
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				try {
					paintResources(g);
				} catch (IOException e) {}
				
				paintOrder(g);
				
				if(game.getPhase()!= 4 && game.getStep()!=0)
				{
					
					g.setColor(Color.black);
					g.setFont(new Font("Arial", Font.BOLD, 50));
					g.drawString("Step: "+game.getStep(), 650, 50);
					g.drawString("Phase: "+game.getPhase(), 1000, 50);
				}
				
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				
				g.drawString("bal: $"+game.getCurrentPlayer().balance(), 900, 600);
				g.drawString("cost: "+rCost, 880, 375);
				
				for(int i = 0; i<game.getCurrentPlayer().getResources().get(Type.Oil).size();i++)
					g.drawImage(oil, 100 +(35*i), 650, 25, 25, null);
				for(int i = 0; i<game.getCurrentPlayer().getResources().get(Type.Coal).size();i++)
					g.drawImage(coal, 100 +(35*i), 700, 25, 25, null);
				for(int i = 0; i<game.getCurrentPlayer().getResources().get(Type.Trash).size();i++)
					g.drawImage(trash, 100 +(35*i), 750, 25, 25, null);
				for(int i = 0; i<game.getCurrentPlayer().getResources().get(Type.Uranium).size();i++)
					g.drawImage(uranium, 100 +(35*i), 800, 25, 25, null);
				
				if(game.getCurrentPlayer().getPowerPlants().size()>0)
					paintPlants(g);
			}
			
			//CITY BUYING
			if(game.getPhase() == 4)
			{
				if(game.citiesDone())
				{
					int a = 0;
					for(Player x: game.getPlayers())
					{
						if(x.getCities().size()>a)
							a = x.getCities().size();
					}
					for(int i = game.getMarket().size()-1;i>-1;i--)
					{
						if(a>=game.getMarket().get(i).getID())
						{
							game.getMarket().remove(i);
							if(game.getDeck().get(0).getName().equals("Step 3")&& game.getStep() == 2)
								game.step3 = true;
							game.getMarket().add(game.getDeck().remove(0));
							Collections.sort(game.getMarket());
						}
					}
					
					int s = 0;
					for(Player x: game.getPlayers())
					{
						if(x.getCities().size()>=17)
							s++;
					}
					if(s>0)
					{
						game.endGame();
						repaint();
					}
					else
					{
						if(game.step3)
							game.endStep();
						
						game.endPhase();
						game.numFin = 0;
					}
					repaint();
				}
				
				if(!game.isOver())
				{
					if(ppVisible) //look at and fill power plants
					{
						paintPP(g);
					}
					else //city building
					{
						g.drawImage(bg, 0, 0, 1920, 1060, null); //map
					
						//g.setColor(Color.orange);
				
						try {
							g.drawImage(ImageIO.read(getClass().getResource("resources/bottom_banner.png")),25, 930, 1330, 110, null);
						} catch (IOException e1) {}
					
						try {
							g.drawImage(ImageIO.read(getClass().getResource("resources/top_banner.png")),33, 20, 500, 115, null);
						} catch (IOException e2) {}
						
						try {
							g.drawImage(ImageIO.read(getClass().getResource("resources/top_banner.png")),1210, 20, 680, 115, null);
						} catch (IOException e1) {}
					
						g.drawImage(game.getPlayers().get(0).getPic(), 1250, 50, 20, 20, null);
						g.drawString(game.getPlayers().get(0).getCities().size()+"", 1275, 60);
					
						g.drawImage(game.getPlayers().get(1).getPic(), 1350, 50, 20, 20, null);
						g.drawString(game.getPlayers().get(1).getCities().size()+"", 1375, 60);
					
						g.drawImage(game.getPlayers().get(2).getPic(), 1450, 50, 20, 20, null);
						g.drawString(game.getPlayers().get(2).getCities().size()+"", 1475, 60);
					
						g.drawImage(game.getPlayers().get(3).getPic(), 1550, 50, 20, 20, null);
						g.drawString(game.getPlayers().get(3).getCities().size()+"", 1575, 60);
					
						//resources in bottom left corner
					
						g.setFont(new Font("Arial", Font.PLAIN, 20));
					
						g.drawString("$"+game.calculateCost(Type.Coal), 80, 1015); 
						g.drawString("$"+game.calculateCost(Type.Oil), 460, 1022);
						g.drawString("$"+game.calculateCost(Type.Trash), 842, 1022);
						g.drawString("$"+game.calculateCost(Type.Uranium), 1220, 1022);

						g.drawImage(coal, 69, 963, 40, 40, null);
						g.drawImage(oil, 451, 965, 40, 40, null);
						g.drawImage(trash, 833, 965, 40, 40, null);
						g.drawImage(uranium, 1215, 965, 40, 40, null);
	
						g.drawString("x"+game.getResources().get("COAL").size(), 120, 990);
						g.drawString("x"+game.getResources().get("OIL").size(), 500, 990);
						g.drawString("x"+game.getResources().get("TRASH").size(), 885, 990);
						g.drawString("x"+game.getResources().get("URANIUM").size(), 1265, 990);
					
					
					
						g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 50));
						g.setColor(Color.black);
				
						g.drawString("Player "+game.getPlayers().get(page).getName(), 1625, 900);//player name
					
						if(game.getPlayers().get(page).getHouse().equals("red"))
							g.drawImage(red, 1715, 915, 17, 17, null);
						if(game.getPlayers().get(page).getHouse().equals("blue"))
							g.drawImage(blue, 1715, 915, 17, 17, null);
						if(game.getPlayers().get(page).getHouse().equals("green"))
							g.drawImage(green, 1715, 915, 17, 17, null);
						if(game.getPlayers().get(page).getHouse().equals("yellow"))
							g.drawImage(yellow, 1715, 915, 17, 17, null);
					
						g.drawString("Step: "+game.getStep(), 900, 60);	//step
						g.drawString("Phase: "+game.getPhase(), 875, 110); //phase
						g.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 25));
						if(game.getPlayers().get(page).getName().equals(game.getCurrentPlayer().getName()))
						{
							g.drawString("CURRENT PLAYER",1612, 850);						
							g.fillOval(910, 865, 100, 50);
							g.setColor(Color.white);
							g.setFont(new Font("Arial", Font.BOLD, 25));
							g.drawString("FINISH", 920, 900);
						}
						paintOrder(g);
						paintCities(g);
					
						//cash
						g.setFont(new Font("Times New Roman", Font.ITALIC| Font.BOLD, 30));
						g.setColor(Color.black);
					
						g.drawImage(cash, 50, 630, 45, 45, null);
						g.drawString(""+game.getPlayers().get(page).balance(), 110, 665); 	
				
						//other resources
						g.drawImage(coal, 50, 670, 45, 45, null);
						g.drawString(""+game.getPlayers().get(page).getCoal(), 110, 710); 
				
						g.drawImage(oil, 50, 730, 40, 40, null);
						g.drawString(""+game.getPlayers().get(page).getOil(), 110, 760);
				
						g.drawImage(trash, 50, 790, 40, 40, null);
						g.drawString(""+game.getPlayers().get(page).getTrash(), 110, 825);
				
						g.drawImage(uranium, 50, 860, 40, 40, null);
						g.drawString(""+game.getPlayers().get(page).getUranium(), 110, 890);
					
						paintCities(g);
					
						//arrows
						try 
						{
							g.drawImage(ImageIO.read(getClass().getResource("resources/arrow3.png")), 1625, 1040, 90, -91, null);
						} catch (IOException e) {e.printStackTrace();}
						
						try {
							g.drawImage(ImageIO.read(getClass().getResource("resources/arrow3.png")), 1825, 949, -90, 91, null);
						} catch (IOException e) {e.printStackTrace();}
					
						try
						{
							g.drawImage(ImageIO.read(getClass().getResource("resources/factory.png")), 1600, 625, 200, 200, null);
						} catch (IOException e) {}
				
					}
				
					if(buying && game.cityA(cityBuy))
						paintBuy(g, 835, 465);
				
				}
			}
			
			if(game.getPhase() == 5)
			{
				//add bureaucracy thing later
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				
				if(game.getPhase()!= 4 && game.getStep()!=0)
				{
					
					g.setColor(Color.black);
					g.setFont(new Font("Arial", Font.BOLD, 50));
					g.drawString("Step: "+game.getStep(), 650, 50);
					g.drawString("Phase: "+game.getPhase(), 1000, 50);
				}
				game.calculatePlayerOrder();
				g.setColor(Color.black);
				g.setFont(new Font("Roboto", Font.BOLD | Font.ITALIC, 35));
				
				
				g.drawString("P"+game.getPlayers().get(0).getName(), 835, 300);
				g.drawString("P"+game.getPlayers().get(1).getName(), 835, 350);
				g.drawString("P"+game.getPlayers().get(2).getName(), 835, 400);
				g.drawString("P"+game.getPlayers().get(3).getName(), 835, 450);
				
				for(int i = 0;i<4;i++)
				{
					if(game.getPlayers().get(i).getHouse().equals("red"))
						g.drawImage(red, 890, 275+i*50, 25, 25, null);
					else if(game.getPlayers().get(i).getHouse().equals("blue"))
						g.drawImage(blue, 890, 275+i*50, 25, 25, null);
					else if(game.getPlayers().get(i).getHouse().equals("green"))
						g.drawImage(green, 890, 275+i*50, 25, 25, null);
					else if(game.getPlayers().get(i).getHouse().equals("yellow"))
						g.drawImage(yellow, 890, 275+i*50, 25, 25, null);
				}
				for(int i = 0;i<4;i++)
				{
					System.out.println(game.getPlayers().get(i).getTCash()+", "+game.getPlayers().get(i).balance());
						g.drawString("+ $"+game.getPlayers().get(i).getTCash(), 945, 300+i*50);
				}
				
				//game.endRound();
				
			}
			if(game.step3g &&stepp3)
			{
				//System.out.println("STEP3 is "+game.step3);
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, 50));
				g.drawString("Drew Step 3 Card", 750, 700);
				stepp3 = false;
			}
			
		}
		
	}
	public void paintCities(Graphics g)
	{
		int count = 0;
		for(City c: game.getCities())
		{
			//Player p = game.getPlayers().get(i);
			//for(City c: p.getCities())
				if(c.getName().equals("Seattle"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 130, 195);
					}
					if(c.getNumOccupants() == 1)
						g.drawImage(c.getOccupants().get(0).getPic(), 135, 152, 15, 15, null);
					else if(c.getNumOccupants() ==2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 135, 152, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 115, 172, 15, 15, null);
					}
					else if (c.getNumOccupants() ==3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 135, 152, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 105, 172, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 162, 172, 15, 15, null);
					}
					
				}
				
				if(c.getName().equals("Portland"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 75, 290);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 84, 246, 15, 15, null);
					else if(c.getNumOccupants() ==2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 84, 246, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 64, 266, 15, 15, null);
					}
					else if (c.getNumOccupants() ==3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 84, 246, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 64, 266, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 110, 266, 15, 15, null);
					}
					
				}
				if(c.getName().equals("San Francisco"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 76, 552);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 85, 518, 15, 15, null);
					else if(c.getNumOccupants() ==2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 85, 518, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 65, 538, 15, 15, null);
					}
					else if (c.getNumOccupants() ==3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 85, 518, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 65, 538, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 112, 538, 15, 15, null);	
					}
				}
				
				if(c.getName().equals("Los Angeles"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 200, 675);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 206, 632, 15, 15, null);
					
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 206, 632, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 186, 652, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 206, 632, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 186, 652, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 233, 652, 15, 15, null);	
					}
					
				}
				if(c.getName().equals("San Diego"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 275, 720);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 281, 695, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 281, 695, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 261, 715, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 281, 695, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 261, 715, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 308, 715, 15, 15, null);	
					}
					
				}
				//
				if(c.getName().equals("Boise"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 318, 345);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 325, 319, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 325, 319, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 305, 339, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 325, 319, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 305, 339, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 352, 339, 15, 15, null);	
					}
				}
				if(c.getName().equals("Las Vegas"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 343, 590);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 350, 564, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 350, 564, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 330, 584, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 350, 564, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 330, 584, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 377, 584, 15, 15, null);	
					}
				}
				if(c.getName().equals("Salt Lake City"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 464, 453);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 469, 428, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 469, 428, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 449, 448, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 469, 428, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 449, 448, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 496, 448, 15, 15, null);	
					}
				}
				if(c.getName().equals("Phoenix"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 460, 687);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 467, 662, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 467, 662, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 447, 682, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 467, 662, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 447, 682, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 494, 682, 15, 15, null);	
					}
				}
				
				if(c.getName().equals("Billings"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 600, 280);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 605, 255, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 605, 255, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 585, 275, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 605, 255, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 585, 275, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 632, 275, 15, 15, null);	
					}
				}
				if(c.getName().equals("Cheyenne"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 711, 413);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 717, 388, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 717, 388, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 697, 408, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 717, 388, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 697, 408, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 744, 408, 15, 15, null);
					}
				}
				if(c.getName().equals("Denver"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 692, 477);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 697, 452, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 697, 452, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 677, 472, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 697, 452, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 677, 472, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 724, 472, 15, 15, null);	
					}
				}
				if(c.getName().equals("Santa Fe"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 655, 620);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 660, 595, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 660, 595, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 640, 615, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 660, 595, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 640, 615, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 687, 615, 15, 15, null);	
					}
				}
				//
				if(c.getName().equals("Fargo"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 957, 247);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 962, 220, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 962, 220, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 942, 240, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 962, 220, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 942, 240, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 989, 240, 15, 15, null);	
					}
				}
				if(c.getName().equals("Duluth"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1108, 217);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1113, 193, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1113, 193, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1093, 212, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1113, 193, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1093, 212, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 1140, 212, 15, 15, null);
					}
				}
				if(c.getName().equals("Minneapolis"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1077, 295);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1082, 269, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1082, 269, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1062, 289, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1082, 269, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1062, 289, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1109, 289, 15, 15, null);	
					}
				}
				if(c.getName().equals("Omaha"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 982, 430);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 987, 402, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 987, 402, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 967, 422, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 987, 402, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 967, 422, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1014, 422, 15, 15, null);	
					}
				}
				//
				if(c.getName().equals("Kansas City"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1023, 513);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1030, 488, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1030, 488, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1010, 508, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1030, 488, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1010, 508, 15, 15, null);
						g.drawImage(c.getOccupants().get(2).getPic(), 1057, 508, 15, 15, null);	
					}
				}
				if(c.getName().equals("Oklahoma City"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 957, 630);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 963, 595, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 963, 595, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 943, 615, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 963, 595, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 943, 615, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 990, 615, 15, 15, null);
					}
				}
				if(c.getName().equals("Dallas"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 980, 707);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 985, 682, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 985, 682, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 965, 702, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 985, 682, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 965, 702, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1012, 702, 15, 15, null);
					}
				}
				if(c.getName().equals("Houston"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 995, 810);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1000, 770, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1000, 770, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 980, 790, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1000, 770, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 980, 790, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1027, 790, 15, 15, null);	
					}
				}
				//
				if(c.getName().equals("Chicago"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1240, 410);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1245, 385, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1245, 385, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1225, 405, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1245, 385, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1225, 405, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1272, 405, 15, 15, null);	
					}
				}
				if(c.getName().equals("St. Louis"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1177, 517);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1183, 491, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1183, 491, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1163, 511, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1183, 491, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1163, 511, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1210, 511, 15, 15, null);
					}
				}
				if(c.getName().equals("Memphis"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1176, 630);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1182, 605, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1182, 605, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1162, 625, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1182, 605, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1162, 625, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1209, 625, 15, 15, null);
					}
				}
				if(c.getName().equals("Birmingham"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1284, 685);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1289, 659, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1289, 659, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1269, 679, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1289, 659, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1269, 679, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1316, 679, 15, 15, null);	
					}
				}
				if(c.getName().equals("New Orleans"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1177, 792);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1183, 766, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1183, 766, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1163, 786, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1183, 766, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1163, 786, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1210, 786, 15, 15, null);
					}
				}
				//
				if(c.getName().equals("Detroit"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1395, 382);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1400, 357, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1400, 357, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1380, 377, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1400, 357, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1380, 377, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1427, 377, 15, 15, null);
					}
				}
				if(c.getName().equals("Cincinnati"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1380, 497);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1385, 470, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1385, 470, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1365, 490, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1385, 470, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1365, 490, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1412, 490, 15, 15, null);	
					}
				}
				if(c.getName().equals("Knoxville"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1387, 595);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1393, 570, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1393, 570, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1373, 590, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1393, 570, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1373, 590, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1420, 590, 15, 15, null);	
					}
				}
				if(c.getName().equals("Atlanta"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1395, 685);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1400, 659, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1400, 659, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1380, 679, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1400, 659, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1380, 679, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1427, 679, 15, 15, null);
					}
				}
				
				if(c.getName().equals("Buffalo"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1582, 360);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1588, 335, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1588, 335, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1568, 355, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1588, 335, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1568, 355, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1615, 255, 15, 15, null);
					}
				}
				if(c.getName().equals("Pittsburgh"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1538, 455);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1543, 430, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1543, 430, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1523, 450, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1543, 430, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1523, 450, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1568, 450, 15, 15, null);	
					}
				}
				//
				if(c.getName().equals("Boston"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1835, 360);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1840, 335, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1840, 335, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1820, 355, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1840, 335, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1820, 355, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1865, 355, 15, 15, null);	
					}
				}
				if(c.getName().equals("New York"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1757, 418);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1762, 393, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1762, 393, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1742, 413, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1762, 393, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1742, 413, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1787, 413, 15, 15, null);	
					}
				}
				if(c.getName().equals("Philadelphia"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1715, 470);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1720, 445, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1720, 445, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1700, 465, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1720, 445, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1700, 465, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1745, 465, 15, 15, null);
					}
				}
				if(c.getName().equals("Washington D.C."))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1625, 510);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1630, 483, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1630, 483, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1610, 503, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1630, 483, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1610, 503, 15, 15, null);	
							g.drawImage(c.getOccupants().get(2).getPic(), 1655, 503, 15, 15, null);	
					}
				}
				if(c.getName().equals("Norfolk"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X",1690, 569);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1695, 544, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1695, 544, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1675, 564, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1695, 544, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1675, 564, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1720, 564, 15, 15, null);	
					}
				}
				if(c.getName().equals("Raleigh"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1595, 615);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1600, 589, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1600, 589, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1580, 609, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1600, 589, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1580, 609, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1625, 609, 15, 15, null);	
					}
				}
				if(c.getName().equals("Savannah"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1512, 710);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1517, 683, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1517, 683, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1497, 703, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1517, 683, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1497, 703, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1542, 703, 15, 15, null);	
					}
				}
				if(c.getName().equals("Jacksonville"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1507, 777);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1513, 752, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1513, 752, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1493, 772, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1513, 752, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1493, 772, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1538, 772, 15, 15, null);	
					}
				}
				if(c.getName().equals("Tampa"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1432, 860);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1437, 835, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1437, 835, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1417, 855, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1437, 835, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1417, 855, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1462, 855, 15, 15, null);
					}
				}
				if(c.getName().equals("Miami"))
				{
					if(!c.isOpen())
					{
						g.setColor(Color.red);
						g.setFont(new Font("Verdana", Font.BOLD, 50));
						g.drawString("X", 1545, 925);
					}
					if(c.getNumOccupants() == 1 )
						g.drawImage(c.getOccupants().get(0).getPic(), 1550, 901, 15, 15, null);
					else if(c.getNumOccupants() == 2)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1550, 901, 15, 15, null);
							g.drawImage(c.getOccupants().get(1).getPic(), 1530, 920, 15, 15, null);
					}
					else if (c.getNumOccupants() == 3)
					{
						g.drawImage(c.getOccupants().get(0).getPic(), 1550, 901, 15, 15, null);
						g.drawImage(c.getOccupants().get(1).getPic(), 1530, 920, 15, 15, null);
							g.drawImage(c.getOccupants().get(2).getPic(), 1575, 920, 15, 15, null);
					}
				}
				
				//count++;
			}
		
	}
	public void paintBuy(Graphics g, int x, int y)
	{
		g.setColor(Color.white);
		g.fillOval(x, y, 250, 125);
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 75));
		g.drawString("BUY", x+50, y+83);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(cityBuy, x+65, y+110);

	}
	public void paintPlants(Graphics g) //paints hands
	{
		int i = 0;
		if(game.getPhase()!=4)
		{
			int s = game.getCurrentPlayer().getPowerPlants().size();
			for(PowerPlant p: game.getCurrentPlayer().getPowerPlants())
			{
				if(s ==1)
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), 835, 725, 250, 250, null); 
					} catch (IOException e) {}
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), (835/s+150)+i*(1200/s), 725, 250, 250, null); 
					} catch (IOException e) {}
				}
				i++;
			}
		}
		else
		{
			int s = game.getPlayers().get(page).getPowerPlants().size();
			for(PowerPlant p: game.getPlayers().get(page).getPowerPlants())
			{
				//String x = p.capType();
				if(s ==1)
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), 835, 350, 250, 250, null); 
					} catch (IOException e) {}
					
					for(int f = 0;f<p.getStorage().size();f++)
					{
						g.drawImage(p.getStorage().get(f).getPic(), (865/s) +(35*f), 435, 25, 25, null);
					}
						
				}
				else
				{
					if(s==3)
					{
						try {
							g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), (835/s+150)+i*(1200/s), 350, 250, 250, null); 
						} catch (IOException e) {}
						
					
							for(int f = 0;f<p.getStorage().size();f++)
							{
								g.drawImage(p.getStorage().get(f).getPic(), (835/s+185)+i*(1200/s) +(35*f), 435, 25, 25, null);
							}
						
							for(int f = 0;f<p.getStorage().size();f++)
							{
								g.drawImage(p.getStorage().get(f).getPic(), (835/s+185)+i*(1200/s) +(35*f), 435, 25, 25, null);
							}
						
						
							for(int f = 0;f<p.getStorage().size();f++)
							{
								g.drawImage(p.getStorage().get(f).getPic(), (835/s+185)+i*(1200/s) +(35*f), 435, 25, 25, null);
							}
					}
					else if (s ==2)
					{
						try {
							g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), (835/s+150)+i*(1200/s), 350, 250, 250, null); 
						} catch (IOException e) {}
						
							for(int f = 0;f<p.getStorage().size();f++)
							{
								g.drawImage(p.getStorage().get(f).getPic(), (835/s+185)+i*(1200/s) +(35*f), 435, 25, 25, null);
							}
						
							for(int f = 0;f<p.getStorage().size();f++)
							{
								g.drawImage(p.getStorage().get(f).getPic(), (835/s+185)+i*(1200/s) +(35*f), 435, 25, 25, null);
							}
						
					}
					
						
				}
				i++;
			}
		}
	}
	
	public void paintPP(Graphics g) //paints phase 4 pp viewer
	{
		
		g.setColor(new Color(0,138,138));
		g.fillRect(0, 0, 1920, 1080);
		
		paintPlants(g);
		
		g.setFont(new Font("Roboto", Font.BOLD, 50));
		g.setColor(Color.black);
		g.drawString("X", 945, 935);
		g.drawString("Player " + game.getPlayers().get(page).getName(), 865, 125);
		g.drawImage(game.getPlayers().get(page).getPic(), 935, 150, 50, 50, null);
		
		for(int i = 0; i<game.getPlayers().get(page).getResources().get(Type.Oil).size();i++)
		{
			if(game.getTurn() == page) 
			{
				paintO = true;
				
			}
			g.drawImage(oil, 840 +(35*i), 650, 25, 25, null);
		}
		if(paintO)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/plus.png")),800, 650, 20, 20, null);
			} catch (IOException e) {}
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/minus.png")),820, 653, 20, 15, null);
			} catch (IOException e) {}
		}
		
		for(int i = 0; i<game.getPlayers().get(page).getResources().get(Type.Coal).size();i++)
		{
			if(game.getTurn() == page) 
			{
				paintC = true;
			}
			g.drawImage(coal, 840 +(35*i), 700, 25, 25, null);
		}
		if(paintC)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/plus.png")),800, 700, 20, 20, null);
			} catch (IOException e) {}
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/minus.png")),820, 703, 20, 15, null);
			} catch (IOException e) {}
			
		}
		
		for(int i = 0; i<game.getPlayers().get(page).getResources().get(Type.Trash).size();i++)
		{
			if(game.getTurn() == page) 
			{
				paintT = true;
			
			}
			g.drawImage(trash, 840 +(35*i), 750, 25, 25, null);
		}
		if(paintT)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/plus.png")),800, 750, 20, 20, null);
			} catch (IOException e) {}
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/minus.png")),820, 753, 20, 15, null);
			} catch (IOException e) {}
		}
		
		for(int i = 0; i<game.getPlayers().get(page).getResources().get(Type.Uranium).size();i++)
		{
			if(game.getTurn() == page) 
			{
				paintU = true;
			
			}
			g.drawImage(uranium, 840 +(35*i), 800, 25, 25, null);
		}
		if(paintU)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/plus.png")),800, 800, 20, 20, null);
			} catch (IOException e) {}
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/minus.png")),820, 803, 20, 15, null);
			} catch (IOException e) {}
		}
		
		//showing which card is being powered
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 75));
		int x = game.getCurrentPlayer().getPowerPlants().size();
		if(x == 1)
		{
			if(ppIndex == 0)
				g.drawString("V", 937, 325);
		}
		else if(x == 2)
		{
			if(ppIndex == 0)
				g.drawString("V", 665, 325);
			if(ppIndex == 1)
				g.drawString("V", 1265, 325);
		}
		else if(x == 3)
		{
			if(ppIndex == 0)
				g.drawString("V", 527, 325);
			if(ppIndex == 1)
				g.drawString("V", 925, 325);
			if(ppIndex == 2)
				g.drawString("V", 1320, 325);
		}
		
		//whether card is powered or not
		BufferedImage check = null;
		try {
			check = ImageIO.read(getClass().getResource("resources/check.png"));
		} catch (IOException e) {}
		if(x == 1)
		{
			if(game.getCurrentPlayer().getPowerPlants().get(0).isPowered() && page == game.getPlayers().indexOf(game.getCurrentPlayer()))
				g.drawImage(check, 937-25, 450, 50, 50, null); 
		}
		else if(x ==2)
		{
			if(game.getCurrentPlayer().getPowerPlants().get(0).isPowered()&& page == game.getPlayers().indexOf(game.getCurrentPlayer()))
				g.drawImage(check, 665-25, 450, 50, 50, null); 
			if(game.getCurrentPlayer().getPowerPlants().get(1).isPowered()&& page == game.getPlayers().indexOf(game.getCurrentPlayer()))
				g.drawImage(check, 1265-25, 450, 50, 50, null); 
		}
		else if (x==3)
		{
			if(game.getCurrentPlayer().getPowerPlants().get(0).isPowered()&& page == game.getPlayers().indexOf(game.getCurrentPlayer()))
				g.drawImage(check, 527-25, 450, 50, 50, null); 
			if(game.getCurrentPlayer().getPowerPlants().get(1).isPowered()&& page == game.getPlayers().indexOf(game.getCurrentPlayer()))
				g.drawImage(check, 925-25, 450, 50, 50, null); 
			if(game.getCurrentPlayer().getPowerPlants().get(2).isPowered()&& page == game.getPlayers().indexOf(game.getCurrentPlayer()))
				g.drawImage(check, 1320-25, 450, 50, 50, null); 
		}
	}
	
	public void paintMarket(Graphics g) //paints pp auctioning market
	{
		int i = 0;
		int c = 0;
		for(PowerPlant p: game.getMarket())
		{
			if(i==4)
			{
				if(p.getName().equals("Step 3"))
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/step 3.jpg")), 350*c+300, 280, 200, 200, null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), 350*c+300, 280, 200, 200, null);
					} catch (IOException e) {}
				}
				c++;
			}
			else
			{
				if(p.getName().equals("Step 3"))
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/step 3.jpg")), 350*c+300, 280, 200, 200, null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("resources/"+p.getID()+".png")), 350*i+300, 50, 200, 200, null);
					} catch (IOException e) {}
				}
			
				i++;
			}
		}
	}
	
	public void paintResources(Graphics g) throws IOException
	{
		for(int i = 0;i<4;i++)
			g.drawImage(ImageIO.read(getClass().getResource("resources/minus.png")), 450+i*300, 300, 25, 20, null);
		for(int i = 0;i<4;i++)
			g.drawImage(ImageIO.read(getClass().getResource("resources/plus.png")), 450 +i*300, 200, 25, 25, null);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.black);
		
		//price 
		
		g.drawString("$"+game.calculateCost(Type.Coal), 450, 340); 
		g.drawString("$"+game.calculateCost(Type.Oil), 750, 340);
		g.drawString("$"+game.calculateCost(Type.Trash), 1050, 340);
		g.drawString("$"+game.calculateCost(Type.Uranium), 1350, 340);

		
		//images
		g.drawImage(coal, 443, 234, 40, 40, null);
		g.drawImage(oil, 743, 237, 40, 40, null);
		g.drawImage(trash, 1043, 237, 40, 40, null);
		g.drawImage(uranium, 1343, 237, 40, 40, null);
		g.drawImage(ImageIO.read(getClass().getResource("resources/pay.png")), 540, 300, 750, 350, null);
		
		//amount purchased and left
		g.drawString("x"+coalC+"/"+game.getResources().get("COAL").size(), 500, 260);
		g.drawString("x"+oilC+"/"+game.getResources().get("OIL").size(), 800, 260);
		g.drawString("x"+trashC+"/"+game.getResources().get("TRASH").size(), 1100, 260);
		g.drawString("x"+uranC+"/"+game.getResources().get("URANIUM").size(), 1400, 260);

	}
	
	public void paintOrder(Graphics g) //paints player order 
	{
		g.setColor(Color.black);
		
		if(game.getPhase()==2 || game.getPhase()==3)
		{
			if(game.getPhase()==3)
				try {
					g.drawImage(ImageIO.read(getClass().getResource("resources/orderArrow2.png")), 35, 90, 45, 75, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			else
				try {
					g.drawImage(ImageIO.read(getClass().getResource("resources/orderArrow2.png")), 35, 160, 45, -75, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
			g.setFont(new Font("Arial", Font.ITALIC, 15));
		
			g.drawString("P"+game.getPlayers().get(0).getName(), 80, 100);
			g.drawString("P"+game.getPlayers().get(1).getName(), 80, 120);
			g.drawString("P"+game.getPlayers().get(2).getName(), 80, 140);
			g.drawString("P"+game.getPlayers().get(3).getName(), 80, 160);
			
			for(int i = 0;i<4;i++)
			{
				if(game.getPlayers().get(i).getHouse().equals("red"))
					g.drawImage(red, 100, 86+i*20, 17, 17, null);
				if(game.getPlayers().get(i).getHouse().equals("blue"))
					g.drawImage(blue, 100, 86+i*20, 17, 17, null);
				if(game.getPlayers().get(i).getHouse().equals("green"))
					g.drawImage(green, 100, 86+i*20, 17, 17, null);
				if(game.getPlayers().get(i).getHouse().equals("yellow"))
					g.drawImage(yellow, 100, 86+i*20, 17, 17, null);
			}
		
		
			if(game.getPlayers().get(0).isFinished())
				g.drawString("done", 120, 100);
			if(game.getPlayers().get(1).isFinished())
				g.drawString("done", 120, 120);
			if(game.getPlayers().get(2).isFinished())
				g.drawString("done", 120, 140);
			if(game.getPlayers().get(3).isFinished())
				g.drawString("done", 120, 160);
		
			g.drawString("<--", 120, game.getTurn()*20+100);
		}
		else if(game.getPhase() == 1)
		{
			g.setFont(new Font("Roboto", Font.BOLD | Font.ITALIC, 35));
			
			g.drawString("Player Order: ", 850, 250);
			
			g.drawString("P"+game.getPlayers().get(0).getName(), 915, 300);
			g.drawString("P"+game.getPlayers().get(1).getName(), 915, 350);
			g.drawString("P"+game.getPlayers().get(2).getName(), 915, 400);
			g.drawString("P"+game.getPlayers().get(3).getName(), 915, 450);
			
			for(int i = 0;i<4;i++)
			{
				if(game.getPlayers().get(i).getHouse().equals("red"))
					g.drawImage(red, 970, 275+i*50, 25, 25, null);
				else if(game.getPlayers().get(i).getHouse().equals("blue"))
					g.drawImage(blue, 970, 275+i*50, 25, 25, null);
				else if(game.getPlayers().get(i).getHouse().equals("green"))
					g.drawImage(green, 970, 275+i*50, 25, 25, null);
				else if(game.getPlayers().get(i).getHouse().equals("yellow"))
					g.drawImage(yellow, 970, 275+i*50, 25, 25, null);
			}
		}
		else if (game.getPhase()==4)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("resources/orderArrow.png")), 235, 77, -200, 65, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.setColor(Color.black);
			g.setFont(new Font("Arial",  Font.ITALIC, 25));
			
			g.drawString("P"+game.getPlayers().get(0).getName(), 50, 47);
			g.drawString("P"+game.getPlayers().get(1).getName(), 100, 47);
			g.drawString("P"+game.getPlayers().get(2).getName(), 150, 47);
			g.drawString("P"+game.getPlayers().get(3).getName(), 200, 47);
			
			for(int i = 0;i<4;i++)
			{
				if(game.getPlayers().get(i).getHouse().equals("red"))
					g.drawImage(red, 50+i*50, 52, 25, 25, null);
				else if(game.getPlayers().get(i).getHouse().equals("blue"))
					g.drawImage(blue, 50+i*50, 52, 25, 25, null);
				else if(game.getPlayers().get(i).getHouse().equals("green"))
					g.drawImage(green, 50+i*50, 52, 25, 25, null);
				else if(game.getPlayers().get(i).getHouse().equals("yellow"))
					g.drawImage(yellow, 50+i*50, 52, 25, 25, null);
			}
			g.drawString("^", game.getTurn()*50+55, 107);
			g.setFont(new Font("Arial",  Font.ITALIC, 15));
			if(game.getPlayers().get(0).isFinished())
				g.drawString("done", 46, 97);
			if(game.getPlayers().get(1).isFinished())
				g.drawString("done", 96, 97);
			if(game.getPlayers().get(2).isFinished())
				g.drawString("done", 146, 97);
			if(game.getPlayers().get(3).isFinished())
				g.drawString("done", 196, 97);
		
			
		}
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		System.out.println("X: "+e.getX()+ ", Y: "+e.getY());
		
		if(game.getPhase()==1)
		{
			
			if(e.getX()>=0 && e.getX()<=1920 && e.getY()>=0 && e.getY()<=1080)
			{
				//System.out.println("PHASE 1");
				game.phase++;
				repaint();
			}
		}
		if(game.getPhase() == 5)
		{
			if(e.getX()>=0 && e.getX()<=1920 && e.getY()>=0 && e.getY()<=1080)
			{
				System.out.println("PHASE 5");
				game.endRound();
				repaint();
			}
		}
		//PHASE 2 PP AUCTIONING 
		if(game.getPhase()==2)
		{
//			if(numFin<game.numFin())
//			{
//				first = true;
//				index = -1;
//				game.cost = 0;
//				numFin++;
//				
//			}
			if(maxPP)
			{
				//(835/s)+i*(1200/s)
				if(e.getX()>= 355 && e.getX()<=605 && e.getY()>= 725 && e.getY()<=975)
				{
					game.getCurrentPlayer().getPowerPlants().remove(0);
					game.getCurrentPlayer().finished();
					game.numFin++;
					if(game.numFin == 4)
						game.auctionDone = true;
					game.nextTurn();
					repaint();
					maxPP = false;
					
				}
				else if(e.getX()>= 655 && e.getX()<= 905 && e.getY()>= 725 && e.getY()<=975)
				{
					game.getCurrentPlayer().getPowerPlants().remove(1);
					game.getCurrentPlayer().finished();
					game.numFin++;
					if(game.numFin == 4)
						game.auctionDone = true;
					game.nextTurn();
					repaint();
					maxPP = false;
				}
				else if(e.getX()>= 955 && e.getX()<=1205 && e.getY()>= 725 && e.getY()<=975)
				{
					game.getCurrentPlayer().getPowerPlants().remove(2);
					game.getCurrentPlayer().finished();
					game.numFin++;
					if(game.numFin == 4)
						game.auctionDone = true;
					game.nextTurn();
					repaint();
					maxPP = false;
				}
				else if(e.getX()>= 1255 && e.getX()<=1505 && e.getY()>= 725 && e.getY()<=975)
				{
					game.getCurrentPlayer().getPowerPlants().remove(3);
					game.getCurrentPlayer().finished();
					game.numFin++;
					if(game.numFin == 4)
						game.auctionDone = true;
					game.nextTurn();
					repaint();
					maxPP = false;
				}
			}
			else if(first)//choosing which pp to bid on
			{
				if(e.getX() >= 300 && e.getX() <= 500 && e.getY() >= 50 && e.getY() <= 250)
				{
					//System.out.println("HOw the FUCK IS IT GOING HERE");
					index = 0;
					ppChosen = true;
					repaint();
					//System.out.println(" 0 chosen");
				}
				else if(e.getX() >= 650 && e.getX() <= 850 && e.getY() >= 50 && e.getY() <=250)
				{
					index = 1;
					ppChosen = true;
					repaint();
					//System.out.println("1 chosen");
				}
				else if(e.getX() >= 1000 && e.getX() <= 1200 && e.getY() >= 50 && e.getY() <= 250)
				{
					index = 2;
					ppChosen = true;
					repaint();
					//System.out.println("2 chosen");
				}
				else if(e.getX() >= 1350 && e.getX() <= 1550 && e.getY() >= 50 && e.getY() <= 250)
				{
					index = 3;
					ppChosen = true;
					repaint();
					//System.out.println("3 chosen");
				}
				else if(game.getStep() == 3 && e.getX() >= 300 && e.getX() <= 500 && e.getY() >= 280 && e.getY() <= 480)
				{
					index = 4;
					ppChosen = true;
					repaint();
				}
				else if(game.getStep() == 3 && e.getX() >= 650 && e.getX() <= 850 && e.getY() >= 280 && e.getY() <= 480)
				{
					index = 5;
					ppChosen = true;
					repaint();
				}
				
				//first bid or pass
				if(ppChosen && index!=-1 && e.getX() >= 600 && e.getX() <= 675 && e.getY() >= 500 && e.getY() <= 550)
				{
					System.out.println("first bid on "+ index);
					
					boolean x = game.bid(index, "first");
					if(x)
						first = false;
					repaint();
				}
				else if(game.getStep()!= 0 && e.getX() >= 1250 && e.getX() <= 1325 && e.getY() >= 500 && e.getY() <= 550)
				{
					System.out.println("first pass");
					game.pass(-1);
					index = -1;
					repaint();
				}
				
			}
			else // normal bidding
			{
				
				if(e.getX() >= 600 && e.getX() <= 675 && e.getY() >= 500 && e.getY() <= 550)
				{
					System.out.println("BID");
					game.bid(index, "n");
					repaint();
				
				}
				else if(e.getX() >= 1250 && e.getX() <= 1325 && e.getY() >= 500 && e.getY() <= 550)
				{
					System.out.println("PASS");
					game.pass(index);
					repaint();
				}
			}
		}
		
		
		//PHASE 3 RESOURCE BUYING
		if(game.getPhase()==3)
		{
			if(fi)
			{
				for(PowerPlant p : game.getCurrentPlayer().getPowerPlants())
				{
					if(p.capType().equals("Coal"))
						pC+=p.getCapacity();
					if(p.capType().equals("Oil"))
						pO+=p.getCapacity();
					if(p.capType().equals("Trash"))
						pT+=p.getCapacity();
					if(p.capType().equals("Uran"))
						pU+=p.getCapacity();
					if(p.capType().equals("Hybrid"))
						pH+=p.getCapacity();
					if(p.capType().equals("Free"))
						free = true;
				}
				fi = false;
			}
			
			//if(buying && )
			//choose resources to buy
			if(e.getX() >= 450 && e.getX() <= 470 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(coalC!=0 && game.getCurrentPlayer().getCoal()>0)
				{
					coalC--;
					
					game.takeResBack(Type.Coal);
					rCost-=game.calculateCost(Type.Coal);
					
				}
			}
			//FOR HYBRIDS FIND WAY TO ALLOCATE ALL OTHER RESOURCES AS MUCH AS POSSIBLE BEFORE CHECKING HYBRID CAPACITY
			if(e.getX() >= 450 && e.getX() <= 470 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(pH>0)
				{
					if(pC>0)
					{
						if(pO>0)
						{
							if(game.getCurrentPlayer().getOil()>=pO)
							{
								if(game.getResources().get("COAL").size() !=0 && game.getCurrentPlayer().getCoal()<(pC+(pH-(game.getCurrentPlayer().getOil()-pO))))
								{
									coalC++;
									rCost+=game.calculateCost(Type.Coal);
									game.buyRes(Type.Coal, 1);
									repaint();
								}
							}
							else 
							{
								if(game.getResources().get("COAL").size() !=0 && game.getCurrentPlayer().getCoal()<(pH+pC))
								{
									coalC++;
									rCost+=game.calculateCost(Type.Coal);
									game.buyRes(Type.Coal, 1);
								}
							}
						}
						else if(game.getResources().get("COAL").size() !=0 && game.getCurrentPlayer().getCoal()+game.getCurrentPlayer().getOil()<(pH+pC))
						{
							coalC++;
							rCost+=game.calculateCost(Type.Coal);
							game.buyRes(Type.Coal, 1);
						}
					}
					else
					{
						
						if(game.getResources().get("COAL").size() !=0 && game.getCurrentPlayer().getCoal()+game.getCurrentPlayer().getOil()<(pH))
						{
							coalC++;
							rCost+=game.calculateCost(Type.Coal);
							game.buyRes(Type.Coal, 1);
						
						}	
					}
				}
				else
				{
					if(game.getResources().get("COAL").size() !=0 && game.getCurrentPlayer().getCoal()<pC)
					{
						coalC++;
						rCost+=game.calculateCost(Type.Coal);
						game.buyRes(Type.Coal, 1);
						
					}
				}

			}
			
			if(e.getX() >= 750 && e.getX() <= 770 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(oilC!=0 && game.getCurrentPlayer().getOil()>0)
				{
					oilC--;
					
					game.takeResBack(Type.Oil);
					rCost-=game.calculateCost(Type.Oil);
					
				}
			}
			if(e.getX() >= 750 && e.getX() <= 770 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(pH>0)
				{
					if(pO>0)
					{
						if(pC>0)
						{
							if(game.getCurrentPlayer().getCoal()>=pC)
							{
								if(game.getResources().get("OIL").size() !=0 &&  game.getCurrentPlayer().getOil()<(pO+(pH-(game.getCurrentPlayer().getCoal()-pC))))
								{
									oilC++;
									rCost+=game.calculateCost(Type.Oil);
									game.buyRes(Type.Oil, 1);
									repaint();
								}
							}
							else
							{
								if(game.getResources().get("OIL").size() !=0 && game.getCurrentPlayer().getOil()<(pH+pO))
								{
									oilC++;
									rCost+=game.calculateCost(Type.Oil);
									game.buyRes(Type.Oil, 1);
								}
							}
						}
						else if(game.getResources().get("OIL").size() !=0 && game.getCurrentPlayer().getOil()+game.getCurrentPlayer().getCoal()<(pH+pO))
						{
							oilC++;
							rCost+=game.calculateCost(Type.Oil);
							game.buyRes(Type.Oil, 1);
						}
					}
					else
					{
						if(game.getResources().get("OIL").size() !=0 && game.getCurrentPlayer().getOil()+game.getCurrentPlayer().getCoal()<pH)
						{
							oilC++;
							rCost+=game.calculateCost(Type.Oil);
							game.buyRes(Type.Oil, 1);
						
						}
					}
				}
				else
					if(game.getResources().get("OIL").size() !=0  && game.getCurrentPlayer().getOil()<pO)
					{
						oilC++;
						rCost+=game.calculateCost(Type.Oil);
						game.buyRes(Type.Oil, 1);
						
					}
				
			}
				
			if(e.getX() >= 1050 && e.getX() <= 1070 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(trashC!=0 && game.getCurrentPlayer().getTrash()>0)
				{
					trashC--;
					
					game.takeResBack(Type.Trash);
					rCost-=game.calculateCost(Type.Trash);
					
				}
			}
			if(e.getX() >=1050 && e.getX() <= 1070 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(game.getResources().get("TRASH").size() !=0  && game.getCurrentPlayer().getTrash()< pT)
				{
					trashC++;
					rCost+=game.calculateCost(Type.Trash);
					game.buyRes(Type.Trash, 1);
					
				}
			}
			
			if(e.getX() >= 1350 && e.getX() <= 1370 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(uranC!=0 && game.getCurrentPlayer().getUranium()>0)
				{
					uranC--;
					
					game.takeResBack(Type.Uranium);
					rCost-=game.calculateCost(Type.Uranium);
					
				}
			}
			if(e.getX() >= 1350 && e.getX() <= 1370 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(game.getResources().get("URANIUM").size() !=0  && game.getCurrentPlayer().getUranium()<pU)
				{
					uranC++;
					rCost+=game.calculateCost(Type.Uranium);
					game.buyRes(Type.Uranium, 1);
					
				}
			}
			
			//finish transaction and pay
			if(e.getX() >= 755 && e.getX() <= 1110 && e.getY() >= 395 && e.getY() <= 550)
			{
				//System.out.println(free);
				if(coalC+oilC+trashC+uranC == 0)
				{
					if(game.step == 0 && (free|| (game.getCurrentPlayer().balance()<game.calculateCost(Type.Oil)||game.getCurrentPlayer().balance()<game.calculateCost(Type.Coal)||game.getCurrentPlayer().balance()<game.calculateCost(Type.Trash)||game.getCurrentPlayer().balance()<game.calculateCost(Type.Uranium))))
					{
						game.numFin++;
						free = false;
						game.getCurrentPlayer().finished();
						game.nextTurn();
						fi = true;
						
						pC = 0;
						pO = 0;
						pT = 0;
						pU = 0;
						pH = 0;
						rCost = 0;
					}
					else if (game.step!=0)
					{
						game.numFin++;
						free = false;
						game.getCurrentPlayer().finished();
						game.nextTurn();
						fi = true;
						
						pC = 0;
						pO = 0;
						pT = 0;
						pU = 0;
						pH = 0;
						rCost = 0;
					}
					
				}
				else
				{
//					if(coalC!=0)
//						game.buyRes(Type.Coal, coalC);
//					if(oilC!=0)
//						game.buyRes(Type.Oil, oilC);
//					if(trashC!=0)
//						game.buyRes(Type.Trash, trashC);
//					if(uranC!=0)
//						game.buyRes(Type.Uranium, uranC);
					if(game.getCurrentPlayer().balance()>=rCost)
					{
					game.getCurrentPlayer().pay(rCost);
					game.numFin++;
					game.getCurrentPlayer().finished();
					game.nextTurn();
					
					coalC = 0;
					trashC = 0;
					uranC = 0;
					oilC = 0;
					
					pC = 0;
					pO = 0;
					pT = 0;
					pU = 0;
					pH = 0;
					rCost = 0;
					fi = true;
					}
				}
					
			}
			repaint();
				
		}
		
		
		//CURRENT PLAYER IN PHASE 4
		if(game.getPhase() == 4) 
		{
			//RESOURCE ALLOCATION IN PP VIEWER
			if(game.getTurn() == page && ppVisible)
			{
				if(game.getCurrentPlayer().getPowerPlants().size()==1)
				{
					if( e.getX() >= 835 && e.getX() <=  1085 && e.getY() >= 350 && e.getY() <= 600)
					{
						ppIndex = 0;
						ppChosen2 = true;
						repaint();
					}
				}
				else if(game.getCurrentPlayer().getPowerPlants().size() == 2)
				{
					if(e.getX() >= 565 && e.getX() <= 815 && e.getY() >= 350 && e.getY() <= 600)
					{
						ppIndex = 0;
						ppChosen2 = true;
						repaint();
					}
					if(e.getX() >= 1165 && e.getX() <= 1415 && e.getY() >= 350 && e.getY() <= 600)
					{
						ppIndex = 1;
						ppChosen2 = true;
						repaint();
					}
				}
				else if(game.getCurrentPlayer().getPowerPlants().size() == 3)
				{
					if(e.getX() >= 425 && e.getX() <= 675 && e.getY() >= 350 && e.getY() <= 600)
					{
						ppIndex = 0;
						ppChosen2 = true;
						repaint();
					}
					if( e.getX() >= 825 && e.getX() <= 1075 && e.getY() >= 350 && e.getY() <= 600)
					{
						ppIndex = 1;
						ppChosen2 = true;
						repaint();
					}
					if(e.getX() >= 1225 && e.getX() <= 1485 && e.getY() >= 350 && e.getY() <= 600)
					{
						ppIndex = 2;
						ppChosen2 = true;
						repaint();
					}
				}
				
				//add resources to power plant
				if(ppChosen2 && e.getX() >= 802 && e.getX() <= 815 && e.getY() >= 652 && e.getY() <= 665)
				{
					game.insert(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "oil");
					repaint();
				}
				else if(ppChosen2 && e.getX() >= 802 && e.getX() <= 815 && e.getY() >= 702 && e.getY() <= 715)
				{
					game.insert(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "coal");
					repaint();
				}
				else if(ppChosen2 && e.getX() >= 802 && e.getX() <= 815 && e.getY() >= 752 && e.getY() <= 765)
				{
					game.insert(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "trash");
					repaint();
				}
				else if(ppChosen2 && e.getX() >= 802 && e.getX() <= 815 && e.getY() >= 802 && e.getY() <= 815)
				{
					game.insert(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "uranium");
					repaint();
				}
				//take resource out of power plant
				else if(ppChosen2 && e.getX() >= 823 && e.getX() <= 837 && e.getY() >= 651 && e.getY() <= 666) 
				{
					game.extract(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "oil");
					repaint();
				}
				else if(ppChosen2 && e.getX() >= 823 && e.getX() <= 837 && e.getY() >= 701 && e.getY() <= 716)
				{
					game.extract(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "coal");
					repaint();
				}
				else if(ppChosen2 && e.getX() >= 823 && e.getX() <= 837 && e.getY() >= 751 && e.getY() <= 766)
				{
					game.extract(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "trash");
					repaint();
				}
				else if(ppChosen2 && e.getX() >= 823 && e.getX() <= 837 && e.getY() >= 801 && e.getY() <= 816)
				{
					game.extract(game.getCurrentPlayer().getPowerPlants().get(ppIndex), "uranium");
					repaint();
				}
			}
			
			else if(game.getTurn() == page && !ppVisible) //current player's turn
			{
				//finished building cities
				if(e.getX() >= 910 && e.getX() <= 1010 && e.getY() >= 865 && e.getY() <= 910)
				{
					buying = false;
					game.numFin++;
					if(game.getCurrentPlayer().getCities().size()==0 && tempResources != game.getCurrentPlayer().getResources())
						//System.out.println("GAVE BACK: "+tempResources);
						game.getCurrentPlayer().setResources(tempResources);
						
					game.getCurrentPlayer().finished();
					game.nextTurn();
					
					paintC = false;
					paintO = false;
					paintT = false;
					paintU = false;
					tempResources = game.getCurrentPlayer().getResourcesCopy();
				}
				
				if(buying && e.getX()>=880 && e.getX()<=1080 && e.getY() >= 460 && e.getY()<= 565)
				{
					game.buyCity(cityBuy);
					buying = false;
					repaint();
				}
				//building cities
				if(e.getX() >= 110 && e.getX() <= 175 && e.getY() >= 155 && e.getY() <= 212)  
				{
					buying = true;
					cityBuy = "Seattle";
				}
				else if(e.getX() >= 60 && e.getX() <= 130 && e.getY() >= 240 && e.getY() <= 300)  
				{
					buying = true;
					cityBuy = "Portland";
				}
				else if(e.getX() >= 61 && e.getX() <= 125 && e.getY() >= 515 && e.getY() <= 575)  
				{
					buying = true;
					cityBuy = "San Francisco";
				}
				else if(e.getX() >= 183 && e.getX() <= 245 && e.getY() >= 630 && e.getY() <= 685)  
				{
					buying = true;
					cityBuy = "Los Angeles";
				}
				else if(e.getX() >= 260 && e.getX() <= 315 && e.getY() >= 695 && e.getY() <= 748)  
				{
					buying = true;
					cityBuy = "San Diego";
				}
				else if(e.getX() >= 308 && e.getX() <= 360 && e.getY() >= 310 && e.getY() <= 366)  
				{
					buying = true;
					cityBuy = "Boise";
				}
				else if(e.getX() >= 325 && e.getX() <= 400 && e.getY() >= 565 && e.getY() <= 615)  
				{
					buying = true;
					cityBuy = "Las Vegas";
				}
				else if(e.getX() >= 450 && e.getX() <= 500 && e.getY() >= 665 && e.getY() <= 715)  
				{
					buying = true;
					cityBuy = "Phoenix";
				}
				else if(e.getX() >= 450 && e.getX() <= 505 && e.getY() >= 430 && e.getY() <= 487)  
				{
					buying = true;
					cityBuy = "Salt Lake City";
				}
				else if(e.getX() >= 575 && e.getX() <= 650 && e.getY() >= 255 && e.getY() <= 305)  
				{
					buying = true;
					cityBuy = "Billings";
				}
				else if(e.getX() >= 695 && e.getX() <= 755 && e.getY() >= 385 && e.getY() <= 440)  
				{
					buying = true;
					cityBuy = "Cheyenne";
				}
				else if(e.getX() >= 675 && e.getX() <= 740 && e.getY() >= 455 && e.getY() <= 505)  
				{
					buying = true;
					cityBuy = "Denver";
				}
				else if(e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 580 && e.getY() <= 655)  
				{
					buying = true;
					cityBuy = "Santa Fe";
				}
				else if(e.getX() >= 940 && e.getX() <= 1005 && e.getY() >= 220 && e.getY() <= 270)  
				{
					buying = true;
					cityBuy = "Fargo";
				}
				else if(e.getX() >= 1080 && e.getX() <= 1155 && e.getY() >= 195 && e.getY() <= 245)  
				{
					buying = true;
					cityBuy = "Duluth";
				}
				else if(e.getX() >= 1060 && e.getX() <= 1120 && e.getY() >= 265 && e.getY() <= 325)  
				{
					buying = true;
					cityBuy = "Minneapolis";
				}
				else if(e.getX() >= 966 && e.getX() <= 1026 && e.getY() >= 400 && e.getY() <= 455)  
				{
					buying = true;
					cityBuy = "Omaha";
				}
				else if(e.getX() >= 1010 && e.getX() <= 1065 && e.getY() >= 485 && e.getY() <= 545)  
				{
					buying = true;
					cityBuy = "Kansas City";
				}
				else if(e.getX() >= 942 && e.getX() <= 999 && e.getY() >= 592 && e.getY() <= 645)  
				{
					buying = true;
					cityBuy = "Oklahoma City";
				}
				else if(e.getX() >= 960 && e.getX() <= 1025 && e.getY() >= 676 && e.getY() <= 730)  
				{
					buying = true;
					cityBuy = "Dallas";
				}
				else if(e.getX() >= 975 && e.getX() <= 1044 && e.getY() >= 765 && e.getY() <= 823)  
				{
					buying = true;
					cityBuy = "Houston";
				}
				else if(e.getX() >= 1225 && e.getX() <= 1280 && e.getY() >= 380 && e.getY() <= 440)  
				{
					buying = true;
					cityBuy = "Chicago";
				}
				else if(e.getX() >= 1160 && e.getX() <= 1225 && e.getY() >= 490 && e.getY() <= 540)  
				{
					buying = true;
					cityBuy = "St. Louis";
				}
				else if(e.getX() >= 1165 && e.getX() <= 1225 && e.getY() >= 600 && e.getY() <= 655)  
				{
					buying = true;
					cityBuy = "Memphis";
				}
				else if(e.getX() >= 1160 && e.getX() <= 1221 && e.getY() >= 760 && e.getY() <= 820)  
				{
					buying = true;
					cityBuy = "New Orleans";
				}
				else if(e.getX() >= 1275 && e.getX() <= 1335 && e.getY() >= 655 && e.getY() <= 715)  
				{
					buying = true;
					cityBuy = "Birmingham";
				}
				else if(e.getX() >= 1375 && e.getX() <= 1445 && e.getY() >= 355 && e.getY() <= 410)  
				{
					buying = true;
					cityBuy = "Detroit";
				}
				else if(e.getX() >= 1365 && e.getX() <= 1425 && e.getY() >= 465 && e.getY() <= 520)  
				{
					buying = true;
					cityBuy = "Cincinnati";
				}
				else if(e.getX() >= 1365 && e.getX() <= 1430 && e.getY() >= 570 && e.getY() <= 625)  
				{
					buying = true;
					cityBuy = "Knoxville";
				}
				else if(e.getX() >= 1375 && e.getX() <= 1445 && e.getY() >= 655 && e.getY() <= 705)  
				{
					buying = true;
					cityBuy = "Atlanta";
				}
				else if(e.getX() >= 1565 && e.getX() <= 1625 && e.getY() >= 335 && e.getY() <= 387)  
				{
					buying = true;
					cityBuy = "Buffalo";
				}
				else if(e.getX() >= 1521 && e.getX() <= 1580 && e.getY() >= 425 && e.getY() <= 475)  
				{
					buying = true;
					cityBuy = "Pittsburgh";
				}
				else if(e.getX() >= 1810 && e.getX() <= 1875 && e.getY() >= 335 && e.getY() <= 390)  
				{
					buying = true;
					cityBuy = "Boston";
				}
				else if(e.getX() >= 1742 && e.getX() <= 1805 && e.getY() >= 390 && e.getY() <= 440)  
				{
					buying = true;
					cityBuy = "New York";
				}
				else if(e.getX() >= 1700 && e.getX() <= 1755 && e.getY() >= 446 && e.getY() <= 500)  
				{
					buying = true;
					cityBuy = "Philadelphia";
				}
				else if(e.getX() >= 1607 && e.getX() <= 1656 && e.getY() >= 494 && e.getY() <= 532)  
				{
					buying = true;
					cityBuy = "Washington D.C.";
				}
				else if(e.getX() >= 1681 && e.getX() <= 1726 && e.getY() >= 550 && e.getY() <= 600)  
				{
					buying = true;
					cityBuy = "Norfolk";
				}
				else if(e.getX() >= 1583 && e.getX() <= 1638 && e.getY() >= 590 && e.getY() <= 644)  
				{
					buying = true;
					cityBuy = "Raleigh";
				}
				else if(e.getX() >= 1499 && e.getX() <= 1560 && e.getY() >= 684 && e.getY() <= 732)  
				{
					buying = true;
					cityBuy = "Savannah";
				}
				else if(e.getX() >= 1488 && e.getX() <= 1548 && e.getY() >= 750 && e.getY() <= 805)  
				{
					buying = true;
					cityBuy = "Jacksonville";
				}
				else if(e.getX() >= 1414 && e.getX() <= 1480 && e.getY() >= 835 && e.getY() <= 880)  
				{
					buying = true;
					cityBuy = "Tampa";
				}
				else if(e.getX() >= 1532 && e.getX() <= 1585 && e.getY() >= 904 && e.getY() <= 955)  
				{
					buying = true;
					cityBuy = "Miami";
				}
				else
				{
					buying = false;
					cityBuy = "";
				}
				
				page = game.getPlayers().indexOf(game.getCurrentPlayer());
				repaint();
			
			}
			
			
			//POWER PLANT VIEWER 
			if(!ppVisible && e.getX() >= 1660 && e.getX() <= 1735 && e.getY() >= 680 && e.getY() <= 755)
			{
				
				ppVisible = true;
				
				repaint();
			}
			if(ppVisible && e.getX() >= 940 && e.getX() <= 980 && e.getY() >= 900 && e.getY() <= 935)
			{
				ppVisible = false;
				
				ppIndex = -1;
				ppChosen2 = false;
				repaint();
			}
			
			
			
			//ARROWS
			if(!ppVisible && e.getX() >= 1610 && e.getX() <= 1715 && e.getY() >= 950 && e.getY() <= 1040)
			{
				if(page == 0)
					page = 3;
				else
					page-=1;
				
				paintC = false;
				paintO = false;
				paintT = false;
				paintU = false;
				repaint();
			}
			if(!ppVisible && e.getX() >= 1734 && e.getX() <= 1825 && e.getY() >= 950 && e.getY() <= 1040)
			{
				if(page == 3)
					page = 0;
				else
					page+=1;
				
				paintC = false;
				paintO = false;
				paintT = false;
				paintU = false;
				repaint();
			}
			
		}
			
		
	}
}

