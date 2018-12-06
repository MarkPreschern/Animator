package cs3500.animator.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import cs3500.animator.controller.EndOperation;
import cs3500.animator.controller.State;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.Shape;
import cs3500.animator.model.ShapeModel;

/**
 * Represents a JPanel which displays graphical information regarding an animation model.
 */
public class AnimationPanel extends JPanel implements ActionListener {

  protected ImmutableModel model;

  protected int time = 0;
  protected State state = State.RUN;
  protected EndOperation endOperation = EndOperation.OPEN;

  /**
   * Default constructor takes in an immutable model.
   *
   * @param model the animation model
   * @throws IllegalArgumentException if the model is null
   */
  protected AnimationPanel(ImmutableModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null.");
    }

    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    ArrayList<ShapeModel> shapes = new ArrayList<>();

    try {
      shapes = this.model.getShapes(this.time);
    } catch (IllegalArgumentException e) {
      this.applyEndOperation();
      if (this.endOperation != EndOperation.CLOSE) {
        shapes = this.model.getShapes(this.time);
      }
    }

    Graphics2D g2d = (Graphics2D) g;
    for (ShapeModel s : shapes) {
      g2d.setColor(s.getColor());

      //applies the shape's rotation
      g2d.translate(s.getPointTopLeft().x + s.getDimensions().x / 2,
              s.getPointTopLeft().y + s.getDimensions().y / 2);
      g2d.rotate(Math.toRadians(s.getTheta()));
      g2d.translate(-s.getPointTopLeft().x - s.getDimensions().x / 2,
              -s.getPointTopLeft().y - s.getDimensions().y / 2);

      switch (s.getType()) {
        case "Rectangle":
          g2d.fill(new Rectangle2D.Double(s.getPointTopLeft().x - this.model.getCanvas().getX(),
                  s.getPointTopLeft().y - this.model.getCanvas().getY(),
                  s.getDimensions().x, s.getDimensions().y));
          break;
        case "Ellipse":
          g2d.fill(new Ellipse2D.Double(s.getPointTopLeft().x - this.model.getCanvas().getX(),
                  s.getPointTopLeft().y - -this.model.getCanvas().getY(),
                  s.getDimensions().x, s.getDimensions().y));
          break;
        default:
          throw new IllegalArgumentException("Invalid shape type.");
      }
    }

    //increments time by the direction of animation
    this.time += this.state.getDirection();
  }

  /**
   * Applies what should be done to the JPanel when the animation is over, based on EndOperation.
   */
  private void applyEndOperation() {
    switch (endOperation) {
      case OPEN: {
        if (this.time < 0) {
          this.time = 0;
        } else {
          this.time = this.model.getEndTime();
        }
        break;
      }
      case CLOSE:
        this.setVisible(false);
        break;
      case LOOP: {
        if (this.time >= 0) {
          this.time = 0;
        } else {
          this.time = this.model.getEndTime();
        }
        break;
      }
      default:
        throw new IllegalStateException("Invalid end operation.");
    }
  }
}
