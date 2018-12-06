package cs3500.animator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cs3500.animator.controller.Controller;
import cs3500.animator.model.Animation;
import cs3500.animator.model.ImmutableAnimation;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.View;
import cs3500.animator.view.ViewFactory;

/**
 * Represents the entry point for an animation, allowing a user to display an animation.
 */
public final class Excellence {

  /**
   * Takes inputs as command-line arguments and produces the appropriate animation.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    //if arguments length is invalid
    if (args.length == 0) {
      throw new IllegalArgumentException("Empty input is invalid. Must specify in and view type.");
    } else if (args.length > 8) {
      throw new IllegalArgumentException("No more than 4 pairs of input are allowed to be given.");
    }

    //stores arguments into a hash map to resemble pairs
    HashMap<String, String> pairs = new HashMap<>();

    String key = null;
    for (int i = 0; i < args.length; i++) {
      if (i % 2 == 0) {
        key = args[i];
      } else {
        pairs.put(key, args[i]);
        key = null;
      }
    }

    if (key != null) {
      throw new IllegalArgumentException("An odd number of inputs is invalid.");
    }

    String in = null;
    Appendable out = System.out;
    String type = null;
    int speed = 1;

    //sets in, out, view, and speed based on the pairs
    for (Map.Entry<String, String> entry : pairs.entrySet()) {
      switch (entry.getKey()) {
        case "-in":
          in = entry.getValue();
          break;
        case "-out":
          try {
            out = new FileWriter(new File(entry.getValue()));
          } catch (IOException e) {
            throw new IllegalArgumentException("Out could not be found.");
          }
          break;
        case "-view":
          type = entry.getValue();
          break;
        case "-speed": {
          try {
            speed = Integer.parseInt(entry.getValue());
            if (speed <= 0) {
              throw new IllegalArgumentException("Speed must be an integer greater than zero.");
            }
          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Speed specified is not an integer.");
          }
          break;
        }
        default:
          throw new IllegalArgumentException("Invalid argument type.");
      }
    }

    if (in == null || type == null) {
      throw new IllegalArgumentException("in and view type must be specified.");
    }

    //creates an immutable model if the file can be found
    File file = new File(in);
    FileReader fileReader;
    try {
      fileReader = new FileReader(file);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Specified file not found.");
    }

    Animation model = AnimationReader.parseFile(fileReader,
            new Animation.Builder());
    ImmutableModel immutableModel = new ImmutableAnimation(model);

    //Creates and displays a view which is created by the view factory
    View view = ViewFactory.getView(type, immutableModel, out, speed);
    try {
      view.makeVisible();
      if (out instanceof FileWriter) {
        ((FileWriter) out).flush();
        ((FileWriter) out).close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Controller controller = new Controller();
    controller.control(model, view, speed);
  }
}
