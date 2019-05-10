/* MoveNode.java //Finish commenting
 * Purpose: A class that creates ArrayCoordinate objects that contain the coordinates of a piece on the game board
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

// Imports

import java.util.ArrayList;
import java.util.Arrays;

class MoveNode {
    // Imports
    public ArrayCoordinate position;
    public ArrayList<MoveNode> branches;
    public boolean isRoot;

    /**
     * MoveNode
     * Constructor that sets the starting point for branching out
     * @param source, the starting point for the move
     */
    MoveNode(ArrayCoordinate source) {
        this.position = source;
        branches = new ArrayList<MoveNode>(0);
    }

    /**
     * Constructor that sets the starting point for branching out
     * @param row, the row of the move
     * @param col, the column of the move
     */
    MoveNode(int row, int col) {
        this.position = new ArrayCoordinate(row, col);
        branches = new ArrayList<MoveNode>(0);
    }

    ;

    MoveNode() {
        branches = new ArrayList<MoveNode>(0);
    }

    ;

    public static ArrayCoordinate[] concat(ArrayCoordinate[] first, ArrayCoordinate[] second) {
        ArrayCoordinate[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public ArrayList<ArrayCoordinate> returnAsArray() {
        ArrayList<ArrayCoordinate> returnList = new ArrayList<ArrayCoordinate>(0);

        if (!this.isRoot) {
            returnList.add(this.position);
        }

        for (int i = 0; i < branches.size(); i++) {
            returnList.addAll(branches.get(i).returnAsArray());
        }
        return returnList;
    }

    public ArrayList<MoveCode> toMoveCodes() {
        ArrayList<ArrayCoordinate> finalCoords = this.returnAsArray();
        ArrayList<MoveCode> returnList = new ArrayList<MoveCode>();

        for (int idx = 0; idx < finalCoords.size(); idx++) {
            returnList.add(new MoveCode(this.position, finalCoords.get(idx)));
        }
        return returnList;
    }

    public ArrayList<ArrayCoordinate> returnAsArray(boolean updateOverload) {
        ArrayList<ArrayCoordinate> returnList = new ArrayList<ArrayCoordinate>(0);

        if (!this.isRoot) {
            returnList.add(this.position);
        }

        for (int i = 0; i < branches.size(); i++) {
            returnList.addAll(branches.get(i).returnAsArray(true));
        }

        for (int idx = 0; idx < returnList.size(); idx++) {
            if (returnList.get(idx).additionalInfo != null) {
                returnList.get(idx).additionalInfo = "(" + Integer.toString(this.position.row + 1) + " "
                        + Integer.toString(this.position.column + 1) + ") " + returnList.get(idx).additionalInfo;
            } else {
                returnList.get(idx).additionalInfo = "(" + Integer.toString(this.position.row + 1) + " "
                        + Integer.toString(this.position.column + 1) + ") ";
            }
        }

        return returnList;
    }

    public ArrayList<MoveCode> toMoveCodes(boolean updateOverload) {
        ArrayList<ArrayCoordinate> finalCoords = this.returnAsArray(true);
        ArrayList<MoveCode> returnList = new ArrayList<MoveCode>();

        for (int idx = 0; idx < finalCoords.size(); idx++) {
            returnList.add(new MoveCode(this.position, finalCoords.get(idx)));
            returnList.get(idx).stringPath = finalCoords.get(idx).additionalInfo;
        }
        return returnList;
    }
}