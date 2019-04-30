
import java.awt.Color;
import java.awt.Graphics;

public class Arbiter { 
 private PieceType[] moveOrder;
 private int teamToMove;
 public Square[][] squares;
 private boolean chainMoveInProgress;
 public String gameWinner;
 
 public boolean isChainMoveInProgress() {
  return chainMoveInProgress;
 }

 Arbiter(Square[][] source) {
  squares = source;
  
  
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
  
  int pastRow = selected.boardLocation.rowValue;
  int pastColumn = selected.boardLocation.columnValue;
  
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
  
  int targetRow = move.targetPosition.rowValue;
  int targetColumn = move.targetPosition.columnValue;
  
  
  if (squares[targetRow][targetColumn].piece != null) {
   return false;
  }
  
  int pastRow = move.startPosition.rowValue;
  int pastColumn = move.startPosition.columnValue;  
  
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
  chainMoveInProgress = false;  
  
  if (teamToMove == 6) {
   teamToMove = 0;
  }
  
  if (moveOrder[teamToMove] == null ) {
   teamToMove = 0;
  }
 }
  
 
 public void determineGameResult(ArrayCoordinate[][] regions) {
  gameWinner = moveOrder[teamToMove].team;
  int checkRegion = moveOrder[teamToMove].targetRegion;  
  for (int j = 0; j < 10; j++) {
   ArrayCoordinate checkCoordinate = regions[checkRegion][j];
   if (squares[checkCoordinate.rowValue][checkCoordinate.columnValue].piece != moveOrder[teamToMove]) {
    gameWinner = null;
    j = 10;
   }
  }     
 }
 
 public int returnCurrentMoveCode() {
  return this.teamToMove;
 }
 
 public PieceType returnCurrentTeam() {
	 return this.moveOrder[teamToMove];
 }
 
 public void displayTeamToMove(Graphics g) {
  g.setColor(Color.WHITE);
  g.fillRect(300 - 150,150,300,100);
  g.setColor(moveOrder[teamToMove].color);
  g.drawRect(310 - 150, 160, 280, 80);
  g.drawString("Team to move is: " + moveOrder[teamToMove].team, 310 - 150 + 40, 200);  
 }
}

