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

public class GameOverController {
  @FXML private TextArea explanationText;
  @FXML private Pane gameOverPane;
  @FXML private ProgressIndicator progressIndicator;
  @FXML private ImageView btnReplayGameImage;
  @FXML private ImageView btnExitGameImage;

  @SuppressWarnings("unused")
  private GameStateContext context;

  private ChatCompletionRequest chatCompletionRequest;

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
    ApiProxyConfig config = ApiProxyConfig.readConfig();
    chatCompletionRequest =
        new ChatCompletionRequest(config).setN(1).setTemperature(0.3).setMaxTokens(400);

    System.out.println(selectedSuspect.replace("image", ""));
    String prompt =
        "This is the player explanation: '"
            + explanation
            + "'. This is the suspect the player guessed: '"
            + selectedSuspect.replace("image", "")
            + "' This is the context: 'There are 3 suspects for stealing. The suspects are:"
            + " Electrician called Aiden Carter, Plumber called Brayden Mitchell, and a Neighbour"
            + " called Ava Collins. The Electrician is the thief because he has used a wrench in"
            + " the crime scene, has a date of birth 1994 and his initials is AC. The Neighbour"
            + " initials is AC and has a date of birth of 1994 but did not interact with the wrench"
            + " so she is the not thief. The Plumber has interacted with the wrench but is not born"
            + " in 1994 and his initials is not AC so he is not the thief'. Give a feedback of the"
            + " player explanation. Use the context to check if player explanation is accurate too."
            + " If explanation is empty then give feedback on that.";

    ChatMessage message = new ChatMessage("user", prompt);
    chatCompletionRequest.addMessage(message);

    Task<ChatMessage> chatCompletionTask =
        new Task<>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
            Choice result = chatCompletionResult.getChoices().iterator().next();
            chatCompletionRequest.addMessage(result.getChatMessage());
            progressIndicator.setVisible(false);
            return result.getChatMessage();
          }
        };

    // When GPT completes, update the UI with feedback
    chatCompletionTask.setOnSucceeded(
        event -> {
          String gptResponse = chatCompletionTask.getValue().getContent();
          Platform.runLater(() -> processGptResponse(gptResponse, explanation, isCorrectGuess));
        });

    Thread chatCompletionThread = new Thread(chatCompletionTask);
    chatCompletionThread.setDaemon(true);
    chatCompletionThread.start();
  }

  // Handle GPT response and update the UI accordingly
  private void processGptResponse(
      String gptResponse, String yourExplanation, boolean isCorrectGuess) {
    if (isCorrectGuess) {
      explanationText.setText(
          "Guess: Correct "
              + "\nYour explanation: "
              + yourExplanation
              + "\nFeedback: "
              + gptResponse);
    } else {
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
  }

  @FXML
  public void handleReplay(ActionEvent event) throws IOException {
    App.resetGame();
  }

  @FXML
  public void handleExit(ActionEvent event) {
    System.exit(0);
  }
}
