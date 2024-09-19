package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

  // Controllers
  private ChatController chatController;
  private SafeController safeController;
  private OpenSafeController openSafeController;
  private SafeRingController safeRingController;
  private WrenchController wrenchController;
  private FlippedWrenchController flippedWrenchController;
  private ScrunchedPaperController scrunchedPaperController;
  private PaperController paperController;
  private ExplanationController explanationController;

  // FXML Loaders
  private FXMLLoader chatPaneLoader;
  private FXMLLoader safePaneLoader;
  private FXMLLoader openSafePaneLoader;
  private FXMLLoader safeRingPaneLoader;
  private FXMLLoader wrenchPaneLoader;
  private FXMLLoader flippedWrenchLoader;
  private FXMLLoader scrunchedPaperLoader;
  private FXMLLoader paperLoader;
  private FXMLLoader explanationLoader;

  private static GameStateContext context = new GameStateContext();
  private String originalRoomName = null;

  private TimerManager timerManager;

  /** Initializes the room view. */
  @FXML
  public void initialize() {

    timerManager = TimerManager.getInstance(); // Initialize the TimerManager

    startTimer(); // Start the timer and update the label
    initialiseChatPane(room); // Initialise chat pane
    initialiseSafePane(room); // Initialise safe pane
    initialiseOpenSafePane(room); // Initialise open safe pane
    initialiseSafeRingPane(room); // Initialise safe ring pane
    initialiseWrenchPane(room); // Initialise wrench pane
    initialiseFlippedWrenchPane(room); // Initialise flipped wrench pane
    initialiseScrunchedPaperPane(room); // Initialise scrunched paper pane
    initialisePaperPane(room); // Initialise paper pane
    initialiseExplanationPane(room); // Initialise explanation pane
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
  private void initialiseChatPane(Pane room) {
    try {
      configureChatPane(room); // Load and configure the chat box
    } catch (IOException e) {
      System.err.println("Error loading chat pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the chat box by loading the corresponding FXML and adding it to the room view.
   *
   * @throws IOException if there is an issue loading the FXML file.
   */
  private void configureChatPane(Pane room) throws IOException {
    chatPaneLoader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    AnchorPane node = chatPaneLoader.load();
    chatController = chatPaneLoader.getController();
    room.getChildren().add(node); // Add the chat box to the room view
  }

  private void initialiseSafePane(Pane room) {
    try {
      configureSafePane(room); // Load and configure the safe box
    } catch (IOException e) {
      System.err.println("Error loading safe pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureSafePane(Pane room) throws IOException {
    safePaneLoader = new FXMLLoader(App.class.getResource("/fxml/safe-password.fxml"));
    AnchorPane node = safePaneLoader.load();
    safeController = safePaneLoader.getController();
    room.getChildren().add(node); // Add the safe box to the room view
  }

  private void initialiseOpenSafePane(Pane room) {
    try {
      configureOpenSafePane(room); // Load and configure the safe box
    } catch (IOException e) {
      System.err.println("Error loading opened safe pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureOpenSafePane(Pane room) throws IOException {
    openSafePaneLoader = new FXMLLoader(App.class.getResource("/fxml/safe-opened.fxml"));
    AnchorPane node = openSafePaneLoader.load();
    openSafeController = openSafePaneLoader.getController();
    room.getChildren().add(node); // Add the safe box to the room view
  }

  private void initialiseSafeRingPane(Pane room) {
    try {
      configureSafeRingPane(room); // Load and configure the flipped wrench pane
    } catch (IOException e) {
      System.err.println("Error loading safe ring pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureSafeRingPane(Pane room) throws IOException {
    safeRingPaneLoader = new FXMLLoader(App.class.getResource("/fxml/safe-ring.fxml"));
    AnchorPane node = safeRingPaneLoader.load();
    safeRingController = safeRingPaneLoader.getController();
    room.getChildren().add(node); // Add the wrench pane to the room view
  }

  private void initialiseWrenchPane(Pane room) {
    try {
      configureWrenchPane(room); // Load and configure the wrench pane
    } catch (IOException e) {
      System.err.println("Error loading wrench pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureWrenchPane(Pane room) throws IOException {
    wrenchPaneLoader = new FXMLLoader(App.class.getResource("/fxml/wrench.fxml"));
    AnchorPane node = wrenchPaneLoader.load();
    wrenchController = wrenchPaneLoader.getController();
    room.getChildren().add(node); // Add the wrench pane to the room view
  }

  private void initialiseFlippedWrenchPane(Pane room) {
    try {
      configureFlippedWrenchPane(room); // Load and configure the flipped wrench pane
    } catch (IOException e) {
      System.err.println("Error loading flipped wrench pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureFlippedWrenchPane(Pane room) throws IOException {
    flippedWrenchLoader = new FXMLLoader(App.class.getResource("/fxml/flipped-wrench.fxml"));
    AnchorPane node = flippedWrenchLoader.load();
    flippedWrenchController = flippedWrenchLoader.getController();
    room.getChildren().add(node); // Add the wrench pane to the room view
  }

  private void initialiseScrunchedPaperPane(Pane room) {
    try {
      configureScrunchedPaperPane(room); // Load and configure the flipped wrench pane
    } catch (IOException e) {
      System.err.println("Error loading flipped scrunched paper pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureScrunchedPaperPane(Pane room) throws IOException {
    scrunchedPaperLoader = new FXMLLoader(App.class.getResource("/fxml/scrunched-paper.fxml"));
    AnchorPane node = scrunchedPaperLoader.load();
    scrunchedPaperController = scrunchedPaperLoader.getController();
    room.getChildren().add(node); // Add the wrench pane to the room view
  }

  private void initialisePaperPane(Pane room) {
    try {
      configurePaperPane(room); // Load and configure the flipped wrench pane
    } catch (IOException e) {
      System.err.println("Error loading paper pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configurePaperPane(Pane room) throws IOException {
    paperLoader = new FXMLLoader(App.class.getResource("/fxml/paper.fxml"));
    AnchorPane node = paperLoader.load();
    paperController = paperLoader.getController();
    room.getChildren().add(node); // Add the wrench pane to the room view
  }

  private void initialiseExplanationPane(Pane room) {
    try {
      configureExplanationPane(room); // Load and configure the flipped wrench pane
    } catch (IOException e) {
      System.err.println("Error loading paper pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  private void configureExplanationPane(Pane room) throws IOException {
    explanationLoader = new FXMLLoader(App.class.getResource("/fxml/explanation.fxml"));
    AnchorPane node = explanationLoader.load();
    explanationController = explanationLoader.getController();
    room.getChildren().add(node); // Add the wrench pane to the room view
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
    if (context.getGameState() == context.getGameStartedState()) {
      // Check which rectangle was clicked and set the interaction flags in GameStateContext
      if (clickedRectangle.getId().equals("rectPlumber")) {
        context.setPlumberInteracted(true);
        System.out.println("Plumber interacted");
      } else if (clickedRectangle.getId().equals("rectElectrician")) {
        context.setElectricianInteracted(true);
        System.out.println("Electrician interacted");
      } else if (clickedRectangle.getId().equals("rectNeighbour")) {
        context.setNeighbourInteracted(true);
        System.out.println("Neighbour interacted");
      }
    }
    System.out.println(
        "plumber: "
            + context.isPlumberInteracted()
            + " electrician: "
            + context.isElectricianInteracted()
            + " neighbour: "
            + context.isNeighbourInteracted());
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /**
   * Handles mouse clicks on images representing objects in the room.
   *
   * @param event the mouse event triggered by clicking an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageClick(MouseEvent event) throws IOException {
    ImageView clickedImage = (ImageView) event.getSource();
    context.handleImageClick(event, clickedImage.getId());
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {

    // check if all rectangles have been interacted with
    if (context.isPlumberInteracted()
        && context.isElectricianInteracted()
        && context.isNeighbourInteracted()) {
        timerManager.stop();
        timerManager.resetToOneMinute();
        timerManager.start();
        context.handleGuessClick();
      }
      else {
      System.out.println("Not all rectangles have been interacted with");
    }
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
   * Handles mouse hover exiting on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageEnter(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(1.0);
  }

  /**
   * Handles mouse hover exiting on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageExit(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(0.0);
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
      case "rectLivingroom":
        lblRoomName.setText("Living Room");
        return;
      case "rectGarage":
        lblRoomName.setText("Garage");
        return;
      case "rectBathroom":
        lblRoomName.setText("Bathroom");
        return;
      case "rectBedroom":
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

  public SafeController getSafeController() {
    return safeController;
  }

  public OpenSafeController getOpenSafeController() {
    return openSafeController;
  }

  public SafeRingController getSafeRingController() {
    return safeRingController;
  }

  public WrenchController getWrenchController() {
    return wrenchController;
  }

  public FlippedWrenchController getFlippedWrenchController() {
    return flippedWrenchController;
  }

  public ScrunchedPaperController getScrunchedPaperController() {
    return scrunchedPaperController;
  }

  public PaperController getPaperController() {
    return paperController;
  }

  public ExplanationController getExplanationController() {
    return explanationController;
  }
}
