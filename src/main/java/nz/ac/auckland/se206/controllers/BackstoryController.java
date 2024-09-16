package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class BackstoryController {

  @FXML private Button continueButton;

  @FXML
  public void handleContinue(ActionEvent event) throws IOException {
    Button button = (Button) event.getSource();
    Scene gameScene = button.getScene();
    gameScene.setRoot(App.loadFxml("homepage"));
  }
}
