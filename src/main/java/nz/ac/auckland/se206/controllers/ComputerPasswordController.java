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
 * input, and timer updates.
 */
public class ComputerPasswordController {

  // Constants
  private static final String CORRECT_PASSWORD = "apple";

  // Instance fields
  private GameStateContext context;
  private TimerManager timerManager;
  private boolean isPasswordVisible = false;

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
   * Sets the game context.
   *
   * @param context the GameStateContext for the game
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /** Starts the timer and continuously updates the timer label. */
  private void startTimer() {
    timerManager.start(); // Start the shared TimerManager

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
   * Handles the event when a key is pressed while focusing on the password field.
   *
   * @param event the key event
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void handleKeyPressed(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER) {
      handlePasswordSubmit(); // Submit password when Enter is pressed
    }
  }

  /**
   * Handles the event when a key is pressed while focusing on the password text field.
   *
   * @param event the key event
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void handlePasswordTextFieldKeyPressed(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER) {
      handlePasswordSubmit(); // Submit password when Enter is pressed
    }
  }

  /**
   * Handles the event when the user clicks on the rectangle.
   *
   * @param event the mouse event
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /** Handles the event when the send button is pressed. */
  @FXML
  private void handleSendButtonPress() throws IOException {
    handlePasswordSubmit(); // Submit password when send button is pressed
  }

  /** Shows the computer password pane and focuses on the password input field. */
  public void showComputerPasswordPane() {
    computerPasswordPane.setVisible(true);
    passwordInput.requestFocus();
    Platform.runLater(() -> passwordInput.positionCaret(passwordInput.getText().length()));
  }

  /** Hides the computer password pane and clears any messages. */
  public void hideComputerPasswordPane() {
    lblMessage.setVisible(false);
    computerPasswordPane.setVisible(false);
  }

  /**
   * Handles the submission of the password. Checks if the entered password is correct and provides
   * feedback.
   *
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void handlePasswordSubmit() throws IOException {
    String inputPassword;

    // Check the visible field
    if (passwordInput.isVisible()) {
      inputPassword = passwordInput.getText();
    } else {
      inputPassword = passwordTextField.getText();
    }

    if (inputPassword.equals(CORRECT_PASSWORD)) {
      // Password is correct, perform the action (e.g., unlock the safe)
      lblMessage.setVisible(false);
      App.openOpenedComputer(null);
    } else {
      // Password is incorrect, provide feedback
      lblMessage.setVisible(true);
    }

    passwordInput.clear(); // Clear the password field
    passwordTextField.clear();

    Platform.runLater(
        () -> {
          passwordInput.requestFocus(); // Ensure the focus is on the password field
          passwordInput.positionCaret(
              passwordInput.getText().length()); // Position the caret at the end
        });
  }

  /** Handles the hover effect when the mouse enters the exit button area. */
  @FXML
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  /** Handles the hover effect when the mouse exits the exit button area. */
  @FXML
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  /** Handles the hover effect when the mouse enters the login button area. */
  @FXML
  private void handleLoginHoverEnter(MouseEvent event) {
    loginButtonHover.setVisible(true);
  }

  /** Handles the hover effect when the mouse exits the login button area. */
  @FXML
  private void handleLoginHoverExit(MouseEvent event) {
    loginButtonHover.setVisible(false);
  }

  /** Handles the hover effect for the view password button. */
  @FXML
  private void handleviewPasswordHoverEnter(MouseEvent event) {
    if (isPasswordVisible) {
      showPasswordEnableHover.setVisible(true);
    } else {
      showPasswordDisableHover.setVisible(true);
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
  private void handleShowPassword() {
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
