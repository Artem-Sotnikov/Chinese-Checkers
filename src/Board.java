import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class Board extends JPanel{
	private int[][] squares;
	private Square[][] newSquares;
	
	Board() {
		this.squares = new int[25][25];
		this.newSquares = new Square[25][25];
	}
	
    public void paintComponent(Graphics g) {
    	for (int i = 0; i < squares.length; i++) {
    		for (int j = 0; j <= i; j++) {
    			int calculatedX = 500 - 15*i + 30*j;
    			int calculatedY = 20 + 30*i;
    			if (/* this.squares[i][j] == -1 &&  */ this.newSquares[i][j] == null) {
    				g.setColor(Color.BLACK);
    			} else if (/*squareOccupied(new Coordinate(i,j)) && */ this.newSquares[i][j].piece != null) {
    				g.setColor(Color.red);
    			}  else {
    				g.setColor(Color.blue);
    			}
    			
    			g.fillOval(calculatedX,calculatedY , 20, 20);
    		}
    	}
    }
    
    public void configureInitialSetup() {
    	for (int i = 0; i < squares.length; i++) {
    		for (int j = 0; j < squares.length; j++) {
    			squares[i][j] = -1;
    		}
    	}
    	
    	for (int i = 7; i < (7 + 14); i++) {
    		for (int j = 0; j < (i - 7); j++) {
    			squares[i][j + 4] = 0;
    			newSquares[i][j + 4] = new Square();
    		}
    	}
    	
    	for (int i = 12; i < 25; i++) {
    		for (int j = 0; j < (25 - i); j++) {
    			squares[i][j + (i - 12)] = 0;
    			newSquares[i][j + (i - 12)] = new Square();
    		}
    	}
    	
//    	for (int i = 0; i < 8; i++) {
//    		for (int j = 0; j <= i; j++) {
//    			squares[i][j] = -1;
//    		}
//    	}
//    	
//    	for (int i = 8; i < 12; i++) {
//    		for (int j = 0; j < 4; j++) {
//    			squares[i][j] = -1;
//    			squares[i][j + 4 + (i - 7)] = -1;
//    		}
//    	}
//    	
//    	for (int i = 13; i < 17; i++) {
//    		for (int j = 0; j <= (i - 13); j++) {
//    			squares[i][j] = -1;
//    			squares[i][j + 13] = -1;
//    		}
//    	}
//    	
//    	for (int i = 17; i < 21; i++) {
//    		for (int j = 0; j < 4; j++) {
//    			squares[i][j] = -1;
//    			squares[i][j + 14 + (i - 17)] = -1;
//    		}
//    	}
    }
    
    public boolean squareOccupied(Coordinate target) {
    	return (squares[target.xValue][target.yValue] == 1);
    }
    
    public boolean movePiece(Coordinate pieceLocation, Coordinate targetLocation) {
    	if (squares[pieceLocation.xValue][pieceLocation.yValue] == 1) {
    		squares[pieceLocation.xValue][pieceLocation.yValue] = 0;
    		squares[targetLocation.xValue][targetLocation.yValue] = 1;
    		return true;
    	}    	
    	
    	return false;
    }
   
}
