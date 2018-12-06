package cs3500.animator.view;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cs3500.animator.model.Animation;
import cs3500.animator.model.ImmutableAnimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Represents tests for public methods in the view factory class.
 */
public class ViewFactoryTest {

  @Test
  public void testGetView() {
    View v1 = ViewFactory.getView("text", new ImmutableAnimation(new Animation()),
            new StringBuilder(), 1);
    View v2 = ViewFactory.getView("visual", new ImmutableAnimation(new Animation()),
            new StringBuilder(), 1);
    View v3 = ViewFactory.getView("svg", new ImmutableAnimation(new Animation()),
            new StringBuilder(), 1);
    View v4 = ViewFactory.getView("edit", new ImmutableAnimation(new Animation()),
            new StringBuilder(), 1);

    assertNotEquals(v1, v2);
    assertNotEquals(v1, v3);
    assertNotEquals(v2, v3);
    assertNotEquals(v2,v4);

    assertEquals(v1.getClass(), TextView.class);
    assertEquals(v2.getClass(), VisualView.class);
    assertEquals(v3.getClass(), SVGView.class);
    assertEquals(v4.getClass(), VisualViewEditable.class);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testConstructorNullAppendableObject() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("View type, model, and appendable object can't be null.");
    ViewFactory.getView("", new ImmutableAnimation(new Animation()), null, 1);
  }

  @Test
  public void testConstructorNullModel() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("View type, model, and appendable object can't be null.");
    ViewFactory.getView("", null, new StringBuilder(), 1);
  }

  @Test
  public void testConstructorNullViewType() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("View type, model, and appendable object can't be null.");
    ViewFactory.getView(null, new ImmutableAnimation(new Animation()), new StringBuilder(),
            1);
  }

  @Test
  public void testConstructorInvalidSpeed() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Speed must be greater than zero.");
    ViewFactory.getView("", new ImmutableAnimation(new Animation()), new StringBuilder(),
            -1);
  }

  @Test
  public void testConstructorInvalidViewType() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid view type.");
    ViewFactory.getView("triangle", new ImmutableAnimation(new Animation()), new StringBuilder(),
            1);
  }
}
