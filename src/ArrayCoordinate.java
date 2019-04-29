
public class ArrayCoordinate {
 public int rowValue;
 public int columnValue;
 private double priority;
 
 public ArrayCoordinate(int a, int b) {
  this.rowValue = a;
  this.columnValue = b;
 }
 
 public void displayCoordinate() {
  System.out.println(rowValue + ", " + columnValue);
 }

 public void setPriority(int priority){
  this.priority = priority;
 }
 public double getPriority(){
  return this.priority;
 }
}
