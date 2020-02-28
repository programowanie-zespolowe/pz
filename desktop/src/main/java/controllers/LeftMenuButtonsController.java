package controllers;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Structs.BuildingLevel;
import utils.FxmlUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LeftMenuButtonsController  {
    @FXML
    public FlowPane flowPane;
    private MasterWindowController masterWindowController;
    public static final String WINDOW_ADD_BUILDING_LEVEL_FXML = "/fxml/AddBuildingLevelWindow.fxml";

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
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
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(level.getPathImage());
                BufferedImage buildingImage = null;
                buildingImage = ImageIO.read(bis);
                Image image = SwingFXUtils.toFXImage(buildingImage, null);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                flowPane.getChildren().add(imageView);
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        SetSelectedColor(level);
                        event.consume();
                    }
                });
                menuObject.imageView = imageView;
            }
            catch (Exception e)
            {

            }
            Label label = new Label();
            label.setText("Poziom: " + level.getBuildingLevel());
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

    void SetSelectedColor(BuildingLevel level)
    {
        for (LeftMenuObject menuObject : leftMenuObjects)
        {
            if(menuObject.level == level)
                menuObject.label.setTextFill(Color.RED);
            else
                menuObject.label.setTextFill(Color.BLACK);
        }
    }

    @FXML
    public void addBuildingLevel(ActionEvent actionEvent)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(WINDOW_ADD_BUILDING_LEVEL_FXML).openStream());
            AddBuildingLevelWindowController controller = (AddBuildingLevelWindowController) fxmlLoader.getController();
            controller.setBuildingId(masterWindowController.GetCurrentBuildingId());
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setScene(scene);
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
