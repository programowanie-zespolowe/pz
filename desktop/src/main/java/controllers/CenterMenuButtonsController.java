package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Constants;
import sample.Structs.BuildingLevel;
import sample.Structs.Point;
import sample.Structs.PointsConnection;
import sample.WebService.WebServiceConnection;

import java.io.IOException;

public class CenterMenuButtonsController {
    private static final String WINDOW_POINT_DETAILS = "/fxml/pointDetailsWindow.fxml";
    @FXML
    public Pane mainPane;
    @FXML
    public ScrollPane scrollPane;
    private MasterWindowController masterWindowController;

    private BuildingLevel level;

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;

    }

    Point points[];
    Point selectedPoint_1 = null;
    Point selectedPoint_2 = null;

    public Point FindPoint(double x, double y)
    {
        for(Point point : points)
        {
            if(point.getIdImage() != level.getIdImage())
                continue;
            if(point.getX() - 5 <= x && x <= point.getX() + 5 &&
                point.getY() - 5 <= y && y <= point.getY() + 5)
                return point;
        }
        return null;
    }

    @FXML
    public void initialize()
    {
        canvas = new Canvas();
        scrollPane.setContent(canvas);

        canvas.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            if(t.getButton() != MouseButton.PRIMARY) {
                if(selectedPoint_1 == null) {
                    selectedPoint_1 = FindPoint(t.getX(), t.getY());
                }
                else if(selectedPoint_2 == null) {
                    selectedPoint_2 = FindPoint(t.getX(), t.getY());

                    if(selectedPoint_2 == selectedPoint_1) {
                        ShowPointDetails(selectedPoint_1);
                        selectedPoint_1 = null;
                        selectedPoint_2 = null;
                        return;
                    }

                    PointsConnection connection = WebServiceConnection.GetInstance().AddPointConnection(selectedPoint_1, selectedPoint_2);
                    if(connection != null)
                        masterWindowController.ConnectionAdded(connection);
                    selectedPoint_1 = null;
                    selectedPoint_2 = null;
                }
                return;
            }
            Point point = new Point();
            point.setX(t.getX());
            point.setY(t.getY());
            point.setIdImage(level.getIdImage());
            point.setDirection(0);
            point.setOnOffDirection(false);

            Point addedPoint = WebServiceConnection.GetInstance().AddPoint(point, level.getIdImage());
            if(addedPoint != null)
                masterWindowController.PointAdded(addedPoint);
        });

        mainPane.widthProperty().addListener((observableValue, number, t1) -> scrollPane.setMaxWidth(mainPane.getWidth()));
        mainPane.heightProperty().addListener((observableValue, number, t1) -> scrollPane.setMaxHeight(mainPane.getHeight()));

    }

    private void ShowPointDetails(Point point)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_POINT_DETAILS).openStream());
            PointDetailsController controller = (PointDetailsController) fxmlLoader.getController();
            controller.setPoint(point);
            controller.setGroups(masterWindowController.getGroups());
            controller.setPointDetails(masterWindowController.GetPointDetails(point));
            controller.setMasterWindowController(masterWindowController);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.showAndWait();

        }
        catch (IOException e)
        {

        }
    }

    public Canvas canvas;
    public CenterMenuButtonsController()
    {

    }

    public void ShowPoints(Point[] points, BuildingLevel level)
    {
        this.points = points;
        this.level = level;
        selectedPoint_1 = null;
        selectedPoint_2 = null;
        for(Point point : points)
        {
            if(point.IdImage != level.getIdImage())
                continue;
            Color color = Color.rgb(255,0,0,1.0);
            if((point.getIdPointType() & Constants.STAIRS_POINT_TYPE_MASK) != 0) //Stairs
                color = Color.rgb(0, 255, 0, 1.0);
            if((point.getIdPointType() & Constants.ELEVATOR_POINT_TYPE_MASK) != 0) //Elevator
                color = Color.rgb(0, 0, 255, 1.0);
            canvas.getGraphicsContext2D().setFill(color);
            canvas.getGraphicsContext2D().fillOval(point.getX() - 5, point.getY() - 5, 10, 10);
            canvas.getGraphicsContext2D().stroke();
        }
    }

    private Point FindPoint(int pointId)
    {
        for(Point point : points)
        {
            if(point.getIdPoint() == pointId)
            {
                return point;
            }
        }
        return null;
    }

    public void ShowPointsConnections(PointsConnection[] pointsConnections, Point[] points, BuildingLevel level) {
        for(PointsConnection connection : pointsConnections)
        {
            Point startPoint = FindPoint(connection.getIdPointStart());
            Point endPoint = FindPoint(connection.getIdPointEnd());
            if(startPoint == null || endPoint == null)
                continue;
            if(startPoint.getIdImage() != level.getIdImage())
                continue;
            if(endPoint.getIdImage() != level.getIdImage())
                continue;
            canvas.getGraphicsContext2D().setStroke(Color.rgb(255,0,0,1.0));
            canvas.getGraphicsContext2D().strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
            canvas.getGraphicsContext2D().stroke();
        }
    }
}

