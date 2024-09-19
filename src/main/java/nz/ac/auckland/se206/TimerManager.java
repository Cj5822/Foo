package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimerManager {
  private static TimerManager instance; // Singleton instance
  private int secondsRemaining = 300; // Track the number of seconds remaining (5 minutes)
  private Timeline timeline;
  private boolean isTimeUp = false; // Flag to indicate if time has run out

  /** Private constructor to prevent instantiation. */
  private TimerManager() {
    timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
    timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely until stopped
  }

  // Public method to get the singleton instance
  public static synchronized TimerManager getInstance() {
    if (instance == null) {
      instance = new TimerManager();
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
    secondsRemaining = 300; // Reset the timer to 10 minutes
    isTimeUp = false; // Reset the time-up flag
  }

  // Update the seconds counter every second
  private void updateTimer() {
    if (secondsRemaining > 0) {
      secondsRemaining--;
    } else {
      stop(); // Stop the timer when it reaches 0
      isTimeUp = true;
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
}
