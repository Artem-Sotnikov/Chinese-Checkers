/* CustomMouseListener.java
 * Purpose: To handle mouse events
 * Creators: Artem, Anthony (from previous project)
 * Date: 2019-05-04
 */

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputListener;

public class CustomMouseListener implements MouseInputListener, MouseMotionListener, MouseWheelListener {
  private int x;
  private int y;
  private int clickX;
  private int clickY;
  private int releaseX;
  private int releaseY;
  private double zoomScale = Constants.scaleFactor;
  private boolean clickHandled;
  private boolean isDragging = false;
  private boolean isScrolling = false;
  
  /** 
   * CustomMouseListener
   * Constructor that set up initial mouse position
   */
  CustomMouseListener() {
    clickHandled = true;
    clickX = -1;
    clickY = -1;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    clickX = e.getX();
    clickY = e.getY();
    this.clickHandled = false;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseEntered(MouseEvent e) {
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseExited(MouseEvent e) {
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
  @Override
  public void mousePressed(MouseEvent e) {
    clickX = e.getX();
    clickY = e.getY();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    releaseX = e.getX();
    releaseY = e.getY();
    this.isDragging = false;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    x = e.getX();
    y = e.getY();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    x = e.getX();
    y = e.getY();
    releaseX = e.getX();
    releaseY = e.getY();
    this.isDragging = true;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.
   * MouseWheelEvent)
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int notches = e.getWheelRotation();
    if (notches < 0) { // mouse wheel moved up
      for (int i = notches; i < 0; i++) {
        zoomScale += 0.1;
      }
      isScrolling = true;
    } else if (notches > 0) { // mouse wheel moved down
      for (int i = notches; i > 0; i--) {
        zoomScale -= 0.1;
      }
      isScrolling = true;
    } else { // mouse wheel remains the same
      isScrolling = false;
    }
    if (zoomScale < 1) {
      zoomScale = 1;
    } else if (zoomScale > 4) {
      zoomScale = 4;
    }
  }
  
  /**
   * getPos()
   * Getter for mouse position
   * @return Point, the position of the mouse
   */
  public Point getPos() {
    return new Point(x, y);
  }
  
  /**
   * getRectifiedPos()
   * Getter for rectified mouse position
   * @return Point, the rectified position of the mouse
   */
  public Point getRectifiedPos() {
    int rectifiedX = (int) (x/zoomScale);
    int rectifiedY = (int) (y/zoomScale);
    return new Point(rectifiedX,rectifiedY);
  }
  
  /**
   * getClick
   * Getter for click position of the mouse
   * @return Point, the click position of the mouse
   */
  public Point getClick() {
    return new Point(clickX, clickY);
  }
  
  /**
   * getRectifiedClick
   * Getter for rectified click position of the mouse
   * @return Point, the rectified click position of the mouse
   */
  public Point getRectifiedClick() {
    int rectifiedX = (int) (clickX/zoomScale);
    int rectifiedY = (int) (clickY/zoomScale);
    
    return new Point(rectifiedX,rectifiedY);
  }
  
  /**
   * getX
   * Getter for x value of mouse
   * @return x, the x value of the mouse
   */
  public int getX() {
    return x;
  }
  
  /**
   * setX
   * Setter for x value of mouse
   * @param x, the x to set
   */
  public void setX(int x) {
    this.x = x;
  }
  
  /**
   * getY
   * Getter for y value of mouse
   * @return int y, the y value of the mouse
   */
  public int getY() {
    return y;
  }
  
  
  /**
   * setY
   * Setter for y value of mouse
   * @param y, the y to set
   */
  public void setY(int y) {
    this.y = y;
  }
  
  /**
   * getClickX
   * Getter for x value of mouse click
   * @return int clickX, the x value of the mouse click
   */
  public int getClickX() {
    return clickX;
  }
  
  /**
   * setClickX
   * Setter for x value of mouse click
   * @param clickX, the x value of the mouse click to set
   */
  public void setClickX(int clickX) {
    this.clickX = clickX;
  }
  
  /**
   * getClickY
   * Getter for y value of mouse click
   * @return int clickY, the y value of the mouse click
   */
  public int getClickY() {
    return clickY;
  }
  
  /**
   * setClickY
   * Setter for y value of mouse click
   * @param clickY, the y value of the mouse click to set
   */
  public void setClickY(int clickY) {
    this.clickY = clickY;
  }
  
  /**
   * getReleaseX
   * Getter for the x value of when the mouse is released
   * @return int releaseX, the x value of where the mouse is released
   */
  public int getReleaseX() {
    return releaseX;
  }
  
  /**
   * setReleaseX
   * Setter for the x value of when the mouse is released
   * @param releaseX, the x value of where the mouse is released
   */
  public void setReleaseX(int releaseX) {
    this.releaseX = releaseX;
  }
  
  /**
   * getReleaseY
   * Getter for the y value of when the mouse is released
   * @return int releaseY, the y value of where the mouse is released
   */
  public int getReleaseY() {
    return releaseY;
  }
  
  /**
   * setReleaseY
   * Setter for the y value of when the mouse is released
   * @param releaseY, the y value of where the mouse is released
   */
  public void setReleaseY(int releaseY) {
    this.releaseY = releaseY;
  }
  
  /**
   * getZoomedScale
   * Getter for the zoom scale of the mouse
   * @return double zoomScale, the zoomScale of the mouse
   */
  public double getZoomScale() {
    return zoomScale;
  }
  
  /**
   * setZoomedScale
   * Setter for the zoom scale of the mouse
   * @param zoomScale, the zoomScale of the mouse
   */
  public void setZoomScale(double zoomScale) {
    this.zoomScale = zoomScale;
  }
  
  /**
   * clickHandled
   * This method will handle a click.
   */
  public void clickHandled() {
    this.clickHandled = true;
  }
  
  /**
   * setClickHandled
   * Setter for handled click
   * @param clickHandled the clickHandled to set
   */
  public void setClickHandled(boolean clickHandled) {
    this.clickHandled = clickHandled;
  }
  
  /**
   * isDragging
   * Determines whether or not the mouse is dragging or not
   * @return boolean isDragging, true for if the mouse is dragging, false for if it is not
   */
  public boolean isDragging() {
    return isDragging;
  }
  
  /**
   * setDragging
   * Setter for whether or not mouse is dragging
   * @param isDragging the boolean isDragging to set
   */
  public void setDragging(boolean isDragging) {
    this.isDragging = isDragging;
  }
  
  /**
   * isScrolling
   * Setter for if the mouse is scrolling or not
   * @return boolean isScrolling, true for if the mouse is scrolling, false if not
   */
  public boolean isScrolling() {
    return isScrolling;
  }
  
  /**
   * setScrolling
   * Setter for is scrolling
   * @param isScrolling the isScrolling to set
   */
  public void setScrolling(boolean isScrolling) {
    this.isScrolling = isScrolling;
  }
  
  /**
   * clickPending
   * Determines if a click is pending or not
   * @return boolean !(clickHandled), true for if pending, false if not
   */
  public boolean clickPending() {
    return (!this.clickHandled);
  } 
}
