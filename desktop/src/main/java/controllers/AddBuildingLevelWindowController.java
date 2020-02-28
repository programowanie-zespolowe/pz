package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sample.Structs.BuildingLevel;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AddBuildingLevelWindowController {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField imageFilePath;
    @FXML
    private TextField levelName;
    @FXML
    private Spinner buildingLevelSpinner;

    private int buildingId = -1;

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
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
        if(levelName.getText() == null || levelName.getText().length() == 0)
            return;
        BuildingLevel buildingLevel = new BuildingLevel();

//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(new File(imageFilePath.getText()));
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", bos );
//            buildingLevel.setPathImage(bos.toByteArray());

//        } catch (IOException e) {
//        }
        buildingLevel.setBuildingLevel((int)buildingLevelSpinner.getValue());
        buildingLevel.setIdBuilding(buildingId);
        buildingLevel.setNorthPointAngle(0);
        buildingLevel.setScale(1);

        WebServiceConnection.GetInstance().AddBuildingLevel(buildingLevel, buildingId, imageFilePath.getText());
    }
}
