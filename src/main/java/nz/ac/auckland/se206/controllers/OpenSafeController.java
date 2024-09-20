package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimerManager;

public class OpenSafeController {
  private static GameStateContext context = new GameStateContext();

  @FXML private AnchorPane openSafePane;
  @FXML private Rectangle rectExitOpenSafe;
  @FXML private Label lblTimer;

  private TimerManager timerManager;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideOpenSafePane(); // Hide wrench pane initially
    // Get the instance of TimerManager
    timerManager = TimerManager.getInstance(context);
    startTimer();
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

  public void showOpenSafePane() {
    openSafePane.setVisible(true);
  }

  public void hideOpenSafePane() {
    openSafePane.setVisible(false);
  }

  /**
   * Handles mouse hover exiting on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageEnter(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(1.0);
  }

  /**
   * Handles mouse hover exiting on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageExit(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(0.0);
  }

  /**
   * Handles mouse clicks on images representing objects in the room.
   *
   * @param event the mouse event triggered by clicking an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageClick(MouseEvent event) throws IOException {
    ImageView clickedImage = (ImageView) event.getSource();
    context.handleImageClick(event, clickedImage.getId());
  }
}
