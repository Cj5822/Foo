package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

public class ScrunchedPaperController {
  private static GameStateContext context = new GameStateContext();

  @FXML private AnchorPane scrunchedPaperPane;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideScrunchedPaperPane(); // Hide wrench pane initially
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  @FXML
  public void handleAnalyse(ActionEvent event) throws IOException {
    App.openPaperHint(event);
      return;
  }

  public void showScrunchedPaperPane() {
    scrunchedPaperPane.setVisible(true);
  }

  public void hideScrunchedPaperPane() {
    scrunchedPaperPane.setVisible(false);
  }
}
