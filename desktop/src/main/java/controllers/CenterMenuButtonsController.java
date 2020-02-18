package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CenterMenuButtonsController {
    @FXML
    public Pane mainPane;
    private MasterWindowController masterWindowController;

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;

    }

    @FXML
    public void initialize()
    {
        canvas = new Canvas(mainPane.getWidth(), mainPane.getHeight());
        mainPane.getChildren().add(canvas);

        canvas.widthProperty().bind(mainPane.widthProperty());
        canvas.heightProperty().bind(mainPane.heightProperty());

        canvas.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            canvas.getGraphicsContext2D().setFill(Color.rgb(255,255,255,0.3));
            canvas.getGraphicsContext2D().fillOval(t.getX() - 5, t.getY() - 5, 10, 10);
            canvas.getGraphicsContext2D().stroke();
    });
    }

    public Canvas canvas;
    public CenterMenuButtonsController()
    {

    }
}

