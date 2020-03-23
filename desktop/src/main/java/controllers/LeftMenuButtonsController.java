package controllers;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Structs.BuildingLevel;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LeftMenuButtonsController  {
    private static final String WINDOW_EDIT_LEVEL = "/fxml/editBuildingLevelWindow.fxml";
    @FXML
    public VBox flowPane;
    private MasterWindowController masterWindowController;
    private BuildingLevel currentBuildLevel;
    public static final String WINDOW_ADD_BUILDING_LEVEL_FXML = "/fxml/AddBuildingLevelWindow.fxml";

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    public BuildingLevel getCurrentBuildLevel() {
        return currentBuildLevel;
    }

    final ArrayList<LeftMenuObject> leftMenuObjects = new ArrayList<>();

    public void RefreshLevels(BuildingLevel[] levels)
    {
        flowPane.getChildren().clear();
        leftMenuObjects.clear();
        if(levels == null)
            return;
        for (BuildingLevel level :
                levels) {
            LeftMenuObject menuObject = new LeftMenuObject();
            menuObject.level = level;
            MenuItem removeMenuItem = new MenuItem("Remove");
            MenuItem editMenuItem = new MenuItem("Edit");
            removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(WebServiceConnection.GetInstance().RemoveBuildingLevel(level.getIdBuilding(), level.getIdImage()))
                    {
                        masterWindowController.LevelRemoved(level.getIdImage());
                    }
                }
            });
            editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    ShowEditLevel(level);
                }
            });
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().add(editMenuItem);
            contextMenu.getItems().add(removeMenuItem);

            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(level.getPathImage());
                BufferedImage buildingImage = null;
                buildingImage = ImageIO.read(bis);
                Image image = SwingFXUtils.toFXImage(buildingImage, null);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                flowPane.setMaxWidth(100);
                flowPane.getChildren().add(imageView);
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        SetSelectedColor(level);
                        event.consume();
                    }
                });
                imageView.setOnContextMenuRequested(e ->
                        contextMenu.show(imageView, e.getScreenX(), e.getScreenY())
                );
                menuObject.imageView = imageView;
            }
            catch (Exception e)
            {

            }
            Label label = new Label();
            label.setContextMenu(contextMenu);
            label.setText("Poziom: " + level.getBuildingLevel());
            label.setAlignment(Pos.CENTER);
            flowPane.getChildren().add(label);

            label.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    SetSelectedColor(level);
                    label.setTextFill(Color.rgb(255, 0, 0, 1));
                    event.consume();
                }
            });
            menuObject.label = label;
            leftMenuObjects.add(menuObject);
        }
        if(leftMenuObjects.size() > 0)
            SetSelectedColor(leftMenuObjects.get(0).level);
    }

    private void ShowEditLevel(BuildingLevel level) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_EDIT_LEVEL).openStream());
            EditBuildingLevelController controller = (EditBuildingLevelController) fxmlLoader.getController();
            controller.setMasterWindowController(masterWindowController);
            controller.setLevel(level);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.group"));
            stage.showAndWait();
        }
        catch (Exception e)
        {

        }
    }

    void SetSelectedColor(BuildingLevel level)
    {
        currentBuildLevel = level;
        for (LeftMenuObject menuObject : leftMenuObjects)
        {
            if(menuObject.level == level)
                menuObject.label.setTextFill(Color.RED);
            else
                menuObject.label.setTextFill(Color.BLACK);
        }
        masterWindowController.BuildingLevelChanged(level);
    }

    @FXML
    public void addBuildingLevel(ActionEvent actionEvent)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_ADD_BUILDING_LEVEL_FXML).openStream());
            AddBuildingLevelWindowController controller = (AddBuildingLevelWindowController) fxmlLoader.getController();
            controller.setBuildingId(masterWindowController.GetCurrentBuildingId());
            controller.setMasterWindowController(masterWindowController);
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            scene.getStylesheets().add(getClass().getResource("/stylesheets/confirm.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(FxmlUtils.getResourceBundle().getString("title.window.level"));
            stage.showAndWait();

        }
        catch (IOException e)
        {

        }
    }

    protected class LeftMenuObject{
        public BuildingLevel level;
        public Label label;
        public ImageView imageView;
    }
}
