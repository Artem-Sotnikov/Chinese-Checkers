
import java.awt.Color;
import java.awt.Graphics;

public class Arbiter { 
 public PieceType[] moveOrder;
 public int teamToMove;
 public Square[][] squares;
 private boolean chainMoveInProgress;
 public String gameWinner; 
 public int numberOfMoves;
 
 private ArrayCoordinate[][] regions;
 
 public boolean isChainMoveInProgress() {
  return chainMoveInProgress;
 }

 Arbiter(Square[][] source, ArrayCoordinate[][] srcRegions) {
  squares = source;
  regions = srcRegions;
  
  
  moveOrder = new PieceType[6];
  
  moveOrder[0] = Constants.teamZeroPiece;
  moveOrder[1] = Constants.teamOnePiece;
  moveOrder[2] = Constants.teamTwoPiece;
  moveOrder[3] = Constants.teamThreePiece;
  moveOrder[4] = Constants.teamFourPiece;
  moveOrder[5] = Constants.teamFivePiece;
  teamToMove = 0;
  
  gameWinner = null;
 } 
 
 public boolean approvesSelection(int row, int column) {
  if (gameWinner != null) {
   return false;
  }
  
  
  if (chainMoveInProgress) {
   return false;
  }
  
  if (squares[row][column].piece != null) {
   if (squares[row][column].piece.team == moveOrder[teamToMove].team) {
    return true;    
   }
  }
  
  return false;
  
 }
 
 public boolean approvesMove(Square selected, int targetRow, int targetColumn, boolean executionActive) {
  if (gameWinner != null) {
   return false;
  }
  
  if (squares[targetRow][targetColumn].piece != null) {
   return false;
  }
  
  int pastRow = selected.boardLocation.row;
  int pastColumn = selected.boardLocation.column;
  
  int deltaRow = (pastRow - targetRow);
  int deltaColumn = (pastColumn - targetColumn);
  int deltaSum = deltaRow + deltaColumn;
    
  
  if (deltaRow < 2 && deltaRow > -2 && 
    deltaColumn < 2 && deltaColumn > -2 &&  
    (deltaSum) != 0) {
   if (chainMoveInProgress) {
    return false;
   } else {
    return true;
   }
  }
  
  
  
   
  if (deltaRow < 3 && deltaRow > -3 && 
    deltaColumn < 3 && deltaColumn > -3 &&  
    deltaSum != 0 && deltaSum != 3 && deltaSum != -3) {
    if (squares[pastRow - deltaRow/2][pastColumn  - deltaColumn/2].piece != null) {
     if (executionActive) {
      chainMoveInProgress = true;
     }
     return true;
    }
  }
  
  return false;
 }
 
 public boolean approvesMove(MoveCode move, boolean executionActive) {
  if (gameWinner != null) {
   return false;
  }
  
  int targetRow = move.targetPosition.row;
  int targetColumn = move.targetPosition.column;
  
  
  if (squares[targetRow][targetColumn].piece != null) {
   return false;
  }
  
  int pastRow = move.startPosition.row;
  int pastColumn = move.startPosition.column;  
  
  int deltaRow = (pastRow - targetRow);
  int deltaColumn = (pastColumn - targetColumn);
  int deltaSum = deltaRow + deltaColumn;
    
  
  if (deltaRow < 2 && deltaRow > -2 && 
    deltaColumn < 2 && deltaColumn > -2 &&  
    (deltaSum) != 0) {
   if (chainMoveInProgress) {
    return false;
   } else {
    return true;
   }
  }
  
  
  
   
  if (deltaRow < 3 && deltaRow > -3 && 
    deltaColumn < 3 && deltaColumn > -3 &&  
    deltaSum != 0 && deltaSum != 3 && deltaSum != -3) {
    if (squares[pastRow - deltaRow/2][pastColumn  - deltaColumn/2].piece != null) {
     if (executionActive) {
      chainMoveInProgress = true;
     }
     return true;
    }
  }
  
  return false;
 }
 
 public void registerMove() {
  this.teamToMove++;
  this.numberOfMoves++;
  chainMoveInProgress = false;  
  
  if (teamToMove == 6) {
   teamToMove = 0;
  }
  
  if (moveOrder[teamToMove] == null ) {
   teamToMove = 0;
  }
  
  
 }
   
 public void determineGameResult() {
  gameWinner = moveOrder[teamToMove].team;
  int checkRegion = moveOrder[teamToMove].targetRegion;  
  for (int j = 0; j < 10; j++) {
   ArrayCoordinate checkCoordinate = regions[checkRegion][j];
   if (squares[checkCoordinate.row][checkCoordinate.column].piece != moveOrder[teamToMove]) {
    gameWinner = null;
    j = 10;
   }
  }     
 }
 
 public boolean hasWon(int override) {	 
  int checkRegion = moveOrder[override].targetRegion;  
  
  for (int j = 0; j < 10; j++) {
	  ArrayCoordinate checkCoordinate = regions[checkRegion][j];
	  if (squares[checkCoordinate.row][checkCoordinate.column].piece != moveOrder[teamToMove]) {
		  return false;
	  }
  } 
  
  return true;
 }
 
 public int returnCurrentMoveCode() {
  return this.teamToMove;
 }
 
 public PieceType returnCurrentTeam() {
	 return this.moveOrder[teamToMove];
 }
 
 public void displayTeamToMove(Graphics g) {
  g.setColor(Color.BLACK);
  g.fillRect(300 - 150,150,300,100);
  g.setColor(moveOrder[teamToMove].color);
  g.drawRect(310 - 150, 160, 280, 80);
  if (gameWinner == null) {
	  g.drawString("Team to move is: " + moveOrder[teamToMove].team, 310 - 150 + 40, 200);
  } else {
	  g.drawString("Game Terminated.", 310 - 150 + 40, 200);
	  g.drawString("Team " + gameWinner + " is victorious", 310 - 150 + 40, 200 + 25);
  }
 }
}

