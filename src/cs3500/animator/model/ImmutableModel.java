package cs3500.animator.model;

/**
 * Represents the model for an immutable implementation of the animation model. No new methods are
 * required, as those from AnimationModel are carried over. This interface is required however, as
 * to distinguish the difference between classes that implement a mutable vs immutable model.
 */
public interface ImmutableModel extends AnimationModel {
}
