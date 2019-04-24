import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Square {
	public ArrayCoordinate boardLocation;
	public Piece piece;	
	private int calculatedX, calculatedY;
	
	private boolean isSelected, isHovered;
	
	public boolean isHovered() {
		return isHovered;
	}

	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	Square() {};
	
	Square(int i, int j) {
		this.boardLocation = new ArrayCoordinate(i,j);
		this.piece = null;
		isSelected = false;
		isHovered = false;
	    
	}
	

	
	public void draw(Graphics g) {
		calculatedX = 500 - 15*boardLocation.rowValue + 30*boardLocation.columnValue;
	    calculatedY = 20 + 30*boardLocation.rowValue;
		if (this.isSelected) {
			g.setColor(Color.YELLOW);
			g.fillOval(calculatedX - 3, calculatedY - 3, 26, 26);
		}
		
		if (this.piece != null) {
			g.setColor(this.piece.color);
		} else {
			g.setColor(Color.black);
		}
		
//		g.setColor(Color.black);
			
		//System.out.println("drawing executed");
		g.fillOval(calculatedX, calculatedY, 20, 20);
		
		if (this.isHovered) {
			g.setColor(Color.YELLOW);
			g.fillOval(calculatedX + 4, calculatedY + 4, 12, 12);
		}
		
		
	}
	
	public void placePiece(Piece newPiece) {
		this.piece = newPiece;		
	}
	
	public boolean containsCoordinates(Point point) {
		
		int centerX = calculatedX + 10;
		int centerY = calculatedY + 10;
		
		int radius = 10;
		
		if (calculatedX < point.x && calculatedX + 20 > point.x &&
				calculatedY < point.y && calculatedY + 20 > point.y) {
			return true;		
		}
		
		if ((Math.pow(centerX + point.x,2) + Math.pow(centerY + point.y, 2)) 
				< Math.pow(radius,2)) {
			return true;
		}
		
		return false;
	}
}
