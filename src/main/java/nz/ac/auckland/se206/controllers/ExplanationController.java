package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

public class ExplanationController {
  private GameStateContext context;

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private AnchorPane explanationPane;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideExplanationPane(); // Hide chat box initially
  }

  @FXML
  public void onSend(ActionEvent event) throws IOException {
    // Get the explanation from the TextField
    String explanation = txtInput.getText();

    // Get the selected suspect from the context (from Guessing state)
    String selectedSuspect = context.getSelectedSuspect();
    System.out.println("Selected suspect: " + selectedSuspect);

    // Evaluate the guess (pseudo code for GPT integration)
    boolean isCorrectGuess = "rectElectrician".equals(selectedSuspect);
    String feedback =
        evaluateExplanation(isCorrectGuess, explanation); // Evaluate explanation and get feedback

    // Move to GameOver state and display the feedback
    context.setFeedback(feedback); // Store feedback in context for GameOver
    System.out.println("Open game over fxml"); // Open the game over window
    context.setState(context.getGameOverState());
    App.changeRoom(null, "gameover");
  }

  public void setGameStateContext(GameStateContext context) {
    this.context = context;
  }

  private String evaluateExplanation(boolean isCorrectGuess, String explanation) {
    // This would be where GPT feedback integration happens
    if (isCorrectGuess) {
      if (isExplanationCorrect(explanation)) {
        return "You guessed correctly, and your explanation is correct!";
      } else {
        return "You guessed correctly, but your explanation is incorrect.";
      }
    } else {
      return "You guessed incorrectly. The correct thief was the electrician.";
    }
  }

  private boolean isExplanationCorrect(String explanation) {
    // Placeholder for GPT evaluation logic
    return true;
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
  }
}
