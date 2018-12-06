package cs3500.animator.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ChangeEvent;

import cs3500.animator.model.Animation;
import cs3500.animator.model.ImmutableAnimation;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.KeyFrame;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;
import cs3500.animator.view.VisualView;
import cs3500.animator.view.VisualViewEditable;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for the controller's constructor and public methods.
 */
public class ControllerTest {

  Animation m;
  ImmutableModel im;
  VisualView v;
  VisualViewEditable ve;
  Controller c;

  /**
   * Initializes private variables to default values.
   */
  private void init() {
    this.m = new Animation();

    this.m.addShape(new Rectangle(new Shape.ShapeBuilder().setKey("HI GRADER!")));
    this.m.addKeyFrame("HI GRADER!", new KeyFrame(0,
            new Rectangle(new Shape.ShapeBuilder().setKey("HI GRADER!"))));
    this.m.addKeyFrame("HI GRADER!", new KeyFrame(20,
            new Rectangle(new Shape.ShapeBuilder().setKey("HI GRADER!"))));

    this.im = new ImmutableAnimation(m);
    this.v = new VisualView(im, 10);
    this.ve = new VisualViewEditable(v);
    this.c = new Controller();
    this.c.control(this.m, this.ve, 1);
  }

  @Test
  public void testActionPerformed() {
    this.init();

    //Asserts that action listeners are linked to the correct JComponent, thus performing the
    //correct update to the view/model
    ActionEvent e1 = new ActionEvent("", 1, "Run");
    ActionEvent e2 = new ActionEvent("", 1, "Pause");
    ActionEvent e3 = new ActionEvent("", 1, "Rewind");
    ActionEvent e4 = new ActionEvent("", 1, "Restart");
    ActionEvent e5 = new ActionEvent("", 1, "Loop");
    ActionEvent e6 = new ActionEvent("", 1, "Set Speed:");
    ActionEvent e7 = new ActionEvent("", 1, "Add Key Frame");
    ActionEvent e8 = new ActionEvent("", 1, "Add Shape");
    ActionEvent e9 = new ActionEvent("", 1, "Remove Shape");
    ActionEvent e10 = new ActionEvent("", 1, "Remove Key Frame");
    ActionEvent e11 = new ActionEvent("", 1, "Save Animation");
    ActionEvent e12 = new ActionEvent("", 1, "Load File");
    ActionEvent e13 = new ActionEvent("", 1, "Unknown");
    ActionEvent e14 = new ActionEvent("", 1, "Modify Selected Key Frame");
    ActionEvent e15 = new ActionEvent("", 1, "Undo Edit");
    ActionEvent e16 = new ActionEvent("", 1, "Redo Edit");


    this.c.actionPerformed(e1);
    assertEquals("View is running", ve.getCurrentMessage());

    this.c.actionPerformed(e2);
    assertEquals("View is paused", ve.getCurrentMessage());

    this.c.actionPerformed(e3);
    assertEquals("View is rewinding", ve.getCurrentMessage());

    this.c.actionPerformed(e4);
    assertEquals("View is restarting", ve.getCurrentMessage());

    this.c.actionPerformed(e5);
    assertEquals("View is looping", ve.getCurrentMessage());

    this.c.actionPerformed(e6);
    assertEquals("Speed can't be negative", ve.getCurrentMessage());

    this.c.actionPerformed(e7);
    assertEquals("No shape given", ve.getCurrentMessage());

    //this.c.actionPerformed(e8);
    //assertEquals("Shape can't be null.", ve.getCurrentMessage());
    //^^ Popup menu corresponding to the given command comes up

    this.c.actionPerformed(e9);
    assertEquals("No shape given", ve.getCurrentMessage());

    this.c.actionPerformed(e10);
    assertEquals("No frame given", ve.getCurrentMessage());

    //this.c.actionPerformed(e11);
    //assertEquals("No frame given", ve.getCurrentMessage());
    //^^ Popup menu corresponding to the given command comes up

    //this.c.actionPerformed(e12);
    //assertEquals("Specified file not found", ve.getCurrentMessage());
    //^^ Popup menu corresponding to the given command comes up

    this.c.actionPerformed(e13);
    assertEquals("Unknown action event.", ve.getCurrentMessage());

    this.c.actionPerformed(e14);
    assertEquals("No shape or key frame given", ve.getCurrentMessage());

    this.c.actionPerformed(e15);
    assertEquals("No undo history", ve.getCurrentMessage());

    this.c.actionPerformed(e16);
    assertEquals("No redo history", ve.getCurrentMessage());
  }

  @Test
  public void valueChanged() {
    this.init();

    JList l1 = new JList();
    l1.setName("shape list");
    JList l2 = new JList();
    l2.setName("frame list");
    JList l3 = new JList();
    l3.setName("");

    ListSelectionEvent e1 = new ListSelectionEvent(l1, 1, 4,
            true);
    ListSelectionEvent e2 = new ListSelectionEvent(l2, 1, 4,
            true);
    ListSelectionEvent e3 = new ListSelectionEvent(l3, 1, 4,
            true);

    this.c.valueChanged(e1);
    assertEquals("", ve.getCurrentMessage());
    //updates frames

    this.c.valueChanged(e2);
    assertEquals("", ve.getCurrentMessage());
    //updates input frames

    this.c.valueChanged(e3);
    assertEquals("", ve.getCurrentMessage());
    //no effect
  }

  @Test
  public void stateChanged() {
    this.init();

    JSlider j1 = new JSlider();
    j1.setValue(0);
    JSlider j2 = new JSlider();
    j2.setValue(10);
    JSlider j3 = new JSlider();
    j3.setValue(30);

    ChangeEvent e1 = new ChangeEvent(j1);
    ChangeEvent e2 = new ChangeEvent(j2);
    ChangeEvent e3 = new ChangeEvent(j3);

    this.c.stateChanged(e1);
    assertEquals("Time has been changed to 0", ve.getCurrentMessage());

    this.c.stateChanged(e2);
    assertEquals("Time has been changed to 10", ve.getCurrentMessage());

    this.c.stateChanged(e3);
    assertEquals("Invalid time given", ve.getCurrentMessage());
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testControlNullModel() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Model and view can't be null.");
    this.init();
    Controller test = new Controller();
    test.control(null, this.v, 10);
  }

  @Test
  public void testControlNullView() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Model and view can't be null.");
    this.init();
    Controller test = new Controller();
    test.control(this.m, null, 10);

  }

  @Test
  public void testControlNullModelAndView() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Model and view can't be null.");
    this.init();
    Controller test = new Controller();
    test.control(null, null, 10);

  }
}