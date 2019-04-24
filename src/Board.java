import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;

public class Board extends JPanel{
	private int[][] squares;
	private Square[][] newSquares;
	MyMouseListener listener;
	
	private GameState gameState;
	private Square selectedSquare;
	private Arbiter arbiter;
	
	Board() {
		this.newSquares = new Square[25][25];
		listener = new MyMouseListener();
		this.addMouseMotionListener(listener);
		this.addMouseListener(listener);
		
		this.arbiter = new Arbiter(newSquares);
		
	}
	
	public void terminateMove() {
		newSquares[selectedSquare.boardLocation.rowValue]
				[selectedSquare.boardLocation.columnValue].setSelected(false);					
		gameState = GameState.STATE_IDLE;
		arbiter.registerMove();
	}
	
	public void handleHovers(int i, int j) {
		switch (gameState) {
		case STATE_IDLE: {
			if (newSquares[i][j].containsCoordinates(listener.getPos()) && 
					arbiter.approvesSelection(i, j)) {
				
					newSquares[i][j].setHovered(true);   							
					
			} else {
				newSquares[i][j].setHovered(false); 
			}
		}
		break;
		case STATE_PIECE_SELECTED: {
			if (newSquares[i][j].containsCoordinates(listener.getPos()) && 
					arbiter.approvesMove(selectedSquare, i, j)) {
				
				newSquares[i][j].setHovered(true);   							
				
			} else {
				newSquares[i][j].setHovered(false);  
			}
		}
		break;
		    					
		}
	}
	
	public void handleClicks(int i, int j) {
		
		if (newSquares[i][j].containsCoordinates(listener.getClick())) {
			switch (gameState) {
			case STATE_IDLE: {
				if (newSquares[i][j].containsCoordinates(listener.getClick()) && 
						arbiter.approvesSelection(i, j)) {
					
						listener.clickHandled();
					
						newSquares[i][j].setSelected(true);  
						selectedSquare = newSquares[i][j];
						
						gameState = GameState.STATE_PIECE_SELECTED;
						
				} 
			
			}
			break;
			case STATE_PIECE_SELECTED: {
				if (newSquares[i][j].containsCoordinates(listener.getClick()) && 
						arbiter.approvesMove(selectedSquare,i, j)) {
					
						listener.clickHandled();
									
						movePiece(selectedSquare,new ArrayCoordinate(i,j));
						
						if (arbiter.isChainMoveInProgress()) {
							
						} else {
							this.terminateMove();
						}
						
				} else if (newSquares[i][j].containsCoordinates(listener.getClick()) && 
						newSquares[i][j].piece != null) {
					
						listener.clickHandled();
					
						newSquares[selectedSquare.boardLocation.rowValue]
								[selectedSquare.boardLocation.columnValue].setSelected(false);
						
						newSquares[i][j].setSelected(true);
						selectedSquare = newSquares[i][j];
				}
			}
			break;
			case STATE_CHAIN_MOVE: {
				
			}
			break;
			}
		}
	}
	
    public void paintComponent(Graphics g) {
    	System.out.println("graphics running");
    	System.out.println(gameState);
    	super.paintComponent(g);
    	g.setColor(Color.black);
    	
    	int currentMouseX = listener.getX();
		System.out.println(currentMouseX);
		int currentMouseY = listener.getY();
		System.out.println(currentMouseY);
		

    	
    	for (int i = 0; i < newSquares.length; i++) {
    		for (int j = 0; j <= i; j++) {
    			if (newSquares[i][j] != null) {
    				
    				this.handleHovers(i,j);
    				if (listener.clickPending()) {
    					this.handleClicks(i,j);
    				}
    				
    				
    				newSquares[i][j].draw(g);
    				
    			}    			
    		}
    	}
    	
    	//repaint();
    }
    
    public void configureInitialSetup() {
    	
    	for (int i = 7; i < (7 + 14); i++) {
    		for (int j = 0; j < (i - 7); j++) {
    			newSquares[i][j + 4] = new Square(i,j + 4);
    		}
    	}
    	
    	for (int i = 12; i < 25; i++) {
    		for (int j = 0; j < (25 - i); j++) {
    			newSquares[i][j + (i - 12)] = new Square(i,j + (i - 12));
    		}
    	}  
    	
    	Piece teamOnePiece = new Piece();
    	teamOnePiece.color = Color.red;
    	teamOnePiece.team = "one";
    	
    	for (int i = 7; i < (7 + 5); i++) {
    		for (int j = 0; j < (i - 7); j++) {
    			System.out.println(i + " " + j);
    			newSquares[i][j + 4].placePiece(teamOnePiece);
    		}
    	} 
    	
    	Piece teamTwoPiece = new Piece();
    	teamTwoPiece.color = Color.blue;
    	teamTwoPiece.team = "two";
    	
    	for (int i = 24; i > (24 - 4); i--) {
    		for (int j = 0; j < (25 - i); j++) {
    			System.out.println(i + " " + j);
    			newSquares[i][j + (i - 12)].placePiece(teamTwoPiece);
    		}
    	}    
    	
    	gameState = GameState.STATE_IDLE;
    }
    
    public boolean squareOccupied(ArrayCoordinate target) {
    	return (newSquares[target.rowValue][target.columnValue].piece != null);
    }
    
    public boolean movePiece(Square selected, ArrayCoordinate targetLocation) {
    	int clearRow = selected.boardLocation.rowValue;
    	int clearColumn = selected.boardLocation.columnValue;
    	
    	selected.boardLocation = targetLocation;
    	newSquares[targetLocation.rowValue][targetLocation.columnValue] = selected;
    	
    	newSquares[clearRow][clearColumn] = new Square(clearRow,clearColumn);
    	
    	return true;
    }
   
}
