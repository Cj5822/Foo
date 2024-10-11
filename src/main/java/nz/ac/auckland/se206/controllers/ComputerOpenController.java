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

public class ComputerOpenController {
  private GameStateContext context;

  @FXML private AnchorPane openComputerPane;
  @FXML private Rectangle rectExitOpenComputer;
  @FXML private Label lblTimer;
  @FXML private Slider soundProgressBar;
  @FXML private ImageView computerPauseButton;
  @FXML private ImageView exitButtonHover;
  @FXML private ImageView exitButtonUnhovered;
  @FXML private ImageView playButtonHovered;
  @FXML private ImageView pauseButtonHovered;

  private TimerManager timerManager;
  private MediaPlayer mediaPlayer;
  private static final String WRENCH = "src/main/resources/sounds/Wrenchdrop.mp3";

  @FXML
  public void initialize() throws ApiProxyException {
    hideOpenComputerPane(); // Hide wrench pane initially
    timerManager = TimerManager.getInstance(context);
    startTimer();
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /** Starts the timer and continuously updates the timer label. */
  private void startTimer() {
    // Initiate the timerManager to start counting time
    timerManager.start();

    // Create an AnimationTimer to update the UI at each frame
    AnimationTimer timerUpdater =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            // Update the timer label with the formatted time from timerManager
            lblTimer.setText(timerManager.getTimeFormatted());
          }
        };

    // Start the timer updater to begin the continuous updates
    timerUpdater.start();
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /** Plays the wrench sound and manages its playback state. */
  @FXML
  private void playWrenchSound() {
    computerPauseButton.setVisible(true);
    if (mediaPlayer != null) {
      // Toggle play/pause or restart
      if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
        mediaPlayer.pause(); // Pause if playing
        computerPauseButton.setVisible(false);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
        mediaPlayer.play(); // Resume if paused
        computerPauseButton.setVisible(true);
      } else if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
        mediaPlayer.seek(Duration.ZERO); // Reset to start
        mediaPlayer.play(); // Play from the start
      }
    } else {
      // Initialize and play sound for the first time
      initializeMediaPlayer();
      computerPauseButton.setVisible(true);
    }
  }

  private void initializeMediaPlayer() {
    // Play sound using SoundManager
    SoundManager.playSound(WRENCH, false);
    mediaPlayer = SoundManager.getMediaPlayer();

    // Bind progress bar to the media player's total duration
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

    // Handle clicks on the progress bar
    soundProgressBar.setOnMousePressed(
        event -> {
          double mouseX = event.getX();
          double progressPercentage = mouseX / soundProgressBar.getWidth();
          Duration newDuration = mediaPlayer.getTotalDuration().multiply(progressPercentage);
          mediaPlayer.seek(newDuration);
          soundProgressBar.setValue(newDuration.toSeconds());
        });

    // Set the end of media listener
    mediaPlayer.setOnEndOfMedia(
        () -> {
          // Stop playing when it reaches the end
          mediaPlayer.stop();
        });
  }

  public void showOpenComputerPane() {
    openComputerPane.setVisible(true);
  }

  public void hideOpenComputerPane() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      mediaPlayer.seek(Duration.ZERO); // Seek back to the start
    }
    openComputerPane.setVisible(false);
  }

  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonUnhovered.setVisible(false);
    exitButtonHover.setVisible(true);
  }

  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonUnhovered.setVisible(true);
    exitButtonHover.setVisible(false);
  }

  // @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  // private void handlePlayEnter(MouseEvent event) {
  //   if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
  //     playButtonHovered.setVisible(true);
  //   } else {
  //     pauseButtonHovered.setVisible(true);
  //   }
  // }

  // @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  // private void handlePlayExit(MouseEvent event) {
  //   if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
  //     playButtonHovered.setVisible(false);
  //   } else {
  //     pauseButtonHovered.setVisible(false);
  //   }
  // }
}
