package cs3500.animator.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import cs3500.animator.util.AnimationBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for an Animation builder sub-class.
 */
public class AnimationBuilderTest {

  @Test
  public void testBuild() {
    Animation a1 = new Animation.Builder().build();
    Animation a2 = new Animation();

    assertEquals(a1.getCanvas().getHeight(), a2.getCanvas().getHeight());
    assertEquals(a1.getStringRepresentation(), a2.getStringRepresentation());
  }

  @Test
  public void testSetBounds() {
    Animation a1 = new Animation.Builder().setBounds(1, 2, 3, 4).build();
    Animation a2 = new Animation(new LinkedHashMap<>(), new Canvas(1, 2, 3, 4));

    assertEquals(a1.getCanvas().getX(), a2.getCanvas().getX());
    assertEquals(a1.getCanvas().getY(), a2.getCanvas().getY());
    assertEquals(a1.getCanvas().getWidth(), a2.getCanvas().getWidth());
    assertEquals(a1.getCanvas().getHeight(), a2.getCanvas().getHeight());
  }

  @Test
  public void testDeclareShape() {
    Animation a1 = new Animation.Builder().declareShape("hi grader", "rectangle")
            .declareShape("hi grader2", "ellipse").build();

    LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> map = new LinkedHashMap<>();
    map.put(new Rectangle(new Shape.ShapeBuilder().setKey("hi grader").build()), new ArrayList<>());
    map.put(new Ellipse(new Shape.ShapeBuilder().setKey("hi grader2").build()), new ArrayList<>());
    Animation a2 = new Animation(map, new Canvas());

    assertEquals(a1.getStringRepresentation(), a2.getStringRepresentation());
    assertEquals(a1.getShapes(0).toString(), a2.getShapes(0).toString());
  }

  @Test
  public void testAddMotion() {
    Animation a1 = new Animation.Builder()
            .declareShape("hi grader", "rectangle")
            .addMotion("hi grader", 0,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                    11, 12, 13, 14, 15)
            .declareShape("hi grader2", "ellipse")
            .addMotion("hi grader2", 0,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                    11, 12, 13, 14, 15).build();

    LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> map = new LinkedHashMap<>();
    map.put(new Rectangle(new Shape.ShapeBuilder().setKey("hi grader").build()),
            new ArrayList<>(Arrays.asList(new KeyFrame(0,
                            new Rectangle(new Shape.ShapeBuilder().setX(1).setY(2).setWidth(3)
                                    .setHeight(4).setRed(5).setGreen(6).setBlue(7)
                                    .setKey("hi grader"))),
                    new KeyFrame(8,
                            new Rectangle(new Shape.ShapeBuilder().setX(9).setY(10).setWidth(11)
                                    .setHeight(12).setRed(13).setGreen(14).setBlue(15)
                                    .setKey("hi grader"))))));
    map.put(new Ellipse(new Shape.ShapeBuilder().setKey("hi grader2").build()),
            new ArrayList<>(Arrays.asList(new KeyFrame(0,
                            new Ellipse(new Shape.ShapeBuilder().setX(1).setY(2).setWidth(3)
                                    .setHeight(4).setRed(5).setGreen(6).setBlue(7)
                                    .setKey("hi grader2"))),
                    new KeyFrame(8,
                            new Ellipse(new Shape.ShapeBuilder().setX(9).setY(10).setWidth(11)
                                    .setHeight(12).setRed(13).setGreen(14).setBlue(15)
                                    .setKey("hi grader2"))))));

    Animation a2 = new Animation(map, new Canvas());

    assertEquals(a1.getStringRepresentation(), a2.getStringRepresentation());
    assertEquals(a1.getShapes(0).toString(), a2.getShapes(0).toString());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testDeclareShapeNullException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape name and type can't be null.");
    AnimationBuilder test = new Animation.Builder().declareShape(null, null);
  }

  @Test
  public void testDeclareShapeDuplicateKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape's key already exists, try a different key.");
    AnimationBuilder test = new Animation.Builder().declareShape("hi", "rectangle")
            .declareShape("hi", "ellipse");
  }

  @Test
  public void testDeclareShapeUnsupportedType() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unsupported shape type.");
    AnimationBuilder test = new Animation.Builder().declareShape("hi", "triangle");
  }

  @Test
  public void testAddMotionNullException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Key can't be null.");
    AnimationBuilder test = new Animation.Builder().addMotion(null, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }

  @Test
  public void testAddMotionNoShapeWithKey() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Shape of the given key doesn't exist.");
    AnimationBuilder test = new Animation.Builder().addMotion("a", 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }
}
