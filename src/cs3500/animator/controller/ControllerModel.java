package cs3500.animator.controller;

import java.awt.event.ActionListener;

import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.view.View;

/**
 * Represents the model for a controller, which handles input and distributes responsibility to it's
 * model and view to effectively represent an animation.
 *
 * Listener's listen for the following:
 *
 * ActionListener: 'Run', 'Pause', 'Rewind', 'Restart', 'Loop', 'Set Speed:', 'Add Key Frame',
 * 'Remove Key Frame', 'Add Shape', 'Remove Shape', 'Modify Selected Key Frame', 'Save Animation',
 * 'Load File', 'Undo Edit', 'Redo Edit'.
 *
 * ListSelectionListener: 'shape list', 'frame list'.
 *
 * ChangeListener: doesn't listen for a specific command, as it's only listening to the JSlider
 */
public interface ControllerModel extends ActionListener, ListSelectionListener, ChangeListener {

  /**
   * Makes the AnimationModel visible, and begins it's animation if it is visual view.
   *
   * @param model the given AnimationModel
   * @param view the given View
   * @param ticksPerSecond the speed of animation in ticks per second, if it applies to the view
   * @throws IllegalArgumentException if the model or view is null
   */
  void control(AnimationModel model, View view, int ticksPerSecond);
}
