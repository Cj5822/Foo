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

public class ScrunchedPaperController {
  private GameStateContext context;

  @FXML private AnchorPane scrunchedPaperPane;
  @FXML private ImageView btnAnalyseImage;
  @FXML private ImageView exitButtonHover;

  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    hideScrunchedPaperPane(); // Hide wrench pane initially
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  @FXML // Handle the exit button hover effect based on hovering the rectangle above it
  private void handleExitHoverEnter(MouseEvent event) {
    exitButtonHover.setVisible(true);
  }

  @FXML // Handle the exit button hover effect based on exiting the rectangle above it
  private void handleExitHoverExit(MouseEvent event) {
    exitButtonHover.setVisible(false);
  }

  @FXML
  public void handleAnalyseHint(ActionEvent event) throws IOException {
    App.openPaperHint(event);
    return;
  }

  public void showScrunchedPaperPane() {
    scrunchedPaperPane.setVisible(true);
  }

  public void hideScrunchedPaperPane() {
    scrunchedPaperPane.setVisible(false);
  }

  @FXML
  public void handleMouseEnter() {
    btnAnalyseImage.setVisible(true);
  }

  @FXML
  public void handleMouseExit() {
    btnAnalyseImage.setVisible(false);
  }
}
