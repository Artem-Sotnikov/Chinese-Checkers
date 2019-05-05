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
 private Square[][] squares;
 private ArrayCoordinate[][] regions;
 private ArrayList<ArrayCoordinate> notedSquares;
 
 private GameState gameState;
 
 private ArrayCoordinate selectedCoordinates;
 
 
 public CustomMouseListener listener;
 public Arbiter arbiter;
 
 private PieceManager manager;
 private EvaluationEngine engine;
 
 public double currentEvaluation;
 public int currentHoverRow, currentHoverColumn; 
 public double bestBranchEval; 
 
 public boolean gameFinished;
 public String gameWinner; 
 

 BoardPanel() {
  Dimension boardSize = new Dimension ((int) (925*Constants.scaleFactor),Toolkit.getDefaultToolkit().getScreenSize().height);
  this.setPreferredSize(boardSize);
  this.setOpaque(false);     
  
  this.squares = new Square[25][25];
  listener = new CustomMouseListener();
  this.addMouseMotionListener(listener);
  this.addMouseListener(listener);    
  
  this.notedSquares = new ArrayList<ArrayCoordinate>(0);
  
  regions = new ArrayCoordinate[6][10];
  
  this.arbiter = new Arbiter(squares,regions);
  this.manager = new PieceManager(squares); 
  this.engine = new EvaluationEngine(arbiter);
  
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
  
  
  
 }
 
 public void terminateMove() {
  if (selectedCoordinates != null) {
   squares[selectedCoordinates.row][selectedCoordinates.column].setSelected(false);
  }
  gameState = GameState.STATE_IDLE;
  arbiter.determineGameResult();
  if (arbiter.gameWinner != null) {
   gameFinished = true;
  } else {
   arbiter.registerMove();
  }
  
  for (int idx = 0; idx < notedSquares.size(); idx++) {
   ArrayCoordinate notedPosition = notedSquares.get(idx);
   squares[notedPosition.row][notedPosition.column].setNoted(false);
  }
  
      
  
 }
 
 public void handleHovers(int i, int j, Graphics g) {
  
  if (squares[i][j].containsCoordinates(listener.getRectifiedPos())) {   
  this.currentHoverRow = i;
  this.currentHoverColumn = j;

   
   
   switch (gameState) {
   case STATE_IDLE: {
    if (squares[i][j].containsCoordinates(listener.getRectifiedPos()) && 
      arbiter.approvesSelection(i, j)) {
     
      squares[i][j].setHovered(true);          
      
    } else {
     squares[i][j].setHovered(false);
    }
   }
   break;
   case STATE_PIECE_SELECTED: {
    if (squares[i][j].containsCoordinates(listener.getRectifiedPos()) && 
      arbiter.approvesMove(new MoveCode(selectedCoordinates, new ArrayCoordinate(i,j)), false)) {
     
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
 
 public void handleClicks(int i, int j) {
  
  if (squares[i][j].containsCoordinates(listener.getRectifiedClick())) {
   switch (gameState) {
   case STATE_IDLE: {
    if (arbiter.approvesSelection(i, j)) {
     
      listener.clickHandled();
     
      squares[i][j].setSelected(true);  
      selectedCoordinates = new ArrayCoordinate(i,j);
      
      gameState = GameState.STATE_PIECE_SELECTED;
      
    } 
   
   }
   break;
   case STATE_PIECE_SELECTED: {
    if (arbiter.approvesMove(new MoveCode(selectedCoordinates, new ArrayCoordinate(i,j)), true)) {
     
      listener.clickHandled();
      
      MoveCode move = new MoveCode(selectedCoordinates,new ArrayCoordinate(i,j));
      movePiece(move);
      
      if (!arbiter.isChainMoveInProgress()) {
       this.terminateMove();
      } 
      
    } else if (arbiter.approvesSelection(i, j)) {
          
      listener.clickHandled();
     
      squares[selectedCoordinates.row]
        [selectedCoordinates.column].setSelected(false);
      
      squares[i][j].setSelected(true);
      selectedCoordinates = new ArrayCoordinate(i,j);
    }
   }
   break;
   }
  }
 }
 
    public void paintComponent(Graphics g) {
     super.paintComponent(g);
     setDoubleBuffered(true);
     
     
   
     Graphics2D g2 = (Graphics2D) g;       
     g2.scale(Constants.scaleFactor, Constants.scaleFactor);
              
     g.setColor(Color.black);
     g.setFont(new Font("TrilliumWeb",Font.PLAIN, 20));
 
  
     arbiter.displayTeamToMove(g);  
     updateEvaluation(g);     
     
     for (int i = 0; i < squares.length; i++) {
      for (int j = 0; j <= i; j++) {
       if (squares[i][j] != null) {
        
        this.handleHovers(i,j,g);
        if (listener.clickPending()) {
         this.handleClicks(i,j);
        }
        
        
        squares[i][j].draw(g);
        
       }       
      }
     }     
    }

    public void configureServerSetup() {
       this.configureInitialSetup();
       arbiter.moveOrder = new PieceType[6];
       arbiter.moveOrder[0] = Constants.teamZeroPiece;
    }

    public void cleanseBoard() {
      this.squares = new Square[25][25];

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
    }

    public void setUpBoard(ArrayCoordinate[] existingPieces) {
     for (int i = 0; i < existingPieces.length - 1; i++) {
      System.out.println(existingPieces[i].row +","+existingPieces[i].column);
       if (i < 10) {
        squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamZeroPiece);
        manager.piecePositionStorage[0][i] = squares[existingPieces[i].row][existingPieces[i].column];
       } else if (i > 9 && i < 20) {
        squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamOnePiece);
       } else if (i > 19 && i < 30) {
        squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamTwoPiece);
       } else if (i > 29 && i < 40) {
        squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamThreePiece);
       } else if (i > 39 && i < 50) {
        squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamFourPiece);
       } else if (i > 49 && i < 60) {
        squares[existingPieces[i].row][existingPieces[i].column].placePiece(Constants.teamFivePiece);
       }
     }

    }

    
    public void configureEndScenario() {
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
    	manager = new PieceManager(squares);
    	
    	for (int i = 0; i < regions[0].length - 1; i++) {
    		squares[regions[3][i].row][regions[3][i].column].placePiece(Constants.teamZeroPiece);
    	    manager.piecePositionStorage[0][i] = squares[regions[3][i].row][regions[3][i].column];
    	}  
    	
    	squares[20][10].placePiece(Constants.teamZeroPiece);
    	
    	manager.piecePositionStorage[0][9] = squares[20][10];
    	
    }
    
    public void configureBottomPosition() {
    	arbiter.teamToMove = 3;
    }
    
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
          
     for (int i = 0; i < regions[0].length; i++) {
      squares[regions[0][i].row][regions[0][i].column].placePiece(Constants.teamZeroPiece);
      manager.piecePositionStorage[0][i] = squares[regions[0][i].row][regions[0][i].column];
     }    
     
     for (int i = 0; i < regions[0].length; i++) {
      squares[regions[1][i].row][regions[1][i].column].placePiece(Constants.teamOnePiece);
      manager.piecePositionStorage[1][i] = squares[regions[1][i].row][regions[1][i].column];
     }
     
     for (int i = 0; i < regions[0].length; i++) {
      squares[regions[2][i].row][regions[2][i].column].placePiece(Constants.teamTwoPiece);
      manager.piecePositionStorage[2][i] = squares[regions[2][i].row][regions[2][i].column];
     }
         
     for (int i = 0; i < regions[0].length; i++) {
      squares[regions[3][i].row][regions[3][i].column].placePiece(Constants.teamThreePiece);
      manager.piecePositionStorage[3][i] = squares[regions[3][i].row][regions[3][i].column];
     }
     
     for (int i = 0; i < regions[0].length; i++) {
      squares[regions[4][i].row][regions[4][i].column].placePiece(Constants.teamFourPiece);
      manager.piecePositionStorage[4][i] = squares[regions[4][i].row][regions[4][i].column];
     }
     
     for (int i = 0; i < regions[0].length; i++) {
      squares[regions[5][i].row][regions[5][i].column].placePiece(Constants.teamFivePiece);
      manager.piecePositionStorage[5][i] = squares[regions[5][i].row][regions[5][i].column];
     }
     
     gameState = GameState.STATE_IDLE;
     //this.configureBottomPosition();
     
    }
    
    public boolean squareOccupied(ArrayCoordinate target) {
     return (squares[target.row][target.column].piece != null);
    }
    
    public boolean movePiece(MoveCode move) {
     Square selected = squares[move.startPosition.row][move.startPosition.column];
     
     int transRow = move.startPosition.row;
     int transCol = move.startPosition.column;
     
     selected.updateLocation(move.targetPosition);
          
     squares[move.targetPosition.row][move.targetPosition.column] = selected;
     
     squares[transRow][transCol] 
       = new Square(transRow,transCol);
     
     selectedCoordinates = move.targetPosition;
               
     return true;              
    }
            
    public void displayPossibleMoves() {
     MoveNode[] moves = manager.returnAllTeamMoves(arbiter.returnCurrentMoveCode());
     for (int idx = 0; idx < moves.length; idx++) {
      ArrayList<ArrayCoordinate> possibilityArray =  moves[idx].returnAsArray();
      for (int idx2 = 0; idx2 < possibilityArray.size(); idx2++) {
       ArrayCoordinate notedPosition = possibilityArray.get(idx2);
       squares[notedPosition.row][notedPosition.column].setNoted(true);
       notedSquares.add(notedPosition);
      }
     }
    }
    
    public void executeRandomMove() {
     ArrayList<MoveCode> possibleMoves = manager.ReturnAllMoveCodes(arbiter.returnCurrentMoveCode());
     MoveCode randomMove = possibleMoves.get((int) (Math.random()*possibleMoves.size()));
     
     movePiece(randomMove);
     terminateMove();
    }
    
    public void updateEvaluation(Graphics g) {
     double eval = 0;
     PieceType currentTeam = arbiter.returnCurrentTeam();
     
     eval = engine.evaluateComplex(manager.piecePositionStorage[currentTeam.teamCode],
       regions[currentTeam.targetRegion][0], currentTeam.teamCode);
     Double.toString(eval);
     currentEvaluation = eval;         
    }
    
    public double executeByEval() {
     ArrayList<MoveCode> possibleMoves = manager.ReturnAllMoveCodes(arbiter.returnCurrentMoveCode());
     
     MoveCode tempMove;
	 MoveCode reverse;
	  
	 double evaluations[] = new double[possibleMoves.size()];
	 double eval;
  
	 PieceType currentTeam = arbiter.returnCurrentTeam();
     
     for (int idx = 0; idx < possibleMoves.size(); idx++) {
      tempMove = possibleMoves.get(idx);
      reverse = new MoveCode(tempMove.targetPosition.row,tempMove.targetPosition.column,
        tempMove.startPosition.row,tempMove.startPosition.column);;
      movePiece(tempMove);
      eval = engine.evaluateComplex(manager.piecePositionStorage[currentTeam.teamCode],
            regions[currentTeam.targetRegion][0], currentTeam.teamCode);
      arbiter.determineGameResult();
      if (arbiter.gameWinner != null) {
       eval = 0;
       arbiter.gameWinner = null;
      }
      evaluations[idx] = eval;      
      
      movePiece(reverse);
     }
     
     int highest = 0;
     
     for (int idx = 1; idx < evaluations.length; idx++) {
      if (evaluations[idx] > evaluations[highest]) {
       highest = idx;
      }
     }
     
     movePiece(possibleMoves.get(highest));
     terminateMove();
     
     return evaluations[highest];     
    }        

    public MoveCode executeByDepth() {
     ArrayList<MoveCode> possibleMoves = manager.ReturnAllMoveCodes(arbiter.returnCurrentMoveCode());
        
     MoveCode tempMove;
   	 MoveCode reverse;
   	  
   	 double evaluations[] = new double[possibleMoves.size()];
   	 double eval;
     
   	 PieceType currentTeam = arbiter.returnCurrentTeam();
        
        for (int idx = 0; idx < possibleMoves.size(); idx++) {
         tempMove = possibleMoves.get(idx);
         reverse = new MoveCode(tempMove.targetPosition.row,tempMove.targetPosition.column,
           tempMove.startPosition.row,tempMove.startPosition.column);
         movePiece(tempMove); 
         if (arbiter.hasWon(arbiter.returnCurrentMoveCode())) {
        	 eval = 100;
        	 System.out.println("win in one move move, IDIOT!");
         } else {
        	 eval = depthEval(1);
         }          
         evaluations[idx] = eval;
         
         movePiece(reverse);
        }
        
        int highest = 0;
        
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
    
    
    private double depthEval(int depth) {
     ArrayList<MoveCode> possibleMoves = manager.ReturnAllMoveCodes(arbiter.returnCurrentMoveCode());
        
     MoveCode tempMove;
   	 MoveCode reverse;
   	  
   	 double evaluations[] = new double[possibleMoves.size()];
   	 double eval;
     
   	 PieceType currentTeam = arbiter.returnCurrentTeam();
   	 
        for (int idx = 0; idx < possibleMoves.size(); idx++) {
         tempMove = possibleMoves.get(idx);
         reverse = new MoveCode(tempMove.targetPosition.row,tempMove.targetPosition.column,
           tempMove.startPosition.row,tempMove.startPosition.column);
         movePiece(tempMove);
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
         
         movePiece(reverse);
        }
        
        int highest = 0;
        
        for (int idx = 1; idx < evaluations.length; idx++) {
         if (evaluations[idx] > evaluations[highest]) {
          highest = idx;
         }
        }
         
        return evaluations[highest];  
    }
    
    public MoveCode executeBestMove() {
     ArrayList<MoveCode> possibleMoves = manager.ReturnAllMoveCodes(arbiter.returnCurrentMoveCode());
        OptimalMoveFinder finder = new OptimalMoveFinder();
        MoveCode chosenMove = finder.findBestMove(possibleMoves);
        MoveCode isolated = new MoveCode(chosenMove.startPosition.row, chosenMove.startPosition.column,
        		chosenMove.targetPosition.row, chosenMove.targetPosition.column);
        movePiece(chosenMove);
        terminateMove();

        return isolated;
    }


}
