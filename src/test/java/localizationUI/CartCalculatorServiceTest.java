package localizationUI;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorServiceTest {

    @Test
    void testCalculateTotal() {
        // Arrange
        Map<String, ItemModel> cart = new HashMap<>();

        // 3 items
        cart.put("item1", new ItemModel("A", 2, 10.0)); // total = 20
        cart.put("item2", new ItemModel("B", 1, 15.0)); // total = 15
        cart.put("item3", new ItemModel("C", 3, 5.0));  // total = 15

        // Total = 20 + 15 + 15 = 50
        // after discount:  10% = 45
        // after tax 24% = 45 * 1.24 = 55.8

        // Act
        Map<String, Double> result = CartCalculatorService.calculateTotal(cart);

        // Assert
        assertEquals(50.0, result.get("total"));
        assertEquals(45.0, result.get("afterDiscount"));
        assertEquals(5.0, result.get("discountAmount"));
        assertEquals(55.8, result.get("afterTax"));
        assertEquals(10.8, result.get("taxAmount"), 0.000001);

    }

    @Test
    void testEmptyCart() {
        Map<String, ItemModel> cart = new HashMap<>();

        Map<String, Double> result = CartCalculatorService.calculateTotal(cart);

        assertEquals(0.0, result.get("total"));
        assertEquals(0.0, result.get("afterDiscount"));
        assertEquals(0.0, result.get("discountAmount"));
        assertEquals(0.0, result.get("afterTax"));
        assertEquals(0.0, result.get("taxAmount"));
    }
}
