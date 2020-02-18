package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Structs.Building;
import sample.Structs.BuildingLevel;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

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

    Building buildings[];
    BuildingLevel levels[];

    @FXML
    public void initialize (){
        menuBarController.setMasterWindowController(this);
        topMenuButtonsController.setMasterWindowController(this);
        leftMenuButtonsController.setMasterWindowController(this);
        centerMenuButtonsController.setMasterWindowController(this);
        bottomMenuButtonsController.setMasterWindowController(this);

        LoadComponents();
        RefreshGUI();
        centerMenuButtonsController.canvas.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            RefreshBuilding();
        });
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
        for (Building building : buildings) {
            topMenuButtonsController.buildingComboBox.getItems().add(building.getNameBuilding());
        }
        topMenuButtonsController.buildingComboBox.getSelectionModel().select(0);
        levels = WebServiceConnection.GetInstance().BuildingLevelList(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getIdBuilding());
        ByteArrayInputStream bis = new ByteArrayInputStream(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getImageBuilding());
        BufferedImage buildingImage = null;
        try {
            buildingImage = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(buildingImage, null);
            centerMenuButtonsController.mainPane.setPrefHeight(image.getHeight());
            centerMenuButtonsController.mainPane.setPrefWidth(image.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Image image = null;
    public void RefreshBuilding()
    {
        try {
            if(image != null) {
                centerMenuButtonsController.canvas.getGraphicsContext2D().drawImage(image,
                        0,
                        0);
            }
            centerMenuButtonsController.canvas.getGraphicsContext2D().setLineWidth(1);
            centerMenuButtonsController.canvas.getGraphicsContext2D().moveTo(0, 0);
            centerMenuButtonsController.canvas.getGraphicsContext2D().lineTo(
                    centerMenuButtonsController.canvas.getWidth(),
                    centerMenuButtonsController.canvas.getHeight());
            centerMenuButtonsController.canvas.getGraphicsContext2D().stroke();

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
