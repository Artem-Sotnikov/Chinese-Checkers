
import javax.swing.JFrame;


public class Display extends JFrame{
	private Board gameArea;
	
	Display() {
		this.setSize(1000, 1000);
		this.setVisible(true);
		gameArea = new Board();
		gameArea.configureInitialSetup();
		//gameArea.getIgnoreRepaint();
		this.add(gameArea);
		
	}
	
	public void refresh() {
		gameArea.repaint();
	}
}
