package localizationUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;

import static localizationUI.LocalizationService.t;

public class MainViewController {

    // UI elements
    @FXML private VBox itemSection;
    @FXML private VBox rootVBox;
    @FXML private MenuButton languageMenu;
    @I18nKey("titleLabel")
    @FXML private Label titleLabel;
    @FXML private ListView<String> itemList;
    @I18nKey("shopping.enter.price")
    @FXML private TextField priceField;
    @I18nKey("shopping.enter.quantity")
    @FXML private TextField quantityField;
    @I18nKey("shopping.add")
    @FXML private Button addButton;
    @FXML private VBox cartSection;
    @FXML private Button cartButton;
    @I18nKey("shopping.cart")
    @FXML private Label cartText;
    @FXML private Label totalItem;
    @FXML private ListView<String> cartList;
    @I18nKey("shopping.calculate")
    @FXML private Button calButton;
    @FXML private VBox priceSummaryBox;
    @I18nKey("shopping.total")
    @FXML private Label totalLabelText;
    @I18nKey("shopping.discount")
    @FXML private Label discountLabelText;
    @I18nKey("total.after.discount")
    @FXML private Label afterDiscountLabelText;
    @I18nKey("shopping.tax")
    @FXML private Label taxLabelText;
    @I18nKey("total.after.tax")
    @FXML private Label afterTaxLabelText;
    @I18nKey("shopping.final.total")
    @FXML private Label finalTotalLabelText;

    @FXML private Label totalLabel;
    @FXML private Label discountLabel;
    @FXML private Label afterDiscountLabel;
    @FXML private Label taxLabel;
    @FXML private Label afterTaxLabel;
    @FXML private Label finalTotalLabel;

    @I18nKey("shopping.pay")
    @FXML private Button payButton;
    @I18nKey("shopping.back")
    @FXML private Button backButton;


    private Locale currentLocale = new Locale("en", "US");
    private Map<String, String> localizedStrings;

    private final Map<String, ItemModel> cart = new HashMap<>();

    /**
     * Initialize controller
     */
    @FXML
    public void initialize() {
        loadLanguageMenu();
        setLanguage(currentLocale);
        cartSection.setVisible(false);
    }

    /**
     * Load language menu dynamically
     */
    private void loadLanguageMenu() {
        Properties props = new Properties();

        try {
            props.load(getClass().getResourceAsStream("/i18n/languages.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        languageMenu.getItems().clear();

        props.forEach((name, code) -> {
            String[] parts = code.toString().split("_");
            if (parts.length == 2) {
                Locale locale = new Locale(parts[0], parts[1]);
                MenuItem item = new MenuItem(name.toString());
                // Laos
                if (locale.getLanguage().equals("lo")) {
                    item.setStyle("-fx-font-family: 'Noto Sans Lao';");
                }
                item.setOnAction(e -> setLanguage(locale));
                languageMenu.getItems().add(item);
            } else {
                System.err.println("Invalid language entry: " + name + "=" + code);
            }
        });
    }
    /**
     * Choose language
     */
    @FXML
    public void switchToLanguageCurrent() {
        setLanguage(currentLocale);
    }

    /**
     * Apply localization
     */
    private void setLanguage(Locale locale) {
        currentLocale = locale;
        localizedStrings = LocalizationService.getLocalizedStrings(locale);
        Locale.setDefault(locale);

        // Update UI text
        // Reset font
        rootVBox.setStyle("");

        // Laos
        if (locale.getLanguage().equals("lo")) {
            rootVBox.setStyle("-fx-font-family: 'Noto Sans Lao';");
        }
        // Normal
        else {
            rootVBox.setStyle("-fx-font-family: 'Inter';");
        }
        languageMenu.setText(t("language.current"));
        loadItemList();
        updateUI();
        applyTextDirection(locale);
    }

    /**
     * Load items into ListView
     */
    @FXML
    private void loadItemList() {
        itemList.getItems().clear();
        // default
        itemList.getItems().add(t("shopping.select.item"));

        localizedStrings.keySet().stream()
                .filter(key -> key.startsWith("item."))
                .sorted()
                .forEach(key -> itemList.getItems().add(t(key)));
    }
    /**
     * Action buttons
     */
    @FXML
    private void onAddItem() {
        try {
            String itemName = itemList.getSelectionModel().getSelectedItem();
            if (itemName == null) {
                showAlert(t("error.invalid_input"));
                return;
            }

            double price = Double.parseDouble(priceField.getText());
            double qty = Double.parseDouble(quantityField.getText());

            // ItemModel
            ItemModel item = new ItemModel(itemName, price, qty);
            cart.put(itemName, item);

            // Display
            cartList.getItems().add(itemName + ":" + price + " x " + qty + " = " + item.getTotal());

            showAlert(t("added") + " " + itemName);

            priceField.clear();
            quantityField.clear();

            //update CartButton
            totalItem.setText(String.valueOf(cart.size()));

        } catch (Exception e) {
            showAlert(t("error.invalid_input"));
        }
    }

    @FXML
    private void onCart() {
        itemSection.setVisible(true);
        cartSection.setVisible(true);
        priceSummaryBox.setVisible(false);

    }

    @FXML
    private void deleteItem() {
        String selected = cartList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(t("delete.item"));

            ButtonType yes = new ButtonType(t("yes"));
            ButtonType no = new ButtonType(t("no"));

            alert.getButtonTypes().setAll(yes, no);

            alert.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    showAlert(t("thanks"));
                } else {
                    showAlert(t("sorry"));
                }
            });
            cartList.getItems().remove(selected);
        }
    }

    @FXML
    private void onCalculate() {
        priceSummaryBox.setVisible(true);

        Map<String, Double> result = CartCalculatorService.calculateTotal(cart);

        totalLabel.setText(String.format("%.2f EUR", result.get("total")));
        discountLabel.setText(String.format("%.2f EUR", result.get("discountAmount")));
        afterDiscountLabel.setText(String.format("%.2f EUR", result.get("afterDiscount")));
        taxLabel.setText(String.format("%.2f EUR", result.get("taxAmount")));
        afterTaxLabel.setText(String.format("%.2f EUR", result.get("afterTax")));
        finalTotalLabel.setText(String.format("%.2f EUR", result.get("afterTax")));
    }

    @FXML
    private void onPay() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(t("shopping.pay"));

        ButtonType yes = new ButtonType(t("yes"));
        ButtonType no = new ButtonType(t("no"));

        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(response -> {
            if (response == yes) {
                showAlert(t("thanks"));
            } else {
                showAlert(t("sorry"));
            }
        });
        cartList.getItems().clear();
        cart.clear();
        totalItem.setText("0");
        priceSummaryBox.getChildren().clear();
    }

    public void onBack() {
        cartSection.setVisible(false);
        itemSection.setVisible(true);
        priceSummaryBox.getChildren().clear();
    }

    /**
     * RTL support for Arabic
     */
    private void applyTextDirection(Locale locale) {
        boolean isRTL = locale.getLanguage().equals("ar");

        Platform.runLater(() -> {
            rootVBox.setNodeOrientation(
                    isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT
            );
        });
    }

    /**
     * Show alert
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Update UI
     */

    private void updateUI() {
        LocalizationService.updateUI(this);
    }
}


