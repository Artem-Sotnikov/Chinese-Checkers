/* InfoPanel.java
 * Purpose: A class that displays mouse and player data when using CheckerMain.java UI
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

// Imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
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
    private int numMoves;
    public double bestBranchEval;

    /**
     * InfoPanel
     * Constructor that creates an information panel for UI
     */
    InfoPanel() {
        // Set size of the InfoPanel
        this.setPreferredSize(new Dimension((int) (300 * Constants.scaleFactor),
                Toolkit.getDefaultToolkit().getScreenSize().height));
        this.setMinimumSize(new Dimension((int) (300 * Constants.scaleFactor),
                Toolkit.getDefaultToolkit().getScreenSize().height));

        // Set other aspects of the InfoPanel's appearance
        this.setBackground(Color.DARK_GRAY);
        this.setOpaque(true);
        this.setVisible(true);
        this.setAlignmentX(RIGHT_ALIGNMENT);

        // Set the values of the data we will use to display information about the display
        this.activeI = -1;
        this.activeJ = -1;
        this.activeX = -1;
        this.activeY = -1;
        this.activeEval = -1;
    }

    /**
     * updateTurnInfo
     * Updates the info each turn
     * @param eval, the ranking of the evaluation for the move
     * @param currentTeam, the information of the current team
     * @param moveNumber, the current move number
     */
    public void updateTurnInfo(double eval, PieceType currentTeam, int moveNumber) {
        this.activeEval = eval;
        this.activeTeam = currentTeam;
        this.numMoves = moveNumber;
        this.repaint();
    }

    /**
     * updateHoverData
     * Coordinates of mouse hovering is updated
     * @param i, the row value
     * @param j, the column value
     */
    public void updateHoverData(int i, int j) {
        this.activeI = i;
        this.activeJ = j;
    }

    /**
     * updateMouseData
     * Coordinates of mouse data is updated
     * @param x, the row value
     * @param y, the column value
     */
    public void updateMouseData(int x, int y) {
        this.activeX = x;
        this.activeY = y;
    }

    /**
     * paintComponent
     * Paints the display
     * @param g, the graphics component
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setDoubleBuffered(true);

        // Set the font
        this.setFont(new Font("TrilliumWeb", Font.BOLD, 17));
        g.setColor(Color.white);

        // Cast the Graphics into Graphics2D
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(Constants.scaleFactor, Constants.scaleFactor);

        // Write out information about which team is moving and how good their move is
        g.drawRect(5, 5, 290, 90);
        g.drawString("Team to move is: " + activeTeam.team, 20, 40);
        g.drawString("Evaluation: " + Double.toString(activeEval), 20, 65);

        // Write out information about what the last hovered coordinates are
        g.drawRect(5, 5 + 100, 290, 90);
        g.drawString("Last Hovered Coordinates Are:", 20, 40 + 100);
        g.drawString("(Row: " + activeI + " Column: " + activeJ + ")", 20, 65 + 100);

        // Write out the current mouse coordinates
        g.drawRect(5, 5 + 200, 290, 90);
        g.drawString("Current True Mouse Coords are: ", 20, 40 + 200);
        g.drawString("(X: " + activeX + " Y: " + activeY + ")", 20, 65 + 200);

        // Write out the number of moves made
        g.drawRect(5, 5 + 300, 290, 90);
        g.drawString("Number of moves: " + numMoves, 20, 40 + 300);

        // Write out the best case evaluation
        g.drawRect(5, 5 + 400, 290, 90);
        g.drawString("Best case evaluation: ", 20, 40 + 400);
        g.drawString(Double.toString(bestBranchEval), 20, 65 + 400);
    }
}
