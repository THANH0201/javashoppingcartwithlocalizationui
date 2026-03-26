package localizationUI;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationService {
    private static Map<String, String> currentStrings;

    private LocalizationService() {}

    /**
     * Load all localized strings for the given locale.
     * Returns a LinkedHashMap<String, String> to preserve order.
     */
    public static Map<String, String> getLocalizedStrings(Locale locale) {
        Map<String, String> strings = new LinkedHashMap<>();

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);

            // Load all keys in order
            for (String key : bundle.keySet()) {
                strings.put(key, bundle.getString(key));
            }


        } catch (Exception e) {
            System.err.println("Failed to load resource bundle for locale: " + locale);

            // Fallback to English
            try {
                ResourceBundle fallback = ResourceBundle.getBundle("MessagesBundle", new Locale("en", "US"));
                for (String key : fallback.keySet()) {
                    strings.put(key, fallback.getString(key));
                }

            } catch (Exception ex) {
                // Last fallback: minimal UI
                strings.put("shopping.title", "Shopping Cart");
                strings.put("shopping.select.item", "Select Item");
                strings.put("shopping.enter.price", "Enter Price");
                strings.put("shopping.enter.quantity", "Enter Quantity");
                strings.put("shopping.add", "Add to Cart");
                strings.put("shopping.cart", "Cart");
                strings.put("shopping.total", "Total:");
                strings.put("shopping.discount", "Discount:");
                strings.put("shopping.tax", "Tax:");
                strings.put("shopping.final.total", "Final Total:");
                strings.put("shopping.calculate", "Calculate");
                strings.put("shopping.pay", "Pay");
                strings.put("error.invalid_input", "Please enter valid numbers");
                strings.put("thanks", "Thank you for your purchase!");
                strings.put("sorry", "Payment cancelled.");
            }
        }
        currentStrings = strings;
        return strings;
    }

    //
    public static String t(String key) {
        if (currentStrings == null) return key;
        return currentStrings.getOrDefault(key, key);
    }
}


