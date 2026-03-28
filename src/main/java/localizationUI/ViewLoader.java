package localizationUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;


public class ViewLoader {

    public static void loadScene(Stage stage, Locale locale) throws IOException {

        Font f = Font.loadFont(ViewLoader.class.getResourceAsStream("/fonts/NotoSansLao-Regular.ttf"), 12);

        System.out.println(f.getName());
        //ResourceBundle if reload FXML
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/main_view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Add CSS
        scene.getStylesheets().add(
                ViewLoader.class.getResource("/style.css").toExternalForm()
        );
        //reload UI
//        MainViewController controller = loader.getController();
//        controller.setStageAndLocale(stage, locale);
    }
}
