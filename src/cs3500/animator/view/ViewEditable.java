package cs3500.animator.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cs3500.animator.controller.ControllerModel;
import cs3500.animator.controller.EndOperation;
import cs3500.animator.controller.State;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.KeyFrameModel;
import cs3500.animator.model.ShapeModel;

/**
 * Represents a view that can be edited via user input.
 */
public interface ViewEditable extends View {
  /**
   * Displays the message to the user in whatever way suitable for the specific view.
   *
   * @param message the message to display
   * @throws UnsupportedOperationException if the view isn't editable
   */
  void displayMessage(String message);

  /**
   * Sets the editable view's relevant JComponent's to this listener.
   *
   * @param listener the given listener
   * @throws UnsupportedOperationException if the view isn't visual or editable
   */
  void setListener(ControllerModel listener);

  /**
   * Applies updates to the model of the view.
   *
   * @param model the updated model of the view
   */
  void applyModelUpdate(ImmutableModel model);

  /**
   * Applies updates relevant to animation to the view if applicable.
   *
   * @param ticksPerSecond the speed of the animation in ticks per second
   * @param state          the state of the animation (run, pause, rewind)
   * @param endOperation   what the view should do when the animation is over
   * @throws UnsupportedOperationException if the view can't be animated
   */
  void applyAnimationUpdate(int ticksPerSecond, State state, EndOperation endOperation)
          throws UnsupportedOperationException;

  /**
   * Updates the size of the view based on the model's dimensions.
   */
  void updateFrameSize();

  /**
   * Sets the time of animation to the given time, if the view can be animated.
   *
   * @param time the given time
   * @throws UnsupportedOperationException if the view can't be animated
   */
  void setTime(int time) throws UnsupportedOperationException;

  /**
   * Returns the speed given by the user in an editable view.
   *
   * @return the speed given by the user
   */
  int getSpeed();

  /**
   * Sets the key frames to those corresponding to the selected shape.
   */
  void setFrames();

  /**
   * Sets the keyframes editable by the user to those corresponding to the selected key frame.
   */
  void setFrameValues();

  /**
   * Gets the currently selected shape.
   *
   * @return the selected shape
   */
  ShapeModel getSelectedShape();

  /**
   * Gets the currently selected key frame.
   *
   * @return the selected key frame
   */
  KeyFrameModel getSelectedKeyFrame();

  /**
   * Gets the key frame input from the user's input.
   *
   * @return the key frame
   */
  ArrayList<Integer> getInputKeyFrameValues();

  /**
   * Updates the representation of shape and key frame information to the current model.
   */
  void updateLists();

  /**
   * Opens a popup that prompts the user to select a shape to add, and returns the type and name of
   * the provided shape respectively in the hash map.
   *
   * @return the shape to be added
   */
  HashMap<String, String> shapePopup();

  /**
   * Opens a popup that prompts the user to select a file type and location, returning the view's
   * type and path if it can be found.
   *
   * @return the view type and out location as a hash map
   */
  HashMap<String, Appendable> savePopup();

  /**
   * Opens a popup that prompts the user to select a file type and location, saving the view if the
   * file path input is found.
   *
   * @return the file to be loaded
   */
  File loadFile();

  /**
   * Opens a popup that prompts the user to select a layer value.
   *
   * @return the layer value
   */
  int getLayerValue();

  /**
   * Opens a popup that prompts the user to select a current and new layer value.
   *
   * @return the current and new layer values
   */
  HashMap<Integer, Integer> getLayerValues();
}
