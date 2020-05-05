import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Enums.Type;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings({"serial", "unused"})
public class GraphicsRunner extends JPanel implements MouseListener
{
	private static String path = "Misc\\";
	private Board game;
	private String regions;
	private BufferedImage bg;
	BufferedImage cash;
	BufferedImage coal;
	BufferedImage oil;
	BufferedImage uranium;
	BufferedImage trash;
	
	
	private int page = 3;
	private int index = -1;
	
	private boolean ppVisible = false;
	private boolean first = true;
	boolean ppChosen = false;
	
	private int numFin = 0;
	
	private int coalC = 0;
	private int oilC = 0;
	private int trashC = 0;
	private int uranC = 0;
	
	boolean free = false;
	int pC = 0;
	int pO = 0;
	int pT = 0;
	int pU = 0;
	int pH = 0;
	boolean fi = true;
	
	public GraphicsRunner(Board g, String r) throws IOException
	{
		game = g;
		regions = r;
//		game.setRegions();
		 JFrame frame = new JFrame("POWERGRID");
		 frame.setBackground(Color.orange);
		 setVisible(true);

		 bg = ImageIO.read(getClass().getResource("Powergridmap-Edited.png"));
		 cash = ImageIO.read(getClass().getResource("cash.png"));
		 coal = ImageIO.read(getClass().getResource("coal.png"));
		 oil = ImageIO.read(getClass().getResource("oil.png"));
		 trash = ImageIO.read(getClass().getResource("trash.png"));
		 uranium = ImageIO.read(getClass().getResource("uranium.png"));
		
		 addMouseListener(this);
		 
		 frame.add(this);
		 frame.setSize(1920,1080);
		 frame.setResizable(true);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setVisible(true);
		 
		
	}
	public void paintComponent(Graphics g)
	{
		
		if(game.isOver())
		{
			g.setColor(Color.orange);
			g.fillRect(0, 0, 1920, 1080);
			//g.drawImage(logo.getImage(), 1545, 0, 375, 122,null);
			
			g.setColor(Color.black);
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 100));
			g.drawString("Good job "+game.getPlayers().get(0), 400, 450);
		}
		else
		{    
			//CHANGE BOARD LOGIC SO THAT PLAY METHOD IS NOT WHILE LOOP AND CAN JUST CHECK WHAT STEP/PHASE IT IS AND CALL PHASE 1, PHASE 2
			//METHODS TO DO WHATEVER NEEDS TO BE DONE FOR THAT SPECIFIC PHASE, THEN GRAPHICS CAN CHECK WHAT PHASE IT IS TO DO RESOURCES AND 
			//POWER PLANT BUYING AND STUFF
			
			
			if (game.getPhase() == 1 && game.getStep() != 0 && game.getRound()!=1)
			{
				game.calculatePlayerOrder();
				paintOrder(g);
				game.endPhase();
			}
			
			
			if(game.getStep() == 0 && game.getPhase()==1)
			{
				game.start();
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				paintOrder(g);
				//game.endPhase();
			}
			
			
			if(game.getPhase() == 2)//add mouse listener part for this
			{
				if(game.auctionDone())
				{
					game.endPhase();
					game.calculatePlayerOrder();
					numFin = 0;
				}
				
				if(numFin<game.numFin())
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
				
				//which card is being auctioned
				BufferedImage check = null;
				
				try {
					check = ImageIO.read(getClass().getResource("check.png"));
				} catch (IOException e) {}
				
				if(index == 0)
					g.drawImage(check, 300, 50, 50, 50, null);
				if(index == 1)
					g.drawImage(check, 650, 50, 50, 50, null);
				if(index == 2)
					g.drawImage(check, 1000, 50, 50, 50, null);
				if(index == 3)
					g.drawImage(check, 1350, 50, 50, 50, null);
				
				
				paintOrder(g); //player order thing
				g.setFont(new Font("Roboto", Font.BOLD, 75));
				g.setColor(Color.black);
				g.drawString("highest bid: "+game.cost, 700, 550);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				g.setColor(Color.white);
				//g.drawString("bal: $"+game.getPlayers().get(0).balance(), 500, 550);
				g.drawString("bal: $"+game.getCurrentPlayer().balance(), 330, 550);
				
				g.setColor(Color.black);
				
				g.fillOval(600, 500, 75, 50);
				g.fillOval(1250, 500, 75, 50);
				g.setColor(Color.white);
				g.drawString("bid", 620, 533);
				g.drawString("pass", 1262, 533);
				
				if(game.getCurrentPlayer().getPowerPlants().size()>0)
					paintPlants(g);
				
			}
			
			
			if(game.getPhase() == 3)//FINISH WORKING RESOURCE BUYING AND REFILLING AND PRICING (MOSTLY LOGIC)
			{
				//System.out.println("TURN: "+game.getTurn());
				
				if(game.resourceDone())
				{
					game.endPhase();
					
					numFin = 0;
					if(game.step == 0)
						game.step++;
					game.calculatePlayerOrder();
				}
				
				//buying resources
				g.setColor(new Color(0,138,138));
				g.fillRect(0, 0, 1920, 1080);
				try {
					paintResources(g);
				} catch (IOException e) {}
				
				paintOrder(g);
				g.setColor(Color.white);
				//g.drawString("pay $"+game.resourceCost(), 850, 500); add resource cost method or something in board
				g.setFont(new Font("Arial", Font.BOLD, 25));
				
				g.drawString("bal: $"+game.getCurrentPlayer().balance(), 900, 600);
				g.drawString("cost: $20", 880, 375);
				//g.drawString("cost: "+game.getCurrentPlayer().spent(), 880, 375);
				
				//System.out.println("HAND SIZE: "+game.getCurrentPlayer().getPowerPlants().size());
				if(game.getCurrentPlayer().getPowerPlants().size()>0)
					paintPlants(g);
			}
			
			
			if(game.getPhase() == 4)//add mouse listener to build cities and finish gui for phase 4
			{
				if(game.citiesDone())
				{
					game.endPhase();
					game.numFin = 0;
				}
				//System.out.println("round "+game.getRound());
				
				if(ppVisible) //look at powerplants and move resources
					paintPP(g);
				
				else //city building
				{
					
					g.drawImage(bg, 0, 0, 1920, 1060, null); //map
					//g.drawImage(logo.getImage(), 1545, 0, 375, 122,null);
					g.setColor(Color.orange);
				
					g.fillRect(25, 930, 1330, 110);
				
					g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 50));
					g.setColor(Color.black);
				
					g.drawString("Player "+game.getPlayers().get(page).getName(), 1625, 900);//player name
					
					g.drawString("Step: "+game.getStep(), 900, 50);	//step
					g.drawString("Phase: "+game.getPhase(), 875, 100); //phase
					g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
					if(game.getPlayers().get(page).getName().equals(game.getCurrentPlayer().getName()))
						g.drawString("current player",1635, 850);						
					
					paintOrder(g);
				
					//cash
					g.setFont(new Font("Times New Roman", Font.ITALIC, 50));
					g.setColor(Color.black);
					
					g.drawImage(cash, 50, 620, 50, 50, null);
					g.drawString(""+game.getPlayers().get(page).balance(), 110, 665); 	
				
					//other resources
					g.drawImage(coal, 50, 660, 50, 50, null);
					g.drawString(""+game.getPlayers().get(page).getCoal(), 110, 710); 
				
					g.drawImage(oil, 50, 720, 50, 50, null);
					g.drawString(""+game.getPlayers().get(page).getOil(), 110, 760);
				
					g.drawImage(trash, 50, 780, 50, 50, null);
					g.drawString(""+game.getPlayers().get(page).getTrash(), 110, 825);
				
					g.drawImage(uranium, 50, 850, 50, 50, null);
					g.drawString(""+game.getPlayers().get(page).getUranium(), 110, 890);
					
					//arrows
					try 
					{
						g.drawImage(ImageIO.read(getClass().getResource("arrow3.png")), 1625, 1040, 90, -91, null);
					} catch (IOException e) {e.printStackTrace();}
					
					try {
						g.drawImage(ImageIO.read(getClass().getResource("arrow3.png")), 1825, 949, -90, 91, null);
					} catch (IOException e) {e.printStackTrace();}
					
					try
					{
						g.drawImage(ImageIO.read(getClass().getResource("factory.png")), 1600, 625, 200, 200, null);
					} catch (IOException e) {}
				
				}
				
				
			}
			
			
			if(game.getPhase() == 5)
			{
				game.endRound();
			}
			
			
		}
		
	}
	
	//FINISH THESE METHODS
	public void paintPlants(Graphics g) //paints hands
	{
		int i = 1;
		if(game.getPhase()!=4)
		{
			int s = game.getCurrentPlayer().getPowerPlants().size();
			for(PowerPlant p: game.getCurrentPlayer().getPowerPlants())
			{
				if(s ==1)
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource(p.getID()+".png")), 835, 725, 250, 250, null); 
					} catch (IOException e) {}
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource(p.getID()+".png")), (835/s)+i*(960/s), 725, 250, 250, null); 
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
				if(s ==1)
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource(p.getID()+".png")), 835, 350, 250, 250, null); 
					} catch (IOException e) {}
				}
				else
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource(p.getID()+".png")), (835/s)+i*(960/s), 350, 250, 250, null); 
					} catch (IOException e) {}
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
		g.drawString("X", 960, 935);
		g.drawString("Player " + game.getPlayers().get(page).getName(), 850, 125);
		
	}
	
	public void paintMarket(Graphics g) //paints pp auctioning market
	{
		int i = 0;
		int c = 0;
		for(PowerPlant p: game.getMarket())
		{
			if(i==4)
			{
				//System.out.println(p.getID());
				try {
					g.drawImage(ImageIO.read(getClass().getResource(p.getID()+".png")), 350*c+300, 280, 200, 200, null);
				} catch (IOException e) {}
				c++;
			}
			else
			{
				try {
					g.drawImage(ImageIO.read(getClass().getResource(p.getID()+".png")), 350*i+300, 50, 200, 200, null);
				} catch (IOException e) {}
			
				i++;
			}
		}
	}
	
	public void paintResources(Graphics g) throws IOException
	{
		for(int i = 0;i<4;i++)
			g.drawImage(ImageIO.read(getClass().getResource("minus.png")), 450+i*300, 300, 25, 20, null);
		for(int i = 0;i<4;i++)
			g.drawImage(ImageIO.read(getClass().getResource("plus.png")), 450 +i*300, 200, 25, 25, null);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.black);
		
		//price (finish the cost thingy and purchasing
//		g.drawString("$ "+game.getCoalCost(), 450, 325); 
//		g.drawString("$ "+game.getOilCost(), 750, 325);
//		g.drawString("$ "+game.getTrashCost(), 1050, 325);
//		g.drawString("$ "+game.getUraniumCost(), 1350, 325);
		g.drawString("$5", 450, 340); 
		g.drawString("$5", 750, 340);
		g.drawString("$5", 1050, 340);
		g.drawString("$5", 1350, 340);
		
		//images
		g.drawImage(coal, 443, 234, 40, 40, null);
		g.drawImage(oil, 743, 237, 40, 40, null);
		g.drawImage(trash, 1043, 237, 40, 40, null);
		g.drawImage(uranium, 1343, 237, 40, 40, null);
		g.drawImage(ImageIO.read(getClass().getResource("pay.png")), 540, 300, 750, 350, null);
		
		//amount purchased and left
		g.drawString("x"+coalC+"/"+game.getResources().get("COAL").size(), 500, 260);
		g.drawString("x"+oilC+"/"+game.getResources().get("OIL").size(), 800, 260);
		g.drawString("x"+trashC+"/"+game.getResources().get("TRASH").size(), 1100, 260);
		g.drawString("x"+uranC+"/"+game.getResources().get("URANIUM").size(), 1400, 260);

	}
	
	public void paintOrder(Graphics g) //paints player order , later make them images of houses
	{
		g.setColor(Color.black);
		
		if(game.getPhase()==2 || game.getPhase()==3)
		{
			g.setFont(new Font("Arial", Font.ITALIC, 15));
		
			g.drawString("P"+game.getPlayers().get(0).getName(), 100, 100);
			g.drawString("P"+game.getPlayers().get(1).getName(), 100, 120);
			g.drawString("P"+game.getPlayers().get(2).getName(), 100, 140);
			g.drawString("P"+game.getPlayers().get(3).getName(), 100, 160);
		
		
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
			
			g.drawString("P"+game.getPlayers().get(0).getName(), 940, 300);
			g.drawString("P"+game.getPlayers().get(1).getName(), 940, 350);
			g.drawString("P"+game.getPlayers().get(2).getName(), 940, 400);
			g.drawString("P"+game.getPlayers().get(3).getName(), 940, 450);
		}
		else if (game.getPhase()==4)
		{
			//g.drawImage(red
			g.setColor(Color.orange);
			g.setFont(new Font("Arial",  Font.ITALIC, 25));
			
			g.drawString("P"+game.getPlayers().get(0).getName(), 50, 40);
			g.drawString("P"+game.getPlayers().get(1).getName(), 100, 40);
			g.drawString("P"+game.getPlayers().get(2).getName(), 150, 40);
			g.drawString("P"+game.getPlayers().get(3).getName(), 200, 40);
			
			if(game.getPlayers().get(0).isFinished())
				g.drawString("done", 60, 60);
			if(game.getPlayers().get(1).isFinished())
				g.drawString("done", 110, 60);
			if(game.getPlayers().get(2).isFinished())
				g.drawString("done", 160, 60);
			if(game.getPlayers().get(3).isFinished())
				g.drawString("done", 210, 60);
		
			g.drawString("^", game.getTurn()*50+50, 60);
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
				game.phase++;
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
				
			if(first)//choosing which pp to bid on
			{
				
				if(e.getX() >= 300 && e.getX() <= 500 && e.getY() >= 50 && e.getY() <= 250)
				{
					index = 0;
					ppChosen = true;
					repaint();
					System.out.println(" 0 chosen");
				}
				if(e.getX() >= 650 && e.getX() <= 850 && e.getY() >= 50 && e.getY() <=250)
				{
					index = 1;
					ppChosen = true;
					repaint();
					System.out.println("1 chosen");
				}
				if(e.getX() >= 1000 && e.getX() <= 1200 && e.getY() >= 50 && e.getY() <= 250)
				{
					index = 2;
					ppChosen = true;
					repaint();
					System.out.println("2 chosen");
				}
				if(e.getX() >= 1350 && e.getX() <= 1550 && e.getY() >= 50 && e.getY() <= 250)
				{
					index = 3;
					ppChosen = true;
					repaint();
					System.out.println("3 chosen");
				}
				//first bid or pass
				if(ppChosen && index!=-1 && e.getX() >= 600 && e.getX() <= 675 && e.getY() >= 500 && e.getY() <= 550)
				{
					System.out.println("first bid on "+ index);
					first = false;
					game.bid(index, "first");
					repaint();
				}
				if(game.getStep()!= 0 && e.getX() >= 1250 && e.getX() <= 1325 && e.getY() >= 500 && e.getY() <= 550)
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
				if(e.getX() >= 1250 && e.getX() <= 1325 && e.getY() >= 500 && e.getY() <= 550)
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
						pH+=4;
					if(p.capType().equals("Free"))
						free = true;
				}
				fi = false;
			}
			
			
			//choose resources to buy
			if(e.getX() >= 450 && e.getX() <= 470 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(coalC!=0)
					coalC--;
			}
			if(e.getX() >= 450 && e.getX() <= 470 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(coalC<game.getResources().get("COAL").size() && (coalC<pC ||coalC+oilC<pH))
					coalC++;
			}
			
			if(e.getX() >= 750 && e.getX() <= 770 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(oilC!=0)
					oilC--;
			}
			if(e.getX() >= 750 && e.getX() <= 770 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(oilC<game.getResources().get("OIL").size() && (oilC<pO ||coalC+oilC<pH))
					oilC++;
			}
				
			if(e.getX() >= 1050 && e.getX() <= 1070 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(trashC!=0)
					trashC--;
			}
			if(e.getX() >=1050 && e.getX() <= 1070 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(trashC<game.getResources().get("TRASH").size() && trashC< pT)
					trashC++;
			}
			
			if(e.getX() >= 1350 && e.getX() <= 1370 && e.getY() >= 300 && e.getY() <= 320)
			{
				if(uranC!=0)
					uranC--;
			}
			if(e.getX() >= 1350 && e.getX() <= 1370 && e.getY() >= 200 && e.getY() <= 220)
			{
				if(uranC<game.getResources().get("URANIUM").size() && uranC<pU)
					uranC++;
			}
			
			//finish transaction and pay
			if(e.getX() >= 755 && e.getX() <= 1110 && e.getY() >= 395 && e.getY() <= 550)
			{
				//System.out.println(free);
				if(coalC+oilC+trashC+uranC == 0)
				{
					if(game.step == 0 && free)
					{
						game.numFin++;
						free = false;
						game.getCurrentPlayer().isFinished();
						game.nextTurn();
						fi = true;
					}
					else if (game.step!=0 && free && game.getCurrentPlayer().getPowerPlants().size() == 1)
					{
						game.numFin++;
						free = false;
						game.getCurrentPlayer().isFinished();
						game.nextTurn();
						fi = true;
					}
					
				}
				else
				{
					if(coalC!=0)
						game.buyRes(Type.Coal, coalC);
					if(oilC!=0)
						game.buyRes(Type.Oil, oilC);
					if(trashC!=0)
						game.buyRes(Type.Trash, trashC);
					if(uranC!=0)
						game.buyRes(Type.Uranium, uranC);
					
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
					
					fi = true;
				}
					
			}
			repaint();
				
		}
		
		
		//CURRENT PLAYER IN PHASE 4
		if(game.getPhase() == 4) 
		{
			if(game.getTurn() == page) //current player's turn
			{
				//if() //this is to build cities (haven't done yet)
			}
			
			
			
			//POWER PLANT VIEWER
			if(!ppVisible && e.getX() >= 1660 && e.getX() <= 1735 && e.getY() >= 680 && e.getY() <= 755)
			{
				
				ppVisible = true;
				
				repaint();
			}
			if(ppVisible && e.getX() >= 960 && e.getX() <= 993 && e.getY() >= 900 && e.getY() <= 935)
			{
				ppVisible = false;
				repaint();
			}
			
			//ARROWS
			if(!ppVisible && e.getX() >= 1610 && e.getX() <= 1715 && e.getY() >= 950 && e.getY() <= 1040)
			{
				if(page == 0)
					page = 3;
				else
					page-=1;
				repaint();
			}
			if(!ppVisible && e.getX() >= 1734 && e.getX() <= 1825 && e.getY() >= 950 && e.getY() <= 1040)
			{
				if(page == 3)
					page = 0;
				else
					page+=1;
				repaint();
			}
			
		}
			
		
		
	}
}
