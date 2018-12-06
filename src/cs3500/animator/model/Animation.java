package cs3500.animator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cs3500.animator.util.AnimationBuilder;

/**
 * Represents and manipulates information regarding numerous shapes and their respective motions.
 */
public final class Animation implements AnimationModel {

  //represents an ordered dictionary of shapes, to sorted list of key frames by time
  private LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> keyFrames;

  //represents the bounds of the animation
  private Canvas canvas;

  /**
   * Creates a new default instance with no motions and a default canvas.
   */
  public Animation() {
    this.keyFrames = new LinkedHashMap<>();
    this.canvas = new Canvas();
  }

  /**
   * Creates a new instance with the given key frames, and canvas. Also checks for animation
   * consistency during instantiation.
   *
   * @param keyFrames the keyFrames being applied
   * @param canvas    the dimensions of the animation
   * @throws IllegalArgumentException if motions or canvas is null, or animations aren't consistent
   */
  public Animation(LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> keyFrames, Canvas canvas)
          throws IllegalArgumentException {
    if (keyFrames == null || canvas == null) {
      throw new IllegalArgumentException("Key frames and canvas can't be null.");
    }

    this.keyFrames = keyFrames;
    this.canvas = canvas;

    this.updateFrames();
  }

  /**
   * Represents a builder class for an animation.
   */
  public static final class Builder implements AnimationBuilder<Animation> {

    private LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> keyFrames;
    private Canvas canvas;

    /**
     * Default constructor for builder sets variables to default values.
     */
    public Builder() {
      this.keyFrames = new LinkedHashMap<>();
      this.canvas = new Canvas();
    }

    @Override
    public Animation build() {
      return new Animation(this.keyFrames, this.canvas);
    }

    @Override
    public AnimationBuilder<Animation> setBounds(int x, int y, int width, int height) {
      this.canvas = new Canvas(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<Animation> declareShape(String name, String type) {
      if (name == null || type == null) {
        throw new IllegalArgumentException("Shape name and type can't be null.");
      } else if (this.containsKey(name)) {
        throw new IllegalArgumentException("Shape's key already exists, try a different key.");
      }

      Shape shape;
      switch (type) {
        case "rectangle":
          shape = new Rectangle(new Shape.ShapeBuilder().setKey(name).build());
          break;
        case "ellipse":
          shape = new Ellipse(new Shape.ShapeBuilder().setKey(name).build());
          break;
        default:
          throw new IllegalArgumentException("Unsupported shape type.");
      }

      this.keyFrames.put(shape, new ArrayList<>());
      return this;
    }

    @Override
    public AnimationBuilder<Animation> addMotion(String name, int t1, int x1, int y1,
                                                 int w1, int h1, int r1, int g1, int b1,
                                                 int t2, int x2, int y2, int w2, int h2,
                                                 int r2, int g2, int b2) {
      if (t1 > t2) {
        throw new IllegalArgumentException("Initial time must be less than final time.");
      }

      this.addKeyframe(name, t1, x1, y1, w1, h1, r1, g1, b1);
      this.addKeyframe(name, t2, x2, y2, w2, h2, r2, g2, b2);
      return this;
    }

    @Override
    public AnimationBuilder<Animation> addKeyframe(String name, int t, int x, int y,
                                                   int w, int h, int r, int g, int b) {
      if (name == null) {
        throw new IllegalArgumentException("Key can't be null.");
      } else if (!this.containsKey(name)) {
        throw new IllegalArgumentException("Shape of the given key doesn't exist.");
      }

      Shape shape = (Shape) this.getShape(name).makeCopy();
      switch (shape.getType()) {
        case "Rectangle":
          shape = new Rectangle(new Shape.ShapeBuilder().setKey(shape.getKey()).setX(x).setY(y)
                  .setWidth(w).setHeight(h).setRed(r).setGreen(g).setBlue(b).build());
          break;
        case "Ellipse":
          shape = new Ellipse(new Shape.ShapeBuilder().setKey(shape.getKey()).setX(x).setY(y)
                  .setWidth(w).setHeight(h).setRed(r).setGreen(g).setBlue(b).build());
          break;
        default:
          throw new IllegalArgumentException("Unsupported shape type.");
      }

      ArrayList<KeyFrameModel> list = this.keyFrames.get(this.getShape(name));
      list.add(new KeyFrame(t, shape));

      this.keyFrames.replace(this.getShape(name), new ArrayList<>(list));
      return this;
    }

    @Override
    public AnimationBuilder<Animation> rotateKeyFrame(String name, int t, double theta) {
      if (name == null) {
        throw new IllegalArgumentException("Key can't be null.");
      } else if (!this.containsKey(name)) {
        throw new IllegalArgumentException("Shape of the given key doesn't exist.");
      } else if (!this.hasKeyFrame(name, t)) {
        throw new IllegalArgumentException("Key frame at time " + t + " doesn't exist.");
      }

      ArrayList<KeyFrameModel> l = this.keyFrames.get(this.getShape(name));
      KeyFrameModel k = this.getKeyFrame(name, t);
      Shape s = (Shape) k.getShape();

      KeyFrameModel newKeyFrame;
      switch (s.getType()) {
        case "Rectangle":
          newKeyFrame = new KeyFrame(k.getTime(), new Rectangle(new Shape.ShapeBuilder()
                  .setKey(s.getKey()).setX(s.x).setY(s.y).setWidth(s.width).setHeight(s.height)
                  .setRed(s.red).setGreen(s.green).setBlue(s.blue).setTheta(theta).build()));
          break;
        case "Ellipse":
          newKeyFrame = new KeyFrame(k.getTime(), new Ellipse(new Shape.ShapeBuilder()
                  .setKey(s.getKey()).setX(s.x).setY(s.y).setWidth(s.width).setHeight(s.height)
                  .setRed(s.red).setGreen(s.green).setBlue(s.blue).setTheta(theta).build()));
          break;
        default:
          throw new IllegalArgumentException("Unsupported shape type.");
      }

      l.remove(k);
      l.add(newKeyFrame);

      this.keyFrames.replace(this.getShape(name), new ArrayList<>(l));
      return this;
    }

    /**
     * returns true if the given key is equal to a key in a shape.
     *
     * @param key the given key
     * @return if the key exists in a shape
     */
    private boolean containsKey(String key) {
      for (ShapeModel s : this.keyFrames.keySet()) {
        if (s.getKey().equals(key)) {
          return true;
        }
      }
      return false;
    }

    /**
     * Returns true if the shape has a key frame as the same time as that given.
     *
     * @param name the name of the shape
     * @param time the time of the key frame
     * @return true if the shape has the key frame
     */
    private boolean hasKeyFrame(String name, int time) {
      for (KeyFrameModel k : this.keyFrames.get(this.getShape(name))) {
        if (k.getTime() == time) {
          return true;
        }
      }
      return false;
    }

    /**
     * Returns the shape corresponding to the key.
     *
     * @param key the given key
     * @return the shape with the key
     * @throws IllegalArgumentException if the key doesn't exist
     */
    private ShapeModel getShape(String key) throws IllegalArgumentException {
      for (ShapeModel s : this.keyFrames.keySet()) {
        if (s.getKey().equals(key)) {
          return s;
        }
      }
      throw new IllegalArgumentException("key doesn't exist");
    }

    /**
     * Returns the key frame with the given name and time, if one exists.
     *
     * @param name the name of the shape
     * @param time the time of the key frame
     * @return the key frame
     * @throws IllegalArgumentException if the key frame doesn't exist
     */
    private KeyFrameModel getKeyFrame(String name, int time) {
      for (KeyFrameModel k : this.keyFrames.get(this.getShape(name))) {
        if (k.getTime() == time) {
          return k;
        }
      }
      throw new IllegalArgumentException("key doesn't exist");
    }
  }

  /**
   * Checks for animation consistency among key frames, sorting them by time and removing
   * duplicates.
   */
  private void updateFrames() throws IllegalArgumentException {
    for (ArrayList<KeyFrameModel> keyFrames : this.keyFrames.values()) {
      keyFrames.sort((o1, o2) -> (int) (o1.getTime() - o2.getTime()));

      for (int i = 0; i < keyFrames.size() - 1; i++) {
        if (keyFrames.get(i).getTime() == keyFrames.get(i + 1).getTime()) {
          keyFrames.remove(i);
          i--;
        }
      }
    }
  }

  /**
   * returns true if the given key is equal to a key in a shape.
   *
   * @param key the given key
   * @return if the key exists in a shape
   */
  private boolean containsKey(String key) {
    for (ShapeModel s : this.keyFrames.keySet()) {
      if (s.getKey().equals(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * returns the shape corresponding to the key.
   *
   * @param key the given key
   * @return the shape with the key
   * @throws IllegalArgumentException if the key doesn't exist
   */
  private ShapeModel getShape(String key) throws IllegalArgumentException {
    for (ShapeModel s : this.keyFrames.keySet()) {
      if (s.getKey().equals(key)) {
        return s;
      }
    }
    throw new IllegalArgumentException("key doesn't exist");
  }

  /**
   * Checks if the key frame occurs at the same time as a key frame in the given list.
   *
   * @param frames a list of key frames
   * @param frame  a key frame
   * @return if frame overlaps with frames
   */
  private boolean overlap(ArrayList<KeyFrameModel> frames, KeyFrameModel frame) {
    for (KeyFrameModel k1 : frames) {
      if (k1.getTime() == frame.getTime()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void addShape(ShapeModel shape) throws IllegalArgumentException {
    if (shape == null) {
      throw new IllegalArgumentException("Shape can't be null.");
    } else if (this.containsKey(shape.getKey())) {
      throw new IllegalArgumentException("Shape's key already exists, try a different key.");
    } else {
      this.keyFrames.put(shape, new ArrayList<>());
    }
  }

  @Override
  public void removeShape(String key) throws IllegalArgumentException {
    if (key == null) {
      throw new IllegalArgumentException("Key can't be null.");
    } else if (!this.containsKey(key)) {
      throw new IllegalArgumentException("The given key doesn't exist.");
    } else {
      ShapeModel toRemove = this.getShape(key);
      this.keyFrames.remove(toRemove);
    }
  }

  @Override
  public void addMotion(String key, MotionModel motion) throws IllegalArgumentException {
    if (key == null || motion == null) {
      throw new IllegalArgumentException("Neither key or motion can be null.");
    } else if (!this.containsKey(key)) {
      throw new IllegalArgumentException("Shape of the given key doesn't exist.");
    } else if (motion.getInitial().getTime() > motion.getFinal().getTime()) {
      throw new IllegalArgumentException("Initial time must be less than final time.");
    } else {
      ArrayList<KeyFrameModel> list = this.keyFrames.get(this.getShape(key));
      list.add(motion.getInitial());
      list.add(motion.getFinal());

      //checks that the new list of motions is consistent and doesn't have time gaps
      this.keyFrames.replace(this.getShape(key), new ArrayList<>(list));
      this.updateFrames();
    }
  }

  @Override
  public void removeMotion(String key, MotionModel motion) throws IllegalArgumentException {
    if (key == null || motion == null) {
      throw new IllegalArgumentException("Neither key or motion can be null.");
    } else if (!this.containsKey(key)) {
      throw new IllegalArgumentException("Shape of the given key doesn't exist.");
    } else if (this.containsKey(key)
            && !this.keyFrames.get(this.getShape(key)).contains(motion.getInitial())
            && !this.keyFrames.get(this.getShape(key)).contains(motion.getFinal())) {
      throw new IllegalArgumentException("Motion isn't contained in this shape's list of "
              + "key frames.");
    } else {
      this.keyFrames.get(this.getShape(key)).remove(motion.getInitial());
      this.keyFrames.get(this.getShape(key)).remove(motion.getFinal());
    }
  }

  @Override
  public void addKeyFrame(String key, KeyFrameModel frame) throws IllegalArgumentException {
    if (key == null || frame == null) {
      throw new IllegalArgumentException("Neither key or frame can be null.");
    } else if (!this.containsKey(key)) {
      throw new IllegalArgumentException("Shape of the given key doesn't exist.");
    } else if (this.containsKey(key) && this.overlap(this.keyFrames.get(this.getShape(key)),
            frame)) {
      throw new IllegalArgumentException("Key frame at time " + frame.getTime()
              + " already exists.");
    } else {
      ArrayList<KeyFrameModel> list = this.keyFrames.get(this.getShape(key));
      list.add(frame);

      //checks that the new list of motions is consistent and doesn't have time gaps
      this.keyFrames.replace(this.getShape(key), new ArrayList<>(list));
      this.updateFrames();
    }
  }

  @Override
  public void removeKeyFrame(String key, KeyFrameModel frame) throws IllegalArgumentException {
    if (key == null || frame == null) {
      throw new IllegalArgumentException("Neither key or frame can be null.");
    } else if (!this.containsKey(key)) {
      throw new IllegalArgumentException("Shape of the given key doesn't exist.");
    } else if (this.containsKey(key) && !this.keyFrames.get(this.getShape(key)).contains(frame)) {
      throw new IllegalArgumentException("Key frame isn't contained in this shape's list of "
              + "key frames.");
    } else {
      this.keyFrames.get(this.getShape(key)).remove(frame);
    }
  }

  @Override
  public ArrayList<ShapeModel> getShapes() {
    return new ArrayList<>(this.keyFrames.keySet());
  }

  @Override
  public ArrayList<ShapeModel> getShapes(int time) throws IllegalArgumentException {
    if (time < 0) {
      throw new IllegalArgumentException("Time can't be negative.");
    } else if (time > this.getEndTime()) {
      throw new IllegalArgumentException("The final time of the last motion has been reached.");
    }

    ArrayList<ShapeModel> newShapes = new ArrayList<>();
    //iterates through the map of shape to list of motions
    for (Map.Entry<ShapeModel, ArrayList<KeyFrameModel>> entry : this.keyFrames.entrySet()) {
      ShapeModel shape = entry.getKey();
      ShapeModel newShape = null;
      ArrayList<KeyFrameModel> frames = entry.getValue();

      //applies motions to their respective shapes if within the time bounds
      for (int i = 0; i < frames.size() - 1; i++) {
        KeyFrameModel f1 = frames.get(i);
        KeyFrameModel f2 = frames.get(i + 1);

        if (time >= f1.getTime() && time <= f2.getTime()) {
          Motion motion = new Motion(f1, f2);
          newShape = motion.getShape(shape, time);
        }
      }
      //updates the shapes based on the motions into a new map
      if (newShape != null) {
        newShapes.add(newShape);
      }
    }

    return newShapes;
  }

  @Override
  public ArrayList<KeyFrameModel> getKeyFrames() {
    ArrayList<KeyFrameModel> values = new ArrayList<>();
    for (ArrayList<KeyFrameModel> value : this.keyFrames.values()) {
      values.addAll(value);
    }

    return values;
  }

  @Override
  public LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> getShapeKeyFrameRepresentation() {
    return this.keyFrames;
  }

  @Override
  public String getStringRepresentation() {
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<ShapeModel, ArrayList<KeyFrameModel>> entry : this.keyFrames.entrySet()) {
      ShapeModel shape = entry.getKey();
      ArrayList<KeyFrameModel> value = entry.getValue();

      sb.append("Shape ").append(shape.getKey()).append(" ").append(shape.getType())
              .append("\n");
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

    return sb.toString();
  }

  @Override
  public CanvasModel getCanvas() {
    return this.canvas;
  }

  @Override
  public int getEndTime() {
    ArrayList<KeyFrameModel> frames = this.getKeyFrames();

    int greatest = 0;
    for (KeyFrameModel a : frames) {
      if (a.getTime() > greatest) {
        greatest = (int) a.getTime();
      }
    }
    return greatest;
  }

  @Override
  public Animation makeCopy() {
    LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> rep = new LinkedHashMap<>();
    for (Map.Entry<ShapeModel, ArrayList<KeyFrameModel>> entry : this.keyFrames.entrySet()) {
      ShapeModel s = entry.getKey().makeCopy();
      ArrayList<KeyFrameModel> fl = new ArrayList<>();

      for (KeyFrameModel f : entry.getValue()) {
        fl.add(new KeyFrame(f.getTime(), (Shape) f.getShape().makeCopy()));
      }
      rep.put(s, fl);
    }

    Canvas c = new Canvas(this.canvas.getX(), this.canvas.getY(), this.canvas.getWidth(),
            this.canvas.getHeight());

    return new Animation(rep, c);
  }
}
