
public class ArrayCoordinate {
 private int row;
 private int column;
 private double priority;
 
 public ArrayCoordinate(int a, int b) {
  this.row = a;
  this.column = b;
  this.priority = 0;
 }
 
 public void displayCoordinate() {
  System.out.println(row + ", " + column);
 }

 public void setPriority(int priority){
  this.priority += priority;
 }
 public void multiplyPriority(int priority) {
     this.priority *= priority;
 }
 public double getPriority(){
  return this.priority;
 }

 public int getRow() {
  return this.row;
 }

 public int getColumn() {
     return this.column;
 }
 
}
