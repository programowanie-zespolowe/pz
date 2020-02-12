package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

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

    @FXML
    private void initialize (){
        menuBarController.setMasterWindowController(this);
        topMenuButtonsController.setMasterWindowController(this);
        leftMenuButtonsController.setMasterWindowController(this);
        centerMenuButtonsController.setMasterWindowController(this);
        bottomMenuButtonsController.setMasterWindowController(this);

        LoadComponents();
    }

    public void LoadComponents()
    {
        WebServiceConnection.GetInstance().BuildingList();
    }

    public void setCenter(String fxmlPath) {

        masterWindow.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }
}
