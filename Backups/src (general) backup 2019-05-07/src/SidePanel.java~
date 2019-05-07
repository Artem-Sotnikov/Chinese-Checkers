import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class SidePanel extends JPanel {
 private CustomMouseListener listener2;
 private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
 
 private final Rectangle endTurnButton = new Rectangle(0,0,100,100);
 private final Rectangle exitButton = new Rectangle(0,1000 - 100, 100, 100);
 private final Rectangle showButton = new Rectangle(0,100,100,100);
 private final Rectangle executeButton = new Rectangle(0,200,100,100);
 private final Rectangle bestButton = new Rectangle(0,300,100,100);
 private final Rectangle byEvalButton = new Rectangle(0,400,100,100); 
 private final Rectangle configScenarioButton = new Rectangle(0,500,100,100); 
 
 public boolean terminationPending;
 public boolean exitPending;
 public boolean executionPending;
 public boolean showPending;
 public boolean bestPending;
 public boolean byEvalPending;
 public boolean scenarioPending;

  
 SidePanel() {
  listener2 = new CustomMouseListener();
  this.addMouseListener(listener2);
  
  Dimension sideBarSize = new Dimension ((int) (100*Constants.scaleFactor),SCREEN_SIZE.height);
  this.setPreferredSize(sideBarSize);
  this.setMaximumSize(sideBarSize);
  this.setBackground(Color.BLUE);
  this.setOpaque(true);
  terminationPending = false;    
  
 }
 
 public void paintComponent(Graphics g) {
  super.paintComponent(g);
  setDoubleBuffered(true);
  
  Graphics2D g2 = (Graphics2D) g;
     g2.scale(Constants.scaleFactor, Constants.scaleFactor);
  
  g.setFont(new Font("TrilliumWeb", Font.BOLD, 16));
  
  g.setColor(Color.YELLOW);
  g.fillRect(5, 5, 90, 90);
  g.setColor(Color.BLACK);
  g.drawString("End",20,40);
  g.drawString("Turn", 20, 55);  
  
  g.setColor(Color.ORANGE);
  g.fillRect(5, 1000 - 105, 90, 90);
  g.setColor(Color.BLACK);
  g.drawString("Exit",20,1000 - 60);
  
  g.setColor(Color.green);
  g.fillRect(5,105,90,90);
  g.setColor(Color.black);
  g.drawString("Display", 20, 145);
  g.drawString("Moves", 20, 160);
  
  g.setColor(Color.CYAN);
  g.fillRect(5, 205, 90, 90);
  g.setColor(Color.BLACK);
  g.drawString("Execute", 20, 245);
  g.drawString("Random", 20, 260);
  g.drawString("Move", 20, 275);  
  
  g.setColor(Color.PINK);
  g.fillRect(5, 305, 90, 90);
  g.setColor(Color.BLACK);
  g.drawString("Execute", 20, 345);
  g.drawString("By", 20, 360);
  g.drawString("Heuristic", 20, 375);
    
  g.setColor(Color.MAGENTA);
  g.fillRect(5, 405, 90, 90);
  g.setColor(Color.BLACK);
  g.drawString("Execute", 20, 445);
  g.drawString("By", 20, 460);
  g.drawString("Eval", 20, 475);  
  
  g.setColor(Color.RED);
  g.fillRect(5, 505, 90, 90);
  g.setColor(Color.BLACK);
  g.drawString("Configure", 20, 545);
  g.drawString("Test", 20, 560);
  g.drawString("Position", 20, 575); 
  
  
  
  
  if (listener2.clickPending()) {
   listener2.clickHandled();
   
   
   if (endTurnButton.contains(listener2.getRectifiedClick())) {
    terminationPending = true;
   }
   
   if (exitButton.contains(listener2.getRectifiedClick())) {
    exitPending = true;
   }
   
   if (showButton.contains(listener2.getRectifiedClick())) {
    showPending = true;
   }
   
   if (executeButton.contains(listener2.getRectifiedClick())) {
    executionPending = true;
   }
   
   if (bestButton.contains(listener2.getRectifiedClick())) {
	   bestPending = true;
   }
   
   if (byEvalButton.contains(listener2.getRectifiedClick())) {
	   byEvalPending = true;
   }
   
   if (configScenarioButton.contains(listener2.getRectifiedClick())) {
	   scenarioPending = true;
   }
  }
  
 }
 
 public void terminationHandled() {
  terminationPending = false;
 }
 
 public void executionHandled() {
  this.executionPending = false;
 }
 
 public void showHandled() {
  this.showPending = false;
 } 
 
 public void bestHandled() {
	 this.bestPending = false;
 }
 
 public void byEvalHandled() {
	 this.byEvalPending = false;
 }
 
 public void scenarioHandled() {
	 this.scenarioPending = false;
 }
 
 
}
 
 
 

