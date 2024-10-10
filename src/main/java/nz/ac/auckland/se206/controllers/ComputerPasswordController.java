package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimerManager;

public class ComputerPasswordController {
  private GameStateContext context;

  @FXML private AnchorPane computerPasswordPane;
  @FXML private Rectangle rectExitOpenSafe;
  @FXML private Label lblTimer;
  @FXML private PasswordField passwordInput;
  @FXML private TextField passwordTextField;
  @FXML private Button showPassword;

  private TimerManager timerManager;

  private static final String CORRECT_PASSWORD = "yourPassword";
  private boolean isPasswordVisible = false;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideComputerPasswordPane(); // Hide wrench pane initially
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
  private void handleKeyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      handlePasswordSubmit(); // Call the submit method when Enter is pressed
    }
  }

  @FXML
  private void handlePasswordTextFieldKeyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      // Call the submit method when Enter is pressed
      handlePasswordSubmit();
    }
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void showComputerPasswordPane() {
    computerPasswordPane.setVisible(true);
    passwordInput.requestFocus();
  }

  public void hideComputerPasswordPane() {
    computerPasswordPane.setVisible(false);
  }

  @FXML
  private void handlePasswordSubmit() {
    String inputPassword = passwordInput.getText(); // Get the input password
    String inputPassword2 = passwordTextField.getText();

    if (inputPassword.equals(CORRECT_PASSWORD) || inputPassword2.equals(CORRECT_PASSWORD)) {
      // Password is correct, perform the action (e.g., unlock the safe)
      System.out.println("Correct password. The safe is now unlocked.");
    } else {
      // Password is incorrect, provide feedback
      System.out.println("Incorrect password. Please try again.");
    }

    // Clear the password field for the next attempt
    passwordInput.clear();
    passwordTextField.clear();

    // Use Platform.runLater to ensure focus and caret positioning after UI updates
    Platform.runLater(
        () -> {
          passwordInput.requestFocus(); // Ensure the focus is on the password field
          passwordInput.positionCaret(
              passwordInput.getText().length()); // Position the caret at the end
        });
  }

  @FXML
  private void handleShowPassword() {
    if (isPasswordVisible) {
      // When showing the password field
      passwordTextField.setVisible(false); // Hide the text field
      passwordInput.setVisible(true); // Show the password field
      // Transfer text from the text field to password field
      passwordInput.setText(passwordTextField.getText());
      // Request focus and position caret at the end of the text
      Platform.runLater(
          () -> {
            passwordInput.requestFocus(); // Focus on password input
            passwordInput.positionCaret(passwordInput.getText().length()); // Move caret to end
          });
      isPasswordVisible = false; // Update visibility flag
    } else {
      // When showing the text field
      passwordInput.setVisible(false); // Hide the password field
      // Set the text field with the current password and show it
      passwordTextField.setText(passwordInput.getText()); // Copy current password to text field
      passwordTextField.setVisible(true); // Show the text field
      // Request focus and position caret at the end of the text
      Platform.runLater(
          () -> {
            passwordTextField.requestFocus(); // Focus on text field
            passwordTextField.positionCaret(
                passwordTextField.getText().length()); // Move caret to end
          });
      isPasswordVisible = true; // Update visibility flag
    }
  }
}
