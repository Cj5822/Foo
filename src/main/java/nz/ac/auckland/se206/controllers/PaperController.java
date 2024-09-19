package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;

public class PaperController {
  private static GameStateContext context = new GameStateContext();

  @FXML private AnchorPane paperPane;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hidePaperPane(); // Hide wrench pane initially
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
