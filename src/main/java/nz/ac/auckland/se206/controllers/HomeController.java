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

  // Static Fields
  private static final String KNOCKING = "src/main/resources/sounds/Knocking.mp3";

  // Instance Fields
  private GameStateContext context;

  // FXML-Injected Fields
  @FXML private Button startGameButton;
  @FXML private Button exitButton;
  @FXML private Label lblTimer;
  @FXML private ImageView btnStartGameImage;
  @FXML private ImageView btnExitGameImage;

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the action when the "Start Game" button is clicked. Loads the backstory scene and sets
   * up the game context.
   *
   * @param event the action event triggered by the start game button.
   * @throws IOException if an I/O error occurs while loading the FXML.
   */
  @FXML
  private void handleStartGame(ActionEvent event) throws IOException {
    // Get the button that triggered the event
    Button button = (Button) event.getSource();

    // Get the current scene from the button
    Scene gameScene = button.getScene();

    // Load the backstory FXML file to prepare the next scene
    FXMLLoader backstoryLoader = new FXMLLoader(App.class.getResource("/fxml/backstory.fxml"));

    // Play the knocking sound effect to indicate the game is starting
    SoundManager.playSound(KNOCKING, false);

    // Load the backstory root node from the FXML
    Parent root = backstoryLoader.load();

    // Get the controller for the backstory scene to set its context
    BackstoryController backstoryController = backstoryLoader.getController();
    backstoryController.setContext(context);

    // Set the newly loaded backstory as the root of the current scene
    gameScene.setRoot(root);
  }

  /**
   * Handles the action when the "Exit" button is clicked. Closes the game window and exits the
   * application.
   *
   * @param event the action event triggered by the exit button.
   */
  @FXML
  private void handleExit(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
    System.exit(0);
  }

  /**
   * Handles mouse hover events over buttons. Displays corresponding images when hovering over
   * buttons.
   *
   * @param event the mouse event triggered by hovering over a button.
   */
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

  /**
   * Handles mouse exit events over buttons. Hides corresponding images when the mouse exits a
   * button.
   *
   * @param event the mouse event triggered by exiting a button.
   */
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
