import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

public class GraphicsLobby extends JPanel implements MouseListener
{
	public GraphicsLobby()
	{
		JFrame frame = new JFrame("POWERGRID");
		 frame.setBackground(Color.cyan);
		 setVisible(true);
		 //frame.getContentPane().setBackground(Color.orange);
//		 map = new ImageIcon("map.png");
//		 logo = new ImageIcon("logo.png");
		 addMouseListener(this);
		 
		 frame.add(this);
		 frame.setSize(1920,1080);
		 frame.setResizable(true);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setVisible(true);
	}
	public void paintComponent(Graphics g)
	{
//		g.drawImage(map.toImage(), 460, 190, 1000, 700, null);
		g.setColor(Color.black);
		g.setFont(new Font("Roboto", Font.ITALIC | Font.BOLD, 150));
		g.drawString("POWERGRID", 500, 150);
		g.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 50));
		g.drawString("Choose the 4 regions", 750, 215);
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
		System.out.println("X: "+e.getX()+ "Y: "+e.getY());
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	 
}
