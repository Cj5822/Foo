package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SoundManager;

public class HomeController {

  @FXML private Button startGameButton;
  @FXML private Button exitButton;
  @FXML private Label lblTimer;
  @FXML private ImageView btnStartGameImage;
  @FXML private ImageView btnExitGameImage;

  private GameStateContext context;

  private static final String KNOCKING = "src/main/resources/sounds/Knocking.mp3";

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  public void handleStartGame(ActionEvent event) throws IOException {
    Button button = (Button) event.getSource();
    Scene gameScene = button.getScene();
    FXMLLoader backstoryLoader = new FXMLLoader(App.class.getResource("/fxml/backstory.fxml"));
    SoundManager.playSound(KNOCKING, false);
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

  @FXML
  public void handleMouseEnter(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "startGameButton":
          btnStartGameImage.setVisible(true);
          break;
        case "exitButton":
          btnExitGameImage.setVisible(true);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }

  @FXML
  public void handleMouseExit(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "startGameButton":
          btnStartGameImage.setVisible(false);
          break;
        case "exitButton":
          btnExitGameImage.setVisible(false);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }
}
