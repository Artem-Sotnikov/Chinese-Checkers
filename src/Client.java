/* Client.java
 * Purpose: Creates a client to join the server to play the game
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class Client {
  public static void main(String[] args) {
    PlayerClient client = new PlayerClient();
    client.run();
  }
}
