package cs3500.animator.model;

/**
 * Represents an elliptical shape.
 */
public class Ellipse extends Shape implements ShapeModel {

  /**
   * Constructs a custom ellipse using a shape builder.
   *
   * @param b shape builder
   */
  public Ellipse(ShapeBuilder b) {
    super(b);
  }

  /**
   * Creates a new instance of this ellipse.
   *
   * @param that the given shape.
   */
  public Ellipse(Shape that) {
    super(that);
  }

  @Override
  public String getType() {
    return "Ellipse";
  }

  @Override
  public ShapeModel makeCopy() {
    return new Ellipse(this);
  }

}
