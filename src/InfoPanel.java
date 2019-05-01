import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import javax.swing.JPanel;

public class InfoPanel extends JPanel {
	private JLabel currentTurnLabel;
	private PieceType activeTeam;
	private double activeEval;
	private int activeI;
	private int activeJ;
	private int activeX;
	private int activeY;
	
	InfoPanel() {
		this.setPreferredSize(new Dimension((int)(300*Constants.scaleFactor),
				Toolkit.getDefaultToolkit().getScreenSize().height));
		this.setMinimumSize(new Dimension((int)(300*Constants.scaleFactor),
				Toolkit.getDefaultToolkit().getScreenSize().height));		
		this.setBackground(Color.DARK_GRAY);
		this.setOpaque(true);		
		this.setVisible(true);
		this.setAlignmentX(RIGHT_ALIGNMENT);
		
		this.activeI = -1;
		this.activeJ = -1;
		this.activeX = -1;
		this.activeY = -1;
		this.activeEval = -1;
		
		
	}
	
	
	public void updateTurnInfo(double eval, PieceType currentTeam) {
		this.activeEval = eval;
		this.activeTeam = currentTeam;
				
		this.repaint();
	}
	
	public void updateHoverData(int i, int j) {
		this.activeI = i;
		this.activeJ = j;
	}
	
	public void updateMouseData(int x,int y) {
		this.activeX = x;
		this.activeY = y;
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		this.setDoubleBuffered(true);		
		
		this.setFont(new Font("TrilliumWeb",Font.BOLD,17));	
		g.setColor(Color.white);
		
		 Graphics2D g2 = (Graphics2D) g;       
	     g2.scale(Constants.scaleFactor, Constants.scaleFactor);
		
				
		g.drawRect(5,5,290,90);	
		g.drawString("Team to move is: " + activeTeam.team, 20, 40);
		g.drawString("Evaluation: " + Double.toString(activeEval),20,65);
	
		g.drawRect(5,5 + 100,290,90);	
		g.drawString("Last Hovered Coordinates Are:", 20, 40 + 100);
		g.drawString("(Row: " + activeI +  " Column: " + activeJ + ")",20,65 + 100);
		
		g.drawRect(5,5 + 200,290,90);	
		g.drawString("Current True Mouse Coords are: ", 20, 40 + 200);
		g.drawString("(X: " + activeX +  " Y: " + activeY + ")",20,65 + 200);
					
	}	
}
