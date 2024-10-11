package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

/**
 * The ScrunchedPaperController is responsible for managing the behavior and interactions of the
 * scrunched paper scene in the game. This includes handling mouse events for analyzing the hint,
 * interacting with rectangles, and hovering over the exit button. The controller also controls the
 * visibility of the scrunched paper pane.
 */
public class ScrunchedPaperController {
  private GameStateContext context;

  @FXML private AnchorPane scrunchedPaperPane;
  @FXML private ImageView btnAnalyseImage;
  @FXML private ImageView exitButtonHover;

  /**
   * Initializes the scrunched paper pane and its components. This method is called after the
   * controller has been fully initialized. It hides the scrunched paper pane initially.
   *
   * @throws ApiProxyException if there is an issue with the API proxy during initialization.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    hideScrunchedPaperPane(); // Hide scrunched paper pane initially
  }

  /**
   * Sets the game state context for the scrunched paper controller.
   *
   * @param context the current game state context.
   */
  public void setContext(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the click event on a rectangle. It retrieves the clicked rectangle and delegates the
   * event handling to the game state context.
   *
   * @param event the mouse event triggered by clicking the rectangle.
   * @throws IOException if an I/O error occurs during handling.
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /**
   * Handles the event where the mouse enters the exit button's hover area. This will make the hover
   * image for the exit button visible.
   *
   * @param event the mouse event triggered by hovering over the exit button.
   */
  @FXML
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonHover.setVisible(true);
  }

  /**
   * Handles the event where the mouse exits the exit button's hover area. This will hide the hover
   * image for the exit button.
   *
   * @param event the mouse event triggered by exiting the hover area of the exit button.
   */
  @FXML
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonHover.setVisible(false);
  }

  /**
   * Handles the action event for analyzing a hint. When this method is called, the user is taken to
   * the paper hint screen.
   *
   * @param event the action event triggered by clicking the 'Analyse Hint' button.
   * @throws IOException if there is an issue opening the paper hint screen.
   */
  @FXML
  private void handleAnalyseHint(ActionEvent event) throws IOException {
    App.openPaperHint(event);
  }

  /** Shows the scrunched paper pane by making it visible. */
  public void showScrunchedPaperPane() {
    scrunchedPaperPane.setVisible(true);
  }

  /** Hides the scrunched paper pane by making it invisible. */
  public void hideScrunchedPaperPane() {
    scrunchedPaperPane.setVisible(false);
  }

  /**
   * Handles the event where the mouse enters the analyze image area. This makes the analyze image
   * button visible.
   */
  @FXML
  public void handleMouseEnter() {
    btnAnalyseImage.setVisible(true);
  }

  /**
   * Handles the event where the mouse exits the analyze image area. This hides the analyze image
   * button.
   */
  @FXML
  public void handleMouseExit() {
    btnAnalyseImage.setVisible(false);
  }
}
