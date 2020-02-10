package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.util.ResourceBundle;

public class DialogUtils {

    private static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    /**
     * obsługa wyjątków, okienko dla użytkownika
     * @param error nazwa błedu
     */
    public static void errorDilog (String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(bundle.getString("error.title"));
        errorAlert.setHeaderText(bundle.getString("error.header"));
        TextArea textArea = new TextArea(error);
        errorAlert.getDialogPane().setContent(textArea);
        errorAlert.showAndWait();
    }
}
