package cs3500.animator.controller;

/**
 * represents the operation to be performed on a JPanel when an animation is over.
 * -- {@code OPEN} indicates that the JPanel will remain open, but idle
 * -- {@code CLOSE} indicates that the JPanel will be not be visible
 * -- {@code LOOP} indicates that the JPanel's time goes to zero, if not currently negative
 */
public enum EndOperation {
  OPEN, CLOSE, LOOP;
}
