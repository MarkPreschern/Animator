package cs3500.animator.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Represents tests for the class ShapeBuilder, which is nested in the class Shape class. Methods
 * and constructors in class Shape are already tested in RectangleTest and EllipseTest.
 */
public class ShapeTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testBuilderNullPointerExceptionSetKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key can't be null.");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setKey(null);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetWidth() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Width can't be negative.");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setWidth(-5);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetHeight() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Height can't be negative.");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setHeight(-5);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetRedNegative() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid red color, not within [0,255].");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setRed(-5);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetRedOver255() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid red color, not within [0,255].");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setRed(300);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetGreenNegative() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid green color, not within [0,255].");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setGreen(-5);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetGreenOver255() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid green color, not within [0,255].");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setGreen(300);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetBlueNegative() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid blue color, not within [0,255].");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setBlue(-5);
  }

  @Test
  public void testBuilderIllegalArgumentExceptionSetBlueOver255() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid blue color, not within [0,255].");
    Shape.ShapeBuilder test = new Shape.ShapeBuilder().setBlue(300);
  }
}