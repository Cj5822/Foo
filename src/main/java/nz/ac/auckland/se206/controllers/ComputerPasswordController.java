package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller for the computer password screen in the game. Handles user interaction, password
 * input, visibility toggling, and timer updates.
 */
public class ComputerPasswordController {

  // Constants
  private static final String CORRECT_PASSWORD = "apple"; // Correct password for unlocking

  // Instance fields
  private GameStateContext context; // Game state context to handle game logic
  private TimerManager timerManager; // Timer manager for managing the countdown
  private boolean isPasswordVisible = false; // Flag to track password visibility

  // FXML-injected fields
  @FXML private AnchorPane computerPasswordPane;
  @FXML private Rectangle rectExitOpenSafe;
  @FXML private Label lblTimer;
  @FXML private PasswordField passwordInput;
  @FXML private TextField passwordTextField;
  @FXML private Button showPassword;
  @FXML private ImageView showPasswordEnable;
  @FXML private ImageView showPasswordDisable;
  @FXML private Button btnSend;
  @FXML private Label lblMessage;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;
  @FXML private ImageView loginButtonHover;
  @FXML private ImageView showPasswordEnableHover;
  @FXML private ImageView showPasswordDisableHover;

  /**
   * Initializes the controller. Sets up the initial UI state and starts the timer.
   *
   * @throws ApiProxyException if there is an error initializing the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    hideComputerPasswordPane(); // Hide the password pane initially
    timerManager = TimerManager.getInstance(context); // Get TimerManager instance
    startTimer(); // Start the timer
  }

  /**
   * Sets the game context to allow interaction with the game logic.
   *
   * @param context the GameStateContext for managing game state transitions
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Starts the timer and continuously updates the timer label on the UI. The timer updates every
   * frame using an AnimationTimer.
   */
  private void startTimer() {
    timerManager.start(); // Start the shared TimerManager

    // Update the timer label every frame with the formatted time
    AnimationTimer timerUpdater =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            lblTimer.setText(timerManager.getTimeFormatted()); // Update timer label
          }
        };
    timerUpdater.start(); // Start the timer updater
  }

  /**
   * Handles the event when a key is pressed while focusing on the password field. Submits the
   * password if the Enter key is pressed.
   *
   * @param event the key event that triggered the handler
   * @throws IOException if an I/O error occurs during password submission
   */
  @FXML
  private void handleKeyPressed(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER) {
      handlePasswordSubmit(); // Submit password when Enter is pressed
    }
  }

  /**
   * Handles the event when a key is pressed while focusing on the password text field. Submits the
   * password if the Enter key is pressed.
   *
   * @param event the key event that triggered the handler
   * @throws IOException if an I/O error occurs during password submission
   */
  @FXML
  private void handlePasswordTextFieldKeyPressed(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER) {
      handlePasswordSubmit(); // Submit password when Enter is pressed
    }
  }

  /**
   * Handles the event when the user clicks on a rectangle element on the screen. Interacts with the
   * game context and triggers the appropriate action.
   *
   * @param event the mouse event triggered by the click
   * @throws IOException if an I/O error occurs during the interaction
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId()); // Handle the rectangle click
  }

  /**
   * Handles the event when the send button is pressed. This triggers the same logic as password
   * submission.
   */
  @FXML
  private void onSendButtonClick() throws IOException {
    handlePasswordSubmit(); // Call the same method that handles password submission
  }

  /**
   * Shows the computer password pane and sets focus on the password input field. This method makes
   * the password input pane visible to the player.
   */
  public void showComputerPasswordPane() {
    computerPasswordPane.setVisible(true); // Make the password pane visible
    passwordInput.requestFocus(); // Focus on the password input field
    Platform.runLater(
        () -> passwordInput.positionCaret(passwordInput.getText().length())); // Move caret to end
  }

  /**
   * Hides the computer password pane and clears any messages. This method is used to hide the pane
   * when not needed.
   */
  public void hideComputerPasswordPane() {
    lblMessage.setVisible(false); // Hide the message label
    computerPasswordPane.setVisible(false); // Hide the password pane
  }

  /**
   * Handles the submission of the entered password. Checks if the entered password is correct, and
   * performs an action (e.g., unlocking the safe) if correct.
   *
   * @throws IOException if an I/O error occurs during password submission
   */
  @FXML
  private void handlePasswordSubmit() throws IOException {
    String inputPassword;

    // Check the visible field and get the entered password
    if (passwordInput.isVisible()) {
      inputPassword = passwordInput.getText();
    } else {
      inputPassword = passwordTextField.getText();
    }

    // Check if the password is correct
    if (inputPassword.equals(CORRECT_PASSWORD)) {
      lblMessage.setVisible(false); // Hide any error message
      App.openOpenedComputer(null); // Perform the action (e.g., unlocking the safe)
    } else {
      lblMessage.setVisible(true); // Show error message if the password is incorrect
    }

    passwordInput.clear(); // Clear the password input field
    passwordTextField.clear(); // Clear the text field

    Platform.runLater(
        () -> {
          passwordInput.requestFocus(); // Ensure the focus is on the password input field
          passwordInput.positionCaret(
              passwordInput.getText().length()); // Position caret at the end
        });
  }

  /**
   * Handles the hover effect when the mouse enters the exit button area. Changes the button's
   * appearance to the hovered version.
   *
   * @param event the mouse event triggered by entering the exit button area
   */
  @FXML
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false); // Hide unhovered version
    exitButtonHover.setVisible(true); // Show hovered version
  }

  /**
   * Handles the hover effect when the mouse exits the exit button area. Restores the button's
   * appearance to the unhovered version.
   *
   * @param event the mouse event triggered by exiting the exit button area
   */
  @FXML
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true); // Show unhovered version
    exitButtonHover.setVisible(false); // Hide hovered version
  }

  /**
   * Handles the hover effect when the mouse enters the login button area. Shows the hovered version
   * of the login button.
   *
   * @param event the mouse event triggered by entering the login button area
   */
  @FXML
  private void handleLoginHoverEnter(MouseEvent event) {
    loginButtonHover.setVisible(true); // Show hovered version
  }

  /**
   * Handles the hover effect when the mouse exits the login button area. Hides the hovered version
   * of the login button.
   *
   * @param event the mouse event triggered by exiting the login button area
   */
  @FXML
  private void handleLoginHoverExit(MouseEvent event) {
    loginButtonHover.setVisible(false); // Hide hovered version
  }

  /**
   * Handles the hover effect for the view password button. Shows the appropriate icon depending on
   * whether the password is visible.
   *
   * @param event the mouse event triggered by entering the view password button area
   */
  @FXML
  private void handleviewPasswordHoverEnter(MouseEvent event) {
    if (isPasswordVisible) {
      showPasswordEnableHover.setVisible(true); // Show enabled hover icon
    } else {
      showPasswordDisableHover.setVisible(true); // Show disabled hover icon
    }
  }

  /** Handles the hover effect when exiting the view password button area. */
  @FXML
  private void handleviewPasswordHoverExit(MouseEvent event) {
    if (isPasswordVisible) {
      showPasswordEnableHover.setVisible(false);
    } else {
      showPasswordDisableHover.setVisible(false);
    }
  }

  /** Toggles the visibility of the password input field. */
  @FXML
  private void onShowPasswordClick() {
    if (isPasswordVisible) {
      showPasswordDisable.setVisible(false);
      showPasswordEnableHover.setVisible(false);
      showPasswordDisableHover.setVisible(true);
      passwordTextField.setVisible(false);
      passwordInput.setVisible(true);
      passwordInput.setText(passwordTextField.getText());
      Platform.runLater(() -> passwordInput.requestFocus());
      isPasswordVisible = false;
    } else {
      showPasswordDisable.setVisible(true);
      showPasswordEnableHover.setVisible(true);
      showPasswordDisableHover.setVisible(false);
      passwordInput.setVisible(false);
      passwordTextField.setText(passwordInput.getText());
      passwordTextField.setVisible(true);
      Platform.runLater(() -> passwordTextField.requestFocus());
      isPasswordVisible = true;
    }
  }
}
