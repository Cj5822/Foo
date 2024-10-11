package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SoundManager;

public class GameOverController {
  @FXML private TextArea explanationText;
  @FXML private Pane gameOverPane;
  @FXML private ProgressIndicator progressIndicator;
  @FXML private ImageView btnReplayGameImage;
  @FXML private ImageView btnExitGameImage;

  @SuppressWarnings("unused")
  private GameStateContext context;

  private ChatCompletionRequest chatCompletionRequest;
  private static final String OUTRO = "src/main/resources/sounds/Outro.mp3";

  @FXML
  public void initialize() throws ApiProxyException {
    gameOverPane.setVisible(false);
    progressIndicator.setVisible(true);
  }

  @FXML
  public void handleMouseEnter(MouseEvent event) {
    // Check the source of the event to determine which button triggered it
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Check the ID or other property of the button to perform specific actions
      switch (hoveredButton.getId()) {
        case "replayGameButton":
          btnReplayGameImage.setVisible(true);
          break;
        case "exitGameButton":
          btnExitGameImage.setVisible(true);
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
        case "replayGameButton":
          btnReplayGameImage.setVisible(false);
          break;
        case "exitGameButton":
          btnExitGameImage.setVisible(false);
          break;
        default:
          System.out.println("Unknown button hovered");
          break;
      }
    }
  }

  // Set the context and process the explanation with GPT
  public void setContext(GameStateContext context) throws ApiProxyException {
    this.context = context;
    if (context != null) {
      String selectedSuspect = context.getSelectedSuspect();
      String yourExplanation = context.getExplanation();

      boolean isCorrectGuess = "imageElectrician".equals(selectedSuspect);
      runGptCheck(yourExplanation, isCorrectGuess, selectedSuspect);
    }
  }

  // Async call to GPT to check the explanation
  private void runGptCheck(String explanation, boolean isCorrectGuess, String selectedSuspect)
      throws ApiProxyException {
    // Read API configuration from the config file
    ApiProxyConfig config = ApiProxyConfig.readConfig();

    // Initialize the chat completion request with parameters
    chatCompletionRequest =
        new ChatCompletionRequest(config)
            .setN(1) // Set the number of responses to generate
            .setTemperature(0.1) // Control randomness in responses (lower is less random)
            .setMaxTokens(400); // Set the maximum number of tokens (words) for the response

    // Log the selected suspect for debugging purposes
    System.out.println(selectedSuspect.replace("image", ""));

    // Create the prompt to send to the GPT model
    String prompt =
        "Player explanation: '"
            + explanation
            + "'. Player guessed the suspect: '"
            + selectedSuspect.replace("image", "")
            + "'. Context: 'There are 3 suspects involved in a theft. The suspects are: 1)"
            + " Electrician: Aiden Carter, 2) Plumber: Brayden Mitchell, 3) Neighbor: Ava Collins."
            + " Aiden Carter is the thief because he used a wrench which was in the crime scene,"
            + " his favorite fruit is an apple, and his initials are A.C which match the fake"
            + " wedding ring initials. Ava Collins shares the initials A.C. and has the same"
            + " favorite fruit, but did not interact with the wrench, so she is not the thief."
            + " Brayden Mitchell also used a wrench, shares the favorite fruit, but his initials"
            + " are not A.C., so he is not the thief either'. Evaluate the player's explanation"
            + " based on this context. If the player explanation matches give feedback on how to"
            + " improve it. If player explanation contradicts these facts, provide detailed"
            + " feedback. If the player explanation is empty, respond with feedback on that."
            + " Explanation is only correct if it mentioned all 3 of the hints, otherwise, give"
            + " feedback.";

    // Create a new chat message containing the prompt
    ChatMessage message = new ChatMessage("user", prompt);
    chatCompletionRequest.addMessage(message); // Add the message to the request

    // Create a task for executing the chat completion request asynchronously
    Task<ChatMessage> chatCompletionTask =
        new Task<>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            // Execute the chat completion request and retrieve the result
            ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
            Choice result =
                chatCompletionResult.getChoices().iterator().next(); // Get the first choice
            chatCompletionRequest.addMessage(
                result.getChatMessage()); // Add the response to the request
            progressIndicator.setVisible(false); // Hide the progress indicator when done
            return result.getChatMessage(); // Return the chat message content
          }
        };

    // When the GPT task completes successfully, update the UI with the feedback
    chatCompletionTask.setOnSucceeded(
        event -> {
          String gptResponse = chatCompletionTask.getValue().getContent(); // Get the GPT response
          Platform.runLater(
              () ->
                  processGptResponse(
                      gptResponse,
                      explanation,
                      isCorrectGuess)); // Process the response on the JavaFX Application Thread
        });

    // Create a new thread for executing the chat completion task
    Thread chatCompletionThread = new Thread(chatCompletionTask);
    chatCompletionThread.setDaemon(true); // Set the thread as a daemon thread
    chatCompletionThread.start(); // Start the thread
  }

  // Handle GPT response and update the UI accordingly
  private void processGptResponse(
      String gptResponse, String yourExplanation, boolean isCorrectGuess) {
    // Check if the guess was correct
    if (isCorrectGuess) {
      // Update the explanation text with the correct guess message
      // Include the user's explanation and feedback from GPT
      explanationText.setText(
          "Guess: Correct "
              + "\nYour explanation: "
              + yourExplanation
              + "\nFeedback: "
              + gptResponse);
    } else {
      // Update the explanation text with the incorrect guess message
      // Include the user's explanation and feedback from GPT
      explanationText.setText(
          "Guess: Incorrect "
              + "\nYour explanation: "
              + yourExplanation
              + "\nFeedback: "
              + gptResponse);
    }
  }

  public void showGameOverPane() {
    gameOverPane.setVisible(true);
    SoundManager.playSound(OUTRO, true);
  }

  @FXML
  public void handleReplay(ActionEvent event) throws IOException {
    SoundManager.stopSound();
    App.resetGame();
  }

  @FXML
  public void handleExit(ActionEvent event) {
    SoundManager.stopSound();
    System.exit(0);
  }
}
