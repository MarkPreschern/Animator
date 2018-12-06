package cs3500.animator.view;

import cs3500.animator.model.ImmutableModel;

/**
 * Represents a factory of views, creating a specific view based on the type given.
 */
public class ViewFactory {

  /**
   * Creates a specific view based on the type, using the other fields given to instantiate it.
   *
   * @param type  the type of view
   * @param model an immutable model
   * @param out   the output type
   * @param speed the speed of the animation
   * @return a view
   * @throws IllegalArgumentException if any null inputs are given, if given an invalid view type,
   *                                  or if speed isn't greater than zero
   */
  public static View getView(String type, ImmutableModel model, Appendable out, int speed)
          throws IllegalArgumentException {
    if (type == null || model == null || out == null) {
      throw new IllegalArgumentException("View type, model, and appendable object can't be null.");
    } else if (speed <= 0) {
      throw new IllegalArgumentException("Speed must be greater than zero.");
    }

    switch (type) {
      case "text":
        return new TextView(model, out);
      case "visual":
        return new VisualView(model, speed);
      case "svg":
        return new SVGView(model, out, speed);
      case "edit":
        return new VisualViewEditable(new VisualView(model, speed));
      default:
        throw new IllegalArgumentException("Invalid view type.");
    }
  }
}
