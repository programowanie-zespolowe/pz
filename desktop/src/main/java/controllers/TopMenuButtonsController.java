package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.FxmlUtils;

import java.io.IOException;

public class TopMenuButtonsController {
    private MasterWindowController masterWindowController;
    public static final String WINDOW_ADD_BUILDING_FXML = "/fxml/AddBuildingWindow.fxml";
    public static final String WINDOW_SHOW_GROUPS = "/fxml/groupsWindow.fxml";
    public static final String WINDOW_EDIT_BUILDING = "/fxml/editBuildingWindow.fxml";

    public TopMenuButtonsController()
    {
    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    @FXML
    public ComboBox buildingComboBox;

    @FXML
    Label scaleLabel;

    @FXML
    public void addBuilding(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_ADD_BUILDING_FXML).openStream());
            AddBuildingWindow controller = (AddBuildingWindow) fxmlLoader.getController();
            controller.setMasterWindowController(masterWindowController);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.building"));
            stage.showAndWait();
        }
        catch (Exception e)
        {

        }
    }

    public void SetScale(double currentScale)
    {
        scaleLabel.setText(Double.toString(currentScale));
    }

    @FXML
    public void showGroups()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_SHOW_GROUPS).openStream());
            GroupsWindowController controller = (GroupsWindowController) fxmlLoader.getController();
            controller.setGroups(masterWindowController.getGroups());
            controller.setIdBuilding(masterWindowController.GetCurrentBuildingId());
            controller.setMasterWindowController(masterWindowController);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.group"));
            stage.showAndWait();

        }
        catch (IOException e)
        {

        }
    }

    @FXML
    public void editBuilding()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_EDIT_BUILDING).openStream());
            EditBuildingController controller = (EditBuildingController) fxmlLoader.getController();
            controller.setMasterWindowController(masterWindowController);
            controller.setBuilding(masterWindowController.GetCurrentBuilding());
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.group"));
            stage.showAndWait();

        }
        catch (IOException e)
        {

        }
    }
}
