package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

  @FXML
  public void initialize() throws ApiProxyException {
    // Initialize the slider to adjust opacity
    paperSlider.setMin(0);
    paperSlider.setMax(1);

    paperSlider
        .valueProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              // Set the opacity of the labels based on the slider value
              paperTitle.setOpacity(newVal.doubleValue());
              paperHint.setOpacity(newVal.doubleValue());
            });
    hidePaperPane(); // Hide wrench pane initially
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void showPaperPane() {
    paperPane.setVisible(true);
  }

  public void hidePaperPane() {
    paperPane.setVisible(false);
  }
}
