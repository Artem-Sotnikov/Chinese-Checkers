/* Square.java
 * Purpose: Creates squares that represent each space on the board
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

// Imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Square {
    // Class Variables
    public ArrayCoordinate boardLocation;
    public PieceType piece;
    private int calculatedX, calculatedY;
    private boolean isSelected, isHovered, isNoted;

    /**
     * Square
     * Constructor in the case that there are no parameters
     */
    Square() {
    }

    /**
     * Square
     * Constructor that makes Square objects
     * @param i, the row value
     * @param j, the column value
     */
    Square(int i, int j) {
        this.boardLocation = new ArrayCoordinate(i, j);
        this.piece = null;
        isSelected = false;
        isHovered = false;
    }

    /**
     * isNoted
     * Getter that determines whether or not the square has been noted
     * @return boolean, true if the square has been noted, false for if it hasn't
     */
    public boolean isNoted() {
        return isNoted;
    }

    /**
     * setNoted
     * Sets the square to be noted or not noted
     * @param isNoted, the state to set the square into
     */
    public void setNoted(boolean isNoted) {
        this.isNoted = isNoted;
    }

    /**
     * isHovered
     * Returns whether or not the square is being hovered over by
     * @return boolean, true if the square is being hovered over by, false if it isn't being hovered
     */
    public boolean isHovered() {
        return isHovered;
    }

    /**
     * setHovered
     * Sets the square to be hovered or not hovered
     * @param isHovered, set square to be hovered or not
     */
    public void setHovered(boolean isHovered) {
        this.isHovered = isHovered;
    }

    /**
     * isSelected
     * Returns whether or not the square has been selected
     * @return boolean, true for for if the square has been selected, false for if it hasn't been
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * setSelected
     * Sets the square to be selected or not selected
     * @param isSelected, set whether or not the square has been selected
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * updateLocation
     * Updates the square location
     * @param row, the new row value
     * @param col, the new column value
     */
    public void updateLocation(int row, int col) {
        this.boardLocation.row = row;
        this.boardLocation.column = col;
    }

    /**
     * updateLocation
     * Updates the square location
     * @param update, the new location coordinates
     */
    public void updateLocation(ArrayCoordinate update) {
        this.boardLocation.row = update.row;
        this.boardLocation.column = update.column;
    }


    /**
     * draw
     * Draws the graphics for the Square
     * @param g, the graphics component
     */
    public void draw(Graphics g) {

        // Calculate the x and y values
        calculatedX = 300 - 15 * boardLocation.row + 30 * boardLocation.column;
        calculatedY = 100 + 30 * boardLocation.row;

        // Draw a circle if a piece is selected
        if (this.isSelected) {
            g.setColor(Color.YELLOW);
            g.fillOval(calculatedX - 3, calculatedY - 3, 26, 26);
        }

        // Set the colour of the graphics component
        if (this.piece != null) {
            g.setColor(this.piece.color);
        } else {
            g.setColor(Color.black);
        }

        // Draw circles
        g.fillOval(calculatedX, calculatedY, 20, 20);

        // Draw a circle if a piece is hovered over
        if (this.isHovered) {
            g.setColor(Color.YELLOW);
            g.fillOval(calculatedX + 4, calculatedY + 4, 12, 12);
        }

        // Draw a circle if a piece is noted
        if (this.isNoted) {
            g.setColor(Color.white);
            g.fillOval(calculatedX + 8, calculatedY + 8, 4, 4);
        }
    }

    /**
     * placePiece
     * Places piece somewhere on the board
     * @param newPiece, the place the piece is to be placed at
     */
    public void placePiece(PieceType newPiece) {
        this.piece = newPiece;
    }


    /**
     * containsCoordinates
     * Checks if coordinates are contained
     * @param point, the point to be checked
     * @return boolean, true if it does contain, false for if it doesn't
     */
    public boolean containsCoordinates(Point point) {
        int centerX = calculatedX + 10;
        int centerY = calculatedY + 10;
        int radius = 10;

        if ((calculatedX < point.x) && (calculatedX + 20 > point.x) && (calculatedY < point.y) && (calculatedY + 20 > point.y)) {
            return true;
        }

        if ((Math.pow(centerX + point.x, 2) + Math.pow(centerY + point.y, 2)) < Math.pow(radius, 2)) {
            return true;
        }
        return false;
    }
}
