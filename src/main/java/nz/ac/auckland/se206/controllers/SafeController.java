package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
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

public class SafeController {
  private static GameStateContext context = new GameStateContext();

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

  // Correct combination to open the safe
  private final String correctCode = "1234";

  // StringBuilder to hold entered digits
  private StringBuilder enteredCode = new StringBuilder();

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideSafePane(); // Hide chat box initially
  }

  // Handle button presses
  @FXML
  public void handleButtonPress(ActionEvent event) {
    // Get the button text (which will be the digit)
    Button pressedButton = (Button) event.getSource();
    String digit = pressedButton.getText();

    // Append digit to enteredCode and update the display
    enteredCode.append(digit);
    safeDisplay.setText(enteredCode.toString());
  }

  @FXML
  private void handleImageClick(MouseEvent event) {
    ImageView clickedImage = (ImageView) event.getSource();
    String imageId = clickedImage.getId();

    // Append digit to enteredCode and update the display
    enteredCode.append(imageId);
    safeDisplay.setText(enteredCode.toString());
  }

  // Handle opening the safe
  @FXML
  public void handleOpenPress(ActionEvent event) throws IOException {
    if (enteredCode.toString().equals(correctCode)) {
      safeDisplay.setText("Safe Opened!");
      App.openOpenedSafe(event);
      return;
    } else {
      safeDisplay.setText("Wrong Code!");
    }
    // Clear entered code after checking
    enteredCode.setLength(0);
  }

  // Handle backspace button press
  @FXML
  public void handleBackspacePress(ActionEvent event) {
    if (enteredCode.length() > 0) {
      // Remove the last character
      enteredCode.deleteCharAt(enteredCode.length() - 1);
      safeDisplay.setText(enteredCode.toString());
    }
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  public void hideSafePane() {
    safePane.setVisible(false);
  }

  public void showSafePane() {
    safePane.setVisible(true);
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
}
