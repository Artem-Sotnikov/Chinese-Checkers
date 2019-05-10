/* EvaluationEngine.java
 * Purpose: Generates the algorithm for games
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class EvaluationEngine { 
  //Variable declaration
  private ArrayCoordinate[][] regions; //Stores locations of each of the teams and their pieces
  private Square[][] source;
  private Square[][] storage;
  private Arbiter arbiter;
  
  public EvaluationEngine() {};
  
  public EvaluationEngine(Square[][] src, ArrayCoordinate[][] regions, Square[][] storage) {};
  
  /** 
   * EvaluationEngine
   * Constructor that creates an evaluation engine
   * @param Arbiter scrArbiter, an Arbiter for the game
   */
  public EvaluationEngine(Arbiter srcArbiter) {
    this.arbiter = srcArbiter;
  }
  
  /** 
   * evaluateBasic
   * Performs a basic algorithm to generate a possible move
   * @param positions, the array containing locations of pieces on the board
   * @param primePosition, a location the team is aiming toward
   * @param subjectCode, the team number
   * @return double score, a number representing the value of an evaluation
   */
  public double evaluateBasic(Square[] positions, ArrayCoordinate primePosition, int subjectCode) {
    double score = 0;
    
    //Iterate through the pieces
    for (int idx = 0; idx < 10; idx++) {
      double tempDeltaRow = Math.abs(primePosition.row - positions[idx].boardLocation.row);  
      double tempDeltaCol = Math.abs(primePosition.column - positions[idx].boardLocation.column);
      //Score is simply based how far a piece advances
      score = score - (tempDeltaRow + tempDeltaCol);
    }
    //Edit score depending on the team
    if ((subjectCode == 1) || (subjectCode == 4)) {
      score -= 70;
    }
    //Determine winner (if any) and edit score
    if (arbiter.hasWon(subjectCode)) {
      score = 0;
    }
    return score;
  } 
  
  /** 
   * evaluateComplex
   * Performs a complex algorithm to generate a possible move
   * @param positions, the array containing locations of pieces on the board
   * @param primePosition, a location the team is aiming toward
   * @param subjectCode, the team number
   * @return double score, a number representing the value of an evaluation
   */
  public double evaluateComplex(Square[] positions, ArrayCoordinate primePosition, int subjectCode) {
    double score = 0;
    
    for (int idx = 0; idx < 10; idx++) {
      //Calculate score based on delta values
      double tempDeltaRow = Math.abs(primePosition.row - positions[idx].boardLocation.row);  
      double tempDeltaCol = Math.abs(primePosition.column - positions[idx].boardLocation.column);
      score = score - (Math.pow(tempDeltaRow,2) + Math.pow(tempDeltaCol,2)); 
    }
    
    //Edit score depending on team
    if ((subjectCode == 1) || (subjectCode == 4)) {
      score -= 70;
    }
    
    //Find winner
    if (arbiter.hasWon(subjectCode)) {
      score = 0;
      System.out.println("Victory!");
    }
    return score;
  }
}
