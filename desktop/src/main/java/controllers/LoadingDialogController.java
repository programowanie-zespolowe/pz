package controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class LoadingDialogController {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize () {
        Image image = new Image("/icons/loading_bez_tła.gif", false);
        imageView.setImage(image);
    }

}
