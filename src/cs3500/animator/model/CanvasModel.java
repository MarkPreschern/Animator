package cs3500.animator.model;

/**
 * Represents a model for the bounds of the animation.
 */
public interface CanvasModel {

  /**
   * Gets the x value (top).
   *
   * @return the x value
   */
  public int getX();

  /**
   * Gets the y value (left).
   *
   * @return the y value
   */
  public int getY();

  /**
   * Gets the width value.
   *
   * @return the width value
   */
  public int getWidth();

  /**
   * Gets the height value.
   *
   * @return the height value
   */
  public int getHeight();
}
