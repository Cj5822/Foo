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

public class SafeRingController {
  private GameStateContext context;

  @FXML private AnchorPane safeRingPane;
  @FXML private Rectangle rectExitRing;
  @FXML private Label hintText;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;
  @FXML private Slider ringSlider;

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

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    exitSafeRingPane();
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void showSafeRingPane() {
    safeRingPane.setVisible(true);
  }

  public void hideSafeRingPane() {
    safeRingPane.setVisible(false);
  }

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

  // Call this method when exiting the SafeRingPane
  public void exitSafeRingPane() {
    // Reset the slider and hint before exiting
    resetSliderAndHint();

    // Hide the SafeRingPane
    hideSafeRingPane();
  }

  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }
}
