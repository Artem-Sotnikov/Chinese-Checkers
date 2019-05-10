/* PieceManager.java
 * Purpose: Manages and determines possible moves of the teams
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

// Imports

import java.util.ArrayList;

public class PieceManager {
  
  // Class variables
  public Square[][] piecePositionStorage;
  public Square[][] overallBoard;
  public ArrayCoordinate[][] regions;
  private static final PieceType buffer = new PieceType(null, null, 0, 0);
  private ArrayList<ArrayCoordinate> bufferCoords;
  ArrayCoordinate[] deltaOpts = new ArrayCoordinate[6];
  private boolean jumpEnable;
  
  /**
   * PieceManager
   * Constructor that sets up initial information
   * @param source, an array of the original data
   */
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
    
    jumpEnable = true;
  }
  
  /**
   * returnAllTeamMoves
   * Returns all the possible moves for a team
   * @param teamCode, the team number
   * @return MoveNode[], a node with links to possibilities
   */
  public MoveNode[] returnAllTeamMoves(int teamCode) {
    MoveNode[] returnNodes = new MoveNode[10];
    
    for (int idx = 0; idx < 10; idx++) {
      returnNodes[idx] = generateMoves(piecePositionStorage[teamCode][idx].boardLocation, true, teamCode);
      clearBuffer();
    }
    return returnNodes;
  }
  
  
  /**
   * returnAllMoveCodes
   * Return all the possible move codes
   * @param teamCode, the team number
   * @return ArrayList<MoveCode>, an ArrayList with all possible moves
   */
  public ArrayList<MoveCode> returnAllMoveCodes(int teamCode) {
    MoveNode[] returnNodes = this.returnAllTeamMoves(teamCode);
    ArrayList<MoveCode> returnList = new ArrayList<MoveCode>();
    
    for (int idx = 0; idx < 10; idx++) {
      returnList.addAll(returnNodes[idx].toMoveCodes());
    }
    return returnList;
  }
  
  /**
   * returnAllMoveCodes
   * Return all the possible move codes
   * @param teamCode, the team number
   * @param updateOverload, temporary boolean to separate from the other returnAllMoveCodes method
   * @return ArrayList<MoveCode>, an ArrayList with all possible moves
   */
  public ArrayList<MoveCode> returnAllMoveCodes(int teamCode, boolean updateOverload) {
    MoveNode[] returnNodes = this.returnAllTeamMoves(teamCode);
    ArrayList<MoveCode> returnList = new ArrayList<MoveCode>();
    
    for (int idx = 0; idx < 10; idx++) {
      returnList.addAll(returnNodes[idx].toMoveCodes(true));
    }
    return returnList;
  }
  
  /**
   * generateMoves
   * Create the possible move for a piece
   * @param position, the original coordinate/position
   * @param isFirstMove, true for if it is the first move, false for if it is not4
   * @param subjectCode, the team number
   * @return MoveNode, a MoveNode with instructions on how to move
   */
  public MoveNode generateMoves(ArrayCoordinate position, boolean isFirstMove, int subjectCode) {
    // Set the original position of the piece
    int sRow = position.row;
    int sCol = position.column;
    
    // Create a new MoveCode
    MoveNode rootPosition = new MoveNode(position);
    
    // Check if this is the first move of the game
    if (isFirstMove) {
      rootPosition.isRoot = true;
    }
    
    // Check if the board contains the piece to move
    if (overallBoard[position.row][position.column].piece == null) {
      overallBoard[position.row][position.column].piece = buffer;
      bufferCoords.add(position);
    }
    
    // Calculate best move for if it is the first move of the game
    if (isFirstMove) {
      
      // Check for all possible moves
      for (int i = sRow - 1; ((i < sRow + 1) && (i >= 0) && (i < 25)); i++) {
        for (int j = sCol - 1; ((j < sCol + 1) && (j >= 0) && (j < 25)); j++) {
          if (overallBoard[i][j] != null) {
            if (overallBoard[i][j].piece == null) {
              if (!violatesEnemyTerritory(i, j, subjectCode)) { // Check if the move ends in enemy territory
                rootPosition.branches.add(new MoveNode(i, j));
              }
            }
          }
        }
      }
      
      // Check for all possible moves
      for (int i = sCol; ((i < sCol + 2) && (i >= 0) && (i < 25)); i++) {
        if ((sRow + 1 < 25) && (sRow + 1 >= 0)) {
          if (overallBoard[sRow + 1][i] != null) {
            if (overallBoard[sRow + 1][i].piece == null) {
              if (!violatesEnemyTerritory(sRow + 1, i, subjectCode)) { // Check if the move ends in enemy territory
                rootPosition.branches.add(new MoveNode(sRow + 1, i));
              }
            }
          }
        }
      }
      
      // Check for all possible moves
      if ((sCol + 1 >= 0) && (sCol + 1 < 25)) {
        if ((overallBoard[sRow][sCol + 1] != null) && (overallBoard[sRow][sCol + 1].piece == null)) {
          if (!violatesEnemyTerritory(sRow, sCol + 1, subjectCode)) { // Check if the move ends in enemy territory
            rootPosition.branches.add(new MoveNode(sRow, sCol + 1));
          }
        }
      }
    }
    
    // Check for any jumps that can be made
    if (jumpEnable) {
      for (int idx = 0; idx < 6; idx++) {
        if ((sRow + deltaOpts[idx].row >= 0) && (sCol + deltaOpts[idx].column >= 0) && (sRow + deltaOpts[idx].row < 25) && (sCol + deltaOpts[idx].column < 25)) {
          if ((overallBoard[sRow + deltaOpts[idx].row][sCol + deltaOpts[idx].column] != null) &&
              (overallBoard[sRow + (deltaOpts[idx].row) / 2][sCol + (deltaOpts[idx].column) / 2] != null)) {
            if ((overallBoard[sRow + deltaOpts[idx].row][sCol + deltaOpts[idx].column].piece == null) &&
                (overallBoard[sRow + (deltaOpts[idx].row) / 2][sCol + (deltaOpts[idx].column) / 2].piece != null)) {
              if (overallBoard[sRow + (deltaOpts[idx].row) / 2][sCol + (deltaOpts[idx].column) / 2].piece.team != null) {
                // Check if the move ends in enemy territory
                if (!violatesEnemyTerritory(sRow + deltaOpts[idx].row, sCol + deltaOpts[idx].column, subjectCode)) {
                  rootPosition.branches.add(generateMoves(new ArrayCoordinate(
                                                                              sRow + deltaOpts[idx].row, sCol + deltaOpts[idx].column), false, subjectCode));
                }
              }
            }
          }
        }
      }
    }
    return rootPosition;
  }
  
  /**
   * clearBuffer
   * Clears the buffer
   */
  private void clearBuffer() {
    for (int i = 0; i < bufferCoords.size(); i++) {
      overallBoard[bufferCoords.get(i).row][bufferCoords.get(i).column].piece = null;
    }
    bufferCoords.clear();
  }
  
  /**
   * violatesEnemyTerritory
   * @param targetRow, the row of the position the piece may want to move to
   * @param targetColumn, the column of the position the piece may want to move to
   * @param teamCode, the team to check
   * @return boolean, true for if enemy territoy is violated, false for if not
   */
  public boolean violatesEnemyTerritory(int targetRow, int targetColumn, int teamCode) {
    ArrayCoordinate sample = new ArrayCoordinate(targetRow, targetColumn);
    int targetRegion = teamCode + 3;
    if (targetRegion > 5) {
      targetRegion = targetRegion - 6;
    }
    
    for (int team = 0; team < 6; team++) {
      if (team == teamCode || team == targetRegion) {
        team++;
      }
      
      if (team < 6) {
        // Check if the final position of the piece is in enemy territory
        for (int idx = 0; idx < 10; idx++) {
          if ((regions[team][idx].row == targetRow) && (regions[team][idx].column == targetColumn)) {
            return true;
          }
        }
      }
    }
    
    return false;
  }
}
