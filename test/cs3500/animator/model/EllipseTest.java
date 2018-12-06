package cs3500.animator.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.Color;
import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for methods and constructors in the ellipse class.
 */
public class EllipseTest {

  private Shape s1;
  private Shape s2;

  /**
   * Initializes private variables.
   */
  private void init() {
    s1 = new Ellipse(new Shape.ShapeBuilder());
    s2 = new Ellipse(new Shape.ShapeBuilder().setKey("hi grader").setX(50).setY(20)
        .setRed(1).setGreen(100).setBlue(40).setWidth(5).setHeight(4));
  }

  @Test
  public void testGetPointTopLeft() {
    this.init();

    assertEquals(new Point2D.Double(0, 0), s1.getPointTopLeft());
    assertEquals(new Point2D.Double(50, 20), s2.getPointTopLeft());
  }

  @Test
  public void testGetDimensions() {
    this.init();

    assertEquals(new Point2D.Double(0, 0), s1.getDimensions());
    assertEquals(new Point2D.Double(5, 4), s2.getDimensions());
  }

  @Test
  public void testGetColor() {
    this.init();

    assertEquals(new Color(0, 0, 0), s1.getColor());
    assertEquals(new Color(1, 100, 40), s2.getColor());
  }

  @Test
  public void testToString() {
    this.init();

    assertEquals("0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0", s1.toString());
    assertEquals("50.0 20.0 5.0 4.0 1.0 100.0 40.0 0.0", s2.toString());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructorExceptionNullBuilder() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape builder can't be null.");
    Shape shape = new Ellipse((Shape.ShapeBuilder) null);
  }
}