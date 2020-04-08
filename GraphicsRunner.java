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
