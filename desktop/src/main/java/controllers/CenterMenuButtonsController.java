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
import sample.Structs.BuildingLevel;
import sample.Structs.Point;
import sample.WebService.WebServiceConnection;

public class CenterMenuButtonsController {
    @FXML
    public Pane mainPane;
    private MasterWindowController masterWindowController;

    private BuildingLevel level;

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
//            canvas.getGraphicsContext2D().setFill(Color.rgb(255,0,0,1.0));
//            canvas.getGraphicsContext2D().fillOval(t.getX() - 5, t.getY() - 5, 10, 10);
//            canvas.getGraphicsContext2D().stroke();

            Point point = new Point();
            point.setX(t.getX());
            point.setY(t.getY());

            WebServiceConnection.GetInstance().AddPoint(point, level.getBuildingLevel());
        });
        mainPane.widthProperty().addListener(((observableValue, number, t1) ->
        {
            canvas.setWidth(mainPane.getWidth());
        }));
        mainPane.heightProperty().addListener(((observableValue, number, t1) ->
        {
            canvas.setHeight(mainPane.getHeight());
        }));
    }

    public Canvas canvas;
    public CenterMenuButtonsController()
    {

    }

    public void ShowPoints(Point[] points, BuildingLevel level)
    {
        this.level = level;
        for(Point point : points)
        {
            if(point.IdImage != level.getIdImage())
                continue;
            canvas.getGraphicsContext2D().setFill(Color.rgb(255,0,0,1.0));
            canvas.getGraphicsContext2D().fillOval(point.getX() - 5, point.getY() - 5, 10, 10);
            canvas.getGraphicsContext2D().stroke();
        }
    }
}

