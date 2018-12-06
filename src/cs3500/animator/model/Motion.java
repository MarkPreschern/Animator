package cs3500.animator.model;

import java.awt.geom.Point2D;

/**
 * Represents an animation over some period of time. The reason for abstracting animations is to
 * make it impossible for the final shape to incorporate animation of more than one 'type'. For
 * instance, a color animation can't mutate anything but the colors.
 */
public class Motion implements MotionModel {

  private final KeyFrameModel k1;
  private final KeyFrameModel k2;

  /**
   * Constructs an animation with a starting and ending key frames.
   *
   * @param k1 the initial key frame
   * @param k2 the final key frame
   * @throws IllegalArgumentException if key frames are null, or initial time is greater than final
   *                                  time
   */
  public Motion(KeyFrameModel k1, KeyFrameModel k2) throws IllegalArgumentException {
    if (k1 == null || k2 == null) {
      throw new IllegalArgumentException("Key frames can't be null.");
    } else if (k1.getTime() > k2.getTime()) {
      throw new IllegalArgumentException("Final time must be greater than or equal to initial "
              + "time.");
    }

    this.k1 = k1;
    this.k2 = k2;
  }

  @Override
  public ShapeModel getShape(ShapeModel shape, int currentTime) {
    double initialTime = this.k1.getTime();
    double finalTime = this.k2.getTime();
    Shape initialShape = (Shape) this.k1.getShape();
    Shape finalShape = (Shape) this.k2.getShape();

    //represents how far along the animation is, with respect to time.
    double difference;
    if (finalTime - initialTime == 0) {
      return finalShape;
    } else {
      difference = 1 - ((finalTime - currentTime) / (finalTime - initialTime));
    }

    Shape shape1 = (Shape) shape;

    shape1.x = initialShape.x + ((finalShape.x - initialShape.x) * difference);
    shape1.y = initialShape.y + ((finalShape.y - initialShape.y) * difference);

    shape1.width = initialShape.width + ((finalShape.width - initialShape.width)
            * difference);
    shape1.height = initialShape.height + ((finalShape.height - initialShape.height)
            * difference);

    shape1.red = initialShape.red + ((finalShape.red - initialShape.red)
            * difference);
    shape1.green = initialShape.green + ((finalShape.green - initialShape.green)
            * difference);
    shape1.blue = initialShape.blue + ((finalShape.blue - initialShape.blue)
            * difference);

    return shape;
  }

  @Override
  public String toString() {
    return "motion " + this.k1.getShape().getKey() + " " + this.k1.getTime() + " "
            + this.k1.getShape().toString() + " " + this.k2.getTime() + " "
            + this.k2.getShape().toString() + "\n";
  }


  @Override
  public KeyFrameModel getInitial() {
    return this.k1;
  }

  @Override
  public KeyFrameModel getFinal() {
    return this.k2;
  }

  @Override
  public Point2D.Double getTimeInterval() {
    return new Point2D.Double(this.k1.getTime(), this.k2.getTime());
  }
}
