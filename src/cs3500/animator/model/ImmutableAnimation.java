package cs3500.animator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Represents an immutable implementation of the animation model.
 */
public class ImmutableAnimation implements ImmutableModel {

  private final AnimationModel model;

  /**
   * Constructor takes in a mutable model and sets it to the private variable.
   */
  public ImmutableAnimation(AnimationModel model) {
    this.model = model;
  }

  @Override
  public void addShape(ShapeModel shape) throws IllegalArgumentException {
    throw new UnsupportedOperationException("Model is immutable.");
  }

  @Override
  public void removeShape(String key) throws IllegalArgumentException {
    throw new UnsupportedOperationException("Model is immutable.");
  }

  @Override
  public void addMotion(String key, MotionModel motion) throws IllegalArgumentException {
    throw new UnsupportedOperationException("Model is immutable.");
  }

  @Override
  public void removeMotion(String key, MotionModel motion) throws IllegalArgumentException {
    throw new UnsupportedOperationException("Model is immutable.");
  }

  @Override
  public void addKeyFrame(String key, KeyFrameModel frame) throws IllegalArgumentException {
    throw new UnsupportedOperationException("Model is immutable.");
  }

  @Override
  public void removeKeyFrame(String key, KeyFrameModel frame) throws IllegalArgumentException {
    throw new UnsupportedOperationException("Model is immutable.");
  }

  @Override
  public ArrayList<ShapeModel> getShapes() {
    return this.model.getShapes();
  }

  @Override
  public ArrayList<ShapeModel> getShapes(int time) throws IllegalArgumentException {
    return this.model.getShapes(time);
  }

  @Override
  public ArrayList<KeyFrameModel> getKeyFrames() {
    return this.model.getKeyFrames();
  }

  @Override
  public LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> getShapeKeyFrameRepresentation() {
    return this.model.getShapeKeyFrameRepresentation();
  }

  @Override
  public String getStringRepresentation() {
    return this.model.getStringRepresentation();
  }

  @Override
  public CanvasModel getCanvas() {
    return this.model.getCanvas();
  }

  @Override
  public int getEndTime() {
    return this.model.getEndTime();
  }

  @Override
  public AnimationModel makeCopy() {
    return this.model.makeCopy();
  }
}
