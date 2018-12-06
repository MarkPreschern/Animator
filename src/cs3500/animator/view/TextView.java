package cs3500.animator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Timer;

import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.KeyFrameModel;
import cs3500.animator.model.Motion;
import cs3500.animator.model.ShapeModel;

/**
 * A text based view of the shapes and animations of an animation model.
 */
public class TextView implements View {

  private ImmutableModel model;
  private Appendable ap;

  /**
   * Default constructor takes in an appendable object and an immutable animation model.
   *
   * @param model the immutable animation model
   * @param ap    the appendable object
   * @throws IllegalArgumentException if appendable object or model is null
   */
  public TextView(ImmutableModel model, Appendable ap) throws IllegalArgumentException {
    if (ap == null || model == null) {
      throw new IllegalArgumentException("Appendable object and model can't be null.");
    }

    this.model = model;
    this.ap = ap;
  }

  @Override
  public void makeVisible() throws IllegalStateException {
    StringBuilder sb = new StringBuilder();

    //Stores shape and animation information in a string builder
    for (Map.Entry<ShapeModel, ArrayList<KeyFrameModel>> entry :
            this.model.getShapeKeyFrameRepresentation().entrySet()) {
      ShapeModel shape = entry.getKey();
      ArrayList<KeyFrameModel> value = entry.getValue();

      sb.append("Shape ").append(shape.getKey()).append(" ")
              .append(shape.getType()).append("\n");
      for (int i = 0; i < value.size() - 1; i++) {
        sb.append(new Motion(value.get(i), value.get(i + 1)).toString());
      }
      sb.append("\n");
    }

    //removes the last 2 \n characters
    for (int i = 0; i < 2; i++) {
      int last = sb.lastIndexOf("\n");
      if (last >= 0) {
        sb.delete(last, sb.length());
      }
    }

    //tries to append the string builder's information onto the appendable object.
    try {
      ap.append(sb.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Unable to successfully transmit output.");
    }
  }

  @Override
  public void animate(Timer timer) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Text view has no graphical display to animate on.");
  }
}
