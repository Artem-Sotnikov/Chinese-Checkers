
public class ArrayCoordinate {
 private int row;
 private int column;
 private double priority;
 
 public ArrayCoordinate(int a, int b) {
  this.row = a;
  this.column = b;
 }
 
 public void displayCoordinate() {
  System.out.println(row + ", " + column);
 }

 public void setPriority(int priority){
  this.priority = priority;
 }
 public double getPriority(){
  return this.priority;
 }

 public int getRowValue() {
  return this.row;
 }

 public int getColumnValue() {
     return this.column;
 }
}
