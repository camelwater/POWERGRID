import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Rules extends JPanel
{
	JFrame frame;
	public Rules()
	{
		frame = new JFrame("POWERGRID GUIDE");
		 frame.setBackground(new Color(0, 138, 138));
		 setVisible(true);
		 
		 frame.add(this);
		 frame.setSize(1920,1080);
		 frame.setResizable(true);
		 frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 frame.setVisible(true);
	}
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.PLAIN, 100));
		
		g.drawString("Useful Guide:", 650, 125);
		
		g.setFont(new Font("Arial", Font.PLAIN, 25));
		g.drawString("1. To buy cities during phase 4, click on the desired city and a 'BUY' button will appear with the name of the city you want to buy.", 100, 200);
		g.drawString("Click on the white 'BUY' button to purchase the city and repeat this process until you are done purchasing cities.", 150, 300);
		g.drawString("2. Once you are finished buying cities, to power your cities, click on the factory above the arrows in the bottom right corner.", 100 , 400);
		g.drawString("You will be able to see your resources and your powerplants (you can also view other players' resources and power plants too by using the arrows).", 150 , 500);
		g.drawString("To 'power' a plant, select the plant that you want to fill (an arrow above the card should appear), and click on the plus (or minus if you change your mind)", 150, 600);
		g.drawString("When you have adequately 'powered' a plant, a check mark will indicate so. You can then click the 'X' to go back when you are done.", 150, 700);
		g.drawString("3. Now you can finish your turn by clicking the 'finish' button around the bottom center of the screen." , 100, 800);
		
		g.drawString("4.If the screen ever shows a cyan screen with a player order or cash (phases 1 and 5), to move on to the next phase just click anywhere on the screen.", 100, 900);
		
		g.drawString("THE GAME IS BEHIND THIS WINDOW", 700, 1000);
	}
}
