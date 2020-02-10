package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.WebService.WebServiceConnection;
import utils.FxmlUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable {

    @FXML
    private GridPane gp;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordField;

    public static final String MAIN_FXML = "/fxml/masterWindow.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gp.setMaxWidth(700);
        gp.setMinSize(200, 200);
    }

    @FXML
    private void handleLoginAction(ActionEvent event)
    {
        if(WebServiceConnection.GetInstance().Login(loginTextField.getText(), passwordField.getText()))
            ShowMainWindow();
        else
            ShowLoginErrorMessageBox();
    }

    private void ShowLoginErrorMessageBox()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login error");
        alert.setHeaderText("Login or password incorrect!");
        alert.showAndWait();
    }

    private void ShowMainWindow()
    {
        Pane borderPane = FxmlUtils.fxmlLoader(MAIN_FXML);

        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.application"));
        primaryStage.show();
        primaryStage.setMaximized(true);
    }
}
