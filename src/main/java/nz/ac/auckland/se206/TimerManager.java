package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimerManager {
  // Static Fields
  private static TimerManager instance; // Singleton instance

  // Instance Fields
  private int secondsRemaining = 300; // Track the number of seconds remaining
  private boolean isTimeUp = false; // Flag to indicate if time has run out
  private Timeline timeline;
  private GameStateContext context;

  /**
   * Private constructor to prevent instantiation. Initializes the timeline and sets up the timer to
   * decrease the time by one second at each interval.
   *
   * @param context the game state context to share across the application.
   */
  private TimerManager(GameStateContext context) {
    setContext(context); // Shared context from the application
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  try {
                    updateTimer();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely until stopped
  }

  /**
   * Returns the singleton instance of TimerManager with the shared game state context. If no
   * instance exists, a new one is created.
   *
   * @param context the game state context to associate with this timer manager.
   * @return the singleton instance of TimerManager.
   */
  public static synchronized TimerManager getInstance(GameStateContext context) {
    if (instance == null) {
      instance = new TimerManager(context);
    }
    return instance;
  }

  /** Resets the timer to 1 minute (60 seconds) and ensures the timer is not running. */
  public void resetToOneMinute() {
    stop();
    secondsRemaining = 60; // Set the timer to 1 minute (60 seconds)
    isTimeUp = false; // Reset the time-up flag
  }

  /** Starts the timer if it is not already running and the time has not expired. */
  public void start() {
    if (!isTimeUp && timeline.getStatus() != Timeline.Status.RUNNING) {
      timeline.play();
    }
  }

  /** Stops the timer by pausing the timeline. */
  public void stop() {
    timeline.stop();
  }

  /** Resets the timer to 5 minutes (300 seconds) and ensures the timer is not running. */
  public void reset() {
    stop();
    secondsRemaining = 300; // Reset the timer to 5 minutes (300 seconds)
    isTimeUp = false; // Reset the time-up flag
  }

  /**
   * Sets the game state context for the TimerManager.
   *
   * @param context the game state context to be set.
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Decreases the seconds remaining on the timer every second. If the timer reaches zero, it stops
   * and triggers the appropriate game state transition.
   *
   * @throws IOException if an I/O error occurs when changing rooms or handling game state
   *     transitions.
   */
  private void updateTimer() throws IOException {
    if (secondsRemaining > 0) {
      secondsRemaining--;
    } else {
      stop(); // Stop the timer when it reaches 0
      isTimeUp = true;
      handleTimerEnd(); // Handle the actions required when time is up
    }
  }

  /**
   * Handles the transition of the game when the timer ends, depending on the current game state and
   * whether all interactions with suspects are completed.
   *
   * @throws IOException if an I/O error occurs during room transitions.
   */
  private void handleTimerEnd() throws IOException {
    if (context != null) {
      if (context.isGameStarted()) {
        if (context.isPlumberInteracted()
            && context.isElectricianInteracted()
            && context.isNeighbourInteracted()) {
          // Transition to guessing state and reset the timer
          context.setState(context.getGuessingState());
          resetToOneMinute();
          start(); // Start the 1-minute timer for guessing state
          App.changeRoom(null, "room-guessing"); // Switch to the guessing room
        } else {
          // Game over if the timer reaches 0 again in guessing state
          context.setState(context.getGameOverState());
          App.changeRoom(null, "gameover-requirements-not-met");
        }
      } else if (context.isGuessingState()) {
        if (context.getSelectedSuspect() == null) {
          context.setState(context.getGameOverState());
          App.changeRoom(null, "gameover-requirements-not-met");
        } else {
          context.setState(context.getGameOverState());
          App.openGameOver(); // Open game over screen
        }
      }
    }
  }

  /**
   * Returns the current time remaining in the format MM:SS.
   *
   * @return the time formatted as a string.
   */
  public String getTimeFormatted() {
    int minutes = secondsRemaining / 60;
    int seconds = secondsRemaining % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  /**
   * Checks whether the timer has reached zero and time is up.
   *
   * @return true if time is up, otherwise false.
   */
  public boolean isTimeUp() {
    return isTimeUp;
  }

  /**
   * Returns the current time remaining on the timer in seconds.
   *
   * @return the remaining time in seconds.
   */
  public int getTimeInSeconds() {
    return secondsRemaining;
  }
}
