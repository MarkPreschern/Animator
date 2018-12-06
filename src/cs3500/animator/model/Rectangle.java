package cs3500.animator.model;

/**
 * Represents a rectangular shape.
 */
public class Rectangle extends Shape implements ShapeModel {

  /**
   * Constructs a custom rectangle using a shape builder.
   *
   * @param b shape builder
   */
  public Rectangle(ShapeBuilder b) {
    super(b);
  }

  /**
   * Creates a new instance of this rectangle.
   *
   * @param that the given shape.
   */
  public Rectangle(Shape that) {
    super(that);
  }

  @Override
  public String getType() {
    return "Rectangle";
  }

  @Override
  public ShapeModel makeCopy() {
    return new Rectangle(this);
  }

}
