package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;

public class FlippedWrenchController {
  private static GameStateContext context = new GameStateContext();

  @FXML private AnchorPane flippedWrenchPane;
  @FXML private Label hintText;
  @FXML private Button analyseButton;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideFlippedWrenchPane(); // Hide wrench pane initially
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  @FXML
  public void handleAnalyse(ActionEvent event) throws IOException {
    hintText.setVisible(true);
  }

  public void showFlippedWrenchPane() {
    flippedWrenchPane.setVisible(true);
  }

  public void hideFlippedWrenchPane() {
    flippedWrenchPane.setVisible(false);
  }
}
