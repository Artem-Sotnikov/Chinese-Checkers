import java.util.ArrayList;


public class PieceManager {
	public Square[][] piecePositionStorage;
	public Square[][] overallBoard;
	private static final PieceType buffer = new PieceType(null, null, 0, 0);
	private ArrayList<ArrayCoordinate> bufferCoords;
	ArrayCoordinate[] deltaOpts = new ArrayCoordinate[6];
	
	PieceManager(Square[][] source) {
		piecePositionStorage = new Square[6][10];
		overallBoard = source;
		
		deltaOpts[0] = new ArrayCoordinate(-2, 0);
		deltaOpts[1] = new ArrayCoordinate(2, 0);
		deltaOpts[2] = new ArrayCoordinate(-2, -2);
		deltaOpts[3] = new ArrayCoordinate(2, 2);
		deltaOpts[4] = new ArrayCoordinate(0, 2);
		deltaOpts[5] = new ArrayCoordinate(0, -2);
		
		bufferCoords = new ArrayList<ArrayCoordinate>(0);
	}
	
	public MoveNode[] returnAllTeamMoves(int teamCode) {
		MoveNode[] returnNodes = new MoveNode[10];
		
		for (int idx = 0; idx < 10; idx++) {
			returnNodes[idx] = generateMoves(piecePositionStorage[teamCode][idx].boardLocation,true);		
			clearBuffer();
		}
			
		return returnNodes;
	}
	
	public ArrayList<MoveCode> returnAllMoveCodes(int teamCode) {
		MoveNode[] returnNodes = this.returnAllTeamMoves(teamCode);
		ArrayList<MoveCode> returnList = new ArrayList<MoveCode>();
		
		for (int idx = 0; idx < 10; idx++) {
			returnList.addAll(returnNodes[idx].toMoveCodes());
		}
		
		return returnList;
	}
	
	public MoveNode generateMoves(ArrayCoordinate position, boolean isFirstMove) {
		int sRow = position.rowValue;
		int sCol = position.columnValue;
		
		MoveNode rootPosition = new MoveNode(position);
		if (isFirstMove) {
			rootPosition.isRoot = true;
		}
		
		if (overallBoard[position.rowValue][position.columnValue].piece == null) {
			overallBoard[position.rowValue][position.columnValue].piece = buffer;
			bufferCoords.add(position);
		}
		
		if (isFirstMove) {
			for (int i = sRow - 1; i < sRow + 1 && i >= 0 && i < 25; i++) {
				for (int j = sCol - 1; j < sCol + 1 && j >= 0 && j < 25; j++) {
					if (overallBoard[i][j] != null) {
						if (overallBoard[i][j].piece == null) {
							rootPosition.branches.add(new MoveNode(i,j));
						}
					}
				}
			}
			
			for (int i = sCol; i < sCol + 2 && i >= 0 && i < 25; i++) {
				if (sRow + 1 < 25 && sRow + 1 >= 0) {
					if (overallBoard[sRow + 1][i] != null) {
						if (overallBoard[sRow + 1][i].piece == null) {
							rootPosition.branches.add(new MoveNode(sRow + 1,i));
						}
					}
				}
			}
			
			if (sCol + 1 <= 0 && sCol + 1 < 25) {
				if (overallBoard[sRow][sCol + 1] != null && overallBoard[sRow][sCol + 1].piece != null) {
					rootPosition.branches.add(new MoveNode(sRow,sCol + 1));
				}
			}
		} 
		
		
		for (int idx = 0; idx < 6; idx++) {
			if (sRow + deltaOpts[idx].rowValue >= 0 && sCol + deltaOpts[idx].columnValue >= 0 && sRow + deltaOpts[idx].rowValue < 25 && sCol + deltaOpts[idx].columnValue < 25) {
				if (overallBoard[sRow + deltaOpts[idx].rowValue][sCol + deltaOpts[idx].columnValue] != null &&
						overallBoard[sRow + (deltaOpts[idx].rowValue)/2][sCol + (deltaOpts[idx].columnValue)/2] != null) {
					if (overallBoard[sRow + deltaOpts[idx].rowValue][sCol + deltaOpts[idx].columnValue].piece == null &&
							overallBoard[sRow + (deltaOpts[idx].rowValue)/2][sCol + (deltaOpts[idx].columnValue)/2].piece != null) {
							 if (overallBoard[sRow + (deltaOpts[idx].rowValue)/2][sCol + (deltaOpts[idx].columnValue)/2].piece.team != null) {
								 rootPosition.branches.add(generateMoves(new ArrayCoordinate(
										 sRow + deltaOpts[idx].rowValue, sCol + deltaOpts[idx].columnValue), false));
							 }
					} 
				}
			}
		}
		
		
		
		return rootPosition;
	}
	
	private void clearBuffer() {
		for (int i = 0; i < bufferCoords.size(); i++) {
			overallBoard[bufferCoords.get(i).rowValue][bufferCoords.get(i).columnValue].piece = null;
		}
		bufferCoords.clear();
	}
}
