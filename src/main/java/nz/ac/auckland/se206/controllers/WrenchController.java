package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;

public class WrenchController {
  private static GameStateContext context = new GameStateContext();

  @FXML private AnchorPane wrenchPane;
  @FXML private Rectangle rectExitWrench;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideWrenchPane(); // Hide wrench pane initially
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void flipWrench() {
    System.out.println("Flipping wrench test");
  }

  public void showWrenchPane() {
    wrenchPane.setVisible(true);
  }

  public void hideWrenchPane() {
    wrenchPane.setVisible(false);
  }
}
