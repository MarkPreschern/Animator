package cs3500.animator.model;

/**
 * Represents a shape at a specific time.
 */
public class KeyFrame implements KeyFrameModel {

  private double time;
  private ShapeModel shape;

  /**
   * Constructs a key frame with the given time and shape.
   *
   * @param time  the time of the frame
   * @param shape the shape at the frame
   * @throws IllegalArgumentException if the shape is null or time is negative
   */
  public KeyFrame(double time, ShapeModel shape) throws IllegalArgumentException {
    if (shape == null) {
      throw new IllegalArgumentException("Shape can't be null.");
    } else if (time < 0) {
      throw new IllegalArgumentException("Time can't be negative.");
    }

    this.time = time;
    this.shape = shape;
  }

  @Override
  public double getTime() {
    return time;
  }

  @Override
  public ShapeModel getShape() {
    return shape;
  }
}
