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
import sample.Structs.*;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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

    Building[] buildings;
    BuildingLevel[] levels;
    Point[] points;
    Group[] groups;
    List<PointDetail>[] pointDetails;

    @FXML
    public void initialize (){
        menuBarController.setMasterWindowController(this);
        topMenuButtonsController.setMasterWindowController(this);
        leftMenuButtonsController.setMasterWindowController(this);
        centerMenuButtonsController.setMasterWindowController(this);
        bottomMenuButtonsController.setMasterWindowController(this);

        centerMenuButtonsController.canvas.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            RefreshBuilding();
        });

        topMenuButtonsController.buildingComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(image != null) {
                try{

                }
                catch (Exception e)
                {

                }
                centerMenuButtonsController.canvas.getGraphicsContext2D().drawImage(image,
                        0,
                        0);
            }

            int buildingId = buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding();
            points = WebServiceConnection.GetInstance().Points(buildingId);
            groups = WebServiceConnection.GetInstance().Groups(buildingId);

            if(points == null)
                return;
            pointDetails = new ArrayList[points.length];
            for(int i = 0; i < points.length; i++)
            {
                Point point = points[i];
                pointDetails[i] = WebServiceConnection.GetInstance().PointDetail(point.getIdPoint());
            }
        });
        LoadComponents();
        RefreshGUI();
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
            centerMenuButtonsController.mainPane.setPrefHeight(image.getHeight());
            centerMenuButtonsController.mainPane.setPrefWidth(image.getWidth());

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
    public void RefreshBuilding()
    {
        try {
            if(levels == null)
                return;
            for(BuildingLevel level : levels)
            {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(level.getPathImage());
                BufferedImage levelBufferedImage = ImageIO.read(byteArrayInputStream);
                Image levelImage = SwingFXUtils.toFXImage(levelBufferedImage, null);
                leftMenuButtonsController.flowPane.getChildren().add(new ImageView(levelImage));
                leftMenuButtonsController.flowPane.getChildren().add(new Label(Integer.toString(level.getBuildingLevel())));
            }
        }
        catch (IOException e)
        {

        }
    }

    public void setCenter(String fxmlPath) {

        masterWindow.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }
}
