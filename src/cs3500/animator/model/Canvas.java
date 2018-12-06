package cs3500.animator.model;

/**
 * Represents the bounds of the animation.
 */
public class Canvas implements CanvasModel {
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Default constructor sets x and y to 0 and width and height to 500.
   */
  public Canvas() {
    this.x = 0;
    this.y = 0;
    this.width = 500;
    this.height = 500;
  }

  /**
   * Initializes variables to those given.
   *
   * @param x      The leftmost x value
   * @param y      The topmost y value
   * @param width  The width of the bounding box
   * @param height The height of the bounding box
   * @throws IllegalArgumentException if x or y is negative, or width or height is not greater than
   *                                  zero
   */
  public Canvas(int x, int y, int width, int height) throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("The width and height of the canvas must be greater than"
              + " zero.");
    }

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }
}
