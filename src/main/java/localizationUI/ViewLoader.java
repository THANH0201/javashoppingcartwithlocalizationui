package localizationUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Locale;

public class ViewLoader {

    public static Scene loadMainView() throws Exception {

        // Load Lao font
        Font.loadFont(ViewLoader.class.getResourceAsStream("/fonts/NotoSansLao.ttf"), 12);

        URL fxmlUrl = ViewLoader.class.getResource("/main_view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        VBox root = loader.load();
        Scene scene = new Scene(root);

        applyLocaleFont(scene);

        // Add CSS
        scene.getStylesheets().add(
                ViewLoader.class.getResource("/style.css").toExternalForm()
        );

        return scene;
    }

    private static void applyLocaleFont(Scene scene) {
        if (scene == null) return;

        Locale locale = Locale.getDefault();

        // Nếu là tiếng Lào → đổi font
        if (locale.getLanguage().equals("lo")) {

            // Áp dụng font
            scene.getRoot().setStyle("-fx-font-family: 'Noto Sans Lao';");

            // Thêm CSS class nếu muốn
            Parent root = scene.getRoot();
            if (!root.getStyleClass().contains("locale-lo")) {
                root.getStyleClass().add("locale-lo");
            }
        }
    }
}
