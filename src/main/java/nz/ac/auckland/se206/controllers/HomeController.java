package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

public class HomeController {

  @FXML private Button startGameButton;
  @FXML private Button exitButton;
  @FXML private Label lblTimer;

  private GameStateContext context;

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  public void handleStartGame(ActionEvent event) throws IOException {
    Button button = (Button) event.getSource();
    Scene gameScene = button.getScene();
    FXMLLoader backstoryLoader = new FXMLLoader(App.class.getResource("/fxml/backstory.fxml"));
    Parent root = backstoryLoader.load();
    BackstoryController backstoryController = backstoryLoader.getController();
    backstoryController.setContext(context);
    gameScene.setRoot(root);
  }

  @FXML
  public void handleExit(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
    System.exit(0);
  }
}
