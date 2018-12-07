package cs3500.animator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Represents a model for an Animator's functionality, which is responsible for representing and
 * mutating an shapes and motions.
 */
public interface AnimationModel {

  /**
   * Adds a shape with no motions to the hash map of motions.
   *
   * @param shape the shape being added
   * @throws IllegalArgumentException if the shape's key already exists
   * @throws IllegalArgumentException if given shape is null
   */
  void addShape(ShapeModel shape) throws IllegalArgumentException;

  /**
   * Removes a shape by searching for it's key, and it's motions.
   *
   * @param key the key of the shape
   * @throws IllegalArgumentException if the key doesn't exist
   * @throws IllegalArgumentException if the key is null
   */
  void removeShape(String key) throws IllegalArgumentException;

  /**
   * Adds a new motion to a shapes list of key frames if a shape with the given key exists.
   *
   * @param key    the shape's key
   * @param motion the motion being added
   * @throws IllegalArgumentException if the key doesn't exist.
   * @throws IllegalArgumentException if the key or motion is null
   * @throws IllegalArgumentException if the motions initial time is greater than it's final time
   */
  void addMotion(String key, MotionModel motion) throws IllegalArgumentException;

  /**
   * Removes a motion from a specific shape's list of key frames if both the shape exists and the
   * motion's key frames are in the list mapped to the shape.
   *
   * @param key    the shape's key
   * @param motion the motion being removed
   * @throws IllegalArgumentException if the key or motion is null
   * @throws IllegalArgumentException if the key doesn't exist or the motion's key frames don't
   *                                  exist
   */
  void removeMotion(String key, MotionModel motion) throws IllegalArgumentException;

  /**
   * Adds a new key frame to a shapes list of key frames if a shape with the given key exists.
   *
   * @param key   the shape's key
   * @param frame the key frame being added
   * @throws IllegalArgumentException if the key doesn't exist.
   * @throws IllegalArgumentException if the key or key frame is null
   * @throws IllegalArgumentException if the key frame overlaps with a key frame of the
   *                                  corresponding shape
   */
  void addKeyFrame(String key, KeyFrameModel frame) throws IllegalArgumentException;

  /**
   * Removes a key frame from a specific shape's list of key frames if both the shape exists and the
   * key frame is in the list mapped to the shape.
   *
   * @param key   the shape's key
   * @param frame the key frame being removed
   * @throws IllegalArgumentException if the key or key frame is null
   * @throws IllegalArgumentException if the key doesn't exist or the key frame doesn't exist
   */
  void removeKeyFrame(String key, KeyFrameModel frame) throws IllegalArgumentException;

  /**
   * Gets a list of all shapes contained in the animation.
   *
   * @return the list of shapes
   */
  ArrayList<ShapeModel> getShapes();

  /**
   * Applies motions to their respective shapes if time is in the bounds of the motions time
   * interval; initialTime <= current time < finalTime. Gets the shapes being animated at the given
   * time.
   *
   * @return a list of shapes at the given time
   * @throws IllegalArgumentException if time < 0 or time is greater than the final time of the last
   *                                  animation.
   */
  ArrayList<ShapeModel> getShapes(int time) throws IllegalArgumentException;

  /**
   * gets the list of key frames.
   *
   * @return list of key frames
   */
  ArrayList<KeyFrameModel> getKeyFrames();

  /**
   * Gets a data representation of both shapes and key frames, where each shape in the map's keys
   * represent the initial shape during instantiation and the map's values represent a list of key
   * frames associated with that shape.
   *
   * @return a data representation of both shapes and key frames
   */
  LinkedHashMap<ShapeModel, ArrayList<KeyFrameModel>> getShapeKeyFrameRepresentation();

  /**
   * A string representation of all shapes and motions listed as a shape followed by it's respective
   * motions.
   *
   * @return a string representation
   */
  String getStringRepresentation();

  /**
   * Gets the canvas of the animation.
   *
   * @return the canvas
   */
  CanvasModel getCanvas();

  /**
   * Gets the final time of the last animation.
   *
   * @return the final time of the last animation.
   */
  int getEndTime();

  /**
   * Returns a new instance of this model.
   *
   * @return a new instance of this model
   */
  AnimationModel makeCopy();

  /**
   * Adds a shape that currently exists to the specified layer.
   *
   * @param name  the name of the shape
   * @param layer the layer specified
   * @throws IllegalArgumentException if the name is negative
   * @throws IllegalArgumentException if the name isn't contained in the current shapes
   * @throws IllegalArgumentException if the layer is negative
   */
  void addShapeToLayer(String name, int layer);

  /**
   * Deletes the layer specified and all shapes contained on it.
   */
  void deleteLayer(int layer);

  /**
   * Changes the layer to the new layer, effectively changing all shapes initial layer to the new
   * layer as well.
   *
   * @param layer    the initial layer
   * @param newLayer the new layer
   * @throws IllegalArgumentException if either layer is negative
   */
  void reorderLayer(int layer, int newLayer);
}
