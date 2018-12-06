Extra Credit Implemented:
    - Saving the animation to a text or svg file during animation
    - Loading a new file to the animation's user interface during animation
    - Animation can be reversed (animate in the opposite direction with respect to time)
    - 'undo' and 'redo' buttons are provided to undo or redo an edit to the model

Model:

    Interfaces:
    1) MotionModel
        - represents a motion between two key frames
        - allows a shape to apply this motion to it
        - toString gives information regarding a motion
        - contains pointers for the initial and final key frames of a motion

        Implemented Classes:
        a) Motion
            - represents a general motion where color, position, and size of a shape can change.

    2) ShapeModel
        - represents a shape with specified fields to describe it
        - toString gives information regarding an animation
        - getters obtain information regarding the shape
        - can make a new instance of a given shape

        Implemented Classes:
            a) Shape
                - represents an abstract Shape, as to be general.
                - contains a subclass called ShapeBuilder that instantiates it's fields
                - contains a key, as to verify uniqueness of a shape and to represent it's name as far
                as input into the cs3500.animator.model is concerned.
            b) Rectangle
                - represents a shape that has the properties of a rectangle
                - extends Shape
            c) Ellipse
                - represents a shape that has the properties of an ellipse
                - extends Shape

    3) AnimationModel
        - representation for managing multiple shapes and key frames
        - mutates shapes and key frames with respect to time and manual setters
        - toString like method return information regarding the shapes and key frames
        - getter obtains information regarding the shapes
        - tick() method gets all shapes with respect to their animations at a time
        - shapes, motions, and key frames can be added/removed
        - shapes can be replaced if of the same type (same subclass of the given shape)
        - Checks for consistency among shapes and motions

        Implemented Classes:
            a) Animation
                - carries out all functionality as described in it's cs3500.animator.model
                - represents data as a LinkedHashMap of Shapes to ArrayList of KeyFrame
                which correspond to that shape

    Class Canvas:
        - Represents the bounds of the animation (position and dimensions)

    Class ImmutableModel:
        - Represents an immutable implementation of the animation model
        - All setters throw an unsupported operation exception if called

    Class KeyFrame:
        - Represents a shape at a specific time


View:

    Interface:
        1) View
            - Represents some type of view, as to be general which can be displayed and animated
            - Allows for a view to be made visible and animated via public methods

            Implemented Classes:
            a) TextView
                - A text based view of the shapes and animations of an animation model
                - Doesn't support animations, only output to an Appendable object
            b) VisualView
                - A graphical representation of an animation model
                - Displays shapes on a JFrame gui, and allows for animation to occur
            c) SVGView
                - A textual description of an animation compliant with the SVG file format
                - Allows for output to an Appendable object, which can also be displayed on a
                browser as it's compliant with the SVG file format

            Extended Interface:
                1) ViewEditable
                    - Represents a view that can be edited by a user, allowing for it's animation
                     properties and model to be edited.
                    - Allows for updating the view itself through the model via public methods
                    - Views that are editable can display a message when an edit is made

                    a) VisualViewEditable
                        - A graphical representation of an animation model
                        - Allows for mutation of the animation during animation via user input
                        - Takes in a VisualView as a parameter
                        - Allows for a text or svg file to be saved during animation, with the
                        edits applied
                        - Allows for a new file to be loaded during animation

    Class AnimationPanel:
        - The JPanel on which graphics objects are updated and displayed for the visual view

    Class ViewFactory:
        - Represents a factory of views, creating a specific view based on the type given
        - Instantiates different types of views with the given parameters including a model, speed,
        and appendable object


Controller:

    Interface:
        1) ControllerModel
            - Represents some type of controller, as to be general which can show a view and run
            an animation
            - Additionally can change animation direction and speed, loop an animation, add
             and remove key frames, and save an editable animation as an svg or text file during
             animation
            - Extends ActionListener, ListSelectionListener, and ChangeListener to act as a
            listener for a view

            Implemented Classes:
            a) Controller
                - Implements all functionality as specified in it's model
                - Takes in a model and view as parameters, controlling interaction between them

    Enum State:
        - Represents the direction of animations (run, pause, rewind)
        - each direction has a value for making calculations in AnimationManagerModel easier

    Enum EndOperation:
        - Represents what to be done to a view that can be animated when it reaches it's end time

Main:

    Class Excellence:
        - Represents the entry point for an animation, allowing a user to display an animation
        - Allows for several different types of views, custom animations, tick speed, and desired
        file input/output
