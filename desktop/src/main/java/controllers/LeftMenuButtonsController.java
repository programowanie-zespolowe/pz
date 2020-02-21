package controllers;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import sample.Structs.Building;
import sample.Structs.BuildingLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class LeftMenuButtonsController  {
    @FXML
    public FlowPane flowPane;
    private MasterWindowController masterWindowController;

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

    protected class LeftMenuObject{
        public BuildingLevel level;
        public Label label;
        public ImageView imageView;
    }
}
