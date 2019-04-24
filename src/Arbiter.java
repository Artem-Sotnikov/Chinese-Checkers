
public class Arbiter {	
	private String[] moveOrder;
	private int teamToMove;
	public Square[][] squares;
	private boolean chainMoveInProgress;
	private Square activeSquare;
	
	public boolean isChainMoveInProgress() {
		return chainMoveInProgress;
	}

	Arbiter(Square[][] source) {
		squares = source;
		
		
		moveOrder = new String[6];
		
		moveOrder[0] = "one";
		moveOrder[1] = "two";
		teamToMove = 0;
	}
	
	public boolean approvesSelection(int row, int column) {
		
		if (chainMoveInProgress) {
			return false;
		}
		
		if (squares[row][column].piece != null) {
			if (squares[row][column].piece.team == moveOrder[teamToMove]) {
				return true;				
			}
		}
		
		return false;
		
	}
	
	public boolean approvesMove(Square selected, int targetRow, int targetColumn) {
		
		if (squares[targetRow][targetColumn].piece != null) {
			return false;
		}
		
		int pastRow = selected.boardLocation.rowValue;
		int pastColumn = selected.boardLocation.columnValue;
		
		int deltaRow = (pastRow - targetRow);
		int deltaColumn = (pastColumn - targetColumn);
		int deltaSum = deltaRow + deltaColumn;
				
		if (!chainMoveInProgress) {
			if (deltaRow < 2 && deltaRow > -2 && 
					deltaColumn < 2 && deltaColumn > -2 && 	
					(deltaSum) != 0) {
				return true;
			}
		}
			
		if (deltaRow < 3 && deltaRow > -3 && 
				deltaColumn < 3 && deltaColumn > -3 && 	
				deltaSum != 0 && deltaSum != 3 && deltaSum != -3) {
			 if (squares[pastRow - deltaRow/2][pastColumn  - deltaColumn/2].piece != null) {
				 chainMoveInProgress = true;
				 return true;
			 }
		}
		
		return false;
	}
	
	public void registerMove() {
		this.teamToMove++;
		chainMoveInProgress = false;
		if (moveOrder[teamToMove] == null) {
			teamToMove = 0;
		}
	}
}
