package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimerManager;

public class ComputerOpenController {
  private GameStateContext context;

  @FXML private AnchorPane openComputerPane;
  @FXML private Rectangle rectExitOpenComputer;
  @FXML private Label lblTimer;

  private TimerManager timerManager;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideOpenComputerPane(); // Hide wrench pane initially
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
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void showOpenComputerPane() {
    openComputerPane.setVisible(true);
  }

  public void hideOpenComputerPane() {
    openComputerPane.setVisible(false);
  }
}
