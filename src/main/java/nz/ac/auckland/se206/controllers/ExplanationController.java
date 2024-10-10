package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

public class ExplanationController {
  private GameStateContext context;

  private StringBuilder accumulatedInput = new StringBuilder();

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button sendButton;
  @FXML private AnchorPane explanationPane;
  @FXML private ImageView btnSubmitImage;
  @FXML private ImageView btnExitChatImage;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideExplanationPane(); // Hide chat box initially
    accumulatedInput.setLength(0); // Clear the current content
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    sendMessage();
  }

  @FXML
  public void sendMessage() throws ApiProxyException, IOException {

    // Get the explanation from the TextField
    String explanation = accumulatedInput.toString();
    if (context != null) {
      context.setExplanation(explanation);
      App.openGameOver();
    }
    App.openGameOver();
  }

  /**
   * Method to handle key presses made by the user.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  public void handleKeyPress(KeyEvent event) throws ApiProxyException, IOException {
    int length = accumulatedInput.length();

    // Handle key events based on the key code
    switch (event.getCode()) {
      case ENTER:
        sendMessage(); // Send the message when the Enter key is pressed
        break;
      case BACK_SPACE:
        // Handle Backspace: Remove the last character from accumulated input if it exists
        if (length > 0) {
          accumulatedInput.deleteCharAt(length - 1); // Remove the last character
        }
        break;
      default:
        // For other keys, add the pressed key to accumulatedInput
        accumulatedInput.append(event.getText());
        break;
    }

    // Update the context with the current accumulated input
    String explanation = accumulatedInput.toString();
    context.setExplanation(explanation);
  }

  @FXML
  public void handleMouseEnter(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "sendButton":
          btnSubmitImage.setVisible(true);
          break;
        case "exitChatButton":
          btnExitChatImage.setVisible(true);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }

  @FXML
  public void handleMouseExit(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "sendButton":
          btnSubmitImage.setVisible(false);
          break;
        case "exitChatButton":
          btnExitChatImage.setVisible(false);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    hideExplanationPane(); // Hide the chat box
  }

  /** Hides the chat box and progress bar. */
  public void hideExplanationPane() {
    explanationPane.setVisible(false); // Make chat box invisible
  }

  /** Shows the chat box and progress bar. */
  public void showExplanationPane() {
    explanationPane.setVisible(true); // Make chat box visible
    txtInput.requestFocus();
  }
}
