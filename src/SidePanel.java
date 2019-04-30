import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class SidePanel extends JPanel {
    private CustomMouseListener listener2;
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private final Rectangle endTurnButton = new Rectangle(0, 0, 100, 100);
    private final Rectangle exitButton = new Rectangle(0, 1000 - 100, 100, 100);
    private final Rectangle showButton = new Rectangle(0, 100, 100, 100);
    private final Rectangle executeButton = new Rectangle(0, 200, 100, 100);
    private final Rectangle bestButton = new Rectangle(0, 300, 100, 100);

    public boolean terminationPending;
    public boolean exitPending;
    public boolean executionPending;
    public boolean showPending;
    public boolean bestPending;

    SidePanel() {
        listener2 = new CustomMouseListener();
        this.addMouseListener(listener2);

        Dimension sideBarSize = new Dimension(100, SCREEN_SIZE.height);
        this.setPreferredSize(sideBarSize);
        this.setBackground(Color.BLUE);
        this.setOpaque(true);
        terminationPending = false;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setDoubleBuffered(true);

        g.setFont(new Font("TrilliumWeb", Font.BOLD, 16));

        g.setColor(Color.YELLOW);
        g.fillRect(5, 5, 90, 90);
        g.setColor(Color.BLACK);
        g.drawString("End Turn", 18, 40);

        g.setColor(Color.ORANGE);
        g.fillRect(5, 1000 - 105, 90, 90);
        g.setColor(Color.BLACK);
        g.drawString("Exit", 20, 1000 - 60);

        g.setColor(Color.green);
        g.fillRect(5, 105, 90, 90);
        g.setColor(Color.black);
        g.drawString("Display", 20, 145);
        g.drawString("Moves", 22, 160);

        g.setColor(Color.CYAN);
        g.fillRect(5, 205, 90, 90);
        g.setColor(Color.BLACK);
        g.drawString("Execute", 20, 245);
        g.drawString("Random", 20, 260);
        g.drawString("Move", 23, 275);

        g.setColor(Color.PINK);
        g.fillRect(5, 305, 90, 90);
        g.setColor(Color.BLACK);
        g.drawString("Execute", 20, 345);
        g.drawString("Best", 20, 360);
        g.drawString("Move", 23, 375);

        if (listener2.clickPending()) {
            listener2.clickHandled();


            if (endTurnButton.contains(listener2.getClick())) {
                terminationPending = true;
            }

            if (exitButton.contains(listener2.getClick())) {
                exitPending = true;
            }

            if (showButton.contains(listener2.getClick())) {
                showPending = true;
            }

            if (executeButton.contains(listener2.getClick())) {
                executionPending = true;
            }

            if (bestButton.contains(listener2.getClick())) {
                bestPending = true;
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


}
