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
 * Represents tests for methods and constructors in the svg view class.
 */
public class SVGViewTest {

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
    SVGView view = new SVGView(model, ap, 5);

    view.makeVisible();
    assertEquals("<svg width=\"500\" height=\"500\" version=\"1.1\""  
          + " xmlns=\"http://www.w3.org/2000/svg\">\n"  
          + "<rect x=\"100.000000\" y=\"100.000000\" width=\"10.000000\" height=\"10.000000\""  
          + " fill=\"rgb(    0,     0,      0)\" visibility=\"visible\" >\n"  
          + "<animate attributeName=\"x\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"100.000000\" to=\"100.000000\" />\n"  
          + "<animate attributeName=\"y\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"100.000000\" to=\"100.000000\" />\n"  
          + "<animate attributeName=\"width\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"10.000000\" to=\"10.000000\" />\n"  
          + "<animate attributeName=\"height\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"10.000000\" to=\"10.000000\" />\n"  
          + "<animate attributeName=\"fill\" attributeType=\"CSS\" from=\"rgb(0, 0,  0)\""  
          + " to =\"rgb(   0,    0,     0)\" begin = \"0.000000s\" dur = \"2.000000s\""  
          + " fill = \"freeze\" />\n"  
          + "<animateTransform attributeName=\"transform\" attributeType=\"XML\""  
          + " type=\"rotate\" from=\"0.000000 105.000000 105.000000\""  
          + " to=\"0.000000 105.000000 105.000000\" dur=\"2.000000s\" repeatCount=\"0\"/>\n"  
          + "\n"  
          + "<animate attributeName=\"x\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"100.000000\" to=\"100.000000\" />\n"  
          + "<animate attributeName=\"y\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"100.000000\" to=\"100.000000\" />\n"  
          + "<animate attributeName=\"width\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"10.000000\" to=\"1.000000\" />\n"  
          + "<animate attributeName=\"height\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"10.000000\" to=\"20.000000\" />\n"  
          + "<animate attributeName=\"fill\" attributeType=\"CSS\" from=\"rgb(0, 0,  0)\""  
          + " to =\"rgb(   0,    0,     0)\" begin = \"2.000000s\" dur = \"4.000000s\""  
          + " fill = \"freeze\" />\n"  
          + "<animateTransform attributeName=\"transform\" attributeType=\"XML\""  
          + " type=\"rotate\" from=\"0.000000 105.000000 105.000000\""  
          + " to=\"0.000000 100.500000 110.000000\" dur=\"4.000000s\" repeatCount=\"0\"/>\n"  
          + "\n"  
          + "</rect>\n"  
          + "<ellipse cx=\"52.500000\" cy=\"52.500000\" rx=\"2.500000\" ry=\"2.500000\""  
          + " fill=\"rgb(  255,   255,    255)\" visibility=\"visible\" >\n"  
          + "<animate attributeName=\"cx\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"52.500000\" to=\"52.500000\" />\n"  
          + "<animate attributeName=\"cy\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"52.500000\" to=\"52.500000\" />\n"  
          + "<animate attributeName=\"rx\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"2.500000\" to=\"2.500000\" />\n"  
          + "<animate attributeName=\"ry\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"2.000000s\" fill=\"freeze\" from=\"2.500000\" to=\"2.500000\" />\n"  
          + "<animate attributeName=\"fill\" attributeType=\"CSS\" from=\"rgb(255,255,255)\""  
          + " to =\"rgb( 255,  255,   255)\" begin = \"0.000000s\" dur = \"2.000000s\""  
          + " fill = \"freeze\" />\n"  
          + "<animateTransform attributeName=\"transform\" attributeType=\"XML\""  
          + " type=\"rotate\" from=\"0.000000 52.500000 52.500000\""  
          + " to=\"0.000000 52.500000 52.500000\" dur=\"2.000000s\" repeatCount=\"0\"/>\n"  
          + "\n"  
          + "<animate attributeName=\"cx\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"0.400000s\" fill=\"freeze\" from=\"52.500000\" to=\"52.500000\" />\n"  
          + "<animate attributeName=\"cy\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"0.400000s\" fill=\"freeze\" from=\"52.500000\" to=\"52.500000\" />\n"  
          + "<animate attributeName=\"rx\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"0.400000s\" fill=\"freeze\" from=\"2.500000\" to=\"2.500000\" />\n"  
          + "<animate attributeName=\"ry\" attributeType=\"XML\" begin=\"2.000000s\""  
          + " dur=\"0.400000s\" fill=\"freeze\" from=\"2.500000\" to=\"2.500000\" />\n"  
          + "<animate attributeName=\"fill\" attributeType=\"CSS\" from=\"rgb(255,255,255)\""  
          + " to =\"rgb( 255,  255,   255)\" begin = \"2.000000s\" dur = \"0.400000s\""  
          + " fill = \"freeze\" />\n"  
          + "<animateTransform attributeName=\"transform\" attributeType=\"XML\""  
          + " type=\"rotate\" from=\"0.000000 52.500000 52.500000\""  
          + " to=\"0.000000 52.500000 52.500000\" dur=\"0.400000s\" repeatCount=\"0\"/>\n"  
          + "\n"  
          + "</ellipse>\n"  
          + "<rect x=\"0.000000\" y=\"0.000000\" width=\"0.000000\" height=\"0.000000\""  
          + " fill=\"rgb(    0,     0,      0)\" visibility=\"visible\" >\n"  
          + "<animate attributeName=\"x\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"0.000000\" to=\"20.000000\" />\n"  
          + "<animate attributeName=\"y\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"0.000000\" to=\"40.000000\" />\n"  
          + "<animate attributeName=\"width\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"0.000000\" to=\"0.000000\" />\n"  
          + "<animate attributeName=\"height\" attributeType=\"XML\" begin=\"0.000000s\""  
          + " dur=\"4.000000s\" fill=\"freeze\" from=\"0.000000\" to=\"0.000000\" />\n"  
          + "<animate attributeName=\"fill\" attributeType=\"CSS\" from=\"rgb(0, 0,  0)\""  
          + " to =\"rgb(   0,    0,     0)\" begin = \"0.000000s\" dur = \"4.000000s\""  
          + " fill = \"freeze\" />\n"  
          + "<animateTransform attributeName=\"transform\" attributeType=\"XML\""  
          + " type=\"rotate\" from=\"0.000000 0.000000 0.000000\""  
          + " to=\"0.000000 20.000000 40.000000\" dur=\"4.000000s\" repeatCount=\"0\"/>\n"  
          + "\n"  
          + "</rect>\n"  
          + "</svg>", ap.toString());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructorNullAppendableObject() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Model and appendable object can't be null.");
    SVGView test = new SVGView(new ImmutableAnimation(new Animation()), null, 1);
  }

  @Test
  public void testConstructorNullModel() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Model and appendable object can't be null.");
    SVGView test = new SVGView(null, new StringBuilder(), 1);
  }

  @Test
  public void testConstructorNullAppendableObjectAndModel() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Model and appendable object can't be null.");
    SVGView test = new SVGView(null, null, 1);
  }

  @Test
  public void testConstructorInvalidSpeed() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Speed must be greater than zero.");
    SVGView test = new SVGView(new ImmutableAnimation(new Animation()), new StringBuilder(), -1);
  }

  @Test
  public void testAnimateException() {
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("SVG view has no graphical display to animate on.");
    SVGView test = new SVGView(new ImmutableAnimation(new Animation()), new StringBuilder(), 1);
    test.animate(new Timer(0, null));
  }

  @Test
  public void testIOException() throws IOException {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Unable to successfully transmit output.");

    FileWriter append = new FileWriter(new File("test.txt"));
    append.close();

    SVGView test = new SVGView(new ImmutableAnimation(new Animation()), append, 1);
    test.makeVisible();
  }
}