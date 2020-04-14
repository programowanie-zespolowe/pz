package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Structs.OutdoorGame;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class TopMenuButtonsController {
    private MasterWindowController masterWindowController;
    public static final String WINDOW_ADD_BUILDING_FXML = "/fxml/AddBuildingWindow.fxml";
    public static final String WINDOW_SHOW_GROUPS = "/fxml/groupsWindow.fxml";
    public static final String WINDOW_EDIT_BUILDING = "/fxml/editBuildingWindow.fxml";
    public static final String WINDOW_EDIT_GAME = "/fxml/editGame.fxml";

    public TopMenuButtonsController()
    {
    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    @FXML
    public void initialize () {
        outdoorGameTypeCombobox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            if(outdoorGameTypeCombobox.getSelectionModel().getSelectedIndex() == 0)
                editOutdoorGameButton.setDisable(true);
            else
                editOutdoorGameButton.setDisable(false);
        });
    }

    @FXML
    public ComboBox buildingComboBox;

    @FXML
    public Label scaleLabel;

    @FXML
    public ComboBox outdoorGameTypeCombobox;
    @FXML
    public Button addNewOutdoorGameButton;
    @FXML
    public Button editOutdoorGameButton;

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

    public void refreshGames(OutdoorGame[] games)
    {
        outdoorGameTypeCombobox.getItems().clear();
        outdoorGameTypeCombobox.getItems().add("Tryb normalny");

        for (OutdoorGame game:
             games) {
            outdoorGameTypeCombobox.getItems().add(game.getNameGame());
        }
        outdoorGameTypeCombobox.getSelectionModel().select(0);
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
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.edit.building"));
            stage.showAndWait();
        }
        catch (IOException e)
        {

        }
    }

    @FXML
    public void addNewGame(ActionEvent actionEvent) {
        OutdoorGame game = new OutdoorGame();
        game.setStartDateGame(new Date());
        game.setNameGame("Nowa gra");

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        game.setEndDateGame(dt);
        game.setIdBuilding(masterWindowController.GetCurrentBuildingId());

        game = WebServiceConnection.GetInstance().AddOutdoorGame(game, masterWindowController.GetCurrentBuildingId());
        if(game == null)
            return;

        masterWindowController.GameAdded(game);
        outdoorGameTypeCombobox.getItems().add(game.getNameGame());
        outdoorGameTypeCombobox.getSelectionModel().select(outdoorGameTypeCombobox.getItems().size() - 1);
        editGame(actionEvent);
    }

    @FXML
    public void editGame(ActionEvent actionEvent) {
        if(outdoorGameTypeCombobox.getSelectionModel().getSelectedIndex() == 0)
            return;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_EDIT_GAME).openStream());
            EditGameController controller = (EditGameController) fxmlLoader.getController();
            controller.setMasterWindowController(masterWindowController);
            controller.setGame(masterWindowController.outdoorGames[outdoorGameTypeCombobox.getSelectionModel().getSelectedIndex() - 1]);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (Exception e)
        {

        }
    }

    public void refreshCurrentGameName() {
        if(outdoorGameTypeCombobox.getSelectionModel().getSelectedIndex() == 0)
            return;

        outdoorGameTypeCombobox.getItems().set(outdoorGameTypeCombobox.getSelectionModel().getSelectedIndex(),
                masterWindowController.outdoorGames[outdoorGameTypeCombobox.getSelectionModel().getSelectedIndex() - 1].getNameGame());
    }
}
