package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SafeController;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static FXMLLoader loader;
  private static SafeController safeController; // Controller for the safe view
  private static ChatController chatController; // Controller for the chat view
  private static RoomController roomController; // Controller for the room view
  private static Stage appStage;

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
    chatController.showChatBox(); // Display the chat box
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
    chatController = roomController.getChatController();

    scene = new Scene(root); // Create a new scene with the loaded root
    appStage.setScene(scene); // Set the scene on the stage
    appStage.show(); // Display the stage
    root.requestFocus();
  }

  public static void openSafe(MouseEvent event) throws IOException {
    // Load the FXML for the safe
    Parent root = loadFxml("safe-password");

    safeController = loader.getController();

    safeController.showSafeBox();

    scene = new Scene(root);
    appStage.setScene(scene);
    appStage.show();
    root.requestFocus();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "backstory" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/backstory.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    appStage = stage;
    Parent root = loadFxml("homepage"); // Load the "backstory" FXML
    scene = new Scene(root); // Create a new scene with the loaded root
    stage.setScene(scene); // Set the scene on the stage
    stage.show(); // Display the stage
    root.requestFocus(); // Request focus for the root node
  }
}
