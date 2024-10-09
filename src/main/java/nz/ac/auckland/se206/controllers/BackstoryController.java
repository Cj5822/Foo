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
import nz.ac.auckland.se206.TimerManager;

public class BackstoryController {

  @FXML private Button continueButton;
  @FXML private Label lblTimer;
  @FXML private ImageView btnContinueImage;

  private TimerManager timerManager;
  private GameStateContext context;

  public void initialize() {
    // Get the instance of TimerManager
    timerManager = TimerManager.getInstance(context);
    startTimer();
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /** Starts the timer and continuously updates the timer label. */
  private void startTimer() {
    // Start the shared TimerManager
    timerManager.start();

    // Update the label every frame with the formatted time
    AnimationTimer timerUpdater =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            lblTimer.setText(timerManager.getTimeFormatted());
          }
        };
    timerUpdater.start();
  }

  @FXML
  public void handleContinue(ActionEvent event) throws IOException {
    App.changeRoom(null, "room");
  }

  @FXML
  public void handleMouseEnter() {
    btnContinueImage.setVisible(true);
  }

  @FXML
  public void handleMouseExit() {
    btnContinueImage.setVisible(false);
  }
}
