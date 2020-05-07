import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

@SuppressWarnings({ "unused", "serial" })
public class GraphicsLobby extends JPanel implements MouseListener
{
	private static String path = "Misc\\";
	Board game;
	GraphicsRunner gr;
	ArrayList<String> regions = new ArrayList<String>();
	BufferedImage map;

	boolean r1 = false;
	boolean r2 = false;
	boolean r3 = false;
	boolean r4 = false;
	boolean r5 = false;
	boolean r6 = false;
	
	int c1 = 0;
	int c2 = 0;
	int c3 = 0;
	int c4 = 0;
	int c5 = 0;
	int c6 = 0;
	
	int count = 0;
	
	boolean cont = false;
	
	JFrame frame;
	boolean lobbyDone = false;
	
	public GraphicsLobby(Board game)
	{
		this.game = game;
		frame = new JFrame("POWERGRID");
		 frame.setBackground(Color.white);
		 setVisible(true);
		 //frame.getContentPane().setBackground(Color.orange);
		 try {
			map = ImageIO.read(getClass().getResource("map.png"));
		} catch (IOException e) {}
		 
		 addMouseListener(this);
		 
		 frame.add(this);
		 frame.setSize(1920,1080);
		 frame.setResizable(true);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setVisible(true);
		 
	}
	public void paintComponent(Graphics g)
	{
		if(lobbyDone)
		{
			if(r1)
				regions.add("Purple");
			if(r2)
				regions.add("Yellow");
			if(r3)
				regions.add("Brown");
			if(r4)
				regions.add("Blue");
			if(r5)
				regions.add("Red");
			if(r6)
				regions.add("Green");
			
			
			try {
				gr = new GraphicsRunner(game, regions);
			} catch (IOException e) {}
			
			System.out.println(regions);
			frame.setVisible(false); 
			frame.dispose();
		}
		g.drawImage(map, 85, 190, 1750, 700, null);
		g.setColor(Color.black);
		g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 150));
		g.drawString("POWERGRID", 500, 150);
		g.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 50));
		g.drawString("Choose the 4 regions", 750, 215);
		
		
		g.fillOval(750, 360, 50, 50);
		g.fillOval(1015,400, 50, 50);
		g.fillOval(640, 510, 50, 50);
		g.fillOval(940, 595, 50, 50);
		g.fillOval(1180, 590, 50, 50);
		g.fillOval(1255, 420, 50, 50);
		
		BufferedImage check = null;
		try {
			check = ImageIO.read(getClass().getResource("check.png"));
		} catch (IOException e) {}
		if(r1)
		{
			g.drawImage(check, 750, 360, 25, 25, null);
			//System.out.println("1");
		}
		if(r2)
		{
			g.drawImage(check, 1012, 400, 25, 25, null);
			//System.out.println("2");
		}
		if(r3)
		{
			g.drawImage(check, 1255, 415, 25, 25, null);
			//System.out.println("3");
		}
		if(r4)
		{
			
			g.drawImage(check, 640, 510, 25, 25, null);
			//System.out.println("4");
		}
		if(r5)
		{
			g.drawImage(check, 940, 590, 25, 25, null);
			
			//System.out.println("5");
		}
		if(r6)
		{
			g.drawImage(check, 1180, 590, 25, 25, null);
			
			//System.out.println("6");
		}
		
		//check whether land is contiguous or not
		
		if(count==4 && cont)
		{
			g.setColor(Color.black);
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 100));
			g.drawString("START GAME", 625, 900);
		}
		else if(count == 4 && !cont)
		{
			g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 75));
			g.setColor(Color.red);
			g.drawString("REGIONS MUST BE CONTIGUOUS", 380, 900);
		}
		else
		{
			g.setColor(Color.white);
			g.fillRect(200, 830, 1720, 920);
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		
		System.out.println("X: "+e.getX()+ ", Y: "+e.getY());
		
		if(e.getX()>=750 && e.getX() <= 800 && e.getY() >= 360 && e.getY() <= 410) //1 done
		{
			c1++;
			if(c1%2 !=0&&count<4)
			{
				if(count==4)
					r1 = false;
				else
				{
					r1 = true;
					count++;
					
				}
			}
			else
			{
				if(r1)
				{
					r1 = false;
					count--;
				}
			}
			
			if(r1&& r3 && r4&&r5)
				cont = false;
			else if(r1&& r3&&r4&&r6)
				cont = false;
			else if(r2&&r3&&r4&&r6)
				cont = false;
			else 
				cont = true;
			repaint();
		}
		if(e.getX()>=1010 && e.getX() <= 1060 && e.getY() >= 400 && e.getY() <= 450)//2 done 
		{
			c2++;
			if(c2%2 !=0&&count<4)
			{
				if(count==4)
					r2 = false;
				else
				{
					r2 = true;
					count++;
					
				}
			}
			else
			{
				if(r2)
				{
					r2 = false;
					count--;
				}
			}
			if(r1&& r3 && r4&&r5)
				cont = false;
			else if(r1&& r3&&r4&&r6)
				cont = false;
			else if(r2&&r3&&r4&&r6)
				cont = false;
			else 
				cont = true;
			repaint();
		}
		if(e.getX()>=1250 && e.getX() <= 1310 && e.getY() >= 410 && e.getY() <= 470)//3 done
		{
			c3++;
			if(c3%2 !=0&&count<4)
			{
				if(count==4)
					r3 = false;
				else
				{
					r3 = true;
					count++;
					
				}
			}
			else
			{
				if(r3)
				{
					r3 = false;
					count--;
				}
			}
			if(r1&& r3 && r4&&r5)
				cont = false;
			else if(r1&& r3&&r4&&r6)
				cont = false;
			else if(r2&&r3&&r4&&r6)
				cont = false;
			else 
				cont = true;
			repaint();
		}
		if(e.getX()>=635 && e.getX() <= 690 && e.getY() >= 500 && e.getY() <= 560)//4 done
		{
			c4++;
			if(c4%2 !=0&&count<4)
			{
				if(count==4)
					r4 = false;
				else
				{
					r4 = true;
					count++;
				}
			}
			else
			{
				if(r4)
				{
					r4 = false;
					count--;
				}
			}
			if(r1&& r3 && r4&&r5)
				cont = false;
			else if(r1&& r3&&r4&&r6)
				cont = false;
			else if(r2&&r3&&r4&&r6)
				cont = false;
			else 
				cont = true;
			repaint();
		}
		if(e.getX()>=940 && e.getX() <= 1000 && e.getY() >= 590 && e.getY() <= 650)//5 done
		{
			c5++;
			if(c5%2 !=0&&count<4)
			{
				if(count==4)
					r5 = false;
				else
				{
					r5 = true;
					count++;
					
				}
			}
			else
			{
				if(r5)
				{
					r5 = false;
					count--;
				}
			}
			if(r1&& r3 && r4&&r5)
				cont = false;
			else if(r1&& r3&&r4&&r6)
				cont = false;
			else if(r2&&r3&&r4&&r6)
				cont = false;
			else 
				cont = true;
			repaint();
		}
		if(e.getX()>=1160 && e.getX() <= 1230 && e.getY() >= 580 && e.getY() <= 645)//6 done
		{
			c6++;
			if(c6%2 !=0&&count<4)
			{
				if(count==4)
					r6 = false;
				else
				{
					r6 = true;
					count++;
				}
			}
			else
			{
				if(r6)
				{
					r6 = false;
					count--;
				}
			}
			if(r1&& r3 && r4&&r5)
				cont = false;
			else if(r1&& r3&&r4&&r6)
				cont = false;
			else if(r2&&r3&&r4&&r6)
				cont = false;
			else 
				cont = true;
			repaint();
		}
		
		if(count==4 && cont && e.getX()>=625 && e.getX() <= 1300 && e.getY() >= 820 && e.getY() <= 900)
		{
			lobbyDone = true;
			repaint();
		}
		if(count==4 && !cont && e.getX()>=625 && e.getX() <= 1300 && e.getY() >= 820 && e.getY() <= 900)
		{
			//System.out.println("REGIONS: "+r1+","+r2+","+r3+","+r4+","+r5+","+r6);
			repaint();
		}
		//repaint();
	}
	 
}

