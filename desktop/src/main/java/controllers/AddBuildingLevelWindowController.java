package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import sample.Structs.BuildingLevel;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AddBuildingLevelWindowController {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private Button addButton;
    @FXML
    private TextField imageFilePath;
    @FXML
    private Spinner buildingLevelSpinner;

    private int buildingId = -1;

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    @FXML
    public void initialize()
    {
        buildingLevelSpinner.getValueFactory().setValue(0);
        init();
    }

    @FXML
    public void BrowseImage(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageFilePath.setText(file.getAbsolutePath());
        }
    }

    public void AddBuildingLevel(ActionEvent actionEvent) {
        if(imageFilePath.getText() == null || imageFilePath.getText().length() == 0)
            return;
        BuildingLevel buildingLevel = new BuildingLevel();

        buildingLevel.setBuildingLevel((int)buildingLevelSpinner.getValue());
        buildingLevel.setIdBuilding(buildingId);
        buildingLevel.setNorthPointAngle(0);

        Integer buildingLevelId = WebServiceConnection.GetInstance().AddBuildingLevel(buildingLevel, buildingId, imageFilePath.getText());
        if(buildingLevelId != null)
        {
            try {
                buildingLevel.setIdBuilding(buildingLevelId);
                InputStream inputStream = new FileInputStream(imageFilePath.getText());
                buildingLevel.setPathImage(IOUtils.readFully(inputStream, inputStream.available()));
                masterWindowController.LevelAdded(buildingLevel);

                Stage stage = (Stage) imageFilePath.getScene().getWindow();
                stage.close();
            }
            catch (Exception e)
            {

            }
        }

    }

    //wygaszenie przycisku
    public void init(){
        addButton.disableProperty().bind(imageFilePath.textProperty().isEmpty());
    }

    private  MasterWindowController masterWindowController;
    public void setMasterWindowController(MasterWindowController masterWindowController)
    {
        this.masterWindowController = masterWindowController;
    }
}
