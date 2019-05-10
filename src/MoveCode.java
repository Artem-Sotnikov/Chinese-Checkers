/* MoveCode.java
 * Purpose: A class that creates MoveCode objects that help determine moves for players
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

public class MoveCode {
    public ArrayCoordinate startPosition;
    public ArrayCoordinate targetPosition;
    public String stringPath;

    /**
     * MoveCode
     * Constructor for if there are no parameters
     */
    MoveCode() {
    }

    ;

    /**
     * MoveCode
     * Constructor that will create a MoveCode instruction for start and end coordinates
     * @param a, start row value
     * @param b, start column value
     * @param p, end row value
     * @param q, end column value
     */
    MoveCode(int a, int b, int p, int q) {
        this.startPosition = new ArrayCoordinate(a, b);
        this.targetPosition = new ArrayCoordinate(p, q);
    }

    /**
     * MoveCode
     * Constructor with start and end coordinates
     * @param start, the original coordinate/position
     * @param end,   the ending coordinate piece is to travel to
     */
    MoveCode(ArrayCoordinate start, ArrayCoordinate end) {
        this.startPosition = start;
        this.targetPosition = end;
    }
}
