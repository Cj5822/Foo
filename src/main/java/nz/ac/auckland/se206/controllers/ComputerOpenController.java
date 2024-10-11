package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SoundManager;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller class for managing the interactions in the "Open Computer" view of the game. This
 * class handles the user interface elements related to the computer screen, such as controlling a
 * timer, playing sound effects, and managing button visibility. It communicates with the game state
 * context and controls various aspects of the game flow.
 *
 * <p>Key responsibilities include: - Starting and updating the countdown timer. - Managing media
 * player controls (e.g., play/pause/stop for sound effects). - Handling button hover effects and
 * mouse events. - Displaying or hiding the computer pane based on game events.
 */
public class ComputerOpenController {
  // Static Fields
  private static final String WRENCH = "src/main/resources/sounds/Wrenchdrop.mp3";

  // Instance Fields
  private GameStateContext context;
  private TimerManager timerManager;
  private MediaPlayer mediaPlayer;

  // FXML-Injected Fields
  @FXML private AnchorPane openComputerPane;
  @FXML private Rectangle rectExitOpenComputer;
  @FXML private Label lblTimer;
  @FXML private Slider soundProgressBar;
  @FXML private ImageView computerPauseButton;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;
  @FXML private ImageView playButtonHovered;
  @FXML private ImageView pauseButtonHovered;

  @FXML
  public void initialize() throws ApiProxyException {
    hideOpenComputerPane(); // Hide wrench pane initially
    timerManager = TimerManager.getInstance(context);
    startTimer();
  }

  /**
   * Sets the context for the controller.
   *
   * @param context the game state context
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /** Starts the timer and continuously updates the timer label on each frame. */
  private void startTimer() {
    // Start the timer using the TimerManager instance
    timerManager.start();

    // Create an AnimationTimer to update the UI each frame
    AnimationTimer timerUpdater =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            // Update the timer label with the formatted time from TimerManager
            lblTimer.setText(timerManager.getTimeFormatted());
          }
        };

    // Start the timer updater to begin continuous updates
    timerUpdater.start();
  }

  /**
   * Handles the click event on a rectangle (typically for navigation).
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /** Plays the wrench sound and manages its playback state (play, pause, stop). */
  @FXML
  private void onPlayWrenchSound() {
    computerPauseButton.setVisible(true);
    playButtonHovered.setVisible(false);
    pauseButtonHovered.setVisible(false);

    if (mediaPlayer != null) {
      // Toggle between play, pause, and restart based on media player state
      if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
        mediaPlayer.pause(); // Pause if currently playing
        computerPauseButton.setVisible(false);
        playButtonHovered.setVisible(true);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
        mediaPlayer.play(); // Resume if paused
        computerPauseButton.setVisible(true);
        pauseButtonHovered.setVisible(true);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
        mediaPlayer.seek(Duration.ZERO); // Restart from the beginning
        mediaPlayer.play(); // Play the sound from the start
      }
    } else {
      // Initialize and play sound for the first time
      initializeMediaPlayer();
      computerPauseButton.setVisible(true);
      pauseButtonHovered.setVisible(true);
    }
  }

  /** Initializes the media player, binds the progress bar, and handles user interactions. */
  private void initializeMediaPlayer() {
    // Play sound using SoundManager
    SoundManager.playSound(WRENCH, false);
    mediaPlayer = SoundManager.getMediaPlayer();

    // Bind progress bar to media player's total duration
    mediaPlayer.setOnReady(
        () -> {
          Duration totalDuration = mediaPlayer.getTotalDuration();
          soundProgressBar.setMax(totalDuration.toSeconds());
        });

    // Update progress bar as sound plays
    mediaPlayer
        .currentTimeProperty()
        .addListener(
            (observable, oldTime, newTime) -> {
              if (!soundProgressBar.isValueChanging()) {
                soundProgressBar.setValue(newTime.toSeconds());
              }
            });

    // Handle user dragging the progress bar
    soundProgressBar
        .valueChangingProperty()
        .addListener(
            (observable, wasChanging, isChanging) -> {
              if (!isChanging) {
                mediaPlayer.seek(Duration.seconds(soundProgressBar.getValue()));
              }
            });

    // Handle mouse clicks on the progress bar
    soundProgressBar.setOnMousePressed(
        event -> {
          double mouseX = event.getX();
          double progressPercentage = mouseX / soundProgressBar.getWidth();
          Duration newDuration = mediaPlayer.getTotalDuration().multiply(progressPercentage);
          mediaPlayer.seek(newDuration);
          soundProgressBar.setValue(newDuration.toSeconds());
        });

    // Set the listener for when the media reaches the end
    mediaPlayer.setOnEndOfMedia(
        () -> {
          mediaPlayer.stop(); // Stop playback
          soundProgressBar.setValue(0); // Reset progress bar
          computerPauseButton.setVisible(false); // Hide pause button
          playButtonHovered.setVisible(false); // Hide play button
          pauseButtonHovered.setVisible(false); // Hide pause button after completion
        });
  }

  /** Displays the computer pane and initializes the media controls. */
  public void showOpenComputerPane() {
    computerPauseButton.setVisible(false); // Set initial visibility of buttons
    openComputerPane.setVisible(true);
  }

  /** Hides the computer pane and stops the media playback. */
  public void hideOpenComputerPane() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      mediaPlayer.seek(Duration.ZERO); // Reset to the start of the audio
    }
    computerPauseButton.setVisible(false); // Reset pause button visibility
    openComputerPane.setVisible(false);
  }

  /**
   * Handles mouse hover effect for the exit button (entering).
   *
   * @param event the mouse event triggered by hovering the exit button
   */
  @FXML
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  /**
   * Handles mouse hover effect for the exit button (exiting).
   *
   * @param event the mouse event triggered by exiting the exit button
   */
  @FXML
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  /**
   * Handles mouse hover effect for the play button (entering).
   *
   * @param event the mouse event triggered by hovering the play button
   */
  @FXML
  private void handlePlayEnter(MouseEvent event) {
    if (mediaPlayer != null) {
      // Adjust play/pause button visibility based on media player status
      if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
        pauseButtonHovered.setVisible(true);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
        playButtonHovered.setVisible(true);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
        playButtonHovered.setVisible(true);
        pauseButtonHovered.setVisible(false);
      }
    } else {
      // Initialize and play sound for the first time
      playButtonHovered.setVisible(true);
    }
  }

  /**
   * Handles mouse hover effect for the play button (exiting).
   *
   * @param event the mouse event triggered by exiting the play button
   */
  @FXML
  private void handlePlayExit(MouseEvent event) {
    if (mediaPlayer != null) {
      // Hide the correct button based on the current media player status
      if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
        pauseButtonHovered.setVisible(false);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
        playButtonHovered.setVisible(false);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
        playButtonHovered.setVisible(false);
        pauseButtonHovered.setVisible(false);
      }
    } else {
      // Hide the buttons when no media player is active
      playButtonHovered.setVisible(false);
    }
  }
}
