package controllers;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.ArrayUtils;
import sample.Constants;
import sample.Structs.*;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MasterWindowController {
    @FXML
    public Pane centerMenuButtons;
    @FXML
    private BorderPane masterWindow;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;

    @FXML
    private  LeftMenuButtonsController leftMenuButtonsController;

    @FXML
    private CenterMenuButtonsController centerMenuButtonsController;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private Pane centerScrollPane;

    Building[] buildings;
    BuildingLevel[] levels;
    Point[] points;
    Group[] groups;
    List<PointDetail>[] pointDetails;
    PointsConnection[] pointsConnections;
    private BuildingLevel currentLevel;

    public BuildingLevel getCurrentLevel() {
        return currentLevel;
    }
    private static final String LOADING_FXML = "/fxml/loadingDialog.fxml";
    private static final String WINDOW_POINT_DETAILS = "/fxml/pointDetailsWindow.fxml";

    Stage loadingStage = null;
    int buildingId;

    @FXML
    public void initialize (){
        topMenuButtonsController.setMasterWindowController(this);
        leftMenuButtonsController.setMasterWindowController(this);
        centerMenuButtonsController.setMasterWindowController(this);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(LOADING_FXML).openStream());
            loadingStage = new Stage();
            loadingStage.setAlwaysOnTop(true);
            loadingStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(p);
            loadingStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
        }
        catch (Exception e1)
        {

        }


        topMenuButtonsController.buildingComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            centerMenuButtonsController.canvas.getGraphicsContext2D().clearRect(0, 0, centerMenuButtonsController.canvas.getWidth(), centerMenuButtonsController.canvas.getHeight());
            buildingId = buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding();
            topMenuButtonsController.SetScale(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getScale());

            Task task = new Task<Boolean>() {
                @Override protected Boolean call() throws Exception {
                    try {
                        levels = WebServiceConnection.GetInstance().BuildingLevelList(buildingId);
                        points = WebServiceConnection.GetInstance().Points(buildingId);
                        groups = WebServiceConnection.GetInstance().Groups(buildingId);
                        pointsConnections = WebServiceConnection.GetInstance().PointsConnections(buildingId);
                        LoadPointDetails();
                    }
                    catch (Exception e)
                    {
                        return false;
                    }
                    return true;
                }
            };

            task.setOnRunning((e) -> loadingStage.show() );
            task.setOnSucceeded((e) -> {
                leftMenuButtonsController.RefreshLevels(levels);
                centerMenuButtonsController.ShowPoints(points, leftMenuButtonsController.getCurrentBuildLevel());
                loadingStage.hide();
            });
            new Thread(task).start();
        });
        centerScrollPane.widthProperty().addListener((observableValue, number, t1) -> centerMenuButtonsController.mainPane.setPrefWidth(centerScrollPane.getWidth()));
        centerScrollPane.heightProperty().addListener((observableValue, number, t1) -> centerMenuButtonsController.mainPane.setPrefHeight(centerScrollPane.getHeight()));

        LoadComponents();
    }

    public void BuildingLevelChanged(BuildingLevel level)
    {
        try {
            currentLevel = level;
            ByteArrayInputStream bis = new ByteArrayInputStream(level.getPathImage());
            BufferedImage buildingImage = null;
            buildingImage = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(buildingImage, null);
            centerMenuButtonsController.canvas.setWidth(image.getRequestedWidth());
            centerMenuButtonsController.canvas.setHeight(image.getRequestedHeight());
            centerMenuButtonsController.canvas.getGraphicsContext2D().drawImage(image,
                    0,
                    0,
                    image.getRequestedWidth(),
                    image.getRequestedHeight());

            centerMenuButtonsController.ShowPoints(points, level);
            centerMenuButtonsController.ShowPointsConnections(pointsConnections, points, level);
        }
        catch (IOException exception)
        {
            centerMenuButtonsController.canvas.getGraphicsContext2D().clearRect(0,
                    0,
                    1000,
                    1000);

        }
    }

    public int GetCurrentBuildingId()
    {
        return buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding();
    }
    public Building GetCurrentBuilding() {
        return buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()];
    }

    private void LoadPointDetails() {
        if(points == null)
            return;
        pointDetails = new ArrayList[points.length];
        for(int i = 0; i < points.length; i++)
        {
            Point point = points[i];
            pointDetails[i] = WebServiceConnection.GetInstance().PointDetail(point.getIdPoint());
        }
    }

    public void PointAdded(Point point)
    {
        points = ArrayUtils.add(points, point);
        pointDetails = ArrayUtils.add(pointDetails, new ArrayList<PointDetail>());
        centerMenuButtonsController.ShowPoints(points, leftMenuButtonsController.getCurrentBuildLevel());
    }

    private void LoadComponents()
    {
        Task task = new Task<Boolean>() {
        @Override protected Boolean call() throws Exception {
            try {
                buildings = WebServiceConnection.GetInstance().BuildingList();
            }
            catch (Exception e)
            {
                return false;
            }
            return true;
        }
        };

        task.setOnRunning((e) -> loadingStage.show() );
        task.setOnSucceeded((e) -> {
            RefreshGUI();
            loadingStage.hide();
        });
        new Thread(task).start();
    }

    private void RefreshGUI()
    {
        topMenuButtonsController.buildingComboBox.getItems().clear();

        if(buildings == null)
            return;
        for (int i = 0; i < buildings.length; i++) {
            Building building = buildings[i];
            topMenuButtonsController.buildingComboBox.getItems().add(Integer.toString(i + 1) + ": " + building.getNameBuilding());
        }
        topMenuButtonsController.buildingComboBox.getSelectionModel().select(0);
        Task task = new Task<Boolean>() {
            @Override protected Boolean call() throws Exception {
                try {
                    levels = WebServiceConnection.GetInstance().BuildingLevelList(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding());
                    LoadPointDetails();
                }
                catch (Exception e)
                {
                    return false;
                }
                return true;
            }
        };

        task.setOnRunning((e) -> loadingStage.show() );
        task.setOnSucceeded((e) -> {
            loadingStage.hide();
        });
        new Thread(task).start();
    }

    Image image = null;

    public void setCenter(String fxmlPath) {

        masterWindow.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }

    public void LevelRemoved(int idImage) {
        for(int i = 0; i < levels.length; i++)
        {
            if(levels[i].getIdImage() == idImage) {
                levels = ArrayUtils.remove(levels, i);
                break;
            }
        }
        leftMenuButtonsController.RefreshLevels(levels);
    }

    public void BuildingAdded(Building building) {
        buildings = ArrayUtils.add(buildings, building);
        RefreshGUI();
    }

    public void LevelAdded(BuildingLevel buildingLevel) {
        levels = ArrayUtils.add(levels, buildingLevel);
        leftMenuButtonsController.RefreshLevels(levels);
    }

    public void ConnectionAdded(PointsConnection connection) {
        pointsConnections = ArrayUtils.add(pointsConnections, connection);
        centerMenuButtonsController.ShowPointsConnections(pointsConnections, points, leftMenuButtonsController.getCurrentBuildLevel());
    }

    public Group[] getGroups() {
        return groups;
    }

    public void groupAdded(Group group) {
        groups = ArrayUtils.add(groups, group);
    }

    public void GroupRemoved(int idGroup) {
        for(int i = 0; i < groups.length; i++)
        {
            if(groups[i].getIdGroup() == idGroup) {
                groups = ArrayUtils.remove(groups, i);
                break;
            }
        }
    }

    public List<PointDetail> GetPointDetails(Point point) {
        for(int i = 0; i < points.length; i++)
        {
            if(points[i].getIdPoint() == point.getIdPoint()) {
                List<PointDetail> list = pointDetails[i];
                return list;
            }
        }
        return null;
    }

    public void PointDetailsAdded(PointDetail pointDetail) {
        for(int i = 0; i < points.length; i++)
        {
            if(pointDetail.getIdPoint() == points[i].getIdPoint())
            {
                pointDetails[i].add(pointDetail);
                break;
            }
        }
    }

    public void PointDetailsRemoved(Point point, PointDetail pointDetail) {
        for(int i = 0; i < points.length; i++)
        {
            if(point.getIdPoint() == points[i].getIdPoint())
            {
                for(PointDetail detail : pointDetails[i])
                {
                    if(detail.getIdPointDetails() == pointDetail.getIdPointDetails()) {
                        pointDetails[i].remove(detail);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void PointRemoved(Point point) {
        for(int i = 0; i < points.length; i++)
        {
            if(point.getIdPoint() == points[i].getIdPoint())
            {
                pointDetails = ArrayUtils.remove(pointDetails, i);
                points = ArrayUtils.remove(points, i);
                BuildingLevelChanged(leftMenuButtonsController.getCurrentBuildLevel());
            }
        }
    }

    public void GroupEditted(Group group) {
        for(int i = 0; i < groups.length; i++)
        {
            if(groups[i].getIdGroup() == group.getIdGroup())
            {
                groups[i] = group;
                return;
            }
        }
    }

    public void PointDetailsEditted(PointDetail pointDetail, Point point) {
        for(int i = 0; i < points.length; i++)
        {
            if(points[i].getIdPoint() == point.getIdPoint())
            {
                for(int j = 0; j < pointDetails[i].size(); j++)
                {
                    if(pointDetails[i].get(j).getIdPointDetails() == pointDetail.getIdPointDetails())
                    {
                        pointDetails[i].get(j).setImagePoint(pointDetail.getImagePoint());
                        pointDetails[i].get(j).setIdGroup(pointDetail.getIdGroup());
                        pointDetails[i].get(j).setNamePoint(pointDetail.getNamePoint());
                    }
                }
                return;
            }
        }
    }

    private Point AddPoint(double x, double y, int pointType, double direction, boolean direcitonOn, int levelNum)
    {
        int levelId = -1;
        for(BuildingLevel level : levels)
        {
            if(levelNum == level.getBuildingLevel())
            {
                levelId = level.getIdImage();
                break;
            }
        }
        if(levelId == -1)
            return null;

        Point otherPoint = new Point();
        otherPoint.setIdPointType(pointType);
        otherPoint.setX(x);
        otherPoint.setY(y);
        otherPoint.setDirection(direction);
        otherPoint.setOnOffDirection(direcitonOn);
        otherPoint.setIdImage(levelId);
        otherPoint = WebServiceConnection.GetInstance().AddPoint(otherPoint, levelId);

        if(otherPoint != null)
            PointAdded(otherPoint);
        return otherPoint;
    }

    public boolean AddElevatorStairs(Point point, int floorsDown, int floorsUp, double direction, boolean directionOn, int pointType)
    {
        List<Point> points = new ArrayList<>();
        points.add(point);
        for(int i = 1; i <= floorsDown; i++)
        {
            Point addedPoint = AddPoint(point.getX(), point.getY(), pointType, direction, directionOn, getCurrentLevel().getBuildingLevel() - i);
            if(addedPoint != null)
                points.add(addedPoint);
        }
        for(int i = 1; i <= floorsUp; i++)
        {
            Point addedPoint = AddPoint(point.getX(), point.getY(), pointType, direction, directionOn, getCurrentLevel().getBuildingLevel() + i);
            if(addedPoint != null)
                points.add(addedPoint);
        }
        for(int i = 0; i < points.size(); i++)
        {
            for(int j = i + 1; j < points.size(); j++)
            {
                if(i == j)
                    continue;
                PointsConnection connection = WebServiceConnection.GetInstance().AddPointConnection(points.get(i), points.get(j));
                if(connection == null)
                    return false;
                ConnectionAdded(connection);
            }
        }
        return true;
    }

    public boolean ArePointsConnected(Point point1, Point point2)
    {
        for(PointsConnection connection : pointsConnections)
        {
            if(connection.getIdPointStart() == point1.getIdPoint() && connection.getIdPointEnd() == point2.getIdPoint())
                return true;
            if(connection.getIdPointStart() == point2.getIdPoint() && connection.getIdPointEnd() == point1.getIdPoint())
                return true;
        }
        return false;
    }
    public PointsConnection GetPointsConnection(Point point1, Point point2)
    {
        for(PointsConnection connection : pointsConnections)
        {
            if(connection.getIdPointStart() == point1.getIdPoint() && connection.getIdPointEnd() == point2.getIdPoint())
                return connection;
            if(connection.getIdPointStart() == point2.getIdPoint() && connection.getIdPointEnd() == point1.getIdPoint())
                return connection;
        }
        return null;
    }

    public void RemoveElevatorStairs(Point point, int pointType)
    {
        for(Point p : points)
        {
            if(p == point)
                continue;
            if((p.getIdPointType() & pointType) == 0)
                continue;
            if(p.getX() != point.getX())
                continue;
            if(p.getY() != point.getY())
                continue;
            if(!ArePointsConnected(p, point))
                continue;
            if(WebServiceConnection.GetInstance().RemovePoint(p))
            {
                PointRemoved(p);
            }
        }
        BuildingLevelChanged(getCurrentLevel());
    }

    public void ConnectionRemoved(PointsConnection connection) {
        for(int i = 0; i < pointsConnections.length; i++)
        {
            if(pointsConnections[i] == connection)
            {
                pointsConnections = ArrayUtils.remove(pointsConnections, i);
                break;
            }
        }
        BuildingLevelChanged(getCurrentLevel());
    }

    public void BuildingEdited(Building building) {
        int num = 0;
        for(int i = 0; i < buildings.length; i++)
        {
            if(buildings[i].getIdBuilding() == building.getIdBuilding())
            {
                num = i;
                buildings[i].setScale(building.getScale());
                buildings[i].setNameBuilding(building.getNameBuilding());
                if(building.getImageBuilding() != null)
                    buildings[i].setImageBuilding(building.getImageBuilding());
                break;
            }
        }
        topMenuButtonsController.SetScale(building.getScale());
        int currentIndex = topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex();
        topMenuButtonsController.buildingComboBox.getItems().set(num, Integer.toString(num + 1) + ": " + building.getNameBuilding());
    }

    public void BuildingDeleted(int idBuilding) {
        for(int i = 0; i < buildings.length; i++)
        {
            if(buildings[i].getIdBuilding() == idBuilding)
            {
                buildings = ArrayUtils.remove(buildings, i);
                break;
            }
        }
        RefreshGUI();
    }

    public void levelEddited(BuildingLevel level) {
        for(int i = 0; i < levels.length; i++)
        {
            if(levels[i].getIdImage() == level.getIdImage())
            {
                levels[i].setBuildingLevel(level.getBuildingLevel());
                break;
            }
        }
        BuildingLevel currentLevel = getCurrentLevel();
        leftMenuButtonsController.RefreshLevels(levels);
        leftMenuButtonsController.SetSelectedColor(currentLevel);
    }
}
