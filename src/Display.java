
import javax.swing.JFrame;


public class Display extends JFrame{
	private Board gameArea;
	
	Display() {
		this.setSize(1000, 1000);

		gameArea = new Board();
		gameArea.configureInitialSetup();
		this.add(gameArea);		
		this.setVisible(true);
		
	}
	
	public void refresh() {
		gameArea.repaint();
	}
}
