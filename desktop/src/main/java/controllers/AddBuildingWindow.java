package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import sample.Structs.Building;
import sample.Structs.BuildingLevel;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AddBuildingWindow {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField BuildingNameTextField;
    @FXML
    private TextField imageFilePath;
    @FXML
    private ImageView imageBuilding;
    @FXML
    private Label labelNoImage;

    @FXML
    public void BrowseImage(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageFilePath.setText(file.getAbsolutePath());
            imageFilePath.setVisible(true);
            try {
                BufferedImage img = ImageIO.read(new File(file.getAbsolutePath()));
                Image image = SwingFXUtils.toFXImage(img, null);
                imageBuilding.setImage(image);
                imageBuilding.setFitWidth(100);
                imageBuilding.setFitHeight(100);
                imageBuilding.setPreserveRatio(true);
                imageBuilding.setVisible(true);

                labelNoImage.setVisible(false);
            }
            catch (Exception e)
            {

            }
        }
    }

    @FXML
    public void AddBuilding(ActionEvent actionEvent) {
        if(imageFilePath.getText() == null || imageFilePath.getText().length() == 0)
            return;
        if(BuildingNameTextField.getText() == null || BuildingNameTextField.getText().length() == 0)
            return;

        Integer id = WebServiceConnection.GetInstance().AddBuilding(BuildingNameTextField.getText(), imageFilePath.getText());
        if(id != null)
        {
            try {
                Building building = new Building();
                building.setIdBuilding(id);
                building.setNameBuilding(BuildingNameTextField.getText());

                InputStream inputStream = new FileInputStream(imageFilePath.getText());
                building.setImageBuilding(IOUtils.readFully(inputStream, inputStream.available()));

                masterWindowController.BuildingAdded(building);

                Stage stage = (Stage) BuildingNameTextField.getScene().getWindow();
                stage.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    private MasterWindowController masterWindowController;
    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }
}
