
public class ArrayCoordinate {
 private int row;
 private int column;
 
 public ArrayCoordinate(int a, int b) {
  this.row = a;
  this.column = b;
 }
 
 public void displayCoordinate() {
  System.out.println(row + ", " + column);
 }

 public int getRow() {
  return this.row;
 }

 public int getColumn() {
     return this.column;
 }
 
}
