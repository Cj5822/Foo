package nz.ac.auckland.se206;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The SoundManager class is responsible for managing and playing sound effects in the game. It
 * provides methods for playing and stopping sounds, including the ability to loop sounds.
 */
public class SoundManager {

  private static MediaPlayer mediaPlayer; // MediaPlayer instance for managing sound playback
  private static final Object lock =
      new Object(); // Lock object for synchronization to ensure thread safety

  /**
   * Plays a sound effect from the given file path.
   *
   * @param soundFilePath the file path of the sound to be played
   * @param loop a boolean indicating whether the sound should loop indefinitely
   */
  public static void playSound(String soundFilePath, boolean loop) {
    synchronized (lock) { // Synchronize access to the sound playback to prevent conflicts
      // Stop current sound if it is playing
      stopSound(); // Ensure we stop any currently playing sound

      // Create a new Media object from the sound file
      Media sound = new Media(new File(soundFilePath).toURI().toString());
      mediaPlayer = new MediaPlayer(sound);

      // Set the sound to loop if specified
      if (loop) {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the sound indefinitely
      }

      // Play the sound
      mediaPlayer.play();

      // Reset the sound to the beginning if it ends and looping is enabled
      mediaPlayer.setOnEndOfMedia(
          () -> {
            if (loop) {
              mediaPlayer.seek(javafx.util.Duration.ZERO); // Loop back to the start of the sound
            }
          });
    }
  }

  /** Stops any sound that is currently playing and releases the resources. */
  public static void stopSound() {
    synchronized (lock) { // Synchronize access to stop sound to ensure thread safety
      if (mediaPlayer != null) {
        mediaPlayer.stop(); // Stop the sound playback
        mediaPlayer.dispose(); // Release the resources used by the MediaPlayer
        mediaPlayer = null; // Reset the mediaPlayer reference to null
      }
    }
  }

  /**
   * Returns the current MediaPlayer instance, if any.
   *
   * @return the MediaPlayer instance managing the current sound playback
   */
  public static MediaPlayer getMediaPlayer() {
    return mediaPlayer;
  }
}
