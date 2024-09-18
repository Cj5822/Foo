package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;

public class OpenSafeController {
  private static GameStateContext context = new GameStateContext();

  @FXML private AnchorPane openSafePane;
  @FXML private Rectangle rectExitOpenSafe;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideOpenSafePane(); // Hide wrench pane initially
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void showOpenSafePane() {
    openSafePane.setVisible(true);
  }

  public void hideOpenSafePane() {
    openSafePane.setVisible(false);
  }
}
