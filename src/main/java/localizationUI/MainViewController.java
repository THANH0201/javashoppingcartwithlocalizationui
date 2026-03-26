package localizationUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.*;

import static localizationUI.LocalizationService.t;

public class MainViewController {
    @FXML private Button backButton;
    // UI elements
    @FXML private Label totalLabelText;
    @FXML private Label finalTotalLabelText;
    @FXML private Label afterTaxLabelText;
    @FXML private Label taxLabelText;
    @FXML private Label afterDiscountLabelText;
    @FXML private Label discountLabelText;
    @FXML private VBox priceSummaryBox;
    @FXML private Button calButton;
    @FXML private VBox itemSection;
    @FXML private VBox rootVBox;
    @FXML private MenuButton languageMenu;
    @FXML private Label titleLabel;
    @FXML private ListView<String> itemList;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Button addButton;
    @FXML private VBox cartSection;
    @FXML private Button cartButton;
    @FXML private ListView<String> cartList;
    @FXML private Label totalLabel;
    @FXML private Label discountLabel;
    @FXML private Label afterDiscountLabel;
    @FXML private Label taxLabel;
    @FXML private Label afterTaxLabel;
    @FXML private Label finalTotalLabel;
    @FXML private Button payButton;

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
            props.load(getClass().getResourceAsStream("/languages.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        languageMenu.getItems().clear();

        props.forEach((name, code) -> {
            String[] parts = code.toString().split("_");
            if (parts.length == 2) {
                Locale locale = new Locale(parts[0], parts[1]);
                MenuItem item = new MenuItem(name.toString());
                item.setOnAction(e -> setLanguage(locale));
                languageMenu.getItems().add(item);
            } else {
                System.err.println("Invalid language entry: " + name + "=" + code);
            }
        });
    }
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
     * Add item to cart
     */
    @FXML
    private void addItem() {
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
            cartList.getItems().add(itemName + " | " + price + " x " + qty + " = " + item.getTotal());

            showAlert(t("added") + " " + itemName);

            priceField.clear();
            quantityField.clear();

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
    /**
     * Calculate total, discount, tax, final total
     */
    @FXML
    private void calculateTotal() {
        priceSummaryBox.setVisible(true);

        Map<String, Double> result = CartCalculatorService.calculateTotal(cart);

        totalLabel.setText(String.format("%.2f EUR", result.get("total")));
        discountLabel.setText( String.format("%.2f EUR", result.get("discountAmount")));
        afterDiscountLabel.setText( String.format("%.2f EUR", result.get("afterDiscount")));
        taxLabel.setText( String.format("%.2f EUR", result.get("taxAmount")));
        afterTaxLabel.setText(String.format("%.2f EUR", result.get("afterTax")));
        finalTotalLabel.setText( String.format("%.2f EUR", result.get("afterTax")));
    }

    /**
     * Payment confirmation
     */
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

        titleLabel.setText(t("titleLabel"));

        priceField.setPromptText(t("shopping.enter.price"));
        quantityField.setPromptText(t("shopping.enter.quantity"));


        addButton.setText(t("shopping.add"));
        cartButton.setText(t("shopping.cart"));
        payButton.setText(t("shopping.pay"));
        calButton.setText(t("shopping.calculate"));
        backButton.setText(t("shopping.back"));

        totalLabelText.setText(t("shopping.total"));
        discountLabelText.setText(t("shopping.discount"));
        afterDiscountLabelText.setText(t("total.after.discount"));
        taxLabelText.setText(t("shopping.tax"));
        afterTaxLabelText.setText(t("total.after.tax"));
        finalTotalLabelText.setText(t("shopping.final.total"));
    }

    public void switchBackToItems() {
        cartSection.setVisible(false);
        itemSection.setVisible(true);
        priceSummaryBox.getChildren().clear();
        }


}

