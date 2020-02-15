package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class CenterMenuButtonsController {
    @FXML
    public Canvas canvas;
    private MasterWindowController masterWindowController;

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }
}
