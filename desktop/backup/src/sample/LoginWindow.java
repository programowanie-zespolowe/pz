package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindow implements Initializable {
    @FXML
    private GridPane gp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gp.setMaxWidth(700);
        gp.setMinSize(200, 200);
    }
}
