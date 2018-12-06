package cs3500.animator.model;

import java.awt.geom.Point2D;

/**
 * Represents a cs3500.animator.model for an animations functionality.
 */
public interface MotionModel {

  /**
   * Calculates the shape's attributes at the given time, based on it's initial and final shape and
   * applies those attributes to the given shape.
   *
   * @param shape       the given shape
   * @param currentTime the current time
   */
  ShapeModel getShape(ShapeModel shape, int currentTime);

  /**
   * gets a string representation of the shape by it's type (Color, Fade, etc.. ) and fields.
   *
   * @return a string representation
   */
  String toString();

  /**
   * Returns the initial key frame of the motion.
   *
   * @return the initial key frame
   */
  KeyFrameModel getInitial();

  /**
   * Returns the final key frame of the motion.
   *
   * @return the final key frame
   */
  KeyFrameModel getFinal();

  /**
   * Gets the time interval of the motion.
   *
   * @return the time interval
   */
  Point2D.Double getTimeInterval();
}
