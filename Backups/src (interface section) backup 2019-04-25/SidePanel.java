import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class SidePanel extends JPanel {
	private CustomMouseListener listener2;
	private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	private final Rectangle endTurnButton = new Rectangle(0,0,100,100);
	private final Rectangle exitButton = new Rectangle(0,1000 - 100, 100, 100);
	
	public boolean terminationPending;
	public boolean exitPending;
	
	SidePanel() {
		listener2 = new CustomMouseListener();
		this.addMouseListener(listener2);
		
		Dimension sideBarSize = new Dimension (100,SCREEN_SIZE.height);
		this.setPreferredSize(sideBarSize);
		this.setBackground(Color.BLUE);
		this.setOpaque(true);
		terminationPending = false;
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setDoubleBuffered(true);
		
		g.setColor(Color.YELLOW);
		g.fillRect(5, 5, 90, 90);
		g.setColor(Color.BLACK);
		g.drawString("End Turn",20,40);
		
		g.setColor(Color.ORANGE);
		g.fillRect(5, 1000 - 105, 90, 90);
		g.setColor(Color.BLACK);
		g.drawString("Exit",20,1000 - 60);
		
		if (listener2.clickPending()) {
			listener2.clickHandled();
			
			
			if (endTurnButton.contains(listener2.getClick())) {
				terminationPending = true;
			}
			
			if (exitButton.contains(listener2.getClick())) {
				exitPending = true;
			}
		}
	}
	
	public void terminationHandled() {
		terminationPending = false;
	}
	
	
	
}
