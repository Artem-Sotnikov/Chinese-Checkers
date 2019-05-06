/* Display.java
 * Purpose: Creates the user interface
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.util.Timer; 
import java.util.TimerTask;

public class Display extends JFrame {
  private BoardPanel gameArea;
  private SidePanel sidePanel;
  private InfoPanel infoPanel;
  public boolean exitFlag;
  
  public CustomTimeManager timeManager;
  public Timer timer;
  private boolean timerFlag;
  
  /** 
   * Display
   * A constructor that sets all the panels and graphics up
   */
  Display() {
    this.setSize((int) (1025*Constants.scaleFactor), Toolkit.getDefaultToolkit().getScreenSize().height);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    this.timer = new Timer();
    this.timeManager = new CustomTimeManager();
    
    this.gameArea = new BoardPanel();
    this.gameArea.configureInitialSetup();        
    this.add(gameArea, BorderLayout.CENTER);
    
    this.sidePanel = new SidePanel();
    this.add(sidePanel, BorderLayout.WEST);
    
    this.infoPanel = new InfoPanel();
    this.add(infoPanel, BorderLayout.EAST);
    
    this.setVisible(true);
    
  }
  
  /** 
   * refresh
   * Resets and refreshes the UI
   * ArrayCoordinate target, the place to be checked for occupancy
   */
  public void refresh() {
    sidePanel.repaint();
    
    if (sidePanel.exitPending) {
      exitFlag = true;
      setVisible(false);
      dispose();
    }
    
    if (!gameArea.gameFinished) {
      
      if (sidePanel.terminationPending) {
        sidePanel.terminationHandled();
        gameArea.terminateMove();
      }        
      
      if (sidePanel.showPending) {
        sidePanel.showHandled();
        gameArea.displayPossibleMoves();
        System.out.println("Possibilities handled");
      }
      
      if (sidePanel.executionPending) {
        sidePanel.executionHandled();
        gameArea.executeRandomMove();
        System.out.println("Random Execution Done");
      }
      
      if (sidePanel.bestPending) {
        sidePanel.bestHandled();
        gameArea.executeBestMove();
        System.out.println("Best Execution Done");
      }
      
      if (sidePanel.byEvalPending || timerFlag) {
        sidePanel.byEvalHandled();
        gameArea.executeByDepth();
        System.out.println("By eval execution done");
        timerFlag = false;
      }                    
    }
    
    if (sidePanel.scenarioPending) {
      sidePanel.scenarioHandled();
      gameArea.configureEndScenario();
      System.out.println("Scenario setup sucessful");
    }
    
    infoPanel.updateTurnInfo(gameArea.currentEvaluation, gameArea.arbiter.returnCurrentTeam(), gameArea.arbiter.numberOfMoves);   
    infoPanel.updateHoverData(gameArea.currentHoverRow, gameArea.currentHoverColumn);
    infoPanel.updateMouseData(gameArea.listener.getRectifiedPos().x, gameArea.listener.getRectifiedPos().y);
    infoPanel.bestBranchEval = gameArea.bestBranchEval;
    
    gameArea.repaint();    
  }
  
  private class CustomTimeManager extends TimerTask {
	  public void run() {
		  timerFlag = true;
	  }
  }
}
