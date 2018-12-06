package cs3500.animator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import cs3500.animator.model.ImmutableModel;

/**
 * A graphical representation of an animation model.
 */
public class VisualView extends JFrame implements View {

  protected AnimationPanel animationPanel;
  protected int ticksPerSecond;

  /**
   * Default constructor takes in an appendable object and an immutable animation model. Assumes
   * that the user specified a canvas when building the model.
   *
   * @param model the immutable animation model
   * @throws IllegalArgumentException if the model is null or ticks per second is not greater than
   *                                  zero
   */
  public VisualView(ImmutableModel model, int ticksPerSecond) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null.");
    } else if (ticksPerSecond <= 0) {
      throw new IllegalArgumentException("Ticks per second must be greater than zero");
    }

    this.animationPanel = new AnimationPanel(model);
    this.ticksPerSecond = ticksPerSecond;

    this.animationPanel.setPreferredSize(new Dimension(model.getCanvas().getWidth(),
            model.getCanvas().getHeight()));

    this.setTitle("Excellence");
    this.add(animationPanel);
    this.setSize(model.getCanvas().getWidth(), model.getCanvas().getHeight());
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(new JScrollPane(animationPanel));
    this.pack();
  }

  @Override
  public void makeVisible() throws IllegalArgumentException {
    this.setVisible(true);
  }

  @Override
  public void animate(Timer timer) throws UnsupportedOperationException {
    timer.addActionListener(this.animationPanel);
    timer.setDelay(1000 / this.ticksPerSecond);
    timer.start();
  }
}
