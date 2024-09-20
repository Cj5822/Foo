package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;

public class homepageController {

  @FXML private Button startGameButton;
  @FXML private Button exitButton;
  @FXML private Label lblTimer;

  @FXML
  public void handleStartGame(ActionEvent event) throws IOException {
    Button button = (Button) event.getSource();
    Scene gameScene = button.getScene();
    gameScene.setRoot(App.loadFxml("backstory"));
  }

  @FXML
  public void handleExit(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
    System.exit(0);
  }
}
