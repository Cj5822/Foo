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
