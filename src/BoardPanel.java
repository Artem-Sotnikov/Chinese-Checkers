import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;

public class BoardPanel extends JPanel{
	private Square[][] newSquares;
	CustomMouseListener listener;
	private ArrayCoordinate[][] regions;
	
	private GameState gameState;
	private Square selectedSquare;
	private Arbiter arbiter;
	private PieceManager manager;
	
	public boolean gameFinished;
	public String gameWinner;
	
	BoardPanel() {
		Dimension boardSize = new Dimension (500,Toolkit.getDefaultToolkit().getScreenSize().height);
		this.setPreferredSize(boardSize);
		this.setOpaque(false);
		
		this.newSquares = new Square[25][25];
		listener = new CustomMouseListener();
		this.addMouseMotionListener(listener);
		this.addMouseListener(listener);
		
		this.arbiter = new Arbiter(newSquares);
		this.manager = new PieceManager();
		
		regions = new ArrayCoordinate[6][10];
		
		regions[0][0] = new ArrayCoordinate(8,4);
		regions[0][1] = new ArrayCoordinate(9,4);
		regions[0][2] = new ArrayCoordinate(9,5);
		regions[0][3] = new ArrayCoordinate(10,4);
		regions[0][4] = new ArrayCoordinate(10,5);
		regions[0][5] = new ArrayCoordinate(10,6);
		regions[0][6] = new ArrayCoordinate(11,4);
		regions[0][7] = new ArrayCoordinate(11,5);
		regions[0][8] = new ArrayCoordinate(11,6);
		regions[0][9] = new ArrayCoordinate(11,7);
		
		regions[1][0] = new ArrayCoordinate(12,9);
		regions[1][1] = new ArrayCoordinate(12,10);
		regions[1][2] = new ArrayCoordinate(12,11);
		regions[1][3] = new ArrayCoordinate(12,12);
		regions[1][4] = new ArrayCoordinate(13,10);
		regions[1][5] = new ArrayCoordinate(13,11);
		regions[1][6] = new ArrayCoordinate(13,12);
		regions[1][7] = new ArrayCoordinate(14,11);
		regions[1][8] = new ArrayCoordinate(14,12);
		regions[1][9] = new ArrayCoordinate(15,12);
		
		regions[2][0] = new ArrayCoordinate(17,13);
		regions[2][1] = new ArrayCoordinate(18,13);
		regions[2][2] = new ArrayCoordinate(18,14);
		regions[2][3] = new ArrayCoordinate(19,13);
		regions[2][4] = new ArrayCoordinate(19,14);
		regions[2][5] = new ArrayCoordinate(19,15);
		regions[2][6] = new ArrayCoordinate(20,13);
		regions[2][7] = new ArrayCoordinate(20,14);
		regions[2][8] = new ArrayCoordinate(20,15);
		regions[2][9] = new ArrayCoordinate(20,16);
		
		regions[3][0] = new ArrayCoordinate(21,9);
		regions[3][1] = new ArrayCoordinate(21,10);
		regions[3][2] = new ArrayCoordinate(21,11);
		regions[3][3] = new ArrayCoordinate(21,12);
		regions[3][4] = new ArrayCoordinate(22,10);
		regions[3][5] = new ArrayCoordinate(22,11);
		regions[3][6] = new ArrayCoordinate(22,12);
		regions[3][7] = new ArrayCoordinate(23,11);
		regions[3][8] = new ArrayCoordinate(23,12);
		regions[3][9] = new ArrayCoordinate(24,12);
		
		regions[4][0] = new ArrayCoordinate(17,4);
		regions[4][1] = new ArrayCoordinate(18,4);
		regions[4][2] = new ArrayCoordinate(18,5);
		regions[4][3] = new ArrayCoordinate(19,4);
		regions[4][4] = new ArrayCoordinate(19,5);
		regions[4][5] = new ArrayCoordinate(19,6);
		regions[4][6] = new ArrayCoordinate(20,4);
		regions[4][7] = new ArrayCoordinate(20,5);
		regions[4][8] = new ArrayCoordinate(20,6);
		regions[4][9] = new ArrayCoordinate(20,7);
		
		regions[5][0] = new ArrayCoordinate(12,0);
		regions[5][1] = new ArrayCoordinate(12,1);
		regions[5][2] = new ArrayCoordinate(12,2);
		regions[5][3] = new ArrayCoordinate(12,3);
		regions[5][4] = new ArrayCoordinate(13,1);
		regions[5][5] = new ArrayCoordinate(13,2);
		regions[5][6] = new ArrayCoordinate(13,3);
		regions[5][7] = new ArrayCoordinate(14,2);
		regions[5][8] = new ArrayCoordinate(14,3);
		regions[5][9] = new ArrayCoordinate(15,3);
		
		
		
	}
	
	public void terminateMove() {
		newSquares[selectedSquare.boardLocation.rowValue]
				[selectedSquare.boardLocation.columnValue].setSelected(false);					
		gameState = GameState.STATE_IDLE;
		arbiter.determineGameResult(regions);
		if (arbiter.gameWinner != null) {
			gameFinished = true;
		} 
		arbiter.registerMove();
		
	}
	
	public void handleHovers(int i, int j, Graphics g) {
		
		if (newSquares[i][j].containsCoordinates(listener.getPos())) {
			
			g.setColor(Color.BLACK);
			g.drawString(newSquares[i][j].boardLocation.rowValue + " " + newSquares[i][j].boardLocation.columnValue, 40, 40);
			
			
			
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
						arbiter.approvesMove(selectedSquare, i, j, false)) {
					
					newSquares[i][j].setHovered(true);   							
					
				} else {
					newSquares[i][j].setHovered(false);
				} 
			}
			break;
			    					
			}
		} else {
			newSquares[i][j].setHovered(false);
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
						arbiter.approvesMove(selectedSquare,i, j, true)) {
					
						listener.clickHandled();
									
						movePiece(selectedSquare,new ArrayCoordinate(i,j));
						
						if (!arbiter.isChainMoveInProgress()) {
							this.terminateMove();
						} 
						
				} else if (newSquares[i][j].containsCoordinates(listener.getClick()) && 
						arbiter.approvesSelection(i, j)) {
										
						listener.clickHandled();
					
						newSquares[selectedSquare.boardLocation.rowValue]
								[selectedSquare.boardLocation.columnValue].setSelected(false);
						
						newSquares[i][j].setSelected(true);
						selectedSquare = newSquares[i][j];
				}
			}
			break;
			}
		}
	}
	
    public void paintComponent(Graphics g) {
    	//System.out.println("graphics running");
    	System.out.println(gameState);
    	super.paintComponent(g);
    	setDoubleBuffered(true);
    	g.setColor(Color.black);
    	
    	int currentMouseX = listener.getX();
		//System.out.println(currentMouseX);
		int currentMouseY = listener.getY();
		//System.out.println(currentMouseY);
		

    	
    	for (int i = 0; i < newSquares.length; i++) {
    		for (int j = 0; j <= i; j++) {
    			if (newSquares[i][j] != null) {
    				
    				this.handleHovers(i,j,g);
    				if (listener.clickPending()) {
    					this.handleClicks(i,j);
    				}
    				
    				
    				newSquares[i][j].draw(g);
    				
    			}    			
    		}
    	}
    	
    	if (gameFinished) {
    		g.setColor(Color.LIGHT_GRAY);
    		g.fillRect(400, 200, 200, 100);
    		g.setColor(Color.BLACK);
    		g.drawString("Game terminated with ", 400 + 40, 200 + 30);
    		g.drawString(arbiter.gameWinner + " victorious", 400 + 40, 200 + 70);
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
    	
    	
    	for (int i = 0; i < regions[0].length; i++) {
    		newSquares[regions[0][i].rowValue][regions[0][i].columnValue].placePiece(Constants.teamZeroPiece);
    		manager.pieceStorage[0][i] = newSquares[regions[0][i].rowValue][regions[0][i].columnValue];
    	}    
    	
    	for (int i = 0; i < regions[0].length; i++) {
    		newSquares[regions[1][i].rowValue][regions[1][i].columnValue].placePiece(Constants.teamOnePiece);
    		manager.pieceStorage[1][i] = newSquares[regions[0][i].rowValue][regions[0][i].columnValue];
    	}
    	
    	for (int i = 0; i < regions[0].length; i++) {
    		newSquares[regions[2][i].rowValue][regions[2][i].columnValue].placePiece(Constants.teamTwoPiece);
    		manager.pieceStorage[2][i] = newSquares[regions[0][i].rowValue][regions[0][i].columnValue];
    	}
    	    
    	for (int i = 0; i < regions[0].length; i++) {
    		newSquares[regions[3][i].rowValue][regions[3][i].columnValue].placePiece(Constants.teamThreePiece);
    		manager.pieceStorage[3][i] = newSquares[regions[0][i].rowValue][regions[0][i].columnValue];
    	}
    	
    	for (int i = 0; i < regions[0].length; i++) {
    		newSquares[regions[4][i].rowValue][regions[4][i].columnValue].placePiece(Constants.teamFourPiece);
    		manager.pieceStorage[4][i] = newSquares[regions[0][i].rowValue][regions[0][i].columnValue];
    	}
    	
    	for (int i = 0; i < regions[0].length; i++) {
    		newSquares[regions[5][i].rowValue][regions[5][i].columnValue].placePiece(Constants.teamFivePiece);
    		manager.pieceStorage[5][i] = newSquares[regions[0][i].rowValue][regions[0][i].columnValue];
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
