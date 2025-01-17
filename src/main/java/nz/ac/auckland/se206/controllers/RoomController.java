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
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController {

  // FXML-Injected Fields
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
  @FXML private ImageView plumberNametag;
  @FXML private ImageView electricianNametag;
  @FXML private ImageView neighbourNametag;
  @FXML private ImageView btnGuessImage;
  @FXML private ImageView btnMakeAGuess;
  @FXML private ImageView btnReplayGameImage;
  @FXML private ImageView btnExitGameImage;
  @FXML private ImageView glowComputer;

  // Controllers
  private ChatController chatController;
  private SafeController safeController;
  private OpenSafeController openSafeController;
  private SafeRingController safeRingController;
  private ScrunchedPaperController scrunchedPaperController;
  private PaperController paperController;
  private ExplanationController explanationController;
  private GameOverController gameOverController;
  private ComputerPasswordController computerPasswordController;
  private ComputerOpenController openComputerController;

  // FXML Loaders
  private FXMLLoader chatPaneLoader;
  private FXMLLoader safePaneLoader;
  private FXMLLoader openSafePaneLoader;
  private FXMLLoader safeRingPaneLoader;
  private FXMLLoader scrunchedPaperLoader;
  private FXMLLoader paperLoader;
  private FXMLLoader explanationLoader;
  private FXMLLoader gameOverLoader;
  private FXMLLoader computerPasswordLoader;
  private FXMLLoader openComputerLoader;

  // Other Fields
  private GameStateContext context;
  private String originalRoomName = null;
  private TimerManager timerManager;

  /**
   * Initializes the room view by setting up various components like chat box, safe, and other UI
   * elements.
   */
  @FXML
  public void initialize() {
    initialiseChatPane(room); // Initialise chat pane
    initialiseSafePane(room); // Initialise safe pane
    initialiseOpenSafePane(room); // Initialise open safe pane
    initialiseSafeRingPane(room); // Initialise safe ring pane
    initialiseScrunchedPaperPane(room); // Initialise scrunched paper pane
    initialisePaperPane(room); // Initialise paper pane
    initialiseExplanationPane(room); // Initialise explanation pane
    initialiseGameOverPane(room); // Initialise game over pane
    initialiseComputerPasswordPane(room); // Initialise computer password pane
    initialiseOpenComputerPane(room); // Initialise open computer pane
  }

  /**
   * Sets the context for the controller and initializes the TimerManager.
   *
   * @param context the game state context to be set for the controller
   */
  public void setContext(GameStateContext context) {
    this.context = context;
    timerManager = TimerManager.getInstance(context); // Initialize the TimerManager
    startTimer(); // Start the timer and update the label
  }

  /**
   * Starts the timer and continuously updates the timer label. This method uses an AnimationTimer
   * to update the timer label every frame.
   */
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
   * @param room the pane in which the chat box should be added
   * @throws IOException if there is an issue loading the FXML file.
   */
  private void configureChatPane(Pane room) throws IOException {
    chatPaneLoader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    AnchorPane node = chatPaneLoader.load();
    chatController = chatPaneLoader.getController();
    room.getChildren().add(node); // Add the chat box to the room view
  }

  /**
   * Initializes the safe pane and handles any IO exceptions that may occur.
   *
   * @param room the room pane where the safe should be added
   */
  private void initialiseSafePane(Pane room) {
    try {
      configureSafePane(room); // Load and configure the safe box
    } catch (IOException e) {
      System.err.println("Error loading safe pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the safe pane by loading the FXML file and adding it to the room.
   *
   * @param room the room pane where the safe should be added
   * @throws IOException if there is an issue loading the safe pane
   */
  private void configureSafePane(Pane room) throws IOException {
    safePaneLoader = new FXMLLoader(App.class.getResource("/fxml/safe-password.fxml"));
    AnchorPane node = safePaneLoader.load();
    safeController = safePaneLoader.getController();
    safeController.setContext(context);
    room.getChildren().add(node); // Add the safe box to the room view
  }

  /**
   * Initializes the open safe pane and handles any IO exceptions that may occur.
   *
   * @param room the room pane where the open safe should be added
   */
  private void initialiseOpenSafePane(Pane room) {
    try {
      configureOpenSafePane(room); // Load and configure the open safe box
    } catch (IOException e) {
      System.err.println("Error loading opened safe pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the open safe pane by loading the corresponding FXML and adding it to the room.
   *
   * @param room the room pane where the open safe should be added
   * @throws IOException if there is an issue loading the open safe pane
   */
  private void configureOpenSafePane(Pane room) throws IOException {
    openSafePaneLoader = new FXMLLoader(App.class.getResource("/fxml/safe-opened.fxml"));
    AnchorPane node = openSafePaneLoader.load();
    openSafeController = openSafePaneLoader.getController();
    openSafeController.setContext(context);
    room.getChildren().add(node); // Add the open safe box to the room view
  }

  /**
   * Initializes the safe ring pane and handles any IO exceptions that may occur.
   *
   * @param room the room pane where the safe ring should be added
   */
  private void initialiseSafeRingPane(Pane room) {
    try {
      configureSafeRingPane(room); // Load and configure the safe ring
    } catch (IOException e) {
      System.err.println("Error loading safe ring pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the safe ring pane by loading the corresponding FXML and adding it to the room.
   *
   * @param room the room pane where the safe ring should be added
   * @throws IOException if there is an issue loading the safe ring pane
   */
  private void configureSafeRingPane(Pane room) throws IOException {
    safeRingPaneLoader = new FXMLLoader(App.class.getResource("/fxml/safe-ring.fxml"));
    AnchorPane node = safeRingPaneLoader.load();
    safeRingController = safeRingPaneLoader.getController();
    safeRingController.setContext(context);
    room.getChildren().add(node); // Add the safe ring pane to the room view
  }

  /**
   * Initializes the scrunched paper pane and handles any IO exceptions that may occur.
   *
   * @param room the room pane where the scrunched paper should be added
   */
  private void initialiseScrunchedPaperPane(Pane room) {
    try {
      configureScrunchedPaperPane(room); // Load and configure the scrunched paper pane
    } catch (IOException e) {
      System.err.println("Error loading scrunched paper pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the scrunched paper pane in the given room.
   *
   * @param room the room pane to which the scrunched paper pane will be added
   * @throws IOException if there is an error loading the scrunched paper pane
   */
  private void configureScrunchedPaperPane(Pane room) throws IOException {
    scrunchedPaperLoader = new FXMLLoader(App.class.getResource("/fxml/scrunched-paper.fxml"));
    AnchorPane node = scrunchedPaperLoader.load();
    scrunchedPaperController = scrunchedPaperLoader.getController();
    scrunchedPaperController.setContext(context);
    room.getChildren().add(node); // Add the scrunched paper pane to the room view
  }

  /**
   * Initializes the scrunched paper pane in the given room.
   *
   * @param room the room pane to which the scrunched paper pane will be added
   */
  private void initialisePaperPane(Pane room) {
    try {
      configurePaperPane(room); // Load and configure the flipped paper pane
    } catch (IOException e) {
      System.err.println("Error loading paper pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the paper pane in the given room.
   *
   * @param room the room pane to which the paper pane will be added
   * @throws IOException if there is an error loading the paper pane
   */
  private void configurePaperPane(Pane room) throws IOException {
    paperLoader = new FXMLLoader(App.class.getResource("/fxml/paper.fxml"));
    AnchorPane node = paperLoader.load();
    paperController = paperLoader.getController();
    paperController.setContext(context);
    room.getChildren().add(node); // Add the paper pane to the room view
  }

  /**
   * Initializes the explanation pane in the given room.
   *
   * @param room the room pane to which the explanation pane will be added
   */
  private void initialiseExplanationPane(Pane room) {
    try {
      configureExplanationPane(room); // Load and configure the explanation pane
    } catch (IOException e) {
      System.err.println("Error loading explanation pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the explanation pane in the given room.
   *
   * @param room the room pane to which the explanation pane will be added
   * @throws IOException if there is an error loading the explanation pane
   */
  private void configureExplanationPane(Pane room) throws IOException {
    explanationLoader = new FXMLLoader(App.class.getResource("/fxml/explanation.fxml"));
    AnchorPane node = explanationLoader.load();
    explanationController = explanationLoader.getController();
    explanationController.setContext(context);
    room.getChildren().add(node); // Add the explanation pane to the room view
  }

  /**
   * Initializes the game over pane in the given room.
   *
   * @param room the room pane to which the game over pane will be added
   */
  private void initialiseGameOverPane(Pane room) {
    try {
      configureGameOverPane(room); // Load and configure the game over pane
    } catch (IOException e) {
      System.err.println("Error loading game over pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the game over pane in the given room.
   *
   * @param room the room pane to which the game over pane will be added
   * @throws IOException if there is an error loading the game over pane
   */
  private void configureGameOverPane(Pane room) throws IOException {
    try {
      gameOverLoader = new FXMLLoader(App.class.getResource("/fxml/gameover.fxml"));
      Pane node = gameOverLoader.load();
      gameOverController = gameOverLoader.getController();
      gameOverController.setContext(context);
      room.getChildren().add(node); // Add the game over pane to the room view
    } catch (ApiProxyException e) {
      System.err.println("Error loading game over pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Initializes the computer password pane in the given room.
   *
   * @param room the room pane to which the computer password pane will be added
   */
  private void initialiseComputerPasswordPane(Pane room) {
    try {
      configureComputerPasswordPane(room); // Load and configure the computer password pane
    } catch (IOException e) {
      System.err.println("Error loading computer password pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the computer password pane in the given room.
   *
   * @param room the room pane to which the computer password pane will be added
   * @throws IOException if there is an error loading the computer password pane
   */
  private void configureComputerPasswordPane(Pane room) throws IOException {
    computerPasswordLoader = new FXMLLoader(App.class.getResource("/fxml/computer-password.fxml"));
    AnchorPane node = computerPasswordLoader.load();
    computerPasswordController = computerPasswordLoader.getController();
    computerPasswordController.setContext(context);
    room.getChildren().add(node); // Add the computer password pane to the room view
  }

  /**
   * Initializes the open computer pane in the given room.
   *
   * @param room the room pane to which the open computer pane will be added
   */
  private void initialiseOpenComputerPane(Pane room) {
    try {
      configureOpenComputerPane(room); // Load and configure the open computer pane
    } catch (IOException e) {
      System.err.println("Error loading open computer pane: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for debugging
    }
  }

  /**
   * Configures the open computer pane in the given room.
   *
   * @param room the room pane to which the open computer pane will be added
   * @throws IOException if there is an error loading the open computer pane
   */
  private void configureOpenComputerPane(Pane room) throws IOException {
    openComputerLoader = new FXMLLoader(App.class.getResource("/fxml/computer-open.fxml"));
    AnchorPane node = openComputerLoader.load();
    openComputerController = openComputerLoader.getController();
    openComputerController.setContext(context);
    room.getChildren().add(node); // Add the open computer pane to the room view
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
      showGuessButton();
    }

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
    // Check which rectangle was clicked and set the interaction flags in GameStateContext
    if (clickedImage.getId().equals("plumberGlow")) {
      context.setPlumberInteracted(true);
    } else if (clickedImage.getId().equals("electricianGlow")) {
      context.setElectricianInteracted(true);
    } else if (clickedImage.getId().equals("neighbourGlow")) {
      context.setNeighbourInteracted(true);
    }

    if (context.getGameState() == context.getGameStartedState()) {
      showGuessButton();
    }

    context.handleImageClick(event, clickedImage.getId());
  }

  /**
   * Handles the replay action event.
   *
   * @param event the action event triggered by the replay button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onReplayAction(ActionEvent event) throws IOException {
    App.resetGame();
  }

  /**
   * Handles the exit action event.
   *
   * @param event the action event triggered by the exit button
   */
  @FXML
  private void onExitButtonClick(ActionEvent event) {
    System.exit(0);
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    // Check if all rectangles have been interacted with
    if (context.isPlumberInteracted()
        && context.isElectricianInteracted()
        && context.isNeighbourInteracted()) {
      context.handleGuessClick();
      timerManager.stop();
      timerManager.resetToOneMinute();
      timerManager.start();
    } else {
      System.out.println("Not all rectangles have been interacted with");
    }
  }

  /**
   * Handles mouse hover exiting on rectangles representing button for map navigation.
   *
   * @param event the mouse event triggered by exiting a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleExit(MouseEvent event) throws IOException {
    Rectangle hoveredRectangle = (Rectangle) event.getSource();
    hoveredRectangle.setOpacity(0.0);

    // Reset room label to the original room name
    lblRoomName.setText(originalRoomName);
    if (hoveredRectangle.getId().equals("rectComputer")) {
      // set specific image that isn't the hovered image to be opaque
      glowComputer.setVisible(false);
    }
  }

  /**
   * Handles mouse hover event on images, making the hovered image and associated name tag opaque.
   *
   * @param event the mouse event triggered by hovering over an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageEnter(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(1.0);

    // Guessing state: set opacity for nametag
    if (hoveredImage.getId().equals("imagePlumber")) {
      plumberNametag.setOpacity(1.0);
    } else if (hoveredImage.getId().equals("imageElectrician")) {
      electricianNametag.setOpacity(1.0);
    } else if (hoveredImage.getId().equals("imageNeighbour")) {
      neighbourNametag.setOpacity(1.0);
    }
  }

  /**
   * Handles mouse exit event on images, making the hovered image and associated name tag
   * transparent.
   *
   * @param event the mouse event triggered by exiting the hover on an image
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleImageExit(MouseEvent event) throws IOException {
    ImageView hoveredImage = (ImageView) event.getSource();
    hoveredImage.setOpacity(0.0);

    // Guessing state: reset opacity for nametag
    if (hoveredImage.getId().equals("imagePlumber")) {
      plumberNametag.setOpacity(0);
    } else if (hoveredImage.getId().equals("imageElectrician")) {
      electricianNametag.setOpacity(0);
    } else if (hoveredImage.getId().equals("imageNeighbour")) {
      neighbourNametag.setOpacity(0);
    }
  }

  /**
   * Handles mouse hover event on rectangles for map navigation, updating opacity and room labels.
   *
   * @param event the mouse event triggered by hovering over a rectangle
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
      case "rectRoom":
        lblRoomName.setText("Bedroom");
        return;
      case "rectComputer":
        glowComputer.setVisible(true);
        return;
    }
  }

  /**
   * Handles mouse enter event for buttons, displaying corresponding UI elements based on button ID.
   *
   * @param event the mouse event triggered by hovering over a button
   */
  @FXML
  public void handleMouseEnter(MouseEvent event) {
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Display specific UI elements based on button ID
      switch (hoveredButton.getId()) {
        case "btnGuess":
          btnMakeAGuess.setVisible(true);
          break;
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

  /**
   * Handles mouse exit event for buttons, hiding corresponding UI elements based on button ID.
   *
   * @param event the mouse event triggered by exiting hover on a button
   */
  @FXML
  public void handleMouseExit(MouseEvent event) {
    if (event.getSource() instanceof Button) {
      Button hoveredButton = (Button) event.getSource();

      // Hide specific UI elements based on button ID
      switch (hoveredButton.getId()) {
        case "btnGuess":
          btnMakeAGuess.setVisible(false);
          break;
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

  /**
   * Gets the chat controller for this view.
   *
   * @return the chat controller
   */
  public ChatController getChatController() {
    chatController.setContext(context);
    return chatController;
  }

  /**
   * Gets the safe controller for this view.
   *
   * @return the safe controller
   */
  public SafeController getSafeController() {
    safeController.setContext(context);
    return safeController;
  }

  /**
   * Gets the open safe controller for this view.
   *
   * @return the open safe controller
   */
  public OpenSafeController getOpenSafeController() {
    openSafeController.setContext(context);
    return openSafeController;
  }

  /**
   * Gets the safe ring controller for this view.
   *
   * @return the safe ring controller
   */
  public SafeRingController getSafeRingController() {
    safeRingController.setContext(context);
    return safeRingController;
  }

  /**
   * Gets the scrunched paper controller for this view.
   *
   * @return the scrunched paper controller
   */
  public ScrunchedPaperController getScrunchedPaperController() {
    scrunchedPaperController.setContext(context);
    return scrunchedPaperController;
  }

  /**
   * Gets the paper controller for this view.
   *
   * @return the paper controller
   */
  public PaperController getPaperController() {
    paperController.setContext(context);
    return paperController;
  }

  /**
   * Gets the explanation controller for this view.
   *
   * @return the explanation controller
   */
  public ExplanationController getExplanationController() {
    explanationController.setContext(context);
    return explanationController;
  }

  /** Displays the guess button when all conditions are met. */
  public void showGuessButton() {
    if (context.isElectricianInteracted()
        && context.isPlumberInteracted()
        && context.isNeighbourInteracted()) {
      if (btnGuess != null) {
        btnGuess.setVisible(true);
        btnGuessImage.setVisible(true);
      }
    }
  }

  /** Hides the guess button. */
  public void hideGuessButton() {
    btnGuess.setVisible(false);
    btnGuessImage.setVisible(false);
  }

  /**
   * Gets the game over controller for this view.
   *
   * @return the game over controller
   * @throws ApiProxyException if there is an API error
   */
  public GameOverController getGameOverController() throws ApiProxyException {
    gameOverController.setContext(context);
    return gameOverController;
  }

  /**
   * Gets the computer password controller for this view.
   *
   * @return the computer password controller
   */
  public ComputerPasswordController getComputerPasswordController() {
    computerPasswordController.setContext(context);
    return computerPasswordController;
  }

  /**
   * Gets the open computer controller for this view.
   *
   * @return the open computer controller
   */
  public ComputerOpenController getOpenComputerController() {
    openComputerController.setContext(context);
    return openComputerController;
  }
}
