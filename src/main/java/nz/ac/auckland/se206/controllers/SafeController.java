package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SoundManager;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller for managing the in-game safe interface, where players must enter a correct code to
 * proceed. It includes functionality for handling button presses, updating the display, and
 * managing the timer.
 */
public class SafeController {
  // Constants (Static Fields)
  private static final String SAFE_SOUND_PATH = "src/main/resources/sounds/Safe.mp3";

  // Instance Fields
  private GameStateContext context;
  private TimerManager timerManager;

  // Other Fields
  private final String correctCode = "2560"; // Correct combination to open the safe
  private StringBuilder enteredCode = new StringBuilder(); // StringBuilder to hold entered digits

  // FXML-Injected Fields
  @FXML private AnchorPane safePane;
  @FXML private Label safeDisplay;
  @FXML private Rectangle rectExitSafe;
  @FXML private Button button1;
  @FXML private Button button2;
  @FXML private Button button3;
  @FXML private Button button4;
  @FXML private Button button5;
  @FXML private Button button6;
  @FXML private Button button7;
  @FXML private Button button8;
  @FXML private Button button9;
  @FXML private Button button0;
  @FXML private Label lblTimer;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;

  /**
   * Initializes the safe interface and hides it initially. Also starts the timer.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    hideSafePane();
    timerManager = TimerManager.getInstance(context); // Initialize TimerManager
    startTimer();
  }

  /**
   * Sets the game state context for the safe controller.
   *
   * @param context the game state context
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /** Starts the timer and continuously updates the timer label. */
  private void startTimer() {
    timerManager.start(); // Start the shared TimerManager

    // AnimationTimer to update the timer label every frame
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
   * Handles digit button presses and updates the entered code accordingly.
   *
   * @param event the action event triggered by pressing a digit button
   */
  @FXML
  private void handleButtonPress(ActionEvent event) {
    // Get the button text (which will be the digit)
    Button pressedButton = (Button) event.getSource();
    String digit = pressedButton.getText();
    enteredCode.append(digit); // Append the digit to the entered code
    safeDisplay.setText(enteredCode.toString()); // Update the display
  }

  /**
   * Handles mouse entering over the exit button area to display a hover effect.
   *
   * @param event the mouse event triggered by hovering over the exit area
   */
  @FXML
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  /**
   * Handles mouse exiting over the exit button area to remove the hover effect.
   *
   * @param event the mouse event triggered by exiting the hover area
   */
  @FXML
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  /**
   * Handles the image button click event for custom interactions.
   *
   * @param event the mouse event triggered by clicking an image button
   */
  @FXML
  private void handleImageClick(MouseEvent event) {
    ImageView clickedImage = (ImageView) event.getSource();
    String imageId = clickedImage.getId();
    enteredCode.append(imageId); // Add the clicked image's ID to the entered code
    safeDisplay.setText(enteredCode.toString()); // Update the display
  }

  /**
   * Handles pressing the button to check the entered code. If correct, opens the safe; otherwise,
   * displays an error.
   *
   * @param event the event triggered by pressing the open button
   * @throws IOException if there is an I/O error while opening the next screen
   */
  @FXML
  private void handleOpenPress(Event event) throws IOException {
    if (enteredCode.toString().equals(correctCode)) {
      safeDisplay.setText("Safe Opened!");
      SoundManager.playSound(SAFE_SOUND_PATH, false);
      App.openOpenedSafe(null);
    } else {
      safeDisplay.setText("Wrong Code!");
    }
    enteredCode.setLength(0); // Clear the entered code
  }

  /**
   * Handles backspace button press to remove the last entered digit from the code.
   *
   * @param event the event triggered by pressing the backspace button
   */
  @FXML
  private void handleBackspacePress(Event event) {
    if (enteredCode.length() > 0) {
      enteredCode.deleteCharAt(enteredCode.length() - 1); // Remove the last digit
      safeDisplay.setText(enteredCode.toString()); // Update the display
    }
  }

  /**
   * Handles clicks on rectangle elements, delegating further action to the context.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /** Hides the safe pane, making it invisible. */
  public void hideSafePane() {
    safePane.setVisible(false);
  }

  /** Shows the safe pane, making it visible. */
  public void showSafePane() {
    safePane.setVisible(true);
  }

  /** Resets the entered code for the safe, clearing the display. */
  public void resetSafeCode() {
    enteredCode.setLength(0);
    safeDisplay.setText("");
  }

  /**
   * Handles mouse hover entering on images to adjust opacity.
   *
   * @param event the mouse event triggered by hovering over an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageEnter(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(1.0); // Set opacity to fully visible
  }

  /**
   * Handles mouse hover exiting on images to adjust opacity.
   *
   * @param event the mouse event triggered by exiting an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageExit(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(0.0); // Set opacity to invisible
  }
}
