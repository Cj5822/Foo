package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

public class GameOverController {
  @FXML private TextArea explanationText;
  @FXML private Pane gameOverPane;
  @SuppressWarnings("unused")
  private GameStateContext context;

  @FXML
  public void initialize() throws ApiProxyException {
    gameOverPane.setVisible(false);
    explanationText.setText("TESTING");
  }

    @FXML
  public void handleReplay(ActionEvent event) throws IOException {
    App.resetGame();
  }

  @FXML
  public void handleExit(ActionEvent event) {
    System.exit(0);
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  public void showGameOverPane() {
    gameOverPane.setVisible(true);
  }


}
