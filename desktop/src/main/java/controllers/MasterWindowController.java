package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.ArrayUtils;
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
    private  MenuBarController menuBarController;

    @FXML
    private BottomMenuButtonsController bottomMenuButtonsController;

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

    @FXML
    public void initialize (){
        menuBarController.setMasterWindowController(this);
        topMenuButtonsController.setMasterWindowController(this);
        leftMenuButtonsController.setMasterWindowController(this);
        centerMenuButtonsController.setMasterWindowController(this);
        bottomMenuButtonsController.setMasterWindowController(this);

        topMenuButtonsController.buildingComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(image != null) {
                centerMenuButtonsController.canvas.getGraphicsContext2D().drawImage(image,
                        0,
                        0);
            }

            int buildingId = buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding();
            levels = WebServiceConnection.GetInstance().BuildingLevelList(buildingId);
            points = WebServiceConnection.GetInstance().Points(buildingId);
            groups = WebServiceConnection.GetInstance().Groups(buildingId);
            pointsConnections = WebServiceConnection.GetInstance().PointsConnections(buildingId);

            leftMenuButtonsController.RefreshLevels(levels);
            LoadPointDetails();
        });
        centerScrollPane.widthProperty().addListener((observableValue, number, t1) -> centerMenuButtonsController.mainPane.setPrefWidth(centerScrollPane.getWidth()));
        centerScrollPane.heightProperty().addListener((observableValue, number, t1) -> centerMenuButtonsController.mainPane.setPrefHeight(centerScrollPane.getHeight()));

        LoadComponents();
        RefreshGUI();
    }

    public void BuildingLevelChanged(BuildingLevel level)
    {
        try {
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
        topMenuButtonsController.SetScale(level.getScale());
    }

    public int GetCurrentBuildingId()
    {
        return buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding();
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
        centerMenuButtonsController.ShowPoints(points, leftMenuButtonsController.getCurrentBuildLevel());
    }

    public void PointAdded(Point point)
    {
        points = ArrayUtils.add(points, point);
        pointDetails = ArrayUtils.add(pointDetails, new ArrayList<PointDetail>());
        centerMenuButtonsController.ShowPoints(points, leftMenuButtonsController.getCurrentBuildLevel());
    }

    private void LoadComponents()
    {
       buildings = WebServiceConnection.GetInstance().BuildingList();
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
        levels = WebServiceConnection.GetInstance().BuildingLevelList(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding());

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getImageBuilding());
            BufferedImage buildingImage = null;
            buildingImage = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(buildingImage, null);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if(image != null) {
                                centerMenuButtonsController.canvas.getGraphicsContext2D().drawImage(image,
                                        0,
                                        0);
                            }
                        }
                    },
                    1000
            );
        } catch (Exception e) {
        }
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
                    if(pointDetails[i].get(j).getIdPointDetails() == pointDetail.getIdPoint())
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
}
