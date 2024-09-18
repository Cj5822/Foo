package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

public class SafeController {
  @FXML private AnchorPane safeBox;
  @FXML private TextField safeDisplay;
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
    hideSafeBox(); // Hide chat box initially
  }

  // Handle button presses
  @FXML
  public void handleButtonPress(javafx.event.ActionEvent event) {
    // Get the button text (which will be the digit)
    Button pressedButton = (Button) event.getSource();
    String digit = pressedButton.getText();

    // Append digit to enteredCode and update the display
    enteredCode.append(digit);
    safeDisplay.setText(enteredCode.toString());
  }

  // Handle opening the safe
  @FXML
  public void handleOpenPress() {
    if (enteredCode.toString().equals(correctCode)) {
      safeDisplay.setText("Safe Opened!");
    } else {
      safeDisplay.setText("Wrong Code!");
    }

    // Clear entered code after checking
    enteredCode.setLength(0);
  }

  public void hideSafeBox() {
    safeBox.setVisible(false);
  }

  public void showSafeBox() {
    safeBox.setVisible(true);
  }
}
