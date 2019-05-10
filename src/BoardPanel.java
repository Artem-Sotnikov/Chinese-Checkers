/* BoardPanel.java
 * Purpose: Manages the game board; sets up the regions and pieces, evaluates and executes the best moves
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

//Imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class BoardPanel extends JPanel{
  //Variables
  private Square[][] squares; 
  private ArrayCoordinate[][] regions; 
  private ArrayList<ArrayCoordinate> notedSquares;
  private GameState gameState;
  private ArrayCoordinate selectedCoordinates;
  private PieceManager manager;
  private EvaluationEngine engine;
  
  public CustomMouseListener listener;
  public Arbiter arbiter;
  public double currentEvaluation;
  public int currentHoverRow, currentHoverColumn; 
  public double bestBranchEval; 
  public boolean gameFinished;
  public String gameWinner; 
  
  /** 
   * BoardPanel
   * Constructor that sets up the game board
   */
  BoardPanel() {
    //Set up board with certain preferences
    Dimension boardSize = new Dimension ((int) (925*Constants.scaleFactor),Toolkit.getDefaultToolkit().getScreenSize().height);
    this.setPreferredSize(boardSize);
    this.setOpaque(false);     
    
    this.squares = new Square[25][25]; //Generates 2D array that will store the board and the spaces
    
    //Set up a mouse listener for user
    listener = new CustomMouseListener();
    this.addMouseMotionListener(listener);
    this.addMouseListener(listener);    
    
    //Squares where a move can possibly be made
    this.notedSquares = new ArrayList<ArrayCoordinate>(0);
    
    regions = new ArrayCoordinate[6][10]; //6 regions, with 10 pieces each
    
    //Prepare some initial setup
    this.arbiter = new Arbiter(squares,regions);
    this.manager = new PieceManager(squares); 
    this.engine = new EvaluationEngine(arbiter);
    
    //Assign regions for each team to occupy
    //Team 1
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
    //Team 2
    regions[1][0] = new ArrayCoordinate(12,12);  
    regions[1][1] = new ArrayCoordinate(12,10);
    regions[1][2] = new ArrayCoordinate(12,11);
    regions[1][3] = new ArrayCoordinate(12,9);
    regions[1][4] = new ArrayCoordinate(13,10);
    regions[1][5] = new ArrayCoordinate(13,11);
    regions[1][6] = new ArrayCoordinate(13,12);
    regions[1][7] = new ArrayCoordinate(14,11);
    regions[1][8] = new ArrayCoordinate(14,12);
    regions[1][9] = new ArrayCoordinate(15,12);
    //Team 3
    regions[2][0] = new ArrayCoordinate(20,16);  
    regions[2][1] = new ArrayCoordinate(18,13);
    regions[2][2] = new ArrayCoordinate(18,14);
    regions[2][3] = new ArrayCoordinate(19,13);
    regions[2][4] = new ArrayCoordinate(19,14);
    regions[2][5] = new ArrayCoordinate(19,15);
    regions[2][6] = new ArrayCoordinate(20,13);
    regions[2][7] = new ArrayCoordinate(20,14);
    regions[2][8] = new ArrayCoordinate(20,15);
    regions[2][9] = new ArrayCoordinate(17,13);
    //Team 4
    regions[3][0] = new ArrayCoordinate(24,12);
    regions[3][1] = new ArrayCoordinate(21,10);
    regions[3][2] = new ArrayCoordinate(21,11);
    regions[3][3] = new ArrayCoordinate(21,12);
    regions[3][4] = new ArrayCoordinate(22,10);
    regions[3][5] = new ArrayCoordinate(22,11);
    regions[3][6] = new ArrayCoordinate(22,12);
    regions[3][7] = new ArrayCoordinate(23,11);
    regions[3][8] = new ArrayCoordinate(23,12);
    regions[3][9] = new ArrayCoordinate(21,9);
    //Team 5
    regions[4][0] = new ArrayCoordinate(20,4);
    regions[4][1] = new ArrayCoordinate(18,4);
    regions[4][2] = new ArrayCoordinate(18,5);
    regions[4][3] = new ArrayCoordinate(19,4);
    regions[4][4] = new ArrayCoordinate(19,5);
    regions[4][5] = new ArrayCoordinate(19,6);
    regions[4][6] = new ArrayCoordinate(17,4);
    regions[4][7] = new ArrayCoordinate(20,5);
    regions[4][8] = new ArrayCoordinate(20,6);
    regions[4][9] = new ArrayCoordinate(20,7);
    //Team 6
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
    
    //Set the manager to store the newly created regions
    this.manager.regions = this.regions;
  }
  
  /** 
   * terminateMove
   * Informs that a move has ended, no more hops are to be made
   */
  public void terminateMove() {
    //Unselect a move to terminate the move
    if (selectedCoordinates != null) {
      squares[selectedCoordinates.row][selectedCoordinates.column].setSelected(false);
    }
    
    //Set to idle to signify get rid of selection and move on
    gameState = GameState.STATE_IDLE; 
    arbiter.determineGameResult();
    
    //Determine end of game
    if (arbiter.gameWinner != null) { 
      gameFinished = true;
    } else {
      arbiter.registerMove(); //Otherwise, register the move as regular and proceed
    }
    
    //Remove notes on previously noted squares to end move
    for (int idx = 0; idx < notedSquares.size(); idx++) {
      ArrayCoordinate notedPosition = notedSquares.get(idx);
      squares[notedPosition.row][notedPosition.column].setNoted(false);
    }
  }
  
  /** 
   * handleHovers
   * Controls what to do as mouse hovers over something 
   * @param i, tbe row coordinate of piece being hovered over by
   * @param j, the column coordinate of the piece being hovered over
   * @param g, the Graphics component
   */
  public void handleHovers(int i, int j, Graphics g) {
    if (squares[i][j].containsCoordinates(listener.getRectifiedPos())) {   
      this.currentHoverRow = i;
      this.currentHoverColumn = j;
      
      switch (gameState) {
        case STATE_IDLE: { //When idle, check and show hovering
          if (squares[i][j].containsCoordinates(listener.getRectifiedPos()) && (arbiter.approvesSelection(i, j))) {
            squares[i][j].setHovered(true);          
          } else {
            squares[i][j].setHovered(false);
          }
        }
        break;
        case STATE_PIECE_SELECTED: { //In a selected state, only show hovering for valid moves
          if ((squares[i][j].containsCoordinates(listener.getRectifiedPos())) && (arbiter.approvesMove(new MoveCode(selectedCoordinates, new ArrayCoordinate(i,j)), false))) {
            squares[i][j].setHovered(true);  
          } else {
            squares[i][j].setHovered(false);
          } 
        }
        break;         
      }
    } else {
      squares[i][j].setHovered(false);
    } 
  }
  
  /** 
   * handleClicks
   * Controls what to do as mouse clicks something 
   * @param i, tbe row coordinate of piece being clicked
   * @param j, the column coordinate of the piece being clicked
   */
  public void handleClicks(int i, int j) {
    if (squares[i][j].containsCoordinates(listener.getRectifiedClick())) {
      switch (gameState) {
        case STATE_IDLE: {
          if (arbiter.approvesSelection(i, j)) { //If the move is valid, proceed
            listener.clickHandled();
            squares[i][j].setSelected(true);  
            selectedCoordinates = new ArrayCoordinate(i,j);
            gameState = GameState.STATE_PIECE_SELECTED; //A piece has now been selected
          }  
        }
        break;
        case STATE_PIECE_SELECTED: { //If a piece has already been selected and user performs a click, then proceed
          if (arbiter.approvesMove(new MoveCode(selectedCoordinates, new ArrayCoordinate(i,j)), true)) { //Check for approval
            listener.clickHandled();
            
            MoveCode move = new MoveCode(selectedCoordinates,new ArrayCoordinate(i,j));
            movePiece(move); //Make a move
            
            //Check to make sure move with consecutive hops is not in progress
            if (!arbiter.isChainMoveInProgress()) { 
              this.terminateMove();
            } 
            
          } else if (arbiter.approvesSelection(i, j)) {          
            listener.clickHandled();
            //Deselect the square to move on
            squares[selectedCoordinates.row][selectedCoordinates.column].setSelected(false);
            //New location is set as the selected
            squares[i][j].setSelected(true);
            selectedCoordinates = new ArrayCoordinate(i,j);
          }
        }
        break;
      }
    }
  }
  
  /** 
   * paintComponent
   * Paints graphics for the panel
   * @param g, the Graphics component
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setDoubleBuffered(true);
    
    Graphics2D g2 = (Graphics2D) g;       
    g2.scale(Constants.scaleFactor, Constants.scaleFactor); //Scale the size of the frame
    
    //Set design characteristics
    g.setColor(Color.black);
    g.setFont(new Font("TrilliumWeb",Font.PLAIN, 20));
    
    //Display progression of game
    arbiter.displayTeamToMove(g);  
    updateEvaluation(g);     
    
    //Check for clicks and hovers and paint accordingly
    for (int i = 0; i < squares.length; i++) {
      for (int j = 0; j <= i; j++) {
        if (squares[i][j] != null) {         
          this.handleHovers(i,j,g); //Hovered over squares have an extra rim painted
          if (listener.clickPending()) {
            this.handleClicks(i,j); //Clicked squares have a small circle inside
          }   
          squares[i][j].draw(g);    
        }       
      }
    }     
  }
  
  /** 
   * configureSeverSetup
   * Set up the initial values and state of our team as soon as we join server
   */
  public void configureServerSetup() {
    this.configureInitialSetup(); //Call configuration set up
    arbiter.moveOrder = new PieceType[6]; //Always six player
    arbiter.moveOrder[0] = Constants.teamZeroPiece;
  }
  
  /** 
   * cleanseBoard
   * Resets up the board
   */
  public void cleanseBoard() {
    //Set up and declare values to be used later
    this.squares = new Square[25][25];
    this.manager.overallBoard = this.squares;
    this.arbiter.squares = this.squares;
    
    //Clear the board by placing new empty squares
    for (int i = 7; i < (7 + 14); i++) {
      for (int j = 0; j < (i - 7); j++) {
        squares[i][j + 4] = new Square(i,j + 4);
      }
    }
    
    //Clear the board by placing new empty squares
    for (int i = 12; i < 25; i++) {
      for (int j = 0; j < (25 - i); j++) {
        squares[i][j + (i - 12)] = new Square(i,j + (i - 12));
      }
    }
  }
  
  /** 
   * setUpBoard
   * Set up all the regions and team's pieces to corresponding places
   * @param existingPieces, an array of coordinates keeping track of all the pieces on the board
   */
  public void setUpBoard(ArrayCoordinate[] existingPieces) {
    for (int i = 0; i < existingPieces.length; i++) {
      System.out.println(existingPieces[i].row +","+existingPieces[i].column);
      try {
        //Set up for all six teams
        if (i < 10) { // Create the user's team and add the manager to manage the pieces
          //squares[existingPieces[i].row][existingPieces[i].column] = new Square(existingPieces[i].row , existingPieces[i].column);
          squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamZeroPiece);
          
          manager.piecePositionStorage[0][i] = squares[existingPieces[i].row][existingPieces[i].column];
          System.out.println("placed:");
          existingPieces[i].displayCoordinate();
          
        } else if (i > 9 && i < 20) { // Add the second team
          
          squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamThreePiece);
          manager.piecePositionStorage[3][i-10] = squares[existingPieces[i].row][existingPieces[i].column];
          System.out.println("placed:");
          existingPieces[i].displayCoordinate();
          
        } else if (i > 19 && i < 30) { // Add the third team
          squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamTwoPiece);
          manager.piecePositionStorage[2][i-20] = squares[existingPieces[i].row][existingPieces[i].column];
        } else if (i > 29 && i < 40) { // Add the fourth team
          squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamThreePiece);
          manager.piecePositionStorage[3][i-30] = squares[existingPieces[i].row][existingPieces[i].column];
        } else if (i > 39 && i < 50) { // Add the fifth team
          squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamFourPiece);
          manager.piecePositionStorage[4][i-40] = squares[existingPieces[i].row][existingPieces[i].column];
        } else if (i > 49 && i < 60) { // Add the sixth team
          squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamFivePiece);
          manager.piecePositionStorage[5][i-50] = squares[existingPieces[i].row][existingPieces[i].column];
        }
      } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    }
  }
  
  /** 
   * configureEndScenario
   * Runs actions that have to happen at the end of a game
   */
  public void configureEndScenario() {
    //Clear squares
    for (int i = 7; i < (7 + 14); i++) {
      for (int j = 0; j < (i - 7); j++) {
        squares[i][j + 4] = new Square(i,j + 4);
      }
    }
    for (int i = 12; i < 25; i++) {
      for (int j = 0; j < (25 - i); j++) {
        squares[i][j + (i - 12)] = new Square(i,j + (i - 12));
      }
    } 
    
    arbiter.moveOrder = new PieceType[6];
    arbiter.moveOrder[0] = Constants.teamZeroPiece;
    arbiter.teamToMove = 0;
    manager.piecePositionStorage = new Square[6][10];
    
    for (int i = 0; i < regions[0].length - 1; i++) {
      squares[regions[3][i].row][regions[3][i].column].placePiece(Constants.teamZeroPiece);
      manager.piecePositionStorage[0][i] = squares[regions[3][i].row][regions[3][i].column];
    }  
    
    squares[20][10].placePiece(Constants.teamZeroPiece);
    
    manager.piecePositionStorage[0][9] = squares[20][10]; 
  }
  
  
  /** 
   * configureBottomPosition
   * When it gets to the bottom of the board, set it to team 3's turn
   */
  public void configureBottomPosition() {
    arbiter.teamToMove = 3;
  }
  
  /** 
   * configureServerScenario
   * Runs actions that have to happen when communicating with server
   */
  public void configureServerScenario() {
    for (int i = 7; i < (7 + 14); i++) {
      for (int j = 0; j < (i - 7); j++) {
        squares[i][j + 4] = new Square(i,j + 4);
      }
    }
    
    for (int i = 12; i < 25; i++) {
      for (int j = 0; j < (25 - i); j++) {
        squares[i][j + (i - 12)] = new Square(i,j + (i - 12));
      }
    } 
    
    arbiter.moveOrder = new PieceType[6];
    arbiter.moveOrder[0] = Constants.teamZeroPiece;
    arbiter.moveOrder[1] = Constants.teamTwoPiece;
    arbiter.teamToMove = 0;
    manager.piecePositionStorage = new Square[6][10];
    
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[0][i].row][regions[0][i].column].placePiece(Constants.teamZeroPiece);
      manager.piecePositionStorage[0][i] = squares[regions[0][i].row][regions[0][i].column];
      
      squares[regions[3][i].row][regions[3][i].column].placePiece(Constants.teamZeroPiece);
      manager.piecePositionStorage[3][i] = squares[regions[3][i].row][regions[3][i].column];
    }                   
  }
  
  
  /** 
   * configureInitialSetup
   * Set up all the initial team and pieces onto the board
   */
  public void configureInitialSetup() {
    
    for (int i = 7; i < (7 + 14); i++) {
      for (int j = 0; j < (i - 7); j++) {
        squares[i][j + 4] = new Square(i,j + 4);
      }
    }
    
    for (int i = 12; i < 25; i++) {
      for (int j = 0; j < (25 - i); j++) {
        squares[i][j + (i - 12)] = new Square(i,j + (i - 12));
      }
    }  
    
    /*
     for (int i = 0; i < newSquares.length; i++) {
     for (int j = 0; j <= i; j++)      {
     newSquares[i][j] = new Square(i,j);
     }
     }
     */      
    //Set up team 1's pieces
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[0][i].row][regions[0][i].column].placePiece(Constants.teamZeroPiece);
      manager.piecePositionStorage[0][i] = squares[regions[0][i].row][regions[0][i].column];
    }    
    //Set up team 2's pieces
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[1][i].row][regions[1][i].column].placePiece(Constants.teamOnePiece);
      manager.piecePositionStorage[1][i] = squares[regions[1][i].row][regions[1][i].column];
    }
    //Set up team 3's pieces
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[2][i].row][regions[2][i].column].placePiece(Constants.teamTwoPiece);
      manager.piecePositionStorage[2][i] = squares[regions[2][i].row][regions[2][i].column];
    }
    //Set up team 4's pieces
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[3][i].row][regions[3][i].column].placePiece(Constants.teamThreePiece);
      manager.piecePositionStorage[3][i] = squares[regions[3][i].row][regions[3][i].column];
    }
    //Set up team 5's pieces
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[4][i].row][regions[4][i].column].placePiece(Constants.teamFourPiece);
      manager.piecePositionStorage[4][i] = squares[regions[4][i].row][regions[4][i].column];
    }
    //Set up team 6's pieces
    for (int i = 0; i < regions[0].length; i++) {
      squares[regions[5][i].row][regions[5][i].column].placePiece(Constants.teamFivePiece);
      manager.piecePositionStorage[5][i] = squares[regions[5][i].row][regions[5][i].column];
    }  
    
    gameState = GameState.STATE_IDLE; //Initially, no pieces can be selected so it is set to idle
  }
  
  /** 
   * squareOccupied
   * Determines whether or not square is occupied
   * @param ArrayCoordinate target, the place to be checked for occupancy
   * @return boolean; true for occupied, false for empty
   */
  public boolean squareOccupied(ArrayCoordinate target) {
    return (squares[target.row][target.column].piece != null);
  }
  
  /** 
   * movePiece
   * Moves a piece
   * @param move, instructions for how to move
   * @return boolean, true for if piece has been moved
   */
  public boolean movePiece(MoveCode move) {    
    int startRow = move.startPosition.row;
    int startCol = move.startPosition.column;
    int targetRow = move.targetPosition.row;
    int targetCol = move.targetPosition.column;
    
    Square selected = squares[startRow][startCol];
    
    //gameState = GameState.STATE_IDLE;
    //this.configureBottomPosition();
    
    //Update locations
    selected.updateLocation(new ArrayCoordinate(targetRow, targetCol));
    
    //Set move destination to be selected
    squares[targetRow][targetCol] = selected;
    //Keep track/the original position
    squares[startRow][startCol] = new Square(startRow,startCol);
    //Store new information
    selectedCoordinates = new ArrayCoordinate(targetRow, targetCol);
    move.startPosition = new ArrayCoordinate(startRow,startCol);
    
    return true;              
  }
  
  /** 
   * displayPossibleMoves
   * Displays all the possible places to move
   */
  public void displayPossibleMoves() {
    //An array of possible moves is made
    MoveNode[] moves = manager.returnAllTeamMoves(arbiter.returnCurrentMoveCode()); 
    //Iterate through all possible moves
    for (int idx = 0; idx < moves.length; idx++) {
      ArrayList<ArrayCoordinate> possibilityArray =  moves[idx].returnAsArray();
      for (int idx2 = 0; idx2 < possibilityArray.size(); idx2++) {
        //Find and store possible moves
        ArrayCoordinate notedPosition = possibilityArray.get(idx2);
        squares[notedPosition.row][notedPosition.column].setNoted(true);
        notedSquares.add(notedPosition);
      }
    }
  }
  
  /** 
   * executeRandomMove
   * Random move is made for a player when called
   * @return randomMove, a random move
   */
  public MoveCode executeRandomMove() {
    //Look at possible move paths and choose a random one
    ArrayList<MoveCode> possibleMoves = manager.returnAllMoveCodes(arbiter.returnCurrentMoveCode(),true);
    MoveCode randomMove = possibleMoves.get((int) (Math.random()*possibleMoves.size())); 
    movePiece(randomMove);
    terminateMove();
    
    return randomMove;
  }
  
  /** 
   * updateEvaluation
   * Updates predictions for next best move based on moves that have been made
   * @param g, the Graphics component
   */
  public void updateEvaluation(Graphics g) {
    double eval = 0;
    PieceType currentTeam = arbiter.returnCurrentTeam(); //Find current team up to move
    //Find a numerical evaluation for the move
    eval = engine.evaluateComplex(manager.piecePositionStorage[currentTeam.teamCode], regions[currentTeam.targetRegion][0], currentTeam.teamCode);
    Double.toString(eval);
    currentEvaluation = eval;         
  }
  
  /** 
   * executeByEval
   * A best possible move is determined and executed by the algorithm's evaluation
   * @return a double number that represents the best possible move
   */
  public MoveCode executeByEval() {
    ArrayList<MoveCode> possibleMoves = manager.returnAllMoveCodes(arbiter.returnCurrentMoveCode());   
    MoveCode tempMove;
    MoveCode reverse;
    double evaluations[] = new double[possibleMoves.size()];
    double eval;
    PieceType currentTeam = arbiter.returnCurrentTeam();
    
    for (int idx = 0; idx < possibleMoves.size(); idx++) {
      tempMove = possibleMoves.get(idx);
      //Store the reverse move
      reverse = new MoveCode(tempMove.targetPosition.row,tempMove.targetPosition.column,
                             tempMove.startPosition.row,tempMove.startPosition.column);
      //Move the piece with the temporary move, and see how the evaluation is
      movePiece(tempMove);
      //Find the move's evaluation
      eval = engine.evaluateComplex(manager.piecePositionStorage[currentTeam.teamCode],
                                    regions[currentTeam.targetRegion][0], currentTeam.teamCode);
      arbiter.determineGameResult(); //Find how the move affects the game
      if (arbiter.gameWinner != null) {
        eval = 0;
        arbiter.gameWinner = null;
      }
      evaluations[idx] = eval;      
      
      movePiece(reverse); //Undo the move
    }
    
    int highest = 0;
    //Find the most optimal move
    for (int idx = 1; idx < evaluations.length; idx++) {
      if (evaluations[idx] > evaluations[highest]) {
        highest = idx;
      }
    }
    
    MoveCode isolated = new MoveCode(
                                     possibleMoves.get(highest).startPosition.row,
                                     possibleMoves.get(highest).startPosition.column,
                                     possibleMoves.get(highest).targetPosition.row,
                                     possibleMoves.get(highest).targetPosition.column
                                    );
    //Actually move the most optimal move
    movePiece(possibleMoves.get(highest));
    terminateMove();
    
    System.out.print("Start position of isolated:");
    isolated.startPosition.displayCoordinate();
    System.out.print("End position of isolated:");
    isolated.targetPosition.displayCoordinate();
    
    return isolated;
    //return evaluations[highest];     
  }        
  
  /** 
   * executeByDepth
   * A best possible move is determined and executed by the algorithm's evaluation with depth
   * @return MoveCode, a best possible move
   */
  public MoveCode executeByDepth() {
    ArrayList<MoveCode> possibleMoves = manager.returnAllMoveCodes(arbiter.returnCurrentMoveCode(),true);
    //Set up a potential move and its reverse move
    MoveCode tempMove;
    MoveCode reverse;
    
    //Store evaluations for each move
    double evaluations[] = new double[possibleMoves.size()];
    double eval;
    
    //Determine current team
    PieceType currentTeam = arbiter.returnCurrentTeam();
    
    //Iterate 
    for (int idx = 0; idx < possibleMoves.size(); idx++) {
      //Set up the potential move and its reverse
      tempMove = possibleMoves.get(idx);
      reverse = new MoveCode(tempMove.targetPosition.row,tempMove.targetPosition.column,
                             tempMove.startPosition.row,tempMove.startPosition.column);
      movePiece(tempMove); //Try the temp move 
      if (arbiter.hasWon(arbiter.returnCurrentMoveCode())) {
        eval = 100;
      } else {
        eval = depthEval(2); //Find and evaluate to the next depth
      }          
      evaluations[idx] = eval;
      movePiece(reverse); //Undo the test move
    }
    
    int highest = 0;
    //Find the most optimal move
    for (int idx = 1; idx < evaluations.length; idx++) {
      if (evaluations[idx] > evaluations[highest]) {
        highest = idx;
      }
    }
    
    //Display coordinates
    possibleMoves.get(highest).startPosition.displayCoordinate();
    possibleMoves.get(highest).targetPosition.displayCoordinate();
    
    MoveCode isolated = new MoveCode(
                                     possibleMoves.get(highest).startPosition.row,
                                     possibleMoves.get(highest).startPosition.column,
                                     possibleMoves.get(highest).targetPosition.row,
                                     possibleMoves.get(highest).targetPosition.column
                                    );
    
    isolated.stringPath = possibleMoves.get(highest).stringPath;
    
    //Use the highest evaluation
    possibleMoves.get(highest).startPosition.displayCoordinate();
    possibleMoves.get(highest).targetPosition.displayCoordinate();
    this.bestBranchEval = evaluations[highest];
    movePiece(possibleMoves.get(highest));
    terminateMove();
    
    System.out.print("Start position of isolated:");
    isolated.startPosition.displayCoordinate();
    System.out.print("End position of isolated:");
    isolated.targetPosition.displayCoordinate();
    
    return isolated;
  }
  
  /** 
   * depthEval
   * Checks multiple depths and comes up with an evaluation for a best move
   * @param depth, the depth to evaluate to
   * @return double, an evaluation 
   */
  
  private double depthEval(int depth) {
    ArrayList<MoveCode> possibleMoves = manager.returnAllMoveCodes(arbiter.returnCurrentMoveCode());
    //Set up a potential move and its reverse
    MoveCode tempMove;
    MoveCode reverse;
    //Find all numerical evaluations
    double evaluations[] = new double[possibleMoves.size()];
    double eval;
    //Dtermine the team up to move
    PieceType currentTeam = arbiter.returnCurrentTeam();
    
    //Iterate through all possible moves
    for (int idx = 0; idx < possibleMoves.size(); idx++) {
      //Set the potential move and its reverse move
      tempMove = possibleMoves.get(idx);
      reverse = new MoveCode(tempMove.targetPosition.row,tempMove.targetPosition.column,
                             tempMove.startPosition.row,tempMove.startPosition.column);
      movePiece(tempMove); //Temporarily try the move
      if (depth == 1) {
        eval = engine.evaluateComplex(manager.piecePositionStorage[currentTeam.teamCode],
                                      regions[currentTeam.targetRegion][0], currentTeam.teamCode);         
      } else {
        if (arbiter.hasWon(arbiter.returnCurrentMoveCode())) {
          eval = depth;
        } else {
          eval = depthEval(depth - 1);
        }
      }
      evaluations[idx] = eval;      
      //Undo move
      movePiece(reverse);
    }
    
    int highest = 0;
    //Find highest evaluation
    for (int idx = 1; idx < evaluations.length; idx++) {
      if (evaluations[idx] > evaluations[highest]) {
        highest = idx;
      }
    }
    //Return highest evalution
    return evaluations[highest];  
  }
}
