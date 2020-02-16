package controllers;


import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class LeftMenuButtonsController  {
    @FXML
    public FlowPane flowPane;
    private MasterWindowController masterWindowController;

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }


}
