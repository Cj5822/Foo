package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimerManager {
  private static TimerManager instance; // Singleton instance
  private int secondsRemaining = 300; // Track the number of seconds remaining
  private Timeline timeline;
  private boolean isTimeUp = false; // Flag to indicate if time has run out
  private boolean isInGuessingState =
      false; // Flag to indicate if the game is in the guessing state
  private final GameStateContext context;

  /** Private constructor to prevent instantiation. */
  private TimerManager(GameStateContext context) {
    this.context = context; // Shared context from the application
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

  // Public method to get the singleton instance with shared context
  public static synchronized TimerManager getInstance(GameStateContext context) {
    if (instance == null) {
      instance = new TimerManager(context);
    }
    return instance;
  }

  // Reset the timer to 1 minute
  public void resetToOneMinute() {
    stop();
    secondsRemaining = 60; // Set the timer to 1 minute (60 seconds)
    isTimeUp = false; // Reset the time-up flag
  }

  // Start the timer
  public void start() {
    if (!isTimeUp && timeline.getStatus() != Timeline.Status.RUNNING) {
      timeline.play();
    }
  }

  // Stop the timer
  public void stop() {
    timeline.stop();
  }

  // Reset the timer to 5 minutes
  public void reset() {
    stop();
    secondsRemaining = 300; // Reset the timer to 5 minutes (300 seconds)
    isTimeUp = false; // Reset the time-up flag
  }

  // Update the seconds counter every second
  private void updateTimer() throws IOException {
    if (secondsRemaining > 0) {
      secondsRemaining--;
    } else {
      stop(); // Stop the timer when it reaches 0
      isTimeUp = true;
      handleTimerEnd();
    }
  }

  // Handle what happens when the timer ends
  private void handleTimerEnd() throws IOException {
    if (!isInGuessingState) {
      // Transition to guessing state and reset the timer
      isInGuessingState = true;
      resetToOneMinute();
      start(); // Start the 1-minute timer for guessing state
      context.setState(context.getGuessingState());
      App.changeRoom(null, "room-guessing"); // Switch to the guessing room
    } else {
      // Game over if the timer reaches 0 again in guessing state
      isTimeUp = true;
      context.setState(context.getGameOverState());
      System.out.println("Game over! You ran out of time in the guessing state.");
    }
  }

  // Get the current time in seconds formatted as MM:SS
  public String getTimeFormatted() {
    int minutes = secondsRemaining / 60;
    int seconds = secondsRemaining % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }

  // Check if time is up
  public boolean isTimeUp() {
    return isTimeUp;
  }

  public int getTimeInSeconds() {
    return secondsRemaining;
  }
}
