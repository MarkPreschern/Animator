package cs3500.animator.view;

import java.awt.Dimension;
import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.border.EtchedBorder;

import cs3500.animator.controller.ControllerModel;
import cs3500.animator.controller.EndOperation;
import cs3500.animator.controller.State;
import cs3500.animator.model.ImmutableModel;
import cs3500.animator.model.KeyFrameModel;
import cs3500.animator.model.ShapeModel;

/**
 * Represents a view that can be edited in various ways during animation by a user.
 */
public class VisualViewEditable extends JFrame implements ViewEditable {

  private AnimationPanel animationPanel;
  private int ticksPerSecond;

  private JPanel userInterface;

  private JPanel time;
  private JButton run;
  private JButton pause;
  private JButton rewind;
  private JButton restart;

  private JPanel speed;
  private JButton loop;
  private JButton setSpeed;
  private JTextField speedValue;

  private JPanel lists;
  private JList shapes;
  private JList frames;

  private JPanel modelChanges;
  private JButton addShape;
  private JButton removeShape;
  private JButton addFrame;
  private JButton removeFrame;
  private JButton modifyFrame;

  private JPanel frameValues;
  private ArrayList<JTextField> frameFields;

  private JSlider timeSlider;

  private JLabel messageToUser;
  private Timer messageTimer;

  private JButton saveAnimation;
  private JButton loadFile;

  private JPanel undoRedoPanel;
  private JButton undo;
  private JButton redo;

  private JPanel layerPanel;
  private JButton addShapeToLayer;
  private JButton deleteLayer;
  private JButton reorderLayer;

  /**
   * Constructs an enhanced view using the given basic visual view.
   *
   * @param visualView a basic visual view
   */
  public VisualViewEditable(VisualView visualView) {
    this.animationPanel = visualView.animationPanel;
    this.ticksPerSecond = visualView.ticksPerSecond;

    ImmutableModel model = this.animationPanel.model;
    this.animationPanel.setPreferredSize(new Dimension(model.getCanvas().getWidth(),
            model.getCanvas().getHeight()));

    this.userInterface = new JPanel();
    this.userInterface.setLayout(new BoxLayout(this.userInterface, BoxLayout.Y_AXIS));

    this.messageToUser = new JLabel();
    this.messageToUser.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.messageToUser.setOpaque(true);
    this.messageToUser.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    this.messageToUser.setVisible(false);
    this.messageTimer = new Timer(5000, e -> this.messageToUser.setVisible(false));

    this.timeSlider = new JSlider(JSlider.HORIZONTAL, 0,
            this.animationPanel.model.getEndTime(), 0);
    this.timeSlider.setMajorTickSpacing(this.animationPanel.model.getEndTime() / 4);
    this.timeSlider.setMinorTickSpacing(0);
    this.timeSlider.setPaintTicks(true);
    this.timeSlider.setPaintLabels(true);
    this.timeSlider.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    JPanel saveAndLoadPanel = new JPanel();
    saveAndLoadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    saveAndLoadPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.saveAnimation = new JButton("Save Animation");
    this.loadFile = new JButton("Load File");

    saveAndLoadPanel.add(this.saveAnimation);
    saveAndLoadPanel.add(this.loadFile);

    this.setTimePanel();
    this.setSpeedPanel();
    this.setLists();
    this.setModelChangesPanel();
    this.setFrameValuesPanel();
    this.setUndoRedoPanel();
    this.setLayerPanel();

    this.userInterface.add(this.messageToUser);
    this.userInterface.add(this.time);
    this.userInterface.add(this.speed);
    this.userInterface.add(this.lists);
    this.userInterface.add(this.modelChanges);
    this.userInterface.add(this.frameValues);
    this.userInterface.add(this.layerPanel);
    this.userInterface.add(this.timeSlider);
    this.userInterface.add(saveAndLoadPanel);
    this.userInterface.add(this.undoRedoPanel);

    this.setTitle("Excellence");

    this.setSize(model.getCanvas().getWidth(), model.getCanvas().getHeight());
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(this.userInterface);
    this.add(this.animationPanel);
    this.add(new JScrollPane(this.animationPanel));
    this.pack();
  }

  /**
   * Initializes the time panel and it's sub JComponents.
   */
  private void setTimePanel() {
    this.time = new JPanel();
    this.time.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.time.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.run = new JButton("Run");
    this.pause = new JButton("Pause");
    this.rewind = new JButton("Rewind");
    this.restart = new JButton("Restart");

    this.time.add(this.run);
    this.time.add(this.pause);
    this.time.add(this.rewind);
    this.time.add(this.restart);
  }

  /**
   * Initializes the speed panel and it's sub JComponents.
   */
  private void setSpeedPanel() {
    this.speed = new JPanel();
    this.speed.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.speed.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.loop = new JButton("Loop");
    this.setSpeed = new JButton("Set Speed:");
    this.speedValue = new JTextField(5);

    this.speed.add(this.loop);
    this.speed.add(this.setSpeed);
    this.speed.add(this.speedValue);
  }

  /**
   * Initializes the lists panel and it's sub JComponents.
   */
  private void setLists() {
    this.lists = new JPanel();
    this.lists.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.lists.setLayout(new BoxLayout(this.lists, BoxLayout.X_AXIS));
    this.lists.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    this.lists.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
            "                          Shapes & Key Frames                          "));

    DefaultListModel<String> listOfShapes = new DefaultListModel<>();
    for (ShapeModel s : this.animationPanel.model.getShapes()) {
      listOfShapes.addElement(s.getType() + " " + s.getKey());
    }

    DefaultListModel<String> listOfFrames = new DefaultListModel<>();
    ArrayList<KeyFrameModel> f;
    if (this.animationPanel.model.getShapes().size() == 0) {
      f = new ArrayList<>();
    } else {
      f = new ArrayList<>(
              this.animationPanel.model.getShapeKeyFrameRepresentation().values()).get(0);
    }
    for (KeyFrameModel k : f) {
      listOfFrames.addElement(Integer.toString((int) k.getTime()));
    }

    this.shapes = new JList(listOfShapes);
    this.shapes.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.shapes.setName("shape list");

    this.frames = new JList(listOfFrames);
    this.frames.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.frames.setName("frame list");

    this.lists.add(new JScrollPane(this.shapes));
    this.lists.add(new JScrollPane(this.frames));
  }

  /**
   * Initializes the model changes panel and it's sub JComponents.
   */
  private void setModelChangesPanel() {
    this.modelChanges = new JPanel();
    this.modelChanges.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.modelChanges.setLayout(new BoxLayout(this.modelChanges, BoxLayout.Y_AXIS));
    this.modelChanges.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    JPanel shapeChanges = new JPanel();
    shapeChanges.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel frameChanges = new JPanel();
    frameChanges.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.addShape = new JButton("Add Shape");
    this.removeShape = new JButton("Remove Shape");
    this.addFrame = new JButton("Add Key Frame");
    this.removeFrame = new JButton("Remove Key Frame");

    shapeChanges.add(this.addShape);
    shapeChanges.add(this.removeShape);
    frameChanges.add(this.addFrame);
    frameChanges.add(this.removeFrame);

    this.modelChanges.add(shapeChanges);
    this.modelChanges.add(frameChanges);
  }

  /**
   * Initializes the frame values panel and it's sub JComponents.
   */
  private void setFrameValuesPanel() {
    this.frameValues = new JPanel();
    this.frameValues.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.frameValues.setLayout(new BoxLayout(this.frameValues, BoxLayout.Y_AXIS));
    this.frameValues.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    JPanel frameValuesTop = new JPanel();
    frameValuesTop.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel frameValuesMiddle = new JPanel();
    frameValuesMiddle.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel frameValuesBottom = new JPanel();
    frameValuesBottom.setAlignmentX(Component.CENTER_ALIGNMENT);

    ArrayList<JTextArea> frameAreas = new ArrayList<>();
    this.frameFields = new ArrayList<>();

    frameAreas.add(new JTextArea("X"));
    frameAreas.add(new JTextArea("Y"));
    frameAreas.add(new JTextArea("Width"));
    frameAreas.add(new JTextArea("Height"));
    frameAreas.add(new JTextArea("Red"));
    frameAreas.add(new JTextArea("Green"));
    frameAreas.add(new JTextArea("Blue"));
    frameAreas.add(new JTextArea("Time"));
    frameAreas.add(new JTextArea("Theta"));
    frameAreas.add(new JTextArea("Layer"));

    for (int i = 0; i < 10; i++) {
      this.frameFields.add(new JTextField(3));
    }

    for (int i = 0; i < frameAreas.size(); i++) {
      if (i < 4) {
        frameValuesTop.add(frameAreas.get(i));
        frameValuesTop.add(this.frameFields.get(i));
      } else if (i < 8) {
        frameValuesMiddle.add(frameAreas.get(i));
        frameValuesMiddle.add(this.frameFields.get(i));
      } else {
        frameValuesBottom.add(frameAreas.get(i));
        frameValuesBottom.add(this.frameFields.get(i));
      }
    }

    this.modifyFrame = new JButton("Modify Selected Key Frame");
    this.modifyFrame.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.frameValues.add(this.modifyFrame);
    this.frameValues.add(frameValuesTop);
    this.frameValues.add(frameValuesMiddle);
    this.frameValues.add(frameValuesBottom);
  }

  /**
   * Initializes the undo and redo buttons on a panel.
   */
  private void setUndoRedoPanel() {
    this.undoRedoPanel = new JPanel();
    this.undoRedoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.undoRedoPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    this.undo = new JButton("Undo Edit");
    this.undo.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.redo = new JButton("Redo Edit");
    this.redo.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.undoRedoPanel.add(this.undo);
    this.undoRedoPanel.add(this.redo);
  }

  /**
   * Initializes the layer buttons on a panel.
   */
  private void setLayerPanel() {
    this.layerPanel = new JPanel();
    this.layerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.layerPanel.setLayout(new BoxLayout(this.layerPanel, BoxLayout.Y_AXIS));
    this.layerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

    JPanel p1 = new JPanel();
    p1.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel p2 = new JPanel();
    p2.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.addShapeToLayer = new JButton("Add Shape To Layer");
    this.addShapeToLayer.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.deleteLayer = new JButton("Delete Layer");
    this.deleteLayer.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.reorderLayer = new JButton("Reorder Layer");
    this.reorderLayer.setAlignmentX(Component.CENTER_ALIGNMENT);

    p1.add(this.addShapeToLayer);
    p2.add(this.deleteLayer);
    p2.add(this.reorderLayer);

    this.layerPanel.add(p1);
    this.layerPanel.add(p2);
  }

  @Override
  public void makeVisible() throws IllegalStateException {
    this.setVisible(true);
  }

  @Override
  public void animate(Timer timer) throws UnsupportedOperationException {
    timer.addActionListener(this.animationPanel);
    timer.setDelay(1000 / this.ticksPerSecond);
    timer.start();
  }

  @Override
  public void applyModelUpdate(ImmutableModel model) {
    this.animationPanel.model = model;
    this.animationPanel.repaint();
  }

  @Override
  public void applyAnimationUpdate(int ticksPerSecond, State state, EndOperation endOperation)
          throws UnsupportedOperationException {
    this.ticksPerSecond = ticksPerSecond;
    this.animationPanel.state = state;
    this.animationPanel.endOperation = endOperation;
    this.animationPanel.repaint();
  }

  @Override
  public void updateFrameSize() {
    ImmutableModel model = this.animationPanel.model;

    this.animationPanel.setPreferredSize(new Dimension(model.getCanvas().getWidth(),
            model.getCanvas().getHeight()));
    this.animationPanel.revalidate();

    if (model.getCanvas().getHeight() > this.userInterface.getHeight()) {
      this.setSize(model.getCanvas().getWidth() + this.userInterface.getWidth(),
              model.getCanvas().getHeight());
    } else {
      this.setSize(model.getCanvas().getWidth() + this.userInterface.getWidth(),
              this.userInterface.getHeight());
    }
    this.revalidate();
  }

  @Override
  public void setTime(int time) {
    this.animationPanel.time = time;
  }

  @Override
  public void displayMessage(String message) {
    this.messageToUser.setText(message);
    this.messageToUser.setVisible(true);
    this.messageTimer.restart();
  }

  @Override
  public void setListener(ControllerModel listener) {
    this.run.addActionListener(listener);
    this.pause.addActionListener(listener);
    this.rewind.addActionListener(listener);
    this.restart.addActionListener(listener);

    this.loop.addActionListener(listener);
    this.setSpeed.addActionListener(listener);

    this.shapes.addListSelectionListener(listener);
    this.frames.addListSelectionListener(listener);

    this.addShape.addActionListener(listener);
    this.removeShape.addActionListener(listener);
    this.addFrame.addActionListener(listener);
    this.removeFrame.addActionListener(listener);
    this.modifyFrame.addActionListener(listener);

    this.timeSlider.addChangeListener(listener);
    this.saveAnimation.addActionListener(listener);
    this.loadFile.addActionListener(listener);

    this.undo.addActionListener(listener);
    this.redo.addActionListener(listener);

    this.addShapeToLayer.addActionListener(listener);
    this.deleteLayer.addActionListener(listener);
    this.reorderLayer.addActionListener(listener);
  }

  @Override
  public int getSpeed() {
    try {
      this.ticksPerSecond = Integer.parseInt(this.speedValue.getText());
      return this.ticksPerSecond;
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  @Override
  public void setFrames() {
    int index = this.shapes.getSelectedIndex();
    if (index != -1) {
      //sets the key frames corresponding to the index of the selected shape
      DefaultListModel<String> listOfFrames = (DefaultListModel) this.frames.getModel();
      listOfFrames.clear();
      ArrayList<KeyFrameModel> f = new ArrayList<>(
              this.animationPanel.model.getShapeKeyFrameRepresentation().values()).get(index);
      for (KeyFrameModel k : f) {
        listOfFrames.addElement(Integer.toString((int) k.getTime()));
      }

      ShapeModel clickedShape = new ArrayList<>(this.animationPanel.model
              .getShapeKeyFrameRepresentation().keySet()).get(index);
      this.frameFields.get(9).setText(Integer.toString((int) clickedShape.getLayer()));

      this.userInterface.repaint();
    }
  }

  @Override
  public void setFrameValues() {
    int index = this.shapes.getSelectedIndex();
    int index2 = this.frames.getSelectedIndex();

    if (index != -1 && index2 != -1) {
      KeyFrameModel clicked = new ArrayList<>(this.animationPanel.model
              .getShapeKeyFrameRepresentation().values()).get(index).get(index2);

      this.frameFields.get(0).setText(Integer.toString(
              (int) clicked.getShape().getPointTopLeft().x));
      this.frameFields.get(1).setText(Integer.toString(
              (int) clicked.getShape().getPointTopLeft().y));
      this.frameFields.get(2).setText(Integer.toString(
              (int) clicked.getShape().getDimensions().x));
      this.frameFields.get(3).setText(Integer.toString(
              (int) clicked.getShape().getDimensions().y));
      this.frameFields.get(4).setText(Integer.toString(clicked.getShape().getColor().getRed()));
      this.frameFields.get(5).setText(Integer.toString(clicked.getShape().getColor().getGreen()));
      this.frameFields.get(6).setText(Integer.toString(clicked.getShape().getColor().getBlue()));
      this.frameFields.get(7).setText(Integer.toString((int) clicked.getTime()));
      this.frameFields.get(8).setText(Integer.toString((int) clicked.getShape().getTheta()));

      this.userInterface.repaint();
    }
  }

  @Override
  public ShapeModel getSelectedShape() {
    if (this.shapes.getSelectedIndex() == -1) {
      return null;
    } else {
      return this.animationPanel.model.getShapes().get(this.shapes.getSelectedIndex());
    }
  }

  @Override
  public KeyFrameModel getSelectedKeyFrame() throws UnsupportedOperationException {
    if (this.shapes.getSelectedIndex() == -1 || this.frames.getSelectedIndex() == -1) {
      return null;
    } else {
      return new ArrayList<>(this.animationPanel.model.getShapeKeyFrameRepresentation().values())
              .get(this.shapes.getSelectedIndex()).get(this.frames.getSelectedIndex());
    }
  }

  @Override
  public ArrayList<Integer> getInputKeyFrameValues() {
    try {
      int x = Integer.parseInt(this.frameFields.get(0).getText());
      int y = Integer.parseInt(this.frameFields.get(1).getText());
      int w = Integer.parseInt(this.frameFields.get(2).getText());
      int h = Integer.parseInt(this.frameFields.get(3).getText());
      int r = Integer.parseInt(this.frameFields.get(4).getText());
      int g = Integer.parseInt(this.frameFields.get(5).getText());
      int b = Integer.parseInt(this.frameFields.get(6).getText());
      int t = Integer.parseInt(this.frameFields.get(7).getText());
      return new ArrayList<>(Arrays.asList(t, x, y, w, h, r, g, b));
    } catch (NumberFormatException e) {
      this.displayMessage("Field doesn't contain an integer");
    } catch (IllegalArgumentException e2) {
      this.displayMessage(e2.getMessage());
    }
    return null;
  }

  @Override
  public void updateLists() {
    DefaultListModel<String> listOfShapes = (DefaultListModel) this.shapes.getModel();
    listOfShapes.clear();
    for (ShapeModel s : this.animationPanel.model.getShapes()) {
      listOfShapes.addElement(s.getType() + " " + s.getKey());
    }

    DefaultListModel<String> listOfFrames = (DefaultListModel) this.frames.getModel();
    listOfFrames.clear();
    ArrayList<KeyFrameModel> f;
    if (this.animationPanel.model.getShapes().size() == 0) {
      f = new ArrayList<>();
    } else if (this.shapes.getSelectedIndex() == -1) {
      f = new ArrayList<>(this.animationPanel.model.getShapeKeyFrameRepresentation().values())
              .get(0);
    } else {
      f = new ArrayList<>(this.animationPanel.model.getShapeKeyFrameRepresentation().values())
              .get(this.shapes.getSelectedIndex());
    }

    for (KeyFrameModel k : f) {
      listOfFrames.addElement(Integer.toString((int) k.getTime()));
    }

    this.userInterface.repaint();
  }

  @Override
  public HashMap<String, String> shapePopup() {
    JPanel popupMenu = new JPanel();
    popupMenu.setLayout(new BoxLayout(popupMenu, BoxLayout.Y_AXIS));

    JPanel names = new JPanel();
    names.setLayout(new BoxLayout(names, BoxLayout.X_AXIS));

    JTextArea name = new JTextArea("Name:");
    name.setOpaque(false);
    JTextField giveName = new JTextField(5);
    JComboBox shapeType = new JComboBox(new String[]{"Rectangle", "Ellipse"});

    names.add(name);
    names.add(giveName);

    popupMenu.add(names);
    popupMenu.add(shapeType);

    //creates the popup
    int j = JOptionPane.showOptionDialog(new JFrame(), popupMenu, "Add Shape",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.UNDEFINED_CONDITION, null, null, null);

    //returns shape type and name
    HashMap<String, String> r = new HashMap<>();
    if (j == -1) {
      r.put(null, null);
      return r;
    } else {
      r.put((String) shapeType.getSelectedItem(), giveName.getText());
      return r;
    }
  }

  @Override
  public HashMap<String, Appendable> savePopup() {
    JPanel popupMenu = new JPanel();
    popupMenu.setLayout(new BoxLayout(popupMenu, BoxLayout.Y_AXIS));

    JPanel names = new JPanel();
    names.setLayout(new BoxLayout(names, BoxLayout.X_AXIS));

    JTextArea name = new JTextArea("File Path:");
    name.setOpaque(false);
    JTextField giveName = new JTextField(5);
    JComboBox fileType = new JComboBox(new String[]{"Svg", "Txt"});

    names.add(name);
    names.add(giveName);

    popupMenu.add(names);
    popupMenu.add(fileType);

    //displays the popup
    int j = JOptionPane.showOptionDialog(new JFrame(), popupMenu, "Save Animation",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.UNDEFINED_CONDITION, null, null, null);

    //sets out
    Appendable out = System.out;
    if (!giveName.getText().equals("")) {
      try {
        out = new FileWriter(new File(giveName.getText()));
      } catch (IOException ignore) {
        out = null;
      }
    }

    //Sets the return map to the type and out
    HashMap<String, Appendable> map = new HashMap<>();
    if (j == -1) {
      map.put(null, out);
    } else if (fileType.getSelectedItem().equals("Svg")) {
      map.put("Svg", out);
    } else {
      map.put("Text", out);
    }
    return map;
  }

  @Override
  public File loadFile() {
    JPanel popupMenu = new JPanel();
    popupMenu.setLayout(new BoxLayout(popupMenu, BoxLayout.Y_AXIS));

    JPanel names = new JPanel();
    names.setLayout(new BoxLayout(names, BoxLayout.X_AXIS));

    JTextArea name = new JTextArea("File Path:");
    name.setOpaque(false);
    JTextField giveName = new JTextField(5);

    names.add(name);
    names.add(giveName);

    popupMenu.add(names);

    //displays the popup
    int j = JOptionPane.showOptionDialog(new JFrame(), popupMenu, "Load File",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.UNDEFINED_CONDITION, null, null, null);

    if (j == -1) {
      return null;
    } else {
      return new File(giveName.getText());
    }
  }

  @Override
  public int getLayerValue() {
    //displays the popup
    return Integer.parseInt(JOptionPane.showInputDialog(new JFrame(), "layer:"));
  }

  @Override
  public HashMap<Integer, Integer> getLayerValues() {
    //displays the popup
    int i1 = Integer.parseInt(JOptionPane.showInputDialog(new JFrame(), "current layer:"));
    int i2 = Integer.parseInt(JOptionPane.showInputDialog(new JFrame(), "new layer:"));

    HashMap<Integer, Integer> h = new HashMap<>();
    h.put(i1, i2);
    return h;
  }

  /**
   * Gets the message being displayed currently to the user about their edit.
   *
   * @return the current message
   */
  public String getCurrentMessage() {
    return this.messageToUser.getText();
  }
}
