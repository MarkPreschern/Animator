package cs3500.animator.model;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Represents different implementations of a shape.
 */
public abstract class Shape implements ShapeModel {

  //a key/name pertaining to the shape
  protected String key;

  //x and y represent the x and y coordinates of the upper left corner of the shape respectively
  protected double x;
  protected double y;

  //represent the dimensions
  protected double width;
  protected double height;

  //represent the color
  protected double red;
  protected double green;
  protected double blue;

  /**
   * Constructs a custom shape using a shape builder.
   *
   * @param b shape builder
   * @throws IllegalArgumentException if the given shape builder is null
   */
  public Shape(ShapeBuilder b) throws IllegalArgumentException {
    if (b == null) {
      throw new IllegalArgumentException("Shape builder can't be null.");
    }

    this.key = b.key;
    this.x = b.x;
    this.y = b.y;
    this.width = b.width;
    this.height = b.height;
    this.red = b.red;
    this.green = b.green;
    this.blue = b.blue;
  }

  /**
   * Constructs a new instance of the given shape.
   *
   * @param s the given sahpe
   * @throws IllegalArgumentException if the given shape is null
   */
  public Shape(Shape s) throws IllegalArgumentException {
    if (s == null) {
      throw new IllegalArgumentException("Shape can't be null.");
    }

    this.key = s.key;
    this.x = s.x;
    this.y = s.y;
    this.width = s.width;
    this.height = s.height;
    this.red = s.red;
    this.green = s.green;
    this.blue = s.blue;
  }

  /**
   * Represents a builder for a shape, using the builder pattern.
   */
  public static class ShapeBuilder {

    //a key/name pertaining to the shape
    String key;

    //x and y represent the x and y coordinates of the upper left corner of the shape respectively
    private double x;
    private double y;

    //represent the dimensions
    private double width;
    private double height;

    //represent the color
    private double red;
    private double green;
    private double blue;

    /**
     * Constructs a default shape builder.
     */
    public ShapeBuilder() {
      this.key = "Default";
      this.x = 0;
      this.y = 0;
      this.width = 0;
      this.height = 0;
      this.red = 0;
      this.green = 0;
      this.blue = 0;
    }

    /**
     * sets key and returns a new instance of the builder.
     *
     * @param key new key
     * @return an instance of the shape builder
     * @throws IllegalArgumentException if the given key is null
     */
    public ShapeBuilder setKey(String key) throws IllegalArgumentException {
      if (key == null) {
        throw new IllegalArgumentException("Key can't be null.");
      }
      this.key = key;
      return this;
    }

    /**
     * sets x and returns a new instance of the builder.
     *
     * @param x new x
     * @return an instance of the shape builder
     */
    public ShapeBuilder setX(double x) throws IllegalArgumentException {
      this.x = x;
      return this;
    }

    /**
     * sets y and returns a new instance of the builder.
     *
     * @param y new y
     * @return an instance of the shape builder
     */
    public ShapeBuilder setY(double y) throws IllegalArgumentException {
      this.y = y;
      return this;
    }

    /**
     * sets width and returns a new instance of the builder.
     *
     * @param width new width
     * @return an instance of the shape builder
     * @throws IllegalArgumentException if width is negative
     */
    public ShapeBuilder setWidth(double width) throws IllegalArgumentException {
      if (width < 0) {
        throw new IllegalArgumentException("Width can't be negative.");
      }
      this.width = width;
      return this;
    }

    /**
     * sets height and returns a new instance of the builder.
     *
     * @param height new height
     * @return an instance of the shape builder
     * @throws IllegalArgumentException if height is negative
     */
    public ShapeBuilder setHeight(double height) throws IllegalArgumentException {
      if (height < 0) {
        throw new IllegalArgumentException("Height can't be negative.");
      }
      this.height = height;
      return this;
    }

    /**
     * sets red and returns a new instance of the builder.
     *
     * @param red new red
     * @return an instance of the shape builder
     * @throws IllegalArgumentException if red is not within [0,255]
     */
    public ShapeBuilder setRed(double red) throws IllegalArgumentException {
      if (red < 0 || red > 255) {
        throw new IllegalArgumentException("Invalid red color, not within [0,255].");
      }
      this.red = red;
      return this;
    }

    /**
     * sets green and returns a new instance of the builder.
     *
     * @param green new green
     * @return an instance of the shape builder
     * @throws IllegalArgumentException if green is not within [0,255]
     */
    public ShapeBuilder setGreen(double green) throws IllegalArgumentException {
      if (green < 0 || green > 255) {
        throw new IllegalArgumentException("Invalid green color, not within [0,255].");
      }
      this.green = green;
      return this;
    }

    /**
     * sets blue and returns a new instance of the builder.
     *
     * @param blue new blue
     * @return an instance of the shape builder
     * @throws IllegalArgumentException if blue is not within [0,255]
     */
    public ShapeBuilder setBlue(double blue) throws IllegalArgumentException {
      if (blue < 0 || blue > 255) {
        throw new IllegalArgumentException("Invalid blue color, not within [0,255].");
      }
      this.blue = blue;
      return this;
    }

    /**
     * Returns an instance of the shape builder.
     *
     * @return an instance of the shape builder.
     */
    public ShapeBuilder build() {
      return this;
    }
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Override
  public Point2D.Double getPointTopLeft() {
    return new Point2D.Double(this.x, this.y);
  }

  @Override
  public Point2D.Double getDimensions() {
    return new Point2D.Double(this.width, this.height);
  }

  @Override
  public Color getColor() {
    return new Color((int) this.red, (int) this.green, (int) this.blue);
  }


  @Override
  public String toString() {
    return this.x + " " + this.y + " " + this.width + " " + this.height + " " + this.red + " "
            + this.green + " " + this.blue;
  }

  @Override
  public abstract String getType();

  @Override
  public abstract ShapeModel makeCopy();
}
