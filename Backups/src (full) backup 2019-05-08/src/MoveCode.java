/* MoveCode.java
 * Purpose: A class that creates MoveCode objects that help determine moves for players
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class MoveCode {
  public ArrayCoordinate startPosition;
  public ArrayCoordinate targetPosition;
  public int priority;
  public String stringPath;
  
  MoveCode(){};
  
  /** 
   * MoveCode
   * Constructor that will create a MoveCode instruction for start and end coordinates
   * @param int a, start row value
   * @param int b, start column value
   * @param int p, end row value
   * @param int q, end column value
   */
  MoveCode(int a, int b, int p, int q) {
    this.startPosition = new ArrayCoordinate(a,b);
    this.targetPosition = new ArrayCoordinate(p,q);
  }
  
  /** 
   * MoveCode
   * Constructor with start and end coordinates
   * @param ArrayCoordinate start, the original coordinate/position
   * @param ArrayCoordinate end, the ending coordinate piece is to travel to
   */
  MoveCode(ArrayCoordinate start, ArrayCoordinate end) {
    this.startPosition = start;
    this.targetPosition = end;
  }
  
  /** 
   * setPriority
   * Assigns a priority for the ArrayCoordinate
   * @param int priority, the priority to be set
   */
  public void setPriority(int priority){
    this.priority += priority;
  }
  
  /** 
   * multiplyPriority
   * Multiplies priority is by a given factor
   * @param int priority, the factor the curretn priority is to be multiplied by
   */
  public void multiplyPriority(int priority) {
    this.priority *= priority;
  }
  
  /** 
   * getPriority
   * Gets the priority of the ArrayCoordinate
   * @return double priority, the priority
   */
  public double getPriority(){
    return this.priority;
  }
}
