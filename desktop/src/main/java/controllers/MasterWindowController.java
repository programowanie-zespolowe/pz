package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;
import sample.Structs.*;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
            GetPointDetails();
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

    private void GetPointDetails() {
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
//            centerMenuButtonsController.mainPane.setPrefHeight(image.getHeight());
//            centerMenuButtonsController.mainPane.setPrefWidth(image.getWidth());

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
            e.printStackTrace();
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
}
