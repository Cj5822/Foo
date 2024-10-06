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

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  public void onSend(ActionEvent event) throws IOException {
    
    // Get the explanation from the TextField
    String explanation = txtInput.getText();
    if (context != null) {
      context.setExplanation(explanation);
    }
    App.openGameOver();
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
