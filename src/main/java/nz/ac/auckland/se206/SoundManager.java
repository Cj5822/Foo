package nz.ac.auckland.se206;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
  private static MediaPlayer mediaPlayer;
  private static final Object lock = new Object(); // Lock object for synchronization

  // Play sound effect
  public static void playSound(String soundFilePath, boolean loop) {
    synchronized (lock) { // Synchronize access to the sound playback
      // Stop current sound if it is playing
      stopSound(); // Ensure we stop any currently playing sound

      Media sound = new Media(new File(soundFilePath).toURI().toString());
      mediaPlayer = new MediaPlayer(sound);
      if (loop) {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the sound indefinitely
      }

      mediaPlayer.play();
      mediaPlayer.setOnEndOfMedia(
          () -> {
            if (loop) {
              mediaPlayer.seek(javafx.util.Duration.ZERO); // Loop back to the start
            }
          });
    }
  }

  // Stop any sound currently playing
  public static void stopSound() {
    synchronized (lock) { // Synchronize access to stop sound
      if (mediaPlayer != null) {
        System.out.println("Stopping sound..."); // Debug statement
        mediaPlayer.stop(); // Stop playback
        mediaPlayer.dispose(); // Release resources
        mediaPlayer = null; // Reset reference
      } else {
        System.out.println("No sound to stop."); // Debug statement
      }
    }
  }

  public static MediaPlayer getMediaPlayer() {
    return mediaPlayer;
  }
}
