package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Constants;
import sample.Structs.*;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import java.io.IOException;

public class CenterMenuButtonsController {
    private static final String WINDOW_POINT_DETAILS = "/fxml/pointDetailsWindow.fxml";
    @FXML
    public Pane mainPane;
    @FXML
    public ScrollPane scrollPane;
    private MasterWindowController masterWindowController;

    private BuildingLevel level;
    private EditMode editMode = EditMode.EditPoints;

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;

    }

    Point points[];
    Point pressedPoint = null;
    boolean dragged = false;
    Point selectedPoint_1 = null;
    Point selectedPoint_2 = null;
    WritableImage copyImage = null;

    public Point FindPoint(double x, double y)
    {
        for(Point point : points)
        {
            if(point.getIdImage() != level.getIdImage())
                continue;
            if(point.getX() - 10 <= x && x <= point.getX() + 10 &&
                point.getY() - 10 <= y && y <= point.getY() + 10)
                return point;
        }
        return null;
    }

    @FXML
    public void initialize()
    {
        canvas = new Canvas();
        scrollPane.setContent(canvas);

        canvas.onMouseMovedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            if(editMode == EditMode.EditPoints) {
                if (t.getButton() == MouseButton.PRIMARY && pressedPoint != null) {
                    pressedPoint.setX(t.getX());
                    pressedPoint.setY(t.getY());
                    RedrawCenter();
                    return;
                }
                if (selectedPoint_1 == null || copyImage == null)
                    return;
                canvas.getGraphicsContext2D().drawImage(copyImage, 0, 0);
                canvas.getGraphicsContext2D().setStroke(Color.rgb(255, 0, 0, 1.0));
                canvas.getGraphicsContext2D().strokeLine(selectedPoint_1.getX(), selectedPoint_1.getY(), t.getX(), t.getY());
                canvas.getGraphicsContext2D().stroke();
            }
        });

        canvas.onMouseDraggedProperty().set(mouseEvent -> {
            if(editMode == EditMode.EditPoints) {
                if (pressedPoint == null)
                    return;
                pressedPoint.setX(mouseEvent.getX());
                pressedPoint.setY(mouseEvent.getY());
                RedrawCenter();
                dragged = true;
            }

        });

        canvas.onMousePressedProperty().set(mouseEvent -> {
            if(editMode == EditMode.EditPoints) {
                pressedPoint = FindPoint(mouseEvent.getX(), mouseEvent.getY());
            }
        });

        canvas.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent t) -> {
            if(editMode == EditMode.EditPoints) {
                if (dragged && pressedPoint != null) {
                    dragged = false;
                    WebServiceConnection.GetInstance().EditPoint(pressedPoint, masterWindowController.GetCurrentBuildingId(), masterWindowController.getCurrentLevel().getIdImage());
                    pressedPoint = null;
                    return;
                }
                if (level == null)
                    return;
                if (t.getButton() != MouseButton.PRIMARY) {
                    if (selectedPoint_1 != null) {
                        selectedPoint_1 = null;
                        canvas.getGraphicsContext2D().drawImage(copyImage, 0, 0);
                    }
                    return;
                }
                if (FindPoint(t.getX(), t.getY()) != null) {
                    if (t.getButton().equals(MouseButton.PRIMARY)) {
                        if (t.getClickCount() == 2) {
                            selectedPoint_1 = null;
                            selectedPoint_2 = null;
                            ShowPointDetails(FindPoint(t.getX(), t.getY()));
                        } else {
                            if (selectedPoint_1 == null) {
                                selectedPoint_1 = FindPoint(t.getX(), t.getY());
                                SnapshotParameters params = new SnapshotParameters();
                                copyImage = canvas.snapshot(params, null);
                            } else if (selectedPoint_2 == null) {
                                selectedPoint_2 = FindPoint(t.getX(), t.getY());
                                if (selectedPoint_2 == selectedPoint_1) {
                                    selectedPoint_2 = null;
                                    selectedPoint_1 = null;
                                    return;
                                }
                                PointsConnection connection = masterWindowController.GetPointsConnection(selectedPoint_1, selectedPoint_2);
                                if (connection != null) {
                                    WebServiceConnection.GetInstance().RemovePointConnection(connection);
                                    masterWindowController.ConnectionRemoved(connection);
                                    RedrawCenter();
                                } else {
                                    PointsConnection newConnection = WebServiceConnection.GetInstance().AddPointConnection(selectedPoint_1, selectedPoint_2);
                                    if (newConnection != null)
                                        masterWindowController.ConnectionAdded(newConnection);
                                    RedrawCenter();
                                }
                                selectedPoint_1 = null;
                                selectedPoint_2 = null;
                            }
                        }
                    }
                    return;
                }
                Point point = new Point();
                point.setX((int) t.getX());
                point.setY((int) t.getY());
                point.setIdImage(level.getIdImage());
                point.setDirection(0);
                point.setOnOffDirection(false);

                Point addedPoint = WebServiceConnection.GetInstance().AddPoint(point, level.getIdImage());
                if (addedPoint != null)
                    masterWindowController.PointAdded(addedPoint);
            }
            else if (editMode == EditMode.EditGame)
            {
                Point point = FindPoint(t.getX(), t.getY());
                if (point != null) {
                    Integer number = masterWindowController.addOutdoorGamePoint(point);
                    drawText(String.valueOf(number), point.getX() + 5, point.getY() - 5);
                }
            }
            else if (editMode == EditMode.EditGameHintPoint)
            {
                if (t.getButton() != MouseButton.PRIMARY) {
                    editMode = EditMode.EditGame;
                    canvas.setCursor(Cursor.DEFAULT);
                    return;
                }
                Point point = FindPoint(t.getX(), t.getY());
                if (point != null) {
                    editMode = EditMode.EditGame;
                    editGameController.hintSelected(point);
                    canvas.setCursor(Cursor.DEFAULT);
                }
            }
        });

        mainPane.widthProperty().addListener((observableValue, number, t1) -> scrollPane.setMaxWidth(mainPane.getWidth()));
        mainPane.heightProperty().addListener((observableValue, number, t1) -> scrollPane.setMaxHeight(mainPane.getHeight()));

    }

    private void drawText(String text, double X, double Y) {
        canvas.getGraphicsContext2D().setFont(new Font(20));
        canvas.getGraphicsContext2D().setStroke(Color.rgb(0,0,0,1.0));
        canvas.getGraphicsContext2D().fillText(text, X, Y);
        canvas.getGraphicsContext2D().stroke();
    }

    private void RedrawCenter() {
        masterWindowController.BuildingLevelChanged(masterWindowController.getCurrentLevel());
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
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/comboBox.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scrollPane.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.point.detail"));
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

    public void SetEditMode(EditMode editMode)
    {
        this.editMode = editMode;
    }

    public void ShowGame(OutdoorGame outdoorGame,
                         OutdoorGamePath[] outdoorGamePoints,
                         OutdoorGameHints[] outdoorGameHints,
                         Point[] points,
                         int currentLevelId) {
        int number = 1;
        Integer idNextPoint = outdoorGame.getIdFirstPoint();
        while(idNextPoint != null && idNextPoint != -1)
        {
            OutdoorGamePath gamePoint = getOutdoorGamePath(outdoorGamePoints, idNextPoint);
            Point point = getPoint(points, gamePoint.getIdPoint());
            if(point.getIdImage() == currentLevelId)
            {
                drawText(String.valueOf(number), point.getX() + 5, point.getY() - 5);
            }
            if(gamePoint.getIdHintPoint() != null)
            {
                OutdoorGameHints gameHint = getOutdoorGameHint(outdoorGameHints, gamePoint.getIdHintPoint());
                Point hintPoint = getPoint(points, gameHint.getIdPoint());
                if(hintPoint.getIdImage() == currentLevelId)
                {
                    drawText(String.valueOf(number) + "H", hintPoint.getX() + 5, hintPoint.getY() - 5);
                }
            }
            number++;
            idNextPoint = gamePoint.getIdNextPoint();
        }
    }

    private OutdoorGameHints getOutdoorGameHint(OutdoorGameHints[] outdoorGameHints, Integer idHintPoint) {
        for (OutdoorGameHints hint :
                outdoorGameHints) {
            if (hint.getIdHints()  != idHintPoint)
                continue;
            return hint;
        }
        return null;
    }

    private  OutdoorGamePath getOutdoorGamePath(OutdoorGamePath[] outdoorGamePoints, int id)
    {
        for (OutdoorGamePath point :
                outdoorGamePoints) {
            if (point.getIdQuestionPoint()  != id)
                continue;
            return point;
        }
        return null;
    }

    private Point getPoint(Point[] points, int id)
    {
        for (Point point :
                points) {
            if (point.getIdPoint() != id)
                continue;
            return point;
        }
        return null;
    }

    EditGameController editGameController = null;
    public void selectHintPoint(EditGameController editGameController) {
        canvas.setCursor(Cursor.CROSSHAIR);
        this.editMode = EditMode.EditGameHintPoint;
        this.editGameController = editGameController;
    }

    public enum EditMode
    {
        EditPoints,
        EditGame,
        EditGameHintPoint
    }
}

