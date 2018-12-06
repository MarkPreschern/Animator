package cs3500.animator.controller;

/**
 * Represents the game state where the values are as below.
 * -- {@code RUN} indicates that the game is ongoing in forward directional time.
 * -- {@code PAUSE} indicates that the game is not ongoing.
 * -- {@code REWIND} indicates that the game is ongoing in backwards directional time.
 * Additionally, all values take in a double value corresponding to a default speed direction for
 * the animation state.
 */
public enum State {

  RUN(1), PAUSE(0), REWIND(-1);

  private int direction;

  /**
   * Constructs a state with the given direction.
   *
   * @param direction the direction
   */
  State(int direction) {
    this.direction = direction;
  }

  /**
   * Gets the default direction of this AnimationState.
   *
   * @return direction
   */
  public int getDirection() {
    return this.direction;
  }
}
