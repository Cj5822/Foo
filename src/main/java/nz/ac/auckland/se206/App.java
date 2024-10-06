package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.ExplanationController;
import nz.ac.auckland.se206.controllers.FlippedWrenchController;
import nz.ac.auckland.se206.controllers.HomeController;
import nz.ac.auckland.se206.controllers.OpenSafeController;
import nz.ac.auckland.se206.controllers.PaperController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SafeController;
import nz.ac.auckland.se206.controllers.SafeRingController;
import nz.ac.auckland.se206.controllers.ScrunchedPaperController;
import nz.ac.auckland.se206.controllers.WrenchController;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;

  private static FXMLLoader loader;

  private static ChatController chatController; // Controller for the chat view
  private static RoomController roomController; // Controller for the room view
  private static SafeController safeController; // Controller for the safe view
  private static OpenSafeController openSafeController; // Controller for the open safe view
  private static SafeRingController safeRingController; // Controller for the safe ring view
  private static WrenchController wrenchController; // Controller for the wrench view
  private static FlippedWrenchController
      flippedWrenchController; // Controller for the flipped wrench view
  private static ScrunchedPaperController
      scrunchedPaperController; // Controller for the scrunched paper view
  private static PaperController paperController; // Controller for the paper view
  private static ExplanationController explanationController; // Controller for the explanation view
  private static Stage appStage;

  private static GameStateContext context;

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    return loader.load(); // Load and return the root node of the specified FXML file
  }

  /**
   * Opens the chat view and sets the profession in the chat controller.
   *
   * @param event the mouse event that triggered the method
   * @param profession the profession to set in the chat controller
   * @throws IOException if the FXML file is not found
   */
  public static void openChat(MouseEvent event, String profession) throws IOException {
    chatController.setProfession(profession); // Set the profession in the chat controller
    chatController.showChatPane(); // Display the chat box
  }

  public static void openSafe(MouseEvent event) throws IOException {
    roomController = loader.getController();
    roomController.setContext(context);
    safeController = roomController.getSafeController();
    safeController.showSafePane();
  }

  public static void closeSafe(MouseEvent event) throws IOException {
    safeController.hideSafePane();
  }

  public static void openOpenedSafe(Event event) throws IOException {
    // Check if the event is either ActionEvent or MouseEvent
    if (event instanceof ActionEvent || event instanceof MouseEvent) {

      // Get the controller (roomController) of the current scene
      roomController = loader.getController();
      roomController.setContext(context);

      // Hide the safe pane using safeController
      safeController.hideSafePane();

      // Get the openSafeController from roomController and show the open safe pane
      openSafeController = roomController.getOpenSafeController();
      openSafeController.showOpenSafePane();
    }
  }

  public static void closeOpenedSafe(MouseEvent event) throws IOException {
    openSafeController.hideOpenSafePane();
  }

  public static void openSafeRing(MouseEvent event) throws IOException {
    roomController = loader.getController();
    roomController.setContext(context);
    safeRingController = roomController.getSafeRingController();
    safeRingController.showSafeRingPane();
  }

  public static void closeSafeRing(MouseEvent event) throws IOException {
    safeRingController.hideSafeRingPane();
  }

  public static void openWrench(MouseEvent event) throws IOException {
    roomController = loader.getController();
    roomController.setContext(context);
    wrenchController = roomController.getWrenchController();
    wrenchController.showWrenchPane();
  }

  public static void closeWrench(MouseEvent event) throws IOException {
    wrenchController.hideWrenchPane();
  }

  public static void flipWrench(MouseEvent event) {
    roomController = loader.getController();
    roomController.setContext(context);
    wrenchController.hideWrenchPane();
    flippedWrenchController = roomController.getFlippedWrenchController();
    flippedWrenchController.showFlippedWrenchPane();
  }

  public static void flipWrenchAgain(MouseEvent event) {
    roomController = loader.getController();
    roomController.setContext(context);
    flippedWrenchController.hideFlippedWrenchPane();
    wrenchController = roomController.getWrenchController();
    wrenchController.showWrenchPane();
  }

  public static void closeFlippedWrench(MouseEvent event) throws IOException {
    flippedWrenchController.hideFlippedWrenchPane();
  }

  public static void openScrunchedPaper(MouseEvent event) throws IOException {
    roomController = loader.getController();
    roomController.setContext(context);
    scrunchedPaperController = roomController.getScrunchedPaperController();
    scrunchedPaperController.showScrunchedPaperPane();
  }

  public static void closeScrunchedPaper(MouseEvent event) throws IOException {
    scrunchedPaperController.hideScrunchedPaperPane();
  }

  public static void openPaperHint(ActionEvent event) throws IOException {
    scrunchedPaperController.hideScrunchedPaperPane();
    roomController = loader.getController();
    roomController.setContext(context);
    paperController = roomController.getPaperController();
    paperController.showPaperPane();
  }

  public static void closePaper(MouseEvent event) throws IOException {
    paperController.hidePaperPane();
  }

  public static void openExplanation(MouseEvent event) throws IOException {
    roomController = loader.getController();
    roomController.setContext(context);
    explanationController = roomController.getExplanationController();
    explanationController.showExplanationPane();
  }

  /**
   * Changes the room to the room selected.
   *
   * @param event the mouse event that triggered the method
   * @param roomName the room of the FXML file to load
   * @throws IOException if the FXML file is not found
   */
  public static void changeRoom(MouseEvent event, String roomName) throws IOException {
    // Load the FXML for the new room
    Parent root = loadFxml(roomName); // Load the "backstory" FXML

    roomController = loader.getController();
    roomController.setContext(context);
    chatController = roomController.getChatController();

    scene = new Scene(root); // Create a new scene with the loaded root
    appStage.setScene(scene); // Set the scene on the stage
    appStage.show(); // Display the stage
    root.requestFocus();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "homepage" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/homepage.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    appStage = stage;

    context = new GameStateContext();
    TimerManager.getInstance(context);

    FXMLLoader homepageLoader = new FXMLLoader(App.class.getResource("/fxml/homepage.fxml"));
    Parent root = homepageLoader.load();
    HomeController homepageController = homepageLoader.getController();
    homepageController.setContext(context);

    scene = new Scene(root); // Create a new scene with the loaded root
    stage.setScene(scene); // Set the scene on the stage
    stage.show(); // Display the stage
    root.requestFocus(); // Request focus for the root node
  }
}
