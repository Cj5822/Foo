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

/**
 * Controller for handling user input and sending explanations in the game. Manages chat UI
 * interactions, user inputs, and sending data to the game state.
 */
public class ExplanationController {

  private GameStateContext context;
  private StringBuilder accumulatedInput =
      new StringBuilder(); // Holds the accumulated input from the user

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button sendButton;
  @FXML private AnchorPane explanationPane;
  @FXML private ImageView btnSubmitImage;
  @FXML private ImageView btnExitChatImage;

  /**
   * Initializes the controller. Sets up the chat pane and clears accumulated input.
   *
   * @throws ApiProxyException if an error occurs when interacting with the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    hideExplanationPane(); // Hide chat box initially
    accumulatedInput.setLength(0); // Clear accumulated input
  }

  /**
   * Sets the game context for the controller.
   *
   * @param context the game context
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the send button click event to send the user's explanation.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if an error occurs when interacting with the API proxy
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    sendMessage();
  }

  /**
   * Sends the explanation message to the game state and opens the game over screen.
   *
   * @throws ApiProxyException if an error occurs when interacting with the API proxy
   * @throws IOException if an I/O error occurs
   */
  @FXML
  public void sendMessage() throws ApiProxyException, IOException {
    String explanation = accumulatedInput.toString(); // Get the accumulated explanation
    if (context != null) {
      context.setExplanation(explanation); // Set explanation in context
      context.setState(context.getGameOverState()); // Transition to game over state
      App.openGameOver(); // Open the game over screen
    }
  }

  /**
   * Handles key presses in the input field. Sends the message if Enter is pressed or updates
   * accumulated input otherwise.
   *
   * @param event the key event triggered by user input
   * @throws ApiProxyException if an error occurs when interacting with the API proxy
   * @throws IOException if an I/O error occurs
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
        if (length > 0) {
          accumulatedInput.deleteCharAt(length - 1); // Remove the last character
        }
        break;
      default:
        accumulatedInput.append(event.getText()); // Append other key inputs
        break;
    }

    // Update the context with the current accumulated input
    String explanation = accumulatedInput.toString();
    context.setExplanation(explanation);
  }

  /**
   * Handles mouse enter events to show hover effects on buttons.
   *
   * @param event the mouse event triggered by hovering
   */
  @FXML
  public void handleMouseEnter(MouseEvent event) {
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

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

  /**
   * Handles mouse exit events to hide hover effects on buttons.
   *
   * @param event the mouse event triggered by exiting hover
   */
  @FXML
  public void handleMouseExit(MouseEvent event) {
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

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
   * Handles the go back action and hides the explanation pane.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if an error occurs when interacting with the API proxy
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    hideExplanationPane(); // Hide the explanation pane
  }

  /** Hides the explanation pane. */
  public void hideExplanationPane() {
    explanationPane.setVisible(false); // Make the explanation pane invisible
  }

  /** Shows the explanation pane. */
  public void showExplanationPane() {
    explanationPane.setVisible(true); // Make the explanation pane visible
    txtInput.requestFocus(); // Set focus on the input field
  }
}
