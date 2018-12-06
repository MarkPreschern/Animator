package cs3500.animator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Timer;

import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.KeyFrameModel;
import cs3500.animator.model.Motion;
import cs3500.animator.model.MotionModel;
import cs3500.animator.model.Shape;
import cs3500.animator.model.ShapeModel;

/**
 * Represents a textual description of an animation compliant with the SVG file format.
 */
public class SVGView implements View {

  private ImmutableModel model;
  private Appendable ap;
  private int speed;

  /**
   * Constructor initializes a new SVG view given an immutable model and an appendable object.
   *
   * @param model an immutable model
   * @param ap    an appendable object
   */
  public SVGView(ImmutableModel model, Appendable ap, int speed) {
    if (model == null || ap == null) {
      throw new IllegalArgumentException("Model and appendable object can't be null.");
    } else if (speed <= 0) {
      throw new IllegalArgumentException("Speed must be greater than zero.");
    }

    this.model = model;
    this.ap = ap;
    this.speed = speed;
  }

  @Override
  public void makeVisible() throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("<svg width=\"%1d\" height=\"%2d\" version=\"1.1\" xmlns="
                    + "\"http://www.w3.org/2000/svg\">", this.model.getCanvas().getWidth(),
            this.model.getCanvas().getHeight())).append("\n");

    //Stores shape and animation information using the SVG file format in a string builder
    for (Map.Entry<ShapeModel, ArrayList<KeyFrameModel>> entry :
            this.model.getShapeKeyFrameRepresentation().entrySet()) {
      ShapeModel shape = entry.getKey();
      ArrayList<KeyFrameModel> value = entry.getValue();
      sb.append(this.shapeAsSVG(shape)).append("\n");

      for (int i = 0; i < value.size() - 1; i++) {
        sb.append(this.motionAsSVG(new Motion(value.get(i), value.get(i + 1)))).append("\n");
      }
      switch (shape.getType()) {
        case "Rectangle":
          sb.append("</rect>");
          break;
        case "Ellipse":
          sb.append("</ellipse>");
          break;
        default:
          throw new IllegalArgumentException("Unsupported shape type.");
      }
      sb.append("\n");
    }

    sb.append("</svg>");

    //tries to append the string builder's information onto the appendable object.
    try {
      ap.append(sb.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Unable to successfully transmit output.");
    }
  }

  @Override
  public void animate(Timer timer) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("SVG view has no graphical display to animate on.");
  }

  /**
   * Constructs a string representation for a shape using the SVG file format.
   *
   * @param shape the given shape
   * @return a string representation
   * @throws IllegalArgumentException if given an unsupported shape
   */
  private String shapeAsSVG(ShapeModel shape) throws IllegalArgumentException {
    switch (shape.getType()) {
      case "Rectangle":
        return String.format("<rect x=\"%1f\" y=\"%2f\" width=\"%3f\" height=\"%4f\""
                        + " fill=\"rgb(%5d,%6d,%7d)\" visibility=\"visible\" >",
                shape.getPointTopLeft().x, shape.getPointTopLeft().y, shape.getDimensions().x,
                shape.getDimensions().y, shape.getColor().getRed(), shape.getColor().getGreen(),
                shape.getColor().getBlue());
      case "Ellipse":
        return String.format("<ellipse cx=\"%1f\" cy=\"%2f\" rx=\"%3f\" ry=\"%4f\""
                        + " fill=\"rgb(%5d,%6d,%7d)\" visibility=\"visible\" >",
                shape.getPointTopLeft().x + (shape.getDimensions().x / 2),
                shape.getPointTopLeft().y + (shape.getDimensions().y / 2),
                shape.getDimensions().x / 2, shape.getDimensions().y / 2, shape.getColor().getRed(),
                shape.getColor().getGreen(), shape.getColor().getBlue());
      default:
        throw new IllegalArgumentException("Unsupported shape type.");
    }
  }

  /**
   * Constructs a string representation for a motion using the SVG file format.
   *
   * @param motion the motion shape
   * @return a string representation
   * @throws IllegalArgumentException if given an unsupported shape for the motion
   */
  private String motionAsSVG(MotionModel motion) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();

    Shape initialShape = (Shape) motion.getInitial().getShape();
    Shape finalShape = (Shape) motion.getFinal().getShape();

    double initialTime = motion.getTimeInterval().x * (1 / (double) this.speed);
    double duration = (motion.getTimeInterval().y - motion.getTimeInterval().x)
            * (1 / (double) this.speed);

    //sets attributes specific to shape type
    switch (initialShape.getType()) {
      case "Rectangle":
        sb.append(String.format("<animate attributeName=\"%1s\" attributeType=\"XML\""
                        + " begin=\"%2fs\" dur=\"%3fs\" fill=\"freeze\" from=\"%4f\" to=\"%5f\" />",
                "x", initialTime, duration,
                initialShape.getPointTopLeft().x - this.model.getCanvas().getX(),
                finalShape.getPointTopLeft().x - this.model.getCanvas().getX())).append("\n");
        sb.append(String.format("<animate attributeName=\"%1s\" attributeType=\"XML\""
                        + " begin=\"%2fs\" dur=\"%3fs\" fill=\"freeze\" from=\"%4f\" to=\"%5f\" />",
                "y", initialTime, duration,
                initialShape.getPointTopLeft().y - this.model.getCanvas().getY(),
                finalShape.getPointTopLeft().y - this.model.getCanvas().getY())).append("\n");
        sb.append(String.format("<animate attributeName=\"width\" attributeType=\"XML\""
                        + " begin=\"%1fs\" dur=\"%2fs\" fill=\"freeze\" from=\"%3f\" to=\"%4f\" />",
                initialTime, duration, initialShape.getDimensions().x,
                finalShape.getDimensions().x)).append("\n");
        sb.append(String.format("<animate attributeName=\"height\" attributeType=\"XML\""
                        + " begin=\"%1fs\" dur=\"%2fs\" fill=\"freeze\" from=\"%3f\" to=\"%4f\" />",
                initialTime, duration, initialShape.getDimensions().y,
                finalShape.getDimensions().y)).append("\n");
        break;
      case "Ellipse":
        sb.append(String.format("<animate attributeName=\"%1s\" attributeType=\"XML\""
                        + " begin=\"%2fs\" dur=\"%3fs\" fill=\"freeze\" from=\"%4f\" to=\"%5f\" />",
                "cx", initialTime, duration,
                initialShape.getPointTopLeft().x - this.model.getCanvas().getX()
                        + (initialShape.getDimensions().x / 2),
                finalShape.getPointTopLeft().x - this.model.getCanvas().getX()
                        + (finalShape.getDimensions().x / 2))).append("\n");
        sb.append(String.format("<animate attributeName=\"%1s\" attributeType=\"XML\""
                        + " begin=\"%2fs\" dur=\"%3fs\" fill=\"freeze\" from=\"%4f\" to=\"%5f\" />",
                "cy", initialTime, duration,
                initialShape.getPointTopLeft().y - this.model.getCanvas().getY()
                        + (initialShape.getDimensions().y / 2),
                finalShape.getPointTopLeft().y - this.model.getCanvas().getY()
                        + (finalShape.getDimensions().y / 2))).append("\n");
        sb.append(String.format("<animate attributeName=\"rx\" attributeType=\"XML\""
                        + " begin=\"%1fs\" dur=\"%2fs\" fill=\"freeze\" from=\"%3f\" to=\"%4f\" />",
                initialTime, duration, initialShape.getDimensions().x / 2,
                finalShape.getDimensions().x / 2)).append("\n");
        sb.append(String.format("<animate attributeName=\"ry\" attributeType=\"XML\""
                        + " begin=\"%1fs\" dur=\"%2fs\" fill=\"freeze\" from=\"%3f\" to=\"%4f\" />",
                initialTime, duration, initialShape.getDimensions().y / 2,
                finalShape.getDimensions().y / 2)).append("\n");
        break;
      default:
        throw new IllegalArgumentException("Unsupported shape type.");
    }

    sb.append(String.format("<animate attributeName=\"fill\" attributeType=\"CSS\""
                    + " from=\"rgb(%1d,%2d,%3d)\" to =\"rgb(%4d,%5d,%6d)\""
                    + " begin = \"%7fs\" dur = \"%8fs\" fill = \"freeze\" />",
            initialShape.getColor().getRed(), initialShape.getColor().getGreen(),
            initialShape.getColor().getBlue(), finalShape.getColor().getRed(),
            finalShape.getColor().getGreen(), finalShape.getColor().getBlue(),
            initialTime, duration)).append("\n");

    sb.append(String.format("<animateTransform attributeName=\"transform\" attributeType=\"XML\""
                    + " type=\"rotate\" from=\"%1f %2f %3f\" to=\"%4f %5f %6f\" dur=\"%7fs\""
                    + " repeatCount=\"0\"/>",
            initialShape.getTheta(),
            initialShape.getPointTopLeft().x + initialShape.getDimensions().x / 2,
            initialShape.getPointTopLeft().y + initialShape.getDimensions().y / 2,
            finalShape.getTheta(),
            finalShape.getPointTopLeft().x + finalShape.getDimensions().x / 2,
            finalShape.getPointTopLeft().y + finalShape.getDimensions().y / 2,
            duration)).append("\n");

    return sb.toString();
  }
}
