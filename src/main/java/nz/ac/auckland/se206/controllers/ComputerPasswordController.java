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

public class ComputerPasswordController {
  // Static fields
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
  private void handleKeyPressed(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER) {
      handlePasswordSubmit(); // Call the submit method when Enter is pressed
    }
  }

  @FXML
  private void handlePasswordTextFieldKeyPressed(KeyEvent event) throws IOException {
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

  @FXML
  private void handleSendButtonPress() throws IOException {
    handlePasswordSubmit(); // Call the same method that handles password submission
  }

  public void showComputerPasswordPane() {
    computerPasswordPane.setVisible(true);
    passwordInput.requestFocus();
    // Position the caret at the end of the password input
    Platform.runLater(
        () -> {
          passwordInput.positionCaret(passwordInput.getText().length());
        });
  }

  public void hideComputerPasswordPane() {
    lblMessage.setVisible(false);
    computerPasswordPane.setVisible(false);
  }

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

  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleLoginHoverEnter(MouseEvent event) {
    loginButtonHover.setVisible(true);
  }

  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleLoginHoverExit(MouseEvent event) {
    loginButtonHover.setVisible(false);
  }

  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleviewPasswordHoverEnter(MouseEvent event) {
    if (isPasswordVisible) {
      showPasswordEnableHover.setVisible(true);
    } else {
      showPasswordDisableHover.setVisible(true);
    }
  }

  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleviewPasswordHoverExit(MouseEvent event) {
    if (isPasswordVisible) {
      showPasswordEnableHover.setVisible(false);
    } else {
      showPasswordDisableHover.setVisible(false);
    }
  }

  @FXML
  private void handleShowPassword() {
    if (isPasswordVisible) {
      // make image visible
      showPasswordDisable.setVisible(false);
      showPasswordEnableHover.setVisible(false);
      showPasswordDisableHover.setVisible(true);
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
      showPasswordDisable.setVisible(true);
      showPasswordEnableHover.setVisible(true);
      showPasswordDisableHover.setVisible(false);
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
