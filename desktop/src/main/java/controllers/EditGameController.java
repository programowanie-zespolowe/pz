package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Structs.Building;
import sample.Structs.OutdoorGame;
import sample.WebService.WebServiceConnection;
import tornadofx.control.DateTimePicker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EditGameController {
    private OutdoorGame game;
    private MasterWindowController masterWindowController;

    @FXML
    private TextField nameTextField;
    @FXML
    private DateTimePicker startDatePicker;
    @FXML
    private DateTimePicker endDatePicker;
    @FXML
    private ImageView gameImage;
    @FXML
    private Label noImageLabel;

    @FXML
    public void initialize()
    {

    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    public void setGame(OutdoorGame game) {
        this.game = game;

        refreshGUI();
    }

    private void refreshGUI() {
        nameTextField.setText(game.getNameGame());
        startDatePicker.setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(game.getStartDateGame().getTime()), ZoneId.systemDefault()).toLocalDate());
        endDatePicker.setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(game.getEndDateGame().getTime()), ZoneId.systemDefault()).toLocalDate());
        if(game.getImageGame() == null)
        {
            gameImage.setVisible(false);
            noImageLabel.setVisible(true);
        }
        else
        {
            SetImage();
        }
    }

    private void SetImage() {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(game.getImageGame());
            BufferedImage buildingImage = null;
            buildingImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(buildingImage, null);
            gameImage.setImage(image);
            gameImage.setFitWidth(200);
            gameImage.setFitHeight(200);
            gameImage.setPreserveRatio(true);
        }
        catch (Exception e)
        {

        }
    }

    final FileChooser fileChooser = new FileChooser();
    private String imagePath = "";
    public void pickImage(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            try {
                gameImage.setImage(new Image("file:///" + file.getAbsolutePath()));
                SetImage();
                noImageLabel.setVisible(false);
                gameImage.setVisible(true);
            }
            catch (Exception e)
            {
                e.toString();
            }
        }
    }

    public void saveGame(ActionEvent actionEvent) {
        game.setNameGame(nameTextField.getText());
        game.setStartDateGame(Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        game.setEndDateGame(Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        try {
            if (imagePath != "")
                game.setImageGame(Files.readAllBytes(Paths.get(imagePath)));
        }
        catch (Exception e)
        {

        }

        WebServiceConnection.GetInstance().EditOutdoorGame(game, imagePath);
        masterWindowController.outdoorGameEdited(game);

        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

    public void deleteGame(ActionEvent actionEvent) {
        if(WebServiceConnection.GetInstance().DeleteOutdoorGame(game.getIdOutdoorGame()))
            masterWindowController.OutdoorGameDeleted(game.getIdOutdoorGame());

        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }
}
