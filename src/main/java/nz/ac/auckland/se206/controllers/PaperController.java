package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;

/**
 * Controller for managing the paper interaction within the game. This class is responsible for
 * handling the paper pane's appearance, movement, rotation, and the interaction with clickable
 * rectangles. It also manages the state of the paper's slider and the hint elements.
 *
 * <p>This class contains methods to initialize the paper's movement and rotation based on the
 * slider value, handle user interaction with the paper (such as mouse hover and click events), and
 * control the visibility of the paper pane.
 *
 * <p>It interacts with the game's context to manage state and communicate user actions.
 */
public class PaperController {
  private GameStateContext context;

  @FXML private AnchorPane paperPane;
  @FXML private Slider paperSlider;
  @FXML private Label paperTitle;
  @FXML private Label paperHint;
  @FXML private ImageView imgPaperTop;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;

  /**
   * Initializes the paper pane by setting up the slider properties and binding the slider values to
   * the paper's movement and rotation. Hides the paper pane initially.
   *
   * @throws ApiProxyException if an error occurs during initialization with the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Initialize the slider for opacity, position, and rotation
    paperSlider.setMin(0); // Minimum value
    paperSlider.setMax(1); // Maximum value

    // Capture initial positions of the paper
    double initialX = imgPaperTop.getLayoutX();
    double initialY = imgPaperTop.getLayoutY();

    // Bind slider value to adjust imgPaperTop's position and rotation
    paperSlider
        .valueProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              imgPaperTop.setLayoutX(initialX + newVal.doubleValue() * -300); // Horizontal move
              imgPaperTop.setLayoutY(initialY + newVal.doubleValue() * 50); // Vertical move
              imgPaperTop.setRotate(newVal.doubleValue() * -5); // Rotate image
            });

    hidePaperPane(); // Hide paper pane initially
  }

  /**
   * Sets the game state context for the paper controller.
   *
   * @param context the current game state context.
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the click event on a rectangle shape. Exits the paper pane and passes the rectangle's
   * ID to the context handler for further processing.
   *
   * @param event the mouse event triggered by clicking a rectangle.
   * @throws IOException if an I/O error occurs during handling.
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    exitPaperPane();
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /**
   * Handles the mouse hover event when entering the area of the exit button. Displays the hover
   * image for the exit button.
   *
   * @param event the mouse event triggered by entering the hover area.
   */
  @FXML
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  /**
   * Handles the mouse hover event when exiting the area of the exit button. Hides the hover image
   * and displays the unhovered image for the exit button.
   *
   * @param event the mouse event triggered by exiting the hover area.
   */
  @FXML
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  /** Displays the paper pane by making it visible. */
  public void showPaperPane() {
    paperPane.setVisible(true);
  }

  /** Hides the paper pane by making it invisible. */
  public void hidePaperPane() {
    paperPane.setVisible(false);
  }

  /**
   * Resets the slider and the hint label to their initial states. This includes resetting the
   * slider's value, making the slider visible, and hiding the paper title and hint text.
   */
  public void resetSliderAndHint() {
    // Reset the slider to its initial value (usually 0)
    paperSlider.setValue(0);

    // Make the slider visible again
    paperSlider.setVisible(true);

    // Reset the opacity of the hint text to fully hidden
    paperTitle.setOpacity(0);

    paperHint.setOpacity(0);
  }

  /** Exits the paper pane by resetting the slider and hint, then hiding the pane. */
  public void exitPaperPane() {
    // Reset the slider and hint before exiting
    resetSliderAndHint();

    // Hide the SafeRingPane
    hidePaperPane();
  }
}
