package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import sample.Structs.Building;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MasterWindowController {
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

    Building buildings[];

    @FXML
    private void initialize (){
        menuBarController.setMasterWindowController(this);
        topMenuButtonsController.setMasterWindowController(this);
        leftMenuButtonsController.setMasterWindowController(this);
        centerMenuButtonsController.setMasterWindowController(this);
        bottomMenuButtonsController.setMasterWindowController(this);

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
        for (Building building : buildings) {
            topMenuButtonsController.buildingComboBox.getItems().add(building.getNameBuilding());
        }
        topMenuButtonsController.buildingComboBox.getSelectionModel().select(0);
    }

    public void RefreshBuilding()
    {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(buildings[topMenuButtonsController.buildingComboBox.getSelectionModel().getSelectedIndex()].getImageBuilding());
            BufferedImage buildingImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(buildingImage, null);
            centerMenuButtonsController.canvas.getGraphicsContext2D().drawImage(image,
                    centerMenuButtonsController.canvas.getWidth(),
                    centerMenuButtonsController.canvas.getHeight());
        }
        catch (IOException e)
        {

        }
    }

    public void setCenter(String fxmlPath) {

        masterWindow.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }
}
