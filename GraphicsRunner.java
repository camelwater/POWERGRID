import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

@SuppressWarnings({"serial", "unused"})
public class GraphicsRunner extends JPanel implements MouseListener
{
	private static String path = "Misc\\";
	private Board game;
	private String regions;
	private ImageIcon bg;
	private ImageIcon logo;
	private int page = 0;
	private int index = -1;
	
	public GraphicsRunner(Board g, String r)
	{
		game = g;
		regions = r;
//		game.setRegions();
		 JFrame frame = new JFrame("POWERGRID");
		 frame.setBackground(Color.orange);
		 setVisible(true);
		 //frame.getContentPane().setBackground(Color.orange);
		 bg = new ImageIcon("Misc\\Powergridmap-Edited.jpg");
		 //logo = new ImageIcon("logo.png");
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
				game.endPhase();
			}
			if(game.getStep() == 0)
			{
				game.start();
				//System.out.println("HELLO");
				//game.endPhase();
			}
			if(game.getPhase() == 2)//add mouse listener part for this
			{
				//game.phase2();
				
				//auctioning power plants
				g.setColor(Color.orange);
				g.fillRect(0, 0, 1920, 1080);
				paintMarket(g);
				//which card is being auctioned
				if(index == 0)
					g.drawImage(new ImageIcon(path+"check.png").getImage(), 300, 100, 25, 25, null);
				if(index == 1)
					g.drawImage(new ImageIcon(path+"check.png").getImage(), 700, 100, 25, 25, null);
				if(index == 2)
					g.drawImage(new ImageIcon(path+"check.png").getImage(), 1100, 100, 25, 25, null);
				if(index == 3)
					g.drawImage(new ImageIcon(path+"check.png").getImage(), 1500, 100, 25, 25, null);
				
				paintOrder(g, 100, 100); //player order thing
				g.setFont(new Font("Roboto", Font.BOLD, 75));
				g.setColor(Color.black);
				g.drawString("highest bid: ", 700, 550);
				g.setFont(new Font("Arial", Font.BOLD, 25));
				g.setColor(Color.white);
				g.drawString("bal: $"+game.getCurrentPlayer().balance(), 330, 550);
				
				g.drawString("bid", 620, 550);
				g.drawString("pass", 1250, 550);
				
				if(game.getCurrentPlayer().getPowerPlants().size()>0)
					paintPlants(g);
				//IN BOARD MAKE WHERE YOU CAN GET PASS OR BID AFTER MOUSE CLICK AND FROM THERE GIVE POWER PLANTS (3 IN A ROW PASS = WIN)
				
//				if(game.auctionDone())
//					game.endPhase();
			}
			
			if(game.getPhase() == 3)//add mouse listener for this
			{
				//game.phase3();
				
				//buying resources
				g.setColor(Color.orange);
				g.fillRect(0, 0, 1920, 1080);
				paintResources(g);
				paintOrder(g, 100, 100);
				g.setColor(Color.white);
				//g.drawString("pay $"+game.resourceCost(), 850, 500); add resource cost method or something in board
				g.setFont(new Font("Arial", Font.BOLD, 25));
				//g.drawString("bal: $"+game.getPlayers().get(0).balance(), 900, 600);
				g.drawString("bal: $"+game.getCurrentPlayer().balance(), 900, 600);
				
//				if(game.resourceDone())
//					game.endPhase();
			}
			
			if(game.getPhase() == 4)//add mouse listener to build cities
			{
				
				//city building
				g.drawImage(bg.getImage(), 0, 0, 1920, 1060, null); //map
				//g.drawImage(logo.getImage(), 1545, 0, 375, 122,null);
				g.setColor(Color.orange);
				
				g.fillRect(25, 930, 1330, 110);
				
				g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 50));
				g.setColor(Color.black);
				
				g.drawString("Player "+game.getPlayers().get(page).getName(), 1625, 900);	//player name
				g.drawString("Step: "+game.getStep(), 900, 50);	//step
				g.drawString("Phase: "+game.getPhase(), 875, 100); //phase
				
				for(int i = 0 ; i < game.getPlayers().get(page).getPowerPlants().size(); i++)
				{
					g.drawImage(game.getPlayers().get(page).getPowerPlants().get(i).getImage(), 50 + (144 * i), 750, 144, 220, null); //power plants
				}
				
				
				//cash
				g.setFont(new Font("Times New Roman", Font.ITALIC, 50));
				
				g.drawImage(new ImageIcon(path+"cash.png").getImage(), 50, 620, 50, 50, null);
				g.drawString(""+game.getPlayers().get(page).balance(), 110, 665); 	
				
				//other resources
				g.drawImage(new ImageIcon(path+"coal.png").getImage(), 50, 660, 50, 50, null);
				g.drawString(""+game.getPlayers().get(page).getCoal(), 110, 710); 
				
				g.drawImage(new ImageIcon(path+"oil.png").getImage(), 50, 720, 50, 50, null);
				g.drawString(""+game.getPlayers().get(page).getOil(), 110, 760);
				
				g.drawImage(new ImageIcon(path+"trash.png").getImage(), 50, 780, 50, 50, null);
				g.drawString(""+game.getPlayers().get(page).getTrash(), 110, 825);
				
				g.drawImage(new ImageIcon(path+"uranium.png").getImage(), 50, 850, 50, 50, null);
				g.drawString(""+game.getPlayers().get(page).getUranium(), 110, 890);
				
				g.drawImage(new ImageIcon("Misc\\arrow2.png").getImage(), 1725, 949, 120, 91, null); //arrows
				g.drawImage(new ImageIcon("Misc\\arrow2.png").getImage(), 1700, 1040, -120, -91, null);
				
//				if(game.citiesDone())
//					game.endPhase();
			}
			if(game.getPhase() == 5)
			{
				game.endRound();
			}
			
			
		}
		
	}
	//FINISH THESE METHODS
	public void paintPlants(Graphics g) //make it divide the screen so it looks better rather than static coordinates
	{
		
	}
	public void paintMarket(Graphics g)
	{
		
	}
	public void paintResources(Graphics g)
	{
		
	}
	public void paintOrder(Graphics g, int startX, int startY)
	{
		
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
		
		
		//PHASE 2 PP AUCTIONING
		if(game.getPhase()==2)
		{
			
		}
		//PHASE 3 RESOURCES
		if(game.getPhase()==3)
		{
			
		}
		//CURRENT PLAYER IN PHASE 4
		if(game.getPhase() == 4 && game.getTurn() == page) 
		{
			//if() //this is power plant button
			
		}
			
		
		
		//ARROWS
		if(e.getX() >= 1595 && e.getX() <= 1690 && e.getY() >= 955 && e.getY() <= 1045)
		{
			if(page == 0)
				page = 3;
			else
				page-=1;
			repaint();
		}
		if(e.getX() >= 1725 && e.getX() <= 1840 && e.getY() >= 955 && e.getY() <= 1045)
		{
			if(page == 3)
				page = 0;
			else
				page+=1;
			repaint();
		}
	}
}
