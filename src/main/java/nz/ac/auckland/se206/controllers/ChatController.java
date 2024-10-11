package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the chat view. Handles user interactions and communication with the GPT
 * model via the API proxy.
 */
public class ChatController {

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private AnchorPane chatPane;
  @FXML private ProgressIndicator progressIndicator;
  @FXML private ImageView btnSendImage;
  @FXML private ImageView chatLogImage;
  @FXML private ImageView sendButtonImage;
  @FXML private TextArea currentChat;
  @FXML private Label roleLabel;

  private ChatCompletionRequest chatCompletionRequest;
  private String profession;
  private GameStateContext context;

  /**
   * Initializes the chat view by hiding the chat pane, chat log, and progress indicator.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    hideChatPane(); // Hide chat box initially
    hideChatLog();
    progressIndicator.setVisible(false); // Hide progress indicator initially
  }

  /**
   * Sets the game context.
   *
   * @param context the game context
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Generates the system prompt based on the current profession.
   *
   * @return the system prompt string, or null if the profession is not recognized
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", profession);
    if (profession.equals("Electrician")) {
      return PromptEngineering.getPrompt("electricianPrompt.txt", map);
    }
    if (profession.equals("Plumber")) {
      return PromptEngineering.getPrompt("plumberPrompt.txt", map);
    }
    if (profession.equals("Neighbour")) {
      return PromptEngineering.getPrompt("neighbourPrompt.txt", map);
    } else {
      return null;
    }
  }

  /**
   * Sets the profession for the chat context and initializes the ChatCompletionRequest object. Also
   * retrieves the latest message if available, otherwise generates a new system message.
   *
   * @param profession the profession to set (e.g., Electrician, Plumber, or Neighbour)
   */
  public void setProfession(String profession) {
    this.profession = profession;
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.5)
              .setMaxTokens(100);

      ChatMessage startMessage = new ChatMessage("system", getSystemPrompt());
      String latestMessage = context.getLatestChatMessage(profession);
      if (latestMessage == null) {
        runGpt(startMessage);
      } else {
        chatCompletionRequest.addMessage(startMessage);
        currentChat.setText(latestMessage);
      }
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat display area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    String formattedMessage = formatMessage(msg); // Format the message

    // Ensure the message is appended on the JavaFX Application Thread
    if (Platform.isFxApplicationThread()) {
      currentChat.clear();
      appendToChat(formattedMessage);
    } else {
      Platform.runLater(() -> appendToChat(formattedMessage));
    }
  }

  /**
   * Formats the chat message for display, prefixing it with the sender's role.
   *
   * @param msg the chat message to format
   * @return the formatted message string
   */
  private String formatMessage(ChatMessage msg) {
    String role = msg.getRole();
    String content = msg.getContent();
    if (role.equals("user")) {
      return "Me:\n" + content + "\n\n"; // User's message
    }
    return profession + ":\n" + msg.getContent() + "\n\n"; // Format: "role: content"
  }

  /**
   * Appends formatted text to the chat display and saves it to the chat history.
   *
   * @param formattedMessage the message to append
   */
  private void appendToChat(String formattedMessage) {
    context.appendToChatHistory(profession, formattedMessage); // Save to suspect's chat log
    currentChat.setText(formattedMessage);
  }

  /**
   * Runs the GPT model with a given chat message and handles the chat completion task.
   *
   * @param msg the chat message to process
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg) throws ApiProxyException {
    // Add the message to the chat completion request
    chatCompletionRequest.addMessage(msg);

    // Disable input and send button when starting the task
    txtInput.setDisable(true); // Disable the input field
    btnSend.setDisable(true); // Disable the send button

    // Show the progress indicator when starting task
    progressIndicator.setVisible(true);
    roleLabel.setText(profession + " is thinking...");

    // Create a task to handle the chat completion asynchronously
    Task<ChatMessage> chatCompletionTask =
        new Task<>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            try {
              // Execute the chat completion request and get the result
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              // Add the result message to the chat request history
              chatCompletionRequest.addMessage(result.getChatMessage());
              // Append the result message to the chat area
              appendChatMessage(result.getChatMessage());
              return result.getChatMessage();
            } catch (ApiProxyException e) {
              e.printStackTrace();
              return null;
            } finally {
              // Ensure this runs in all cases to hide the progress indicator
              Platform.runLater(
                  () -> {
                    progressIndicator.setVisible(false);
                    roleLabel.setText("");
                    txtInput.setDisable(false); // Re-enable the input field
                    btnSend.setDisable(false); // Re-enable the send button
                    txtInput.requestFocus(); // Set focus back to the input field
                  });
            }
          }
        };

    // Run the chat completion task in a background thread
    Thread chatCompletionThread = new Thread(chatCompletionTask);
    chatCompletionThread.setDaemon(true);
    chatCompletionThread.start();
  }

  /**
   * Sends a message to the GPT model and processes the response.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(Event event) throws ApiProxyException, IOException {
    sendMessage();
  }

  /**
   * Sends the user's message and runs the GPT model.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  private void sendMessage() throws ApiProxyException, IOException {
    // Retrieve the message input from the text field and trim any whitespace
    String message = txtInput.getText().trim();

    // If the message is empty, do not proceed
    if (message.isEmpty()) {
      return; // Exit the method early if there is no message to send
    }

    // Clear the input text field after retrieving the message
    txtInput.clear();

    // Create a new ChatMessage object with the user's message
    ChatMessage msg = new ChatMessage("user", message);

    // Append the user's message to the chat display
    appendChatMessage(msg);

    // Call the method to process the message with GPT
    runGpt(msg);
  }

  /**
   * Handles the key press event for sending a message when the Enter key is pressed.
   *
   * @param event the key event triggered by pressing a key
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  public void handleKeyPress(KeyEvent event) throws ApiProxyException, IOException {
    switch (event.getCode()) {
      case ENTER:
        sendMessage(); // Call the helper method to send the message on Enter key press
        break;
      default:
        break;
    }
  }

  /**
   * Handles the click event on the chat log toggle button. Toggles between showing the current chat
   * and the chat log.
   *
   * @param event the event triggered when the chat log is clicked
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onChatLogClick(Event event) throws ApiProxyException, IOException {
    if (txtaChat.isVisible()) {
      hideChatLog();
      showCurrentChat();
      txtInput.setVisible(true); // Enables the input field
      btnSend.setVisible(true); // Enables the send button
      btnSendImage.setVisible(true); // Enables the send button image
      sendButtonImage.setVisible(true); // Enables the send button image
    } else {
      showChatLog();
      hideCurrentChat();
      txtInput.setVisible(false); // Disable the input field
      btnSend.setVisible(false); // Disable the send button
      btnSendImage.setVisible(false); // Disable the send button image
      sendButtonImage.setVisible(false); // Disable the send button image
    }
  }

  /**
   * Handles the mouse enter event for buttons. Makes certain buttons visible or adjusts the opacity
   * when the mouse enters the button area.
   *
   * @param event the mouse event triggered when the mouse enters a button
   */
  @FXML
  public void handleMouseEnter(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof ImageView) {
      ImageView hoveredButton = (ImageView) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "sendButtonImage":
          btnSendImage.setVisible(true);
          break;
        case "btnSendImage":
          btnSendImage.setVisible(true);
          break;
        case "chatLogImage":
          chatLogImage.setOpacity(1.0);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }

  /**
   * Handles the mouse exit event for buttons. Hides or adjusts the opacity of buttons when the
   * mouse exits the button area.
   *
   * @param event the mouse event triggered when the mouse exits a button
   */
  @FXML
  public void handleMouseExit(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof ImageView) {
      ImageView hoveredButton = (ImageView) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "sendButtonImage":
          btnSendImage.setVisible(false);
          break;
        case "btnSendImage":
          btnSendImage.setVisible(false);
          break;
        case "chatLogImage":
          chatLogImage.setOpacity(0);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }

  /** Hides the chat log by making it invisible. */
  public void hideChatLog() {
    txtaChat.setVisible(false); // Make chat log invisible
  }

  /** Displays the chat log and populates it with the chat history of the current profession. */
  public void showChatLog() {
    txtaChat.setText(context.getChatHistory(profession));
    txtaChat.setVisible(true); // Make chat log visible
  }

  /** Displays the chat pane, making it visible. */
  public void showChatPane() {
    chatPane.setVisible(true); // Make chat box visible
  }

  /** Hides the chat pane, making it invisible. */
  public void hideChatPane() {
    chatPane.setVisible(false); // Make chat box invisible
  }

  /** Hides the current chat display by making it invisible. */
  public void hideCurrentChat() {
    currentChat.setVisible(false);
  }

  /** Displays the current chat by making it visible. */
  public void showCurrentChat() {
    currentChat.setVisible(true);
  }
}
