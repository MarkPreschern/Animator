package cs3500.animator.model;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Represents a cs3500.animator.model for a shape's functionality, which is responsible for
 * representing and mutating a shape.
 */
public interface ShapeModel {

  /**
   * Gets the key of the shape.
   *
   * @return the key
   */
  String getKey();

  /**
   * gets the position of the top left corner of the shape.
   *
   * @return a point
   */
  Point2D.Double getPointTopLeft();

  /**
   * gets the dimensions of the shape.
   *
   * @return a point
   */
  Point2D.Double getDimensions();

  /**
   * gets the color of the shape.
   *
   * @return a color
   */
  Color getColor();

  /**
   * gets the theta in degrees with respect to rotation of the shape.
   *
   * @return theta
   */
  double getTheta();

  /**
   * gets a string representation of the shape by it's type (rectangle, ellipse, etc.. ) and
   * fields.
   *
   * @return string representation
   */
  String toString();

  /**
   * Gets shape type of this shape. Supported shapes currently include: 'Rectangle' and 'Ellipse'
   *
   * @return the shape type
   */
  String getType();

  /**
   * Constructs a new instance of this shape.
   *
   * @return a new instance of this shape
   */
  ShapeModel makeCopy();
}
