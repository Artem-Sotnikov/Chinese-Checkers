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
  public ArrayCoordinate[][] regions; //Stores locations of each team and their pieces
  private boolean chainMoveInProgress; //Keeps track of if the player is in the middle of consecutive hops
  public int teamToMove; //Organizes turns so teams make moves in order
  public PieceType[] moveOrder; //Keeps track of information for each team
  public Square[][] squares; //Creates the actual places on the game board
  public String gameWinner; //Stores the victorious team
  public int numberOfMoves; //Keeps track of the number of moves for that game
  
  /** 
   * Arbiter
   * Constructor for the class, creates a board and regions  
   * @param source, the game's board
   * @param srcRegions, the regions where each team will occupy
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
    
    gameWinner = null; //No winner yet
  } 
  
  /** 
   * isChainMoveInProgress
   * Used for moves with multiple hops; true if multiple jumps will happen, false if no more hopping can be done  
   * @return chainMoveInProgress a boolean value that determines whether or not multiple hops are happening 
   */
  public boolean isChainMoveInProgress() {
    return chainMoveInProgress;
  }
  
  /** 
   * approvesSelection
   * Makes sure that the piece the player wants to select is valid  
   * @param row, the row of the potential selection
   * @param column, the column of the potential selection
   * @return boolean true for if the piece can be selected, false for if it isn't a valid selection
   */
  public boolean approvesSelection(int row, int column) { 
    if (gameWinner != null) { //Checks to see if there is already a winner
      return false;
    }
    
    if (chainMoveInProgress) { //Checks if someone is still in the middle of making a move
      return false;
    }
    
    if (squares[row][column].piece != null) { //Check if the piece to be moved exists and if it is the right turn
      if (squares[row][column].piece.team == moveOrder[teamToMove].team) { //Check to ensure it is the right team's turn
        return true;    
      }
    }
    return false;
  }
  
  /** 
   * approvesMove
   * Makes sure that the move the player wants to make is valid  
   * @param selected, the place that is selected
   * @param targetRow, the row of the desired move
   * @param targetColumn, the column of the desired move
   * @param executionActive, a value that determines if it is even the time for the move or not
   * @return boolean true for if the move is approved, false for if the move is not
   */
  public boolean approvesMove(Square selected, int targetRow, int targetColumn, boolean executionActive) {
    if (gameWinner != null) { //If there is a game winner, no more moves are to be made
      return false;
    }
    
    if (squares[targetRow][targetColumn].piece != null) { //Make sure the place the piece wants to move to isn't already occupied
      return false;
    }
    
    //Store original coordinates
    int pastRow = selected.boardLocation.row;
    int pastColumn = selected.boardLocation.column;
    
    //Store the change in coordinate values, this info will be used for determining valid jumps
    int deltaRow = (pastRow - targetRow); 
    int deltaColumn = (pastColumn - targetColumn);
    int deltaSum = deltaRow + deltaColumn;
    
    //Check to make sure hops are valid by checking distances
    if ((deltaRow < 2) && (deltaRow > -2) && (deltaColumn < 2) && (deltaColumn > -2) && (deltaSum != 0)) { 
      if (chainMoveInProgress) { //Make sure nothing is in the middle of a move, else, hop distances are different
        return false;
      } else {
        return true;
      }
    }
    
    //Check to make sure hops are valid by checking distances
    if ((deltaRow < 3) && (deltaRow > -3) && (deltaColumn < 3) && (deltaColumn > -3) && (deltaSum != 0) && (deltaSum != 3) && (deltaSum != -3)) {
      if (squares[pastRow - deltaRow/2][pastColumn - deltaColumn/2].piece != null) { //Check to make sure jumps do not exceed too many spaces
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
   * @param move, an object with start and end coordinates
   * @param executionActive, a value determining whether a move is being made or not 
   * @return boolean true for if the move is approved, false for if the move is not
   */
  public boolean approvesMove(MoveCode move, boolean executionActive) {
    if (gameWinner != null) { //If a team has already won no more moves can be made
      return false;
    }
    
    //Store the targeted move positions for later use
    int targetRow = move.targetPosition.row;
    int targetColumn = move.targetPosition.column; 
    
    //Check that the targeted move isn't already occupied
    if (squares[targetRow][targetColumn].piece != null) {
      return false;
    }
    
    //Check that piece doesn't move to enemy territory to aviod errors from server
    if (!this.enemyTerritoryChecker(targetRow, targetColumn)) {
      return false;
    }
    
    //Store the location of the original position
    int pastRow = move.startPosition.row;
    int pastColumn = move.startPosition.column;  
    
    //Store the hop distances 
    int deltaRow = (pastRow - targetRow);
    int deltaColumn = (pastColumn - targetColumn);
    int deltaSum = deltaRow + deltaColumn;
    
    //Check to make sure hops are valid by checking distances
    if ((deltaRow < 2) && (deltaRow > -2) && (deltaColumn < 2) && (deltaColumn > -2) && (deltaSum != 0)) {
      if (chainMoveInProgress) { //Ensure valid hops
        return false;
      } else {
        return true;
      }
    }
    
    //Check to make sure hops are valid by checking distances
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
    this.numberOfMoves++; //Increases number of moves
    chainMoveInProgress = false; //No more hops can be made, turn is over  
    
    //Resets to first team after all teams have gone
    if (this.teamToMove == 6) { 
      this.teamToMove = 0;
    }
    
    //Lets the first team move
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
    int checkRegion = moveOrder[teamToMove].targetRegion; //Checks the targeted win position of the team 
    for (int j = 0; j < 10; j++) {
      ArrayCoordinate checkCoordinate = regions[checkRegion][j]; //Store the locations of the region to be checked
      if (squares[checkCoordinate.row][checkCoordinate.column].piece != moveOrder[teamToMove]) { //Makes sure the target win region matches all the pieces
        gameWinner = null;
        j = 10; //Exit loop immediately
      }
    }     
  }
  
  /** 
   * hasWon
   * Determines if the team has won or not
   */
  public boolean hasWon(int override) {  
    int checkRegion = moveOrder[override].targetRegion; //Store the target win region into a checking variable
    
    for (int j = 0; j < 10; j++) { //Make sure pieces are in a winning formation
      ArrayCoordinate checkCoordinate = regions[checkRegion][j];
      if (squares[checkCoordinate.row][checkCoordinate.column].piece != moveOrder[teamToMove]) { //If the pieces do not match, the team has not won
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
   * @param g, the Graphics component
   */
  public void displayTeamToMove(Graphics g) {
    //Draw a rectangle for a display panel
    g.setColor(Color.BLACK);
    g.fillRect(300 - 150,150,300,100);
    g.setColor(moveOrder[teamToMove].color);
    g.drawRect(310 - 150, 160, 280, 80);
    //Draw game information onto the display panel
    if (gameWinner == null) {
      g.drawString("Team to move is: " + moveOrder[teamToMove].team, 310 - 150 + 40, 200);
    } else {
      g.drawString("Game Terminated.", 310 - 150 + 40, 200);
      g.drawString("Team " + gameWinner + " is victorious", 310 - 150 + 40, 200 + 25);
    }
  }
  
  /** 
   * enemyTerritoryChecker
   * Checks to see if the piece will land in enemy territoy
   * @param targetRow, the row value of the position the piece wants to move to
   * @param targetColumn, the column value of the position the piece wants to move to
   */
  public boolean enemyTerritoryChecker(int targetRow, int targetColumn) {
    //Check all regions
    if ((targetRow > 11) && (targetRow < 16) && (targetColumn < 4)) {
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