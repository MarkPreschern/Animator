package cs3500.animator.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.Timer;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

import cs3500.animator.model.Animation;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.ImmutableAnimation;
import cs3500.animator.model.KeyFrame;
import cs3500.animator.model.KeyFrameModel;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;
import cs3500.animator.model.ShapeModel;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.SVGView;
import cs3500.animator.view.TextView;
import cs3500.animator.view.View;
import cs3500.animator.view.ViewEditable;

/**
 * Represents the controller for the model and view of an animation, responsible for distributing
 * responsibility and handling global time and user input.
 */
public class Controller implements ControllerModel {

  private AnimationModel model;
  private ViewEditable view;
  private Timer timer = new Timer(0, null);

  private State state = State.RUN;
  private EndOperation endOperation = EndOperation.OPEN;
  private int ticksPerSecond;

  //represents edit states to the model in the past and future of the current edit state
  private Stack<AnimationModel> history = new Stack<>();
  private Stack<AnimationModel> future = new Stack<>();

  /**
   * Instantiates a default controller.
   */
  public Controller() {
  }

  @Override
  public void control(AnimationModel model, View view, int ticksPerSecond) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view can't be null.");
    }
    this.model = model;
    this.ticksPerSecond = ticksPerSecond;

    try {
      view.animate(this.timer);
    } catch (UnsupportedOperationException ignore) {
      //exception is ignored, as views that don't support animation should still be able to run
    }

    if (view instanceof ViewEditable) {
      this.view = (ViewEditable) view;
      this.view.setListener(this);
    }
  }

  /**
   * Sets the direction of animation of forward, and resumes the animation.
   */
  private void run() {
    this.state = State.RUN;
    this.timer.start();
    this.applyAnimationUpdateToView("View is running");
  }

  /**
   * Pauses the animation at the current time.
   */
  private void pause() {
    this.state = State.PAUSE;
    this.timer.stop();
    this.applyAnimationUpdateToView("View is paused");
  }

  /**
   * Sets the direction of animation to rewind, and resumes the animation.
   */
  private void rewind() {
    this.state = State.REWIND;
    this.timer.start();
    this.applyAnimationUpdateToView("View is rewinding");
  }

  /**
   * Starts the animation from time equals zero, in the forward direction of animation.
   */
  private void restart() {
    this.state = State.RUN;
    this.timer.start();
    this.applyAnimationUpdateToView("");
    this.setTime(0);
    this.view.displayMessage("View is restarting");
  }

  /**
   * Changes the looping value from it's current, which allows the animation to restart once it is
   * over. If the animation is currently set to loop, calling this method will change the animation
   * to remain at the last frame once reaching it's end. Looping is set to false by default when an
   * animation is created.
   */
  private void changeLooping() {
    if (this.endOperation == EndOperation.LOOP) {
      this.endOperation = EndOperation.OPEN;
      this.applyAnimationUpdateToView("View is not looping");
    } else {
      this.endOperation = EndOperation.LOOP;
      this.applyAnimationUpdateToView("View is looping");
    }
  }

  /**
   * Changes the speed of animation to the given speed in ticks per second.
   *
   * @param ticksPerSecond the speed in ticks per second
   */
  private void changeSpeed(int ticksPerSecond) {
    if (ticksPerSecond < 0) {
      this.view.displayMessage("Speed can't be negative");
    } else {
      this.ticksPerSecond = ticksPerSecond;
      this.timer.setDelay(1000 / ticksPerSecond);
      this.applyAnimationUpdateToView("Speed is now "
              + Integer.toString(ticksPerSecond) + " ticks per second");
    }
  }

  /**
   * Adds the given shape to the model.
   *
   * @param shape the given shape
   */
  private void addShape(ShapeModel shape) {
    Stack<AnimationModel> temp = (Stack<AnimationModel>) this.future.clone();
    try {
      this.addToHistory();
      this.model.addShape(shape);
      this.applyModelUpdateToView("Shape has been added");
    } catch (IllegalArgumentException e) {
      this.history.pop();
      this.future = temp;
      this.view.displayMessage(e.getMessage());
    }
  }

  /**
   * Removes the shape of the given key.
   *
   * @param key the key of the shape
   */
  private void removeShape(String key) {
    Stack<AnimationModel> temp = (Stack<AnimationModel>) this.future.clone();
    try {
      this.addToHistory();
      this.model.removeShape(key);
      this.applyModelUpdateToView("Shape has been removed");
    } catch (IllegalArgumentException e) {
      this.history.pop();
      this.future = temp;
      this.view.displayMessage(e.getMessage());
    }
  }

  /**
   * Adds the key frame to the key's shape during animation.
   *
   * @param key      the shape's key
   * @param keyFrame the given key frame
   */
  private void addFrame(String key, KeyFrameModel keyFrame) {
    Stack<AnimationModel> temp = (Stack<AnimationModel>) this.future.clone();
    try {
      this.addToHistory();
      this.model.addKeyFrame(key, keyFrame);
      this.applyModelUpdateToView("Key frame has been added");
    } catch (IllegalArgumentException e) {
      this.history.pop();
      this.future = temp;
      this.view.displayMessage(e.getMessage());
    }
  }

  /**
   * Removes the key frame from the key's shape during animation.
   *
   * @param key      the shape's key
   * @param keyFrame the given key frame
   */
  private void removeFrame(String key, KeyFrameModel keyFrame) {
    Stack<AnimationModel> temp = (Stack<AnimationModel>) this.future.clone();
    try {
      this.addToHistory();
      this.model.removeKeyFrame(key, keyFrame);
      this.applyModelUpdateToView("Key frame has been removed");
    } catch (IllegalArgumentException e) {
      this.history.pop();
      this.future = temp;
      this.view.displayMessage(e.getMessage());
    }
  }

  /**
   * Modifies the key frame of the shape and key frame selected to the input key frame.
   *
   * @param key      the shape's key
   * @param selected the selected key frame
   * @param input    the key frame input from the user
   */
  private void modifyFrame(String key, KeyFrameModel selected, KeyFrameModel input) {
    Stack<AnimationModel> temp = (Stack<AnimationModel>) this.future.clone();
    try {
      this.addToHistory();
      this.model.removeKeyFrame(key, selected);
      this.model.addKeyFrame(key, input);
      this.applyModelUpdateToView("Key frame has been modified");
    } catch (IllegalArgumentException e) {
      this.history.pop();
      this.future = temp;
      this.view.displayMessage(e.getMessage());
    }
  }

  /**
   * Sets time to the given time if possible, or sends an error message to the user if not possible
   * because it is negative or greater than the animation's end time.
   *
   * @param time the given time
   */
  private void setTime(int time) {
    if (time < 0 || time > this.model.getEndTime()) {
      this.view.displayMessage("Invalid time given");
    } else {
      this.view.setTime(time);
      this.view.displayMessage("Time has been changed to " + time);
      
      this.state = State.PAUSE;
      this.timer.stop();
      this.view.applyAnimationUpdate(this.ticksPerSecond, this.state, this.endOperation);
    }
  }

  /**
   * Saves a new animation of the given type to the given out.
   *
   * @param typeAndOut the animations view type and out
   */
  private void saveAnimation(HashMap<String, Appendable> typeAndOut) {
    String type = new ArrayList<>(typeAndOut.keySet()).get(0);
    Appendable out = new ArrayList<>(typeAndOut.values()).get(0);
    if (out == null) {
      this.view.displayMessage("File path not found");
      return;
    }

    //creates the view
    View view;
    if (type == null) {
      return;
    } else if (type.equals("Svg")) {
      view = new SVGView(new ImmutableAnimation(this.model), out, this.ticksPerSecond);
    } else {
      view = new TextView(new ImmutableAnimation(this.model), out);
    }

    //tries to save the view
    try {
      view.makeVisible();
      if (out instanceof FileWriter) {
        ((FileWriter) out).flush();
        ((FileWriter) out).close();
      }

      if (out == System.out) {
        this.view.displayMessage(type + " file has been saved to console");
      } else {
        this.view.displayMessage(type + " file has been saved");
      }
    } catch (IOException e) {
      this.view.displayMessage("File path not found");
    }
  }

  /**
   * Loads a new animation based on the given file.
   *
   * @param file the given file
   */
  private void loadFile(File file) {
    if (file == null) {
      this.view.displayMessage("Specified file not found.");
      return;
    }

    FileReader fileReader;
    try {
      fileReader = new FileReader(file);
    } catch (FileNotFoundException e) {
      this.view.displayMessage("Specified file not found.");
      return;
    }

    this.addToHistory();
    this.model = AnimationReader.parseFile(fileReader, new Animation.Builder());
    this.applyModelUpdateToView("");
    this.view.updateFrameSize();
    this.view.updateLists();
    this.restart();
    this.view.displayMessage("File loaded");
  }

  /**
   * Reverses the model/view to a state prior to the last edit if one exists.
   */
  private void undo() {
    try {
      AnimationModel p = this.history.pop();
      this.future.add(this.model.makeCopy());
      this.model = p;

      this.applyAnimationUpdateToView("");
      this.applyModelUpdateToView("");
      this.view.updateLists();
      this.view.displayMessage("Undo performed");
    } catch (EmptyStackException e) {
      this.view.displayMessage("No undo history");
    }
  }

  /**
   * Forwards the model/view to a state previous to the last undo if one exists.
   */
  private void redo() {
    try {
      AnimationModel p = this.future.pop();
      this.history.add(this.model.makeCopy());
      this.model = p;

      this.applyAnimationUpdateToView("");
      this.applyModelUpdateToView("");
      this.view.updateLists();
      this.view.displayMessage("Redo performed");
    } catch (EmptyStackException e) {
      this.view.displayMessage("No redo history");
    }
  }

  /**
   * Applies an animation update to the view, and displays a message to the user based on the
   * animation update. If the update can't be applied, the exception's error message is displayed.
   *
   * @param updateMessage the message to display
   */
  private void applyAnimationUpdateToView(String updateMessage) {
    this.view.applyAnimationUpdate(this.ticksPerSecond, this.state, this.endOperation);
    this.view.displayMessage(updateMessage);
  }

  /**
   * Applies a model update to the view, and displays a message to the user based on the model
   * update.
   *
   * @param updateMessage the message to display
   */
  private void applyModelUpdateToView(String updateMessage) {
    this.view.applyModelUpdate(new ImmutableAnimation(this.model));
    this.view.displayMessage(updateMessage);
  }

  /**
   * Adds the current state of the model and view to the history of edits.
   */
  private void addToHistory() {
    this.history.add(this.model.makeCopy());
    this.future = new Stack<>();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Run":
        this.run();
        break;
      case "Pause":
        this.pause();
        break;
      case "Rewind":
        this.rewind();
        break;
      case "Restart":
        this.restart();
        break;
      case "Loop":
        this.changeLooping();
        break;
      case "Set Speed:":
        this.changeSpeed(this.view.getSpeed());
        break;
      case "Add Key Frame": {
        try {
          this.addFrame(this.view.getSelectedShape().getKey(),
                  this.initializeFrame(this.view.getInputKeyFrameValues()));
        } catch (NullPointerException n) {
          this.view.displayMessage("No shape given");
        } catch (IllegalArgumentException e2) {
          this.view.displayMessage(e2.getMessage());
        }
        this.view.updateLists();
        break;
      }
      case "Add Shape": {
        this.timer.stop();
        this.addShape(this.initializeShape(this.view.shapePopup()));
        this.view.updateLists();
        break;
      }
      case "Remove Shape": {
        try {
          this.removeShape(this.view.getSelectedShape().getKey());
        } catch (NullPointerException n) {
          this.view.displayMessage("No shape given");
        }
        this.view.updateLists();
        break;
      }
      case "Remove Key Frame": {
        try {
          this.removeFrame(this.view.getSelectedShape().getKey(), this.view.getSelectedKeyFrame());
        } catch (NullPointerException n) {
          this.view.displayMessage("No frame given");
        }
        this.view.updateLists();
        break;
      }
      case "Modify Selected Key Frame": {
        try {
          this.modifyFrame(this.view.getSelectedShape().getKey(), this.view.getSelectedKeyFrame(),
                  this.initializeFrame(this.view.getInputKeyFrameValues()));
        } catch (NullPointerException n) {
          this.view.displayMessage("No shape or key frame given");
        }
        this.view.updateLists();
        break;
      }
      case "Save Animation": {
        this.timer.stop();
        this.saveAnimation(this.view.savePopup());
        break;
      }
      case "Load File": {
        this.timer.stop();
        this.loadFile(this.view.loadFile());
        break;
      }
      case "Undo Edit":
        this.undo();
        break;
      case "Redo Edit":
        this.redo();
        break;
      default:
        this.view.displayMessage("Unknown action event.");
        break;
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    JList list = (JList) e.getSource();

    switch (list.getName()) {
      case "shape list": {
        this.view.setFrames();
        break;
      }
      case "frame list":
        this.view.setFrameValues();
        break;
      default:
        break;
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    this.setTime(((JSlider) e.getSource()).getValue());
  }

  /**
   * Constructs a key frame with the given list of values corresponding to key frame values.
   *
   * @param values the given values of key frame information
   * @return a key frame model
   */
  private KeyFrameModel initializeFrame(ArrayList<Integer> values) {
    if (values == null) {
      return null;
    }

    return new KeyFrame(values.get(0), new Rectangle(new Shape.ShapeBuilder().setX(values.get(1))
            .setY(values.get(2)).setWidth(3).setHeight(4).setRed(5).setGreen(6)
            .setBlue(7).build()));
  }

  /**
   * Constructs a shape with the given hash map of shape type to name.
   *
   * @param values the provided shape values.
   * @return a shape model
   */
  private ShapeModel initializeShape(HashMap<String, String> values) {
    Map.Entry<String, String> entry = values.entrySet().iterator().next();
    String type = entry.getKey();
    String name = entry.getValue();

    System.out.println(type + " " + name);

    //returns the shape
    if (type == null || name == null) {
      return null;
    } else if (type.equals("Rectangle")) {
      return new Rectangle(new Shape.ShapeBuilder().setKey(name));
    } else {
      return new Ellipse(new Shape.ShapeBuilder().setKey(name));
    }
  }
}
