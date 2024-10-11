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

public class PaperController {
  private GameStateContext context;

  @FXML private AnchorPane paperPane;
  @FXML private Slider paperSlider;
  @FXML private Label paperTitle;
  @FXML private Label paperHint;
  @FXML private ImageView imgPaperTop;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;

  @FXML
  public void initialize() throws ApiProxyException {
    // Initialize the slider to adjust opacity, position, and rotation
    paperSlider.setMin(0); // Minimum value of slider
    paperSlider.setMax(1); // Maximum value of slider

    // Capture the initial positions of the paper
    double initialX = imgPaperTop.getLayoutX(); // Initial X position
    double initialY = imgPaperTop.getLayoutY(); // Initial Y position

    // Bind the slider value to change the opacity, position, and rotation of paperTitle and
    // paperHint
    paperSlider
        .valueProperty()
        .addListener(
            (obs, oldVal, newVal) -> {

              // Adjust the layout X and Y positions of imgPaperTop relative to its initial position
              imgPaperTop.setLayoutX(initialX + newVal.doubleValue() * -300); // Moves horizontally
              imgPaperTop.setLayoutY(initialY + newVal.doubleValue() * 50); // Moves vertically

              // Rotate the imgPaperTop based on slider value
              // For clockwise rotation, increase angle from 0 to 360 degrees as slider value
              // changes from 0 to 1
              imgPaperTop.setRotate(
                  newVal.doubleValue() * -5); // Full rotation from 0 to 360 degrees
            });

    hidePaperPane(); // Hide paper pane initially
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
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

  public void showPaperPane() {
    paperPane.setVisible(true);
  }

  public void hidePaperPane() {
    paperPane.setVisible(false);
  }
}
