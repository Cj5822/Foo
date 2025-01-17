package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SoundManager;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted implements GameState {

  private final GameStateContext context;
  private static final String HEARTBEAT = "src/main/resources/sounds/Heartbeat.mp3";

  /**
   * Constructs a new GameStarted state with the given game state context.
   *
   * @param context the context of the game state
   */
  public GameStarted(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the event when a rectangle is clicked. Depending on the clicked rectangle, it either
   * provides an introduction or transitions to the chat view.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    String targetRoom =
        getRoomForRectangleId(rectangleId); // Create this method to map rectangle IDs to room names

    // Check if the clicked room is the same as the current room
    if (targetRoom.equals(context.getCurrentRoom())) {
      System.out.println(
          "You are already in the " + targetRoom + "."); // Optional: feedback for the player
      return; // Prevent the action if the same room is clicked
    }

    // Transition to chat view or provide an introduction based on the clicked rectangle
    switch (rectangleId) {
      case "rectLivingroom":
        context.setNeighbourInteracted(true);
        context.setCurrentRoom("living-room"); // Update the current room
        App.changeRoom(event, "living-room");
        App.openChat(event, "Neighbour");
        return;
      case "rectGarage":
        context.setElectricianInteracted(true);
        context.setCurrentRoom("garage"); // Update the current room
        App.changeRoom(event, "garage");
        App.openChat(event, "Electrician");
        return;
      case "rectBathroom":
        context.setPlumberInteracted(true);
        context.setCurrentRoom("bathroom"); // Update the current room
        App.changeRoom(event, "bathroom");
        App.openChat(event, "Plumber");
        return;
      case "rectRoom":
        context.setCurrentRoom("room"); // Update the current room
        App.changeRoom(event, "room");
        return;
      case "rectSafe":
        App.openSafe(event);
        return;
      case "rectExitSafe":
        App.closeSafe(event);
        return;
      case "rectExitOpenSafe":
        App.closeOpenedSafe(event);
        return;
      case "rectSafeRing":
        App.openSafeRing(event);
        return;
      case "rectExitSafeRing":
        App.closeSafeRing(event);
        return;
      case "rectScrunchedPaper":
        App.openScrunchedPaper(event);
        return;
      case "rectExitScrunchedPaper":
        App.closeScrunchedPaper(event);
        return;
      case "rectExitPaper":
        App.closePaper(event);
        return;
      case "rectComputer":
        App.openComputer(event);
        return;
      case "rectExitComputer":
        App.closeComputer(event);
        return;
      case "rectExitOpenComputer":
        App.closeOpenComputer(event);
        return;
    }
  }

  // Example method to map rectangle IDs to room names
  private String getRoomForRectangleId(String rectangleId) {
    switch (rectangleId) {
      case "rectLivingroom":
        return "living-room";
      case "rectGarage":
        return "garage";
      case "rectBathroom":
        return "bathroom";
      case "rectRoom":
        return "room";
      // Add more cases as needed...
      default:
        return ""; // Unknown room
    }
  }

  /**
   * Handles the event when the guess button is clicked. Prompts the player to make a guess and
   * transitions to the guessing state.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {
    // TextToSpeech.speak("Make a guess, click on the " + context.getProfessionToGuess());
    context.setState(context.getGuessingState());
    App.changeRoom(null, "room-guessing");
    SoundManager.playSound(HEARTBEAT, false);
  }

  /**
   * Handles the event when an image is clicked. Depending on the clicked image, it opens the hint
   * controller of the corresponding hint.
   *
   * @param event the mouse event triggered by clicking an image
   * @param imageId the ID of the clicked image
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleImageClick(MouseEvent event, String imageId) throws IOException {
    // Handle the click event based on the image ID
    switch (imageId) {
      case "electricianGlow":
        // Open chat for the Electrician when the corresponding image is clicked
        App.openChat(event, "Electrician");
        return; // Exit the method after handling the click
      case "neighbourGlow":
        // Open chat for the Neighbour when the corresponding image is clicked
        App.openChat(event, "Neighbour");
        return; // Exit the method after handling the click
      case "plumberGlow":
        // Open chat for the Plumber when the corresponding image is clicked
        App.openChat(event, "Plumber");
        return; // Exit the method after handling the click
      case "paperGlow":
        // Open the scrunched paper view when the corresponding image is clicked
        App.openScrunchedPaper(event);
        return; // Exit the method after handling the click
      case "safeGlow":
        // Open the safe when the corresponding image is clicked
        App.openSafe(event);
        return; // Exit the method after handling the click
      case "imageRingGlow":
        // Open the safe ring view when the corresponding image is clicked
        App.openSafeRing(event);
        return; // Exit the method after handling the click
        // No default case needed as all cases are covered
    }
  }
}
