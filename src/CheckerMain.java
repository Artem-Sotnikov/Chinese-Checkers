/* CheckerMain.java
 * Purpose: A graphical interface that displays a chinese checkers game
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class CheckerMain {
  public static void main(String[] args) {
    Display disp = new Display(); //A frame is created to run a chinese checkers UI
    disp.timer.schedule(disp.timeManager, 2000, 100); //Assign timer for game
    disp.refresh(); //Refreshes frame so subsequent moves can be displayed 
    System.out.println("executed");
    while (!disp.exitFlag) {
      disp.refresh(); //Keep updating
    }
  }
}
