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
 * Controller for managing the safe ring interaction within the game. This controller handles the
 * functionality of the safe ring pane, including displaying hints, controlling slider interactions,
 * and managing hover effects for the exit button. It is responsible for controlling the visibility
 * of elements like the slider and hint text based on user actions.
 *
 * <p>The class includes methods for initializing the safe ring pane, handling rectangle clicks,
 * displaying and hiding the safe ring pane, resetting the slider, and managing hover effects on the
 * exit button. It also interacts with the {@link GameStateContext} to pass game-specific data and
 * actions.
 */
public class SafeRingController {
  private GameStateContext context;

  @FXML private AnchorPane safeRingPane;
  @FXML private Rectangle rectExitRing;
  @FXML private Label hintText;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;
  @FXML private Slider ringSlider;

  /**
   * Initializes the safe ring pane and its components, including setting up the slider to control
   * the visibility of the hint text based on the slider's value. Initially hides the safe ring pane
   * and sets the hint text opacity to zero.
   *
   * @throws ApiProxyException if an error occurs during initialization with the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Hide hint text initially and set its opacity to 0 (completely hidden)
    hintText.setVisible(true);
    hintText.setOpacity(0);

    // Set the initial visibility of the safe ring pane
    hideSafeRingPane();

    // Add a listener to the slider to gradually show the hintText as the slider moves
    ringSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              double sliderProgress =
                  newValue.doubleValue() / ringSlider.getMax(); // Normalize slider value

              // Gradually adjust the opacity of hintText based on slider value
              hintText.setOpacity(sliderProgress);

              // If the slider is fully slided to the maximum, hide the slider
              if (newValue.doubleValue() == ringSlider.getMax()) {
                ringSlider.setVisible(false); // Hide the slider when fully pulled
                ringSlider.setDisable(true);
              }
            });
  }

  /**
   * Sets the game state context for the safe ring controller.
   *
   * @param context the current game state context.
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the click event on a rectangle shape. Exits the safe ring pane and passes the
   * rectangle's ID to the context handler for further processing.
   *
   * @param event the mouse event triggered by clicking a rectangle.
   * @throws IOException if an I/O error occurs during handling.
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    exitSafeRingPane();
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /** Displays the safe ring pane by making it visible. */
  public void showSafeRingPane() {
    safeRingPane.setVisible(true);
  }

  /** Hides the safe ring pane by making it invisible. */
  public void hideSafeRingPane() {
    safeRingPane.setVisible(false);
  }

  /**
   * Resets the slider and the hint text to their initial states. This includes resetting the
   * slider's value, making the slider visible, and hiding the hint text.
   */
  public void resetSliderAndHint() {
    // Reset the slider to its initial value (usually 0)
    ringSlider.setValue(0);

    // Make the slider visible again
    ringSlider.setVisible(true);

    // Reset the opacity of the hint text to fully hidden
    hintText.setOpacity(0);

    // Enable the slider
    ringSlider.setDisable(false);
  }

  /** Exits the safe ring pane by resetting the slider and hint, then hiding the pane. */
  public void exitSafeRingPane() {
    // Reset the slider and hint before exiting
    resetSliderAndHint();

    // Hide the SafeRingPane
    hideSafeRingPane();
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
}
