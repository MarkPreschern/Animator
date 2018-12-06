package cs3500.animator.view;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.Timer;

import cs3500.animator.model.Animation;
import cs3500.animator.model.Canvas;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.ImmutableAnimation;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.KeyFrame;
import cs3500.animator.model.KeyFrameModel;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;
import cs3500.animator.model.ShapeModel;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for methods and constructors in the text view class.
 */
public class TextViewTest {

  @Test
  public void testMakeVisible() {
    Shape sRectangle = new Rectangle(new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(100)
            .setY(100).setWidth(10).setHeight(10).setRed(0).setGreen(0).setBlue(0).build());
    Shape sEllipse = new Ellipse(new Shape.ShapeBuilder().setKey("Oh hi there grader2").setX(50)
            .setY(50).setWidth(5).setHeight(5).setRed(255).setGreen(255).setBlue(255).build());
    Shape sRectangleDefault = new Rectangle(new Shape.ShapeBuilder());

    KeyFrame aColorK1 = new KeyFrame(0, sRectangle);
    KeyFrame aColorK2 = new KeyFrame(10, new Rectangle(
            new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(100).setY(100)
                    .setWidth(10).setHeight(10).setRed(255).setGreen(0).setBlue(100).build()));

    KeyFrame aColor2K1 = new KeyFrame(0, sEllipse);
    KeyFrame aColor2K2 = new KeyFrame(10, new Ellipse(
            new Shape.ShapeBuilder().setKey("Oh hi there grader2").setX(50).setY(50)
                    .setWidth(5).setHeight(5).setRed(255).setGreen(0).setBlue(100).build()));

    KeyFrame aFadeK1 = new KeyFrame(10, sEllipse);
    KeyFrame aFadeK2 = new KeyFrame(12, new Ellipse(
            new Shape.ShapeBuilder().setKey("Oh hi there grader2").setX(50).setY(50)
                    .setWidth(5).setHeight(5).setRed(255).setGreen(255).setBlue(255).build()));

    KeyFrame aMoveK1 = new KeyFrame(0, sRectangleDefault);
    KeyFrame aMoveK2 = new KeyFrame(20, new Rectangle(
            new Shape.ShapeBuilder().setX(20).setY(40)));

    KeyFrame aSizeK1 = new KeyFrame(10, sRectangle);
    KeyFrame aSizeK2 = new KeyFrame(30, new Rectangle(
            new Shape.ShapeBuilder().setKey("Oh hi there grader").setX(100).setY(100)
                    .setWidth(1).setHeight(20).setRed(0).setGreen(0).setBlue(0).build()));

    LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> map = new LinkedHashMap<>();
    map.put(sRectangle, new ArrayList<>(Arrays.asList(aColorK1, aColorK2, aSizeK1, aSizeK2)));
    map.put(sEllipse, new ArrayList<>(Arrays.asList(aColor2K1, aColor2K2, aFadeK1, aFadeK2)));
    map.put(sRectangleDefault, new ArrayList<>(Arrays.asList(aMoveK1, aMoveK2)));

    ImmutableModel model = new ImmutableAnimation(new Animation(map, new Canvas()));
    Appendable ap = new StringBuilder();
    TextView view = new TextView(model, ap);

    view.makeVisible();
    assertEquals("Shape Oh hi there grader Rectangle\n"
            + "motion Oh hi there grader 0.0 100.0 100.0 10.0 10.0 0.0 0.0 0.0 10.0 100.0"
            + " 100.0 10.0 10.0 0.0 0.0 0.0\n"
            + "motion Oh hi there grader 10.0 100.0 100.0 10.0 10.0 0.0 0.0 0.0 30.0 100.0"
            + " 100.0 1.0 20.0 0.0 0.0 0.0\n"
            + "\n"
            + "Shape Oh hi there grader2 Ellipse\n"
            + "motion Oh hi there grader2 0.0 50.0 50.0 5.0 5.0 255.0 255.0 255.0 10.0 50.0"
            + " 50.0 5.0 5.0 255.0 255.0 255.0\n"
            + "motion Oh hi there grader2 10.0 50.0 50.0 5.0 5.0 255.0 255.0 255.0 12.0 50.0"
            + " 50.0 5.0 5.0 255.0 255.0 255.0\n"
            + "\n"
            + "Shape Default Rectangle\n"
            + "motion Default 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 20.0 20.0 40.0 0.0 0.0 0.0 0.0"
            + " 0.0", ap.toString());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructorNullAppendableObject() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Appendable object and model can't be null.");
    TextView test = new TextView(new ImmutableAnimation(new Animation()), null);
  }

  @Test
  public void testConstructorNullModel() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Appendable object and model can't be null.");
    TextView test = new TextView(null, new StringBuilder());
  }

  @Test
  public void testConstructorNullAppendableObjectAndModel() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Appendable object and model can't be null.");
    TextView test = new TextView(null, null);
  }

  @Test
  public void testAnimateException() {
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("Text view has no graphical display to animate on.");
    TextView test = new TextView(new ImmutableAnimation(new Animation()), new StringBuilder());
    test.animate(new Timer(0, null));
  }

  @Test
  public void testIOException() throws IOException {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Unable to successfully transmit output.");

    FileWriter append = new FileWriter(new File("test.txt"));
    append.close();

    TextView test = new TextView(new ImmutableAnimation(new Animation()), append);
    test.makeVisible();
  }
}