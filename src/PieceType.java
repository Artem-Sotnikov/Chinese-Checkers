/** PieceType.java
 * Purpose: To set up the characteristics of the different teams in the game
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

// Imports
import java.awt.Color;

public class PieceType {
    // Variables
    public String team;
    public Color color;
    public int targetRegion;
    public int teamCode;

    /**
     * PieceType
     * Constructor that sets up characteristics for a team
     * @param teamName, the name that identifies the team
     * @param clr, the colour of the team's pieces
     * @param target, the region where the team aims to go to
     * @param code, the team's code
     */
    PieceType(String teamName, Color clr, int target, int code) {
        this.team = teamName;
        this.color = clr;
        this.targetRegion = target;
        this.teamCode = code;
    }
}
