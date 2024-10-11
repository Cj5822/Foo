package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideSafeRingPane();
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void showSafeRingPane() {
    safeRingPane.setVisible(true);
  }

  public void hideSafeRingPane() {
    safeRingPane.setVisible(false);
  }

  @FXML
  public void handleRingAnalyse(ActionEvent event) throws IOException {
    hintText.setVisible(true);
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
