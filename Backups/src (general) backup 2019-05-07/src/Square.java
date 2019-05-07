/* Square.java 
 * Purpose: Creates squares that represent each space on the board
 * Creators: Artem, Joyce, Shi Han
 * Date: 2019-05-04
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Square {
  public ArrayCoordinate boardLocation;
  public PieceType piece; 
  private int calculatedX, calculatedY;
  private boolean isSelected, isHovered, isNoted;
  
  
  /** 
   * Square
   * Constructor that makes Square objects
   * @param int i, the row value
   * @param int j, the column value
   */
  Square(int i, int j) {
    this.boardLocation = new ArrayCoordinate(i,j);
    this.piece = null;
    isSelected = false;
    isHovered = false;
  }
    
// =======
//  public ArrayCoordinate boardLocation;
//  public PieceType piece; 
//  private int calculatedX, calculatedY;
 
//  private boolean isSelected, isHovered, isNoted;
 
//  public boolean isNoted() {
//   return isNoted;
//  }

//  public void setNoted(boolean isNoted) {
//   this.isNoted = isNoted;
//  }

//  public boolean isHovered() {
//   return isHovered;
//  }

//  public void setHovered(boolean isHovered) {
//   this.isHovered = isHovered;
//  }

//  public boolean isSelected() {
//   return isSelected;
//  }

//  public void setSelected(boolean isSelected) {
//   this.isSelected = isSelected;
//  }
 
//  public void updateLocation(int row, int col) {
//   this.boardLocation.row = row;
//   this.boardLocation.column = col;
//  }
 
//  public void updateLocation(ArrayCoordinate update) {
//   this.boardLocation.row = update.row;
//   this.boardLocation.column = update.column;
//  }

//  Square() {};
 
//  Square(int i, int j) {
//   this.boardLocation = new ArrayCoordinate(i,j);
//   this.piece = null;
//   isSelected = false;
//   isHovered = false;
     
//  }

 
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
   * @param boolean isNoted, the state to set the square into
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
   * @param boolean isHovered, set square to be hovered or not
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
   * @param boolean isSelected, set whether or not the square has been selected
   */
  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }
  
  /** 
   * updateLocation
   * Updates the square location
   * @param int row, the new row value
   * @param int col, the new column value
   */
  public void updateLocation(int row, int col) {
    this.boardLocation.row = row;
    this.boardLocation.column = col;
  }
  
  /** 
   * updateLocation
   * Updates the square location
   * @param ArrayCoordinate update, the new location coordinates
   */
  public void updateLocation(ArrayCoordinate update) {
    this.boardLocation.row = update.row;
    this.boardLocation.column = update.column;
  }
  
  //?????????????????????????????
  Square() {};
  
  
  /** 
   * draw
   * Draws the grpahics for the Square
   * @param Graphics g
   */
  public void draw(Graphics g) {
    calculatedX = 300 - 15*boardLocation.row + 30*boardLocation.column;
    calculatedY = 100 + 30*boardLocation.row;
    if (this.isSelected) {
      g.setColor(Color.YELLOW);
      g.fillOval(calculatedX - 3, calculatedY - 3, 26, 26);
    }
    
    if (this.piece != null) {
      g.setColor(this.piece.color);
    } else {
      g.setColor(Color.black);
    }
    
//  g.setColor(Color.black);
    
    //System.out.println("drawing executed");
    g.fillOval(calculatedX, calculatedY, 20, 20);
    
    if (this.isHovered) {
      g.setColor(Color.YELLOW);
      g.fillOval(calculatedX + 4, calculatedY + 4, 12, 12);
    }
    
    if (this.isNoted) {
      g.setColor(Color.white);
      g.fillOval(calculatedX + 8, calculatedY + 8, 4, 4);   
    }  
  }
  
  
  /** 
   * placePiece
   * Places piece somewhere on the board
   * @param PieceType newPiece, the place the piece is to be placed at
   */
  public void placePiece(PieceType newPiece) {
    this.piece = newPiece;  
  }
  
  
  /** 
   * containsCoordinates
   * Checks if coordinates are contained
   * @param Point point, the point to be checked
   * @return boolean, true if it does contain, false for if it doesn't
   */
  public boolean containsCoordinates(Point point) {
    int centerX = calculatedX + 10;
    int centerY = calculatedY + 10; 
    int radius = 10;
    
    if ((calculatedX < point.x) && (calculatedX + 20 > point.x) && (calculatedY < point.y) && (calculatedY + 20 > point.y)){
      return true;  
    }
    
    if ((Math.pow(centerX + point.x,2) + Math.pow(centerY + point.y, 2)) < Math.pow(radius,2)) {
      return true;
    } 
    return false;
  }
}
