package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

/**
 * Controller class for the chat view. Handles user interactions and communication with the GPT
 * model via the API proxy.
 */
public class ChatController {

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private AnchorPane chatPane;

  private ChatCompletionRequest chatCompletionRequest;
  private String profession;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideChatPane(); // Hide chat box initially
  }

  /**
   * Generates the system prompt based on the profession.
   *
   * @return the system prompt string
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
   * Sets the profession for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
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
      runGpt(new ChatMessage("system", getSystemPrompt()));
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    String formattedMessage = formatMessage(msg); // Format the message

    // Ensure the message is appended on the JavaFX Application Thread
    if (Platform.isFxApplicationThread()) {
      appendToChat(formattedMessage);
    } else {
      Platform.runLater(() -> appendToChat(formattedMessage));
    }

    System.out.println(txtaChat.getText()); // Print the current chat text to console
  }

  /**
   * Formats the chat message for display.
   *
   * @param msg the chat message to format
   * @return the formatted message string
   */
  private String formatMessage(ChatMessage msg) {
    return profession + ": " + msg.getContent() + "\n\n"; // Format: "role: content"
  }

  /**
   * Appends formatted text to the chat text area.
   *
   * @param formattedMessage the message to append
   */
  private void appendToChat(String formattedMessage) {
    txtaChat.appendText(formattedMessage); // Append text to chat area
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage());
      FreeTextToSpeech.speak(result.getChatMessage().getContent());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
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
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
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
    hideChatPane(); // Hide the chat box
    txtaChat.clear(); // Clear the chat area
  }

  /** Hides the chat box and progress bar. */
  public void hideChatPane() {
    chatPane.setVisible(false); // Make chat box invisible
  }

  /** Shows the chat box and progress bar. */
  public void showChatPane() {
    chatPane.setVisible(true); // Make chat box visible
  }
}
