package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

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
    switch (rectangleId) {
      case "rectPlumber":
        App.openChat(event, "Plumber");
        return;
      case "rectElectrician":
        App.openChat(event, "Electrician");
        return;
      case "rectNeighbour":
        App.openChat(event, "Neighbour");
        return;
      case "rectLivingroom":
        App.changeRoom(event, "living-room");
        return;
      case "rectGarage":
        App.changeRoom(event, "garage");
        return;
      case "rectBathroom":
        App.changeRoom(event, "bathroom");
        return;
      case "rectRoom":
        App.changeRoom(event, "room");
        return;
      case "rectWrench":
        App.openWrench(event);
        return;
      case "rectExitWrench":
        App.closeWrench(event);
        return;
      case "rectExitFlippedWrench":
        App.closeFlippedWrench(event);
        return;
      case "rectFlipWrench":
        App.flipWrench(event);
        return;
      case "rectFlipWrench2":
        App.flipWrenchAgain(event);
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
    System.out.println("Guessing state");
  }
}
