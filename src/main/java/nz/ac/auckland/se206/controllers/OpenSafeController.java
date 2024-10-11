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
  private GameStateContext context;

  @FXML private AnchorPane openSafePane;
  @FXML private Rectangle rectExitOpenSafe;
  @FXML private Label lblTimer;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;

  private TimerManager timerManager;

  /**
   * Initializes the controller, hiding the open safe pane initially and setting up the timer. The
   * TimerManager instance is obtained and the timer is started.
   *
   * @throws ApiProxyException if there is an issue initializing the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    hideOpenSafePane(); // Hide wrench pane initially
    // Get the instance of TimerManager
    timerManager = TimerManager.getInstance(context);
    startTimer();
  }

  /**
   * Sets the context for the OpenSafeController.
   *
   * @param context the game state context that holds game-specific information
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Starts the timer and continuously updates the timer label to reflect the current time. It uses
   * the TimerManager to track the time and updates the label every frame.
   */
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

  /**
   * Handles mouse click events on rectangles within the open safe pane. It triggers the appropriate
   * action in the context based on the clicked rectangle's ID.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /** Displays the open safe pane by setting its visibility to true. */
  public void showOpenSafePane() {
    openSafePane.setVisible(true);
  }

  /** Hides the open safe pane by setting its visibility to false. */
  public void hideOpenSafePane() {
    openSafePane.setVisible(false);
  }

  /**
   * Handles mouse hover enter event for the exit button. This method shows the hover version of the
   * exit button when the mouse enters the exit button area.
   *
   * @param event the mouse event triggered by entering the exit button area
   */
  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  /**
   * Handles mouse hover exit event for the exit button. This method restores the original exit
   * button state when the mouse exits the exit button area.
   *
   * @param event the mouse event triggered by exiting the exit button area
   */
  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  /**
   * Handles mouse hover enter event on images representing buttons for map navigation. This method
   * increases the opacity of the hovered image when the mouse enters.
   *
   * @param event the mouse event triggered by hovering over an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageEnter(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(1.0);
  }

  /**
   * Handles mouse hover exit event on images representing buttons for map navigation. This method
   * decreases the opacity of the hovered image when the mouse exits.
   *
   * @param event the mouse event triggered by exiting the image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageExit(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(0.0);
  }

  /**
   * Handles mouse click events on images representing objects in the room. This method triggers the
   * appropriate action in the context based on the clicked image's ID.
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
