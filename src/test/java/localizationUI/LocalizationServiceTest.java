package localizationUI;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceTest {

    @Test
    void testLoadEnglishBundle() {
        Locale locale = new Locale("en", "US");
        Map<String, String> strings = LocalizationService.getLocalizedStrings(locale);

        assertNotNull(strings);
        assertFalse(strings.isEmpty());
        assertTrue(strings.containsKey("shopping.title"));
    }

    @Test
    void testFallbackWhenLocaleNotFound() {
        Locale invalidLocale = new Locale("xx", "YY");
        Map<String, String> strings = LocalizationService.getLocalizedStrings(invalidLocale);

        assertNotNull(strings);
        assertFalse(strings.isEmpty());

        // Fallback must contain English keys
        assertTrue(strings.containsKey("shopping.title"));
    }

    @Test
    void testTranslateFunction() {
        Locale locale = new Locale("en", "US");
        LocalizationService.getLocalizedStrings(locale);

        String value = LocalizationService.t("shopping.title");
        assertNotNull(value);
        assertNotEquals("shopping.title", value); // must translate
    }

    @Test
    void testTranslateMissingKey() {
        Locale locale = new Locale("en", "US");
        LocalizationService.getLocalizedStrings(locale);

        String value = LocalizationService.t("non_existing_key");
        assertEquals("non_existing_key", value); // return key itself
    }

        @Test
        void testLastFallbackMinimalUI() {
            // create classloader empty to get ResourceBundle fail
            ClassLoader emptyLoader = new ClassLoader(null) {};

            Locale weirdLocale = new Locale("xx", "YY");

            Map<String, String> strings;

            // change đổi classloader của thread
            ClassLoader original = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(emptyLoader);

            try {
                strings = LocalizationService.getLocalizedStrings(weirdLocale);
            } finally {
                // refresh classloader
                Thread.currentThread().setContextClassLoader(original);
            }

            // check fallback
            assertEquals("Shopping Cart", strings.get("shopping.title"));
            assertEquals("Select Item", strings.get("shopping.select.item"));
            assertEquals("Enter Price", strings.get("shopping.enter.price"));
            assertEquals("Enter Quantity", strings.get("shopping.enter.quantity"));
            assertEquals("Add to Cart", strings.get("shopping.add"));
            assertEquals("Cart", strings.get("shopping.cart"));
            assertEquals("Total:", strings.get("shopping.total"));
            assertEquals("Discount:", strings.get("shopping.discount"));
            assertEquals("Tax:", strings.get("shopping.tax"));
            assertEquals("Final Total:", strings.get("shopping.final.total"));
            assertEquals("Calculate", strings.get("shopping.calculate"));
            assertEquals("Pay", strings.get("shopping.pay"));
            assertEquals("Please enter valid numbers", strings.get("error.invalid_input"));
            assertEquals("Thank you for your purchase!", strings.get("thanks"));
            assertEquals("Cancelled.", strings.get("sorry"));
        }
    }

