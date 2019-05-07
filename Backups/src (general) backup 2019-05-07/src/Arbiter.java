/* Arbiter.java
 * Purpose: To control and monitor the moves of the players during the progression of the game
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

//Imports
import java.awt.Color;
import java.awt.Graphics;

public class Arbiter { 

  //Class variables
  public ArrayCoordinate[][] regions;
  private boolean chainMoveInProgress;
  public int teamToMove;
  public PieceType[] moveOrder;
  public Square[][] squares;
  public String gameWinner; 
  public int numberOfMoves;
  
  /** 
   * Arbiter
   * Constructor for the class, creates a board and regions  
   * @param Square[][] source, the game's board
   * @param ArrayCoordinate[][] srcRegions, the regions where each team will occupy
   */
  Arbiter(Square[][] source, ArrayCoordinate[][] srcRegions) {
    squares = source;
    regions = srcRegions;
    
    moveOrder = new PieceType[6]; //An array is created; each element contains info for each team in the game
    
    moveOrder[0] = Constants.teamZeroPiece;
    moveOrder[1] = Constants.teamOnePiece;
    moveOrder[2] = Constants.teamTwoPiece;
    moveOrder[3] = Constants.teamThreePiece;
    moveOrder[4] = Constants.teamFourPiece;
    moveOrder[5] = Constants.teamFivePiece;
    teamToMove = 0;
    
    gameWinner = null;
  } 
  
  /** 
   * isChainMoveInProgress
   * Used for moves with multiple hops; true if multiple jumps will happen, false if no more hopping can be done  
   * @return chainMoveInProgress a boolean value that determines whether or not multiple hops are happening is in progress
   */
  public boolean isChainMoveInProgress() {
    return chainMoveInProgress;
  }
  
  /** 
   * approvesSelection
   * Makes sure that the piece the player wants to select is valid  
   * @param int row, the row of the potential selection
   * @param int column, the column of the potential selection
   * @return boolean true for if the piece can be selected, false for if it isn't a valid selection
   */
  public boolean approvesSelection(int row, int column) { 
    if (gameWinner != null) { //Checks to see if there is already a winner
      return false;
    }
    
    if (chainMoveInProgress) { //Checkes if someone is still in the middle of making a move
      return false;
    }
    
    if (squares[row][column].piece != null) { //Check if the piece to be moved exists and if it is the right turn
      if (squares[row][column].piece.team == moveOrder[teamToMove].team) {
        return true;    
      }
    }
    return false;
  }
  
  
  /** 
   * approvesMove
   * Makes sure that the move the player wants to make is valid  
   * @param Square selected, the place that is selected
   * @param int targetRow, the row of the desired move
   * @param int targetColumn, the column of the desired move
   * @param boolean executionActive, a value that determines if it is even the time for the move or not
   * @return boolean true for if the move is approved, false for if the move is not
   */
  public boolean approvesMove(Square selected, int targetRow, int targetColumn, boolean executionActive) {
    if (gameWinner != null) { 
      return false;
    }
    
    if (squares[targetRow][targetColumn].piece != null) {
      return false;
    }
    
    int pastRow = selected.boardLocation.row; //Store original coordinates
    int pastColumn = selected.boardLocation.column;
    
    int deltaRow = (pastRow - targetRow); //Store the change in coordinate values
    int deltaColumn = (pastColumn - targetColumn);
    int deltaSum = deltaRow + deltaColumn;
    
    
    if ((deltaRow < 2) && (deltaRow > -2) && (deltaColumn < 2) && (deltaColumn > -2) && (deltaSum != 0)) { //Check to make sure hops are valid
      if (chainMoveInProgress) { //Make sure nothing is in the middle of a move
        return false;
      } else {
        return true;
      }
    }
    
    if ((deltaRow < 3) && (deltaRow > -3) && (deltaColumn < 3) && (deltaColumn > -3) && (deltaSum != 0) && (deltaSum != 3) && (deltaSum != -3)) {
      if (squares[pastRow - deltaRow/2][pastColumn  - deltaColumn/2].piece != null) { //Check to make sure jumps do not exceed too many spaces
        if (executionActive) {
          chainMoveInProgress = true;
        }
        return true;
      }
    }
    return false;
  }
  
  /** 
   * approvesMove
   * Makes sure that the move the player wants to make is valid  
   * @param MoveCode move, an object with start and end coordinates
   * @param boolean executionActive, a value determining whether a move is being made or not 
   * @return boolean true for if the move is approved, false for if the move is not
   */
  public boolean approvesMove(MoveCode move, boolean executionActive) {
    if (gameWinner != null) {
      return false;
    }
    
    int targetRow = move.targetPosition.row;
    int targetColumn = move.targetPosition.column; 
    
    if (squares[targetRow][targetColumn].piece != null) {
      return false;
    }
    
    if (!this.enemyTerritoryChecker(targetRow, targetColumn)) {
    	return false;
    }
    
    int pastRow = move.startPosition.row;
    int pastColumn = move.startPosition.column;  
    
    int deltaRow = (pastRow - targetRow);
    int deltaColumn = (pastColumn - targetColumn);
    int deltaSum = deltaRow + deltaColumn;
    
    if ((deltaRow < 2) && (deltaRow > -2) && (deltaColumn < 2) && (deltaColumn > -2) && (deltaSum != 0)) {
      if (chainMoveInProgress) { //Ensure valid hops
        return false;
      } else {
        return true;
      }
    }
    
    if ((deltaRow < 3) && (deltaRow > -3) && (deltaColumn < 3) && (deltaColumn > -3) && (deltaSum != 0) && (deltaSum != 3) && (deltaSum != -3)) {
      if (squares[pastRow - deltaRow/2][pastColumn  - deltaColumn/2].piece != null) { //Ensure valid hops
        if (executionActive) { 
          chainMoveInProgress = true;
        }
        return true;
      }
    }
    return false;
  }
  
  /** 
   * registerMove
   * Registers that a move has been made and sets up variables for following moves 
   */
  public void registerMove() {
    this.teamToMove++; //Sets up so next team can move
    this.numberOfMoves++; 
    chainMoveInProgress = false; //No more hops can be made, turn is over  
    
    if (this.teamToMove == 6) { //Resets first team
      this.teamToMove = 0;
    }
    
    if (moveOrder[teamToMove] == null ) {
      teamToMove = 0;
    }
  }
  
  /** 
   * determineGameResult
   * Determines the result of the game
   */
  public void determineGameResult() {
    gameWinner = moveOrder[teamToMove].team; //Winner is set as the last team that moved
    //Check to make sure there is actually a victory
    int checkRegion = moveOrder[teamToMove].targetRegion;  
    for (int j = 0; j < 10; j++) {
      ArrayCoordinate checkCoordinate = regions[checkRegion][j];
      if (squares[checkCoordinate.row][checkCoordinate.column].piece != moveOrder[teamToMove]) {
        gameWinner = null;
        j = 10;
      }
    }     
  }
  
  /** 
   * hasWon
   * Determines if the team has won or not
   */
  public boolean hasWon(int override) {  
    int checkRegion = moveOrder[override].targetRegion;  
    
    for (int j = 0; j < 10; j++) { //Make sure pieces are in a winnign formation
      ArrayCoordinate checkCoordinate = regions[checkRegion][j];
      if (squares[checkCoordinate.row][checkCoordinate.column].piece != moveOrder[teamToMove]) {
        return false;
      }
    } 
    return true;
  }
  
  /** 
   * returnCurrentMoveCode
   * Returns the team code that's up to move
   * @return teamToMove, an int representation of which team is up to move
   */
  public int returnCurrentMoveCode() {
    return this.teamToMove;
  }
  
  /** 
   * returnCurrentTeam
   * Returns the piece type of the current team
   * @return moveOrder[teamToMove], the PieceType of the team up to move
   */
  public PieceType returnCurrentTeam() {
    return this.moveOrder[teamToMove];
  }
  
  /** 
   * displayTeamToMove
   * Displays the details of the team's move
   * @param Graphics g
   */
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
  
  public boolean enemyTerritoryChecker(int targetRow, int targetColumn) {
      if ((targetRow > 11) && (targetRow) < 16 && (targetColumn < 4)) {
          return false;
      } else if (targetRow > 16 && targetRow < 21 && targetColumn > 12) {
          return false;
      } else if (targetRow == 12 && targetColumn > 8) {
          return false;
      } else if (targetRow == 13 && targetColumn > 9) {
          return false;
      } else if (targetRow == 14 && targetColumn > 10) {
          return false;
      } else if (targetRow == 15 && targetColumn > 11) {
          return false;
      } else if (targetRow == 17 && targetColumn < 5) {
          return false;
      } else if (targetRow == 18 && targetColumn < 6) {
          return false;
      } else if (targetRow == 19 && targetColumn < 7) {
          return false;
      } else if (targetRow == 20 && targetColumn < 8) {
          return false;
      }
      
      return true;
  }
}

