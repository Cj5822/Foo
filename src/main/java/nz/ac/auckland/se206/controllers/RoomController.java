package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController {

  @FXML private Rectangle rectPlumber;
  @FXML private Rectangle rectElectrician;
  @FXML private Rectangle rectNeighbour;
  @FXML private Rectangle rectPerson3;
  @FXML private Rectangle rectWaitress;
  @FXML private Label lblProfession;
  @FXML private Button btnGuess;
  @FXML private Pane room;
  @FXML private Label lblRoomName;
  @FXML private Label lblTimer;

  private ChatController chatController;
  private FXMLLoader chatBoxLoader;

  private static GameStateContext context = new GameStateContext();
  private String originalRoomName = null;

  private TimerManager timerManager;

  /** Initializes the room view. */
  @FXML
  public void initialize() {

    timerManager = TimerManager.getInstance(); // Initialize the TimerManager

    startTimer(); // Start the timer and update the label

    initialiseChatBox(
        room); // Call the method that handles chat box initialization with error handling
  }

  /** Starts the timer and continuously updates the timer label. */
  private void startTimer() {
    // Start the shared TimerManager
    timerManager.start();

    // Update the label every frame with the formatted time
    AnimationTimer timerUpdater =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            lblTimer.setText(timerManager.getTimeFormatted());
          }
        };
    timerUpdater.start();
  }

  /**
   * Tries to load and configure the chat box. If an exception occurs, it will be handled by the
   * handleError method.
   */
  private void initialiseChatBox(Pane room) {
    try {
      configureChatBox(room); // Load and configure the chat box
    } catch (IOException e) {
      System.err.println("Error loading chat box: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the chat box by loading the corresponding FXML and adding it to the room view.
   *
   * @throws IOException if there is an issue loading the FXML file.
   */
  private void configureChatBox(Pane room) throws IOException {
    chatBoxLoader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    AnchorPane node = chatBoxLoader.load();
    chatController = chatBoxLoader.getController();
    room.getChildren().add(node); // Add the chat box to the room view
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    context.handleGuessClick();
  }

  /**
   * Handles mouse hover exiting on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleExit(MouseEvent event) throws IOException {
    Rectangle hoveredRectangle = (Rectangle) event.getSource();
    hoveredRectangle.setOpacity(0.0);

    // Reset room label to the original room name
    lblRoomName.setText(originalRoomName);
  }

  /**
   * Handles mouse hovers on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleEnter(MouseEvent event) throws IOException {
    Rectangle hoveredRectangle = (Rectangle) event.getSource();
    hoveredRectangle.setOpacity(1.0);

    // Store the original room name if not already stored
    if (originalRoomName == null) {
      originalRoomName = lblRoomName.getText();
    }

    // Update room label depending on the hovered rectangle
    switch (hoveredRectangle.getId()) {
      case "rectPerson1":
        lblRoomName.setText("Living Room");
        return;
      case "rectPerson2":
        lblRoomName.setText("Garage");
        return;
      case "rectPerson3":
        lblRoomName.setText("Bathroom");
        return;
      case "rectPerson4":
        lblRoomName.setText("Bedroom");
        return;
    }
  }

  /**
   * Gets the chat controller.
   *
   * @return the chat controller associated with this view.
   */
  public ChatController getChatController() {
    return chatController;
  }
}
