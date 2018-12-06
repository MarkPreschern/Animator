package cs3500.animator.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for an Animation's constructors and public methods.
 */
public class AnimationTest {

  private Shape sRectangle;
  private Shape sEllipse;
  private Shape sRectangleDefault;

  private KeyFrame aColorK1;
  private KeyFrame aColorK2;
  private KeyFrame aColor2K1;
  private KeyFrame aColor2K2;
  private KeyFrame aSizeK1;
  private KeyFrame aSizeK2;


  private LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> hashMap;

  private Animation am1;
  private Animation am2;

  /**
   * Initializes private variables.
   */
  private void init() {
    this.sRectangle = new Rectangle(new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(100)
            .setY(100).setWidth(10).setHeight(10).setRed(0).setGreen(0).setBlue(0).build());
    this.sEllipse = new Ellipse(new Shape.ShapeBuilder().setKey("Oh hi there grader2").setX(50)
            .setY(50).setWidth(5).setHeight(5).setRed(255).setGreen(255).setBlue(255).build());
    this.sRectangleDefault = new Rectangle(new Shape.ShapeBuilder());

    this.aColorK1 = new KeyFrame(0, sRectangle);
    this.aColorK2 = new KeyFrame(10, new Rectangle(
            new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(100).setY(100)
                    .setWidth(10).setHeight(10).setRed(255).setGreen(0).setBlue(100).build()));

    this.aColor2K1 = new KeyFrame(0, sEllipse);
    this.aColor2K2 = new KeyFrame(10, new Ellipse(
            new Shape.ShapeBuilder().setKey("Oh hi there grader2").setX(50).setY(50)
                    .setWidth(5).setHeight(5).setRed(255).setGreen(0).setBlue(100).build()));

    KeyFrame aFadeK1 = new KeyFrame(10, sEllipse);
    KeyFrame aFadeK2 = new KeyFrame(12, new Ellipse(
            new Shape.ShapeBuilder().setKey("Oh hi there grader2").setX(50).setY(50)
                    .setWidth(5).setHeight(5).setRed(255).setGreen(255).setBlue(255).build()));

    KeyFrame aMoveK1 = new KeyFrame(0, sRectangleDefault);
    KeyFrame aMoveK2 = new KeyFrame(20, new Rectangle(
            new Shape.ShapeBuilder().setX(20).setY(40)));

    KeyFrame aRotateK1 = new KeyFrame(0, sRectangle);
    KeyFrame aRotateK2 = new KeyFrame(0, new Rectangle(
            new Shape.ShapeBuilder().setTheta(360).setKey("Oh hi there grader3")));

    this.aSizeK1 = new KeyFrame(45, sRectangle);
    this.aSizeK2 = new KeyFrame(60, new Rectangle(
            new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(100).setY(100)
                    .setWidth(1).setHeight(20).setRed(0).setGreen(0).setBlue(0).build()));

    this.hashMap = new LinkedHashMap<>();
    this.hashMap.put(this.sRectangle, new ArrayList<>(Arrays.asList(this.aColorK1, this.aColorK2,
            aFadeK1, aFadeK2, aMoveK1, aMoveK2, this.aSizeK1, this.aSizeK2)));
    this.hashMap.put(this.sEllipse, new ArrayList<>(Arrays.asList(this.aColor2K1, this.aColor2K2,
            aFadeK1, aFadeK2)));
    this.hashMap.put(this.sRectangleDefault, new ArrayList<>());

    this.am1 = new Animation();
    this.am2 = new Animation(this.hashMap, new Canvas());
  }

  @Test
  public void testAddShape() {
    this.init();

    this.am1.addShape(this.sEllipse);
    assertEquals(new ArrayList<>(Collections.singletonList(this.sEllipse)),
            this.am1.getShapes());
    this.am1.addShape(this.sRectangleDefault);
    assertEquals(new ArrayList<>(Arrays.asList(this.sEllipse, this.sRectangleDefault)),
            this.am1.getShapes());
  }

  @Test
  public void testRemoveShape() {
    this.init();

    this.am2.removeShape(this.sEllipse.key);
    assertEquals(new ArrayList<>(Arrays.asList(this.sRectangle, this.sRectangleDefault)),
            this.am2.getShapes());
    this.am2.removeShape(this.sRectangleDefault.key);
    assertEquals(new ArrayList<>(Collections.singleton(this.sRectangle)), this.am2.getShapes());
  }

  @Test
  public void testAddMotion() {
    this.init();

    Motion a1 = new Motion(new KeyFrame(0, this.sRectangleDefault),
            new KeyFrame(10, this.sRectangleDefault));
    Motion a2 = new Motion(new KeyFrame(30, this.sRectangle),
            new KeyFrame(40, this.sRectangle));

    assertEquals(9, this.am2.getKeyFrames().size());
    this.am2.addMotion("Default", a1);
    assertEquals(11, this.am2.getKeyFrames().size());
    this.am2.addMotion("Oh hi there grader", a2);
    assertEquals(13, this.am2.getKeyFrames().size());
  }

  @Test
  public void testRemoveMotion() {
    this.init();

    assertEquals(9, this.am2.getKeyFrames().size());
    this.am2.removeMotion("Oh hi there grader2", new Motion(this.aColor2K1, this.aColor2K2));
    assertEquals(8, this.am2.getKeyFrames().size());
    this.am2.removeMotion("Oh hi there grader", new Motion(this.aSizeK1, this.aSizeK2));
    assertEquals(6, this.am2.getKeyFrames().size());
  }

  @Test
  public void testGetKeyFrames() {
    this.init();

    assertEquals(0, am1.getKeyFrames().size());
    assertEquals(9, am2.getKeyFrames().size());
  }

  @Test
  public void testAddKeyFrame() {
    this.init();

    assertEquals(9, this.am2.getKeyFrames().size());
    this.am2.addKeyFrame("Oh hi there grader", new KeyFrame(50, new Rectangle(
            new Shape.ShapeBuilder().setKey("Oh hi there grader").build())));
    assertEquals(10, this.am2.getKeyFrames().size());
  }

  @Test
  public void testRemoveKeyFrame() {
    this.init();

    assertEquals(9, this.am2.getKeyFrames().size());
    this.am2.removeKeyFrame("Oh hi there grader", this.am2.getKeyFrames().get(0));
    assertEquals(8, this.am2.getKeyFrames().size());
  }

  @Test
  public void testGetShapesAtTime() {
    this.init();

    //asserts the originally instantiated shapes are so
    assertEquals(new ArrayList<>(Arrays.asList(this.sRectangle, this.sEllipse,
            this.sRectangleDefault)), this.am2.getShapes());


    //asserts that the shapes have their default values, as per their first animation
    assertEquals("[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0, 50.0 50.0 5.0 5.0 255.0 255.0 255.0"
            + " 0.0]", this.am2.getShapes(0).toString());

    //asserts what the shapes are after the tick()
    assertEquals("4.999999999999999 4.999999999999999 0.4999999999999999"
                    + " 0.4999999999999999 25.499999999999993 25.499999999999993 25.499999999999993 0.0",
            this.am2.getShapes(1).get(0).toString());
  }

  @Test
  public void testGetStringRepresentation() {
    this.init();

    assertEquals("", this.am1.getStringRepresentation());

    assertEquals("Shape Oh hi there grader Rectangle\n"
            + "motion Default 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 10.0 50.0 50.0 5.0 5.0 255.0"
            + " 255.0 255.0 0.0\n"
            + "motion Oh hi there grader2 10.0 50.0 50.0 5.0 5.0 255.0 255.0 255.0 0.0 12.0 50.0"
            + " 50.0 5.0 5.0 255.0 255.0 255.0 0.0\n"
            + "motion Oh hi there grader2 12.0 50.0 50.0 5.0 5.0 255.0 255.0 255.0 0.0 20.0 20.0"
            + " 40.0 0.0 0.0 0.0 0.0 0.0 0.0\n"
            + "motion Default 20.0 20.0 40.0 0.0 0.0 0.0 0.0 0.0 0.0 45.0 100.0 100.0 10.0 10.0"
            + " 0.0 0.0 0.0 0.0\n"
            + "motion Oh hi there grader 45.0 100.0 100.0 10.0 10.0 0.0 0.0 0.0 0.0 60.0 100.0"
            + " 100.0 1.0 20.0 0.0 0.0 0.0 0.0\n"
            + "\n"
            + "Shape Oh hi there grader2 Ellipse\n"
            + "motion Oh hi there grader2 0.0 50.0 50.0 5.0 5.0 255.0 255.0 255.0 0.0 10.0 50.0"
            + " 50.0 5.0 5.0 255.0 255.0 255.0 0.0\n"
            + "motion Oh hi there grader2 10.0 50.0 50.0 5.0 5.0 255.0 255.0 255.0 0.0 12.0 50.0"
            + " 50.0 5.0 5.0 255.0 255.0 255.0 0.0\n"
            + "\n"
            + "Shape Default Rectangle", this.am2.getStringRepresentation());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructor2NullPointerExceptionAnimation() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key frames and canvas can't be null.");
    this.init();
    Animation test = new Animation(null, new Canvas());
  }

  @Test
  public void testConstructor2NullPointerExceptionCanvas() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key frames and canvas can't be null.");
    this.init();
    Animation test = new Animation(this.hashMap, null);
  }

  @Test
  public void testAddShapeNullPointerException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape can't be null.");
    this.init();
    this.am1.addShape(null);
  }

  @Test
  public void testAddShapeIllegalArgumentException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape's key already exists, try a different key.");
    this.init();
    this.am1.addShape(this.sEllipse);
    this.am1.addShape(this.sEllipse);
  }

  @Test
  public void testRemoveShapeNullPointerException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key can't be null.");
    this.init();
    this.am1.removeShape(null);
  }

  @Test
  public void testRemoveShapeIllegalArgumentException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The given key doesn't exist.");
    this.init();
    this.am1.removeShape("non-existent key");
  }

  @Test
  public void testAddAnimationNullPointerExceptionKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Neither key or motion can be null.");
    this.init();
    this.am1.addMotion(null, new Motion(this.aColorK1, this.aColorK2));
  }

  @Test
  public void testAddAnimationNullPointerExceptionAnimation() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Neither key or motion can be null.");
    this.init();
    this.am1.addMotion("lol", null);
  }

  @Test
  public void testAddAnimationIllegalArgumentException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape of the given key doesn't exist.");
    this.init();
    this.am1.removeMotion("non-existent key", new Motion(this.aColorK1, this.aColorK2));
  }

  @Test
  public void testRemoveAnimationNullPointerExceptionKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Neither key or motion can be null.");
    this.init();
    this.am1.removeMotion(null, new Motion(this.aColorK1, this.aColorK2));
  }

  @Test
  public void testRemoveAnimationNullPointerExceptionAnimation() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Neither key or motion can be null.");
    this.init();
    this.am1.removeMotion("lol", null);
  }

  @Test
  public void testRemoveAnimationIllegalArgumentExceptionNonExistentKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape of the given key doesn't exist.");
    this.init();
    this.am1.removeMotion("non-existent key", new Motion(this.aColorK1, this.aColorK2));
  }

  @Test
  public void testRemoveAnimationIllegalArgumentExceptionNonExistentAnimation() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Motion isn't contained in this shape's list of key frames.");
    this.init();
    this.am2.removeMotion(this.sRectangleDefault.key, new Motion(this.aColorK1, this.aColorK2));
  }

  @Test
  public void testGetShapesIllegalArgumentExceptionTimeBelowZero() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Time can't be negative.");
    this.init();
    this.am2.getShapes(-1);
  }

  @Test
  public void testOverlappingMotion() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Final time must be greater than or equal to initial time.");
    this.init();
    this.am2.addMotion("Oh hi there grader", new Motion(new KeyFrame(50,
            this.sRectangleDefault), new KeyFrame(40,
            new Rectangle(new Shape.ShapeBuilder().setX(20).setY(40)))));
  }

  @Test
  public void testAddNullKeyFrameNull() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Neither key or frame can be null.");
    this.init();
    this.am2.addKeyFrame("Oh hi there grader", null);
  }

  @Test
  public void testRemoveKeyFrameNull() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Neither key or frame can be null.");
    this.init();
    this.am2.removeKeyFrame("Oh hi there grader", null);
  }

  @Test
  public void testAddKeyFrameInvalidKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape of the given key doesn't exist.");
    this.init();
    this.am2.addKeyFrame("invalid", this.am2.getKeyFrames().get(0));
  }

  @Test
  public void testRemoveKeyFrameInvalidKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape of the given key doesn't exist.");
    this.init();
    this.am2.removeKeyFrame("invalid", this.am2.getKeyFrames().get(0));
  }

  @Test
  public void testAddKeyFrameOverlappingTime() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key frame at time 0.0 already exists.");
    this.init();
    this.am2.addKeyFrame("Oh hi there grader", this.am2.getKeyFrames().get(0));
  }

}