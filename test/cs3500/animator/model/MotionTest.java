package cs3500.animator.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class MotionTest {

  private ShapeModel sRectangle = new Rectangle(new Shape.ShapeBuilder()
          .setKey("Oh hi there grader").setX(100).setY(100).setWidth(10).setHeight(10).setRed(0)
          .setGreen(0).setBlue(0).setTheta(0));

  private KeyFrame a1 = new KeyFrame(0, this.sRectangle);
  private KeyFrame a2 = new KeyFrame(10, new Rectangle(
          new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(0).setY(0)
                  .setWidth(0).setHeight(0).setRed(255).setGreen(255).setBlue(255).setTheta(360)));

  private MotionModel motionModel = new Motion(this.a1, this.a2);

  @Test
  public void testGetShape() {
    ShapeModel s1 = this.motionModel.getShape(this.sRectangle, 0).makeCopy();
    ShapeModel s2 = this.motionModel.getShape(this.sRectangle, 5).makeCopy();
    ShapeModel s3 = this.motionModel.getShape(this.sRectangle, 10).makeCopy();

    assertEquals("100.0 100.0 10.0 10.0 0.0 0.0 0.0 0.0", s1.toString());
    assertEquals("50.0 50.0 5.0 5.0 127.5 127.5 127.5 180.0", s2.toString());
    assertEquals("0.0 0.0 0.0 0.0 255.0 255.0 255.0 360.0", s3.toString());

  }

  @Test
  public void testToString() {
    assertEquals("motion Oh hi there grader 0.0 100.0 100.0 10.0 10.0 0.0 0.0 0.0 0.0"
            + " 10.0 0.0 0.0 0.0 0.0 255.0 255.0 255.0 360.0\n", this.motionModel.toString());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructorExceptionNullKeyFrame() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key frames can't be null.");
    Motion m = new Motion(null, null);
  }

  @Test
  public void testConstructorInvalidTimesException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Final time must be greater than or equal to initial time.");
    Motion m = new Motion(new KeyFrame(20, new Rectangle(new Shape.ShapeBuilder())),
            new KeyFrame(10, new Ellipse(new Shape.ShapeBuilder())));
  }
}