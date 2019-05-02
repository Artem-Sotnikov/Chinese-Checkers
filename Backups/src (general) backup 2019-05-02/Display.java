
import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class Display extends JFrame {
    private BoardPanel gameArea;
    private SidePanel sidePanel;
    private InfoPanel infoPanel;
    public boolean exitFlag;

    Display() {
        this.setSize((int) (1025*Constants.scaleFactor), Toolkit.getDefaultToolkit().getScreenSize().height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.gameArea = new BoardPanel();
        this.gameArea.configureInitialSetup();        
        this.add(gameArea, BorderLayout.CENTER);

        this.sidePanel = new SidePanel();
        this.add(sidePanel, BorderLayout.WEST);
        
        this.infoPanel = new InfoPanel();
        this.add(infoPanel, BorderLayout.EAST);

        this.setVisible(true);

    }

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
	        
	        if (sidePanel.byEvalPending) {
	        	sidePanel.byEvalHandled();
	        	gameArea.executeByEval();
	        	System.out.println("By eval execution done");
	        }	        
        }
        
        infoPanel.updateTurnInfo(gameArea.currentEvaluation, gameArea.arbiter.returnCurrentTeam(), gameArea.arbiter.numberOfMoves);   
        infoPanel.updateHoverData(gameArea.currentHoverRow, gameArea.currentHoverColumn);
        infoPanel.updateMouseData(gameArea.listener.getRectifiedPos().x, gameArea.listener.getRectifiedPos().y);

        gameArea.repaint();

    }
}
