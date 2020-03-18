package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class LoadingDialogController {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize () {
        Image image = new Image("/icons/loading.gif", false);
        imageView.setImage(image);
    }

}
