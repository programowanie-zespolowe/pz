package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import sample.Structs.BuildingLevel;
import sample.WebService.WebServiceConnection;

public class EditBuildingLevelController {
    @FXML
    private Spinner levelSpinner;

    private BuildingLevel level;
    private MasterWindowController masterWindowController;

    @FXML
    public void initialize() {
        levelSpinner.getValueFactory().setValue(0);
    }

    public void setLevel(BuildingLevel level)
    {
        this.level = level;
        levelSpinner.getValueFactory().setValue(level.getBuildingLevel());
    }

    public void Close(ActionEvent actionEvent) {
        Stage stage = (Stage) levelSpinner.getScene().getWindow();
        stage.close();
    }

    public void Save(ActionEvent actionEvent) {
        level.setBuildingLevel((int)levelSpinner.getValueFactory().getValue());
        if(WebServiceConnection.GetInstance().EditLevel(level))
        {
            masterWindowController.levelEddited(level);
        }


        Stage stage = (Stage) levelSpinner.getScene().getWindow();
        stage.close();
    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }
}
