package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SoundManager;
import nz.ac.auckland.se206.TimerManager;

/**
 * The BackstoryController class is responsible for managing the backstory scene in the game. It
 * handles the timer, continues the game, and provides mouse interaction effects for buttons.
 */
public class BackstoryController {

  // Constants
  private static final String DOOR = "src/main/resources/sounds/Door.mp3";

  // Instance Fields
  private TimerManager timerManager;
  private GameStateContext context;

  // FXML-Injected Fields
  @FXML private Button continueButton;
  @FXML private Label lblTimer;
  @FXML private ImageView btnContinueImage;

  /** Initializes the BackstoryController, setting up the timer and starting it. */
  public void initialize() {
    // Get the instance of TimerManager for managing the timer state
    timerManager = TimerManager.getInstance(context);
    startTimer(); // Start the timer when the backstory scene is initialized
  }

  /**
   * Sets the game state context for the controller.
   *
   * @param context the GameStateContext to be used in this controller
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /** Starts the countdown timer and continuously updates the timer label every frame. */
  private void startTimer() {
    // Start the shared TimerManager to handle the countdown
    timerManager.start();

    // Use an AnimationTimer to update the timer label every frame
    AnimationTimer timerUpdater =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            // Update the timer label with the formatted time string
            lblTimer.setText(timerManager.getTimeFormatted());
          }
        };
    timerUpdater.start(); // Start the timer updater animation
  }

  /**
   * Handles the action of clicking the continue button. It transitions to the next room and plays a
   * door sound effect.
   *
   * @param event the ActionEvent triggered by clicking the continue button
   * @throws IOException if there is an error loading the next room
   */
  @FXML
  private void handleContinue(ActionEvent event) throws IOException {
    App.changeRoom(null, "room");
    SoundManager.playSound(DOOR, false);
  }

  /**
   * Handles mouse hover event when the mouse enters the continue button. Makes the continue
   * button's image visible.
   */
  @FXML
  public void handleMouseEnter() {
    btnContinueImage.setVisible(true); // Show the continue button image
  }

  /**
   * Handles mouse hover event when the mouse exits the continue button. Hides the continue button's
   * image.
   */
  @FXML
  public void handleMouseExit() {
    btnContinueImage.setVisible(false); // Hide the continue button image
  }
}
