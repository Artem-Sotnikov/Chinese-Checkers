
import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Display extends JFrame {
    private BoardPanel gameArea;
    private SidePanel sidePanel;
    public boolean exitFlag;

    Display() {
        this.setSize(750, 1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.gameArea = new BoardPanel();
        this.gameArea.configureInitialSetup();
        this.add(gameArea, BorderLayout.CENTER);

        this.sidePanel = new SidePanel();
        this.add(sidePanel, BorderLayout.WEST);

        this.setVisible(true);

    }

    public void refresh() {
        sidePanel.repaint();

        if (sidePanel.terminationPending) {
            sidePanel.terminationHandled();
            gameArea.terminateMove();
        }

        if (sidePanel.exitPending) {
            exitFlag = true;
            setVisible(false);
            dispose();
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

        gameArea.repaint();

    }
}
