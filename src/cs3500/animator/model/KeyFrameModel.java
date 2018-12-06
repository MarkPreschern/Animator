package cs3500.animator.model;

/**
 * Represents the model for a shape at a specific time.
 */
public interface KeyFrameModel {

  /**
   * Gets the time of the key frame.
   *
   * @return the time
   */
  public double getTime();

  /**
   * Gets the shape of the key frame.
   *
   * @return the shape
   */
  public ShapeModel getShape();
}
