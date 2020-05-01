import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

public class GraphicsRunner extends JPanel implements MouseListener
{
	private Board game;
	private String regions;
	private ImageIcon bg;
	private ImageIcon logo;
	private int page = 0;
	public GraphicsRunner(Board g, String r)
	{
		game = g;
		regions = r;
		 JFrame frame = new JFrame("POWERGRID");
		 frame.setBackground(Color.orange);
		 setVisible(true);
		 //frame.getContentPane().setBackground(Color.orange);
		 bg = new ImageIcon("Powergridmap.jpeg");
		 logo = new ImageIcon("logo.png");
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
			g.drawImage(bg.getImage(), 0, 0, 1920, 1080, null); //background
			g.drawImage(logo.getImage(), 1545, 0, 375, 122,null);
			
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 50));
			//g.drawString("Winner: "+game.getWinner(), 500, 500);
		}
		else
		{    
			g.drawImage(bg.getImage(), 0, 0, 1920, 1060, null); //background
			g.drawImage(logo.getImage(), 1545, 0, 375, 122,null);
			
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 50));
			
			g.drawString("Player "+game.getPlayers().get(page).getName(), 1625, 900);	//player name
			g.drawString("Step: "+game.getStep(), 900, 50);	//step
			g.drawString("Phase: "+game.getPhase(), 875, 100); //phase
			
			for(int i = 0 ; i < game.getPlayers().get(page).getPowerPlants().size(); i++)
			{
				g.drawImage(game.getPlayers().get(page).getPowerPlants().get(i).getImage(), 50 + (144 * i), 750, 144, 220, null); //power plants
			}
			
			g.drawImage(new ImageIcon("cash.png").getImage(), 50, 650, 50, 50, null);
			g.drawString(""+game.getPlayers().get(page).balance(), 110, 700); //cash			
//			g.drawString("Coal: " + game.getPlayers().get(page).getCoal(), 100, 600); //other resources
//			g.drawString("Oil: " + game.getPlayers().get(page).getOil(), 100, 700);
//			g.drawString("Trash: " + game.getPlayers().get(page).getTrash(), 100, 800);
//			g.drawString("Uranium: " + game.getPlayers().get(page).getUranium(), 100, 900);
			
			g.drawImage(new ImageIcon("arrow2.png").getImage(), 1725, 949, 120, 91, null); //arrows
			g.drawImage(new ImageIcon("arrow2.png").getImage(), 1700, 1040, -120, -91, null);
			
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
	}
}
