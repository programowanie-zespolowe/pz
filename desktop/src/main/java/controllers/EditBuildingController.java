package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Structs.Building;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EditBuildingController {
    private MasterWindowController masterWindowController;
    private Building building;

    @FXML
    private TextField imagePath;
    @FXML
    private TextField nameTextField;
    @FXML
    private Spinner spinnerScale;
    @FXML
    private ImageView imageView;

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    @FXML
    public void initialize() {
    }

    public void setBuilding(Building building) {
        this.building = building;
        nameTextField.setText(building.getNameBuilding());
        spinnerScale.getValueFactory().setValue(building.getScale());
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(building.getImageBuilding());
            BufferedImage buildingImage = null;
            buildingImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(buildingImage, null);
            imageView.setImage(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
        }
        catch (Exception e)
        {

        }

    }

    final FileChooser fileChooser = new FileChooser();
    public void Browse(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath.setText(file.getAbsolutePath());
            try {
                imageView.setImage(new Image(file.getAbsolutePath()));
            }
            catch (Exception e)
            {

            }
        }
    }

    public void Close(ActionEvent actionEvent) {
        Stage stage = (Stage) imagePath.getScene().getWindow();
        stage.close();
    }

    public void SaveAndClose(ActionEvent actionEvent) {
        Building building = new Building();
        building.setIdBuilding(this.building.getIdBuilding());
        building.setNameBuilding(nameTextField.getText());
        building.setScale((double)spinnerScale.getValueFactory().getValue());
        if(WebServiceConnection.GetInstance().EditBuilding(building, imagePath.getText()) == false)
            return;
        if(imagePath.getText() != "")
        {
            try {
                building.setImageBuilding(Files.readAllBytes(Paths.get(imagePath.getText())));
            }
            catch (Exception e)
            {

            }
        }
        masterWindowController.BuildingEdited(building);
        Stage stage = (Stage) imagePath.getScene().getWindow();
        stage.close();
    }

    public void DeleteAndClose(ActionEvent actionEvent) {
        if(WebServiceConnection.GetInstance().DeleteBuilding(building.getIdBuilding()) == false)
            return;
        masterWindowController.BuildingDeleted(building.getIdBuilding());
        Stage stage = (Stage) imagePath.getScene().getWindow();
        stage.close();
    }
}
