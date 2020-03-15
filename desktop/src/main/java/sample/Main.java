package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.FxmlUtils;

public class Main extends Application {

    public static final String BORDER_PANE_MAIN_FXML = "/fxml/loginWindow.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception{

        Pane borderPane = FxmlUtils.fxmlLoader(BORDER_PANE_MAIN_FXML);
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(getClass().getResource("/stylesheets/scene.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.application"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
