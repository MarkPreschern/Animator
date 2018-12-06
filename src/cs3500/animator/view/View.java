package cs3500.animator.view;

import javax.swing.Timer;

/**
 * Represents some type of view, as to be general which can be displayed and animated.
 */
public interface View {

  /**
   * Makes the view visible or outputs an output file dependant on the implementation. In other
   * words it shows the output.
   *
   * @throws IllegalStateException if the given implementation catches an IOException
   */
  void makeVisible() throws IllegalStateException;

  /**
   * Starts an animation. If the animation time is less than zero or equal to the final animation
   * time (when the animation is over), the window stays at the final frame and remains open.
   *
   * @param timer the timer which the animation is using
   * @throws UnsupportedOperationException if the given implementation doesn't support this feature
   */
  void animate(Timer timer) throws UnsupportedOperationException;
}
