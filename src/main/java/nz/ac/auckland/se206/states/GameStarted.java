package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted implements GameState {

  private final GameStateContext context;

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
    // Transition to chat view or provide an introduction based on the clicked rectangle
    String roomName = "";
    switch (rectangleId) {
      case "rectPlumber":
        roomName = "bathroom";
        App.openChat(event, "Plumber");
        return;
      case "rectElectrician":
        roomName = "bathroom";
        App.openChat(event, "Electrician");
        return;
      case "rectNeighbour":
        roomName = "bathroom";
        App.openChat(event, "Electrician");
        return;
      case "rectPerson1":
        roomName = "living-room";
        App.changeRoom(event, roomName);
      case "rectPerson2":
        roomName = "garage";
        App.changeRoom(event, roomName);
      case "rectPerson3":
        roomName = "bathroom";
        App.changeRoom(event, roomName);
      case "rectPerson4":
        roomName = "room";
        App.changeRoom(event, roomName);
    }
    App.changeRoom(event, roomName);
  }

  /**
   * Handles the event when the guess button is clicked. Prompts the player to make a guess and
   * transitions to the guessing state.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {
    TextToSpeech.speak("Make a guess, click on the " + context.getProfessionToGuess());
    context.setState(context.getGuessingState());
  }
}
