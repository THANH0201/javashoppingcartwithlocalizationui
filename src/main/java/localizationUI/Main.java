
package localizationUI;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;

import static localizationUI.ViewLoader.loadScene;

public class Main extends Application {
    private static Locale currentLocale = new Locale("en");

    @Override
    public void start(Stage stage) throws Exception {
        loadScene(stage, currentLocale);
        stage.setTitle("Thanh/Shopping Cart");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
