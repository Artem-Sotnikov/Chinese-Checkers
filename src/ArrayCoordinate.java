/* ArrayCoordinate.java
 * Purpose: A class that creates ArrayCoordinate objects that contain the coordinates of a piece on the game board
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class ArrayCoordinate {
  //Variables
  public int row;
  public int column;
  public String additionalInfo;
  
  /** 
   * ArrayCoordinate
   * Constructor that sets up the row and column value for the ArrayCoordinate
   * @param a, the value to be set for the row
   * @param b, the value to be set for the column
   */
  public ArrayCoordinate(int a, int b) {
    this.row = a;
    this.column = b;
  }
  
  /** 
   * displayCoordinate
   * Displays the coordinate when
   */
  public void displayCoordinate() {
    System.out.println(row + ", " + column);
  }
}
