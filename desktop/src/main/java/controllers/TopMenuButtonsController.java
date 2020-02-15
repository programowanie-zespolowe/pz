package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.FxmlUtils;

public class TopMenuButtonsController {
    private MasterWindowController masterWindowController;
    public static final String OKNO_ADD_BUILDING_FXML = "/fxml/AddBuildingWindow.fxml";

    public TopMenuButtonsController()
    {
    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;

        buildingComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    masterWindowController.RefreshBuilding();
                }
        );
    }

    @FXML
    public ComboBox buildingComboBox;

    @FXML
    public void addBuilding(){
        Pane borderpane = FxmlUtils.fxmlLoader(OKNO_ADD_BUILDING_FXML);
        Stage stage = new Stage();
        Scene scene = new Scene (borderpane);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
