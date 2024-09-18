package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

public class SafeController {
  @FXML private AnchorPane safePane;
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

  // Handle opening the safe
  @FXML
  public void handleOpenPress(ActionEvent event) throws IOException {
    if (enteredCode.toString().equals(correctCode)) {
      safeDisplay.setText("Safe Opened!");
      Button button = (Button) event.getSource();
      Scene gameScene = button.getScene();
      gameScene.setRoot(App.loadFxml("safe-opened"));
    } else {
      safeDisplay.setText("Wrong Code!");
    }

    // Clear entered code after checking
    enteredCode.setLength(0);
  }

  public void hideSafePane() {
    safePane.setVisible(false);
  }

  public void showSafePane() {
    safePane.setVisible(true);
  }
}
