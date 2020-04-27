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
	private ImageIcon bg;
	private ImageIcon logo;
	private int page = 0;
	public GraphicsRunner(Board g)
	{
		 JFrame frame = new JFrame("POWERGRID");
		 //frame.setBackground(Color.white);
		 setVisible(true);
		 //frame.getContentPane().setBackground(Color.orange);
		 bg = new ImageIcon("bg.jpg");
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
			//add end game stuff later
		}
		else
		{
			g.drawImage(bg.getImage(), 0, 0, 1920, 1080, null); //background
			g.drawImage(logo.getImage(), 1545, 0, 375, 122,null);
			
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 50));
			
			g.drawString(game.getPlayers().get(page).getName(), 500, 50);	//player name
			g.drawString(game.getStep(), 500, 200);	//step
			g.drawString(game.getPhase(), 500, 250); //phase
			
			for(int i = 0 ; i < game.getPlayers().get(page).getPowerPlants().size(); i++)
			{
				g.drawImage(game.getPlayers().get(page).getPowerPlants().get(i).getImage().getImage(), 50 + (144 * i), 750, 144, 220, null); //power plants
			}
			
			g.drawString("Cash: " + game.getPlayers().get(page).getCash(), 100, 500); //cash			
			g.drawString("Coal: " + game.getPlayers().get(page).getCoal(), 100, 600); //other resources
			g.drawString("Oil: " + game.getPlayers().get(page).getOil(), 100, 700);
			g.drawString("Trash: " + game.getPlayers().get(page).getTrash(), 100, 800);
			g.drawString("Uranium: " + game.getPlayers().get(page).getUranium(), 100, 900);
			
			g.drawImage(new ImageIcon("arrow.png").getImage(), 464, 201, 120, 91, null); //arrows
			g.drawImage(new ImageIcon("arrow.png").getImage(), 897, 290, -120, -91, null);
			
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
	public void mouseReleased(MouseEvent e) {
		System.out.println(e.getX());
	}
}
