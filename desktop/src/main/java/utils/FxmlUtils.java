package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.util.ResourceBundle;

public class FxmlUtils {

    /**
     * metoda do obługi pane'a
     * @param fxmlPath ścieżka
     * @return
     */
    public static Pane fxmlLoader (String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        try {
            return loader.load();
        }
        catch (Exception e){
            DialogUtils.errorDilog(e.getMessage());
        }
        return null;
    }

    /**
     * metoda do obsługi FxmlLoader
     * @param fxmlPath ścieżka
     * @return zwraca loader
     */
    public static FXMLLoader getLoader(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        return loader;
    }

    /**
     * metoda do obsługi etykiet w aplikacji
     * @return zwraca ścieżkę pliku
     */
    public static ResourceBundle getResourceBundle(){

        return ResourceBundle.getBundle("bundles.messages");
    }
}
