/* CheckerMain.java
 * Purpose: A graphical interface that displays a chinese checkers game
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class CheckerMain {
  public static void main(String[] args) {
    Display disp = new Display();
    disp.refresh();
    System.out.println("executed");
    while (!disp.exitFlag) {
      disp.refresh();
    }
  }
}
