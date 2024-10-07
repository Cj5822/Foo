package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
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

  @SuppressWarnings("unused")
  private GameStateContext context;

  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  public void initialize() throws ApiProxyException {
    gameOverPane.setVisible(false);
  }

  // Set the context and process the explanation with GPT
  public void setContext(GameStateContext context) throws ApiProxyException {
    this.context = context;
    if (context != null) {
      String selectedSuspect = context.getSelectedSuspect();
      String yourExplanation = context.getExplanation();

      boolean isCorrectGuess = "imageElectrician".equals(selectedSuspect);
      runGptCheck(yourExplanation, isCorrectGuess);
    }
  }

  // Async call to GPT to check the explanation
  private void runGptCheck(String explanation, boolean isCorrectGuess) throws ApiProxyException {
    ApiProxyConfig config = ApiProxyConfig.readConfig();
    chatCompletionRequest =
        new ChatCompletionRequest(config).setN(1).setTemperature(0.2).setMaxTokens(400);

    String prompt =
        "This is a game involving player guessing the thief out of 3 suspects. The suspects are an"
            + " electrician called Aiden Carter, a plumber called Brayden Mitchell, and a neighbour"
            + " called Ava Collins. The electrician is the thief because he has used a wrench in"
            + " the crime scene, has a date of birth 1994 which is written in the paper hint and"
            + " the initials is AC which match the initials of the ring in the crime scene. The"
            + " neighbour has not interacted with the wrench but her initials are AC and has a date"
            + " of birth of 1994 so she is the not thief. The plumber has interacted with the"
            + " wrench but is not born in 1994 and his initials is not AC so he is not the thief."
            + " The player explanation was: "
            + explanation
            + "If the explanation contains most of the important details such as the intials, date"
            + " of birth and wrench, it should tell the player that the explanation is correct."
            + " Give a feedback on the explanation regardless of outcome. Keep it not too short and"
            + " not too long";

    ChatMessage message = new ChatMessage("user", prompt);
    chatCompletionRequest.addMessage(message);

    Task<ChatMessage> chatCompletionTask =
        new Task<>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
            Choice result = chatCompletionResult.getChoices().iterator().next();
            chatCompletionRequest.addMessage(result.getChatMessage());
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
      explanationText.setText("You: " + yourExplanation + "\nExplanation: " + gptResponse);
    } else {
      explanationText.setText("You: " + yourExplanation + "\nExplanation: " + gptResponse);
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
