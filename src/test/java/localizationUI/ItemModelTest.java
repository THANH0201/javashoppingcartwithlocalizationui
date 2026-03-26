package localizationUI;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemModelTest {

    @Test
    void testGetTotal() {
        ItemModel item = new ItemModel("Apple", 2.5, 4); // 2.5 * 4 = 10
        assertEquals(10.0, item.getTotal());
    }

    @Test
    void testGetters() {
        ItemModel item = new ItemModel("Banana", 1.2, 3);

        assertEquals("Banana", item.getName());
        assertEquals(1.2, item.getPrice());
        assertEquals(3, item.getQuantity());
    }
}
