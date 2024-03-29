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
  //Variables
  private BoardPanel gameArea;
  private SidePanel sidePanel;
  private InfoPanel infoPanel;
  public boolean exitFlag;
  
  public CustomTimeManager timeManager;
  public Timer timer;
  private boolean timerFlag;
  private boolean timerEnable;
  
  /** 
   * Display
   * A constructor that sets all the panels and graphics up
   */
  Display() {
    //Set up window characteristics
    this.setSize((int) (1025*Constants.scaleFactor), Toolkit.getDefaultToolkit().getScreenSize().height);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    //Set up a timer
    this.timer = new Timer();
    this.timeManager = new CustomTimeManager();
    this.timerEnable = false;
    
    //Set up a game board panel
    this.gameArea = new BoardPanel();
    this.gameArea.configureInitialSetup();        
    this.add(gameArea, BorderLayout.CENTER);
    
    //Set up the side panel
    this.sidePanel = new SidePanel();
    this.add(sidePanel, BorderLayout.WEST);
    
    //Set up the info panel
    this.infoPanel = new InfoPanel();
    this.add(infoPanel, BorderLayout.EAST);
    
    this.setVisible(true);
    
  }
  
  /** 
   * refresh
   * Resets and refreshes the UI
   */
  public void refresh() {
    sidePanel.repaint();
    
    //Exit the panel
    if (sidePanel.exitPending) {
      exitFlag = true;
      setVisible(false);
      dispose();
    }
    
    if (!gameArea.gameFinished) {
      //Handle game over
      if (sidePanel.terminationPending) {
        sidePanel.terminationHandled();
        gameArea.terminateMove();
      }        
      //Show message
      if (sidePanel.showPending) {
        sidePanel.showHandled();
        gameArea.displayPossibleMoves();
        System.out.println("Possibilities handled");
      }
      //Show message
      if (sidePanel.executionPending) {
        sidePanel.executionHandled();
        gameArea.executeRandomMove();
        System.out.println("Random Execution Done");
      }
      //Show message
      if (sidePanel.bestPending) {
        sidePanel.bestHandled();
        gameArea.executeBestMove();
        System.out.println("Best Execution Done");
      }
      //Show message
      if (sidePanel.byEvalPending || (timerFlag && timerEnable)) {
        sidePanel.byEvalHandled();
        gameArea.executeByDepth();
        System.out.println("By eval execution done");
        timerFlag = false;
      }                   
      //Enable timer
      if (sidePanel.timeTriggerPending) {
        this.timerEnable = true;
      }
    }
    //Show message
    if (sidePanel.scenarioPending) {
      sidePanel.scenarioHandled();
      gameArea.configureEndScenario();
      System.out.println("Scenario setup sucessful");
    }
    
    //Update game information
    infoPanel.updateTurnInfo(gameArea.currentEvaluation, gameArea.arbiter.returnCurrentTeam(), gameArea.arbiter.numberOfMoves);   
    infoPanel.updateHoverData(gameArea.currentHoverRow, gameArea.currentHoverColumn);
    infoPanel.updateMouseData(gameArea.listener.getRectifiedPos().x, gameArea.listener.getRectifiedPos().y);
    infoPanel.bestBranchEval = gameArea.bestBranchEval;
    
    gameArea.repaint();    
  }
  
  /* CustomTimeManager.java
   * Purpose: To act as a timer
   * Creators: Artem, Joyce, Shi Han
   * Date: 2019-05-04
   */
  private class CustomTimeManager extends TimerTask {
    public void run() {
      timerFlag = true;
    }
  }
}
