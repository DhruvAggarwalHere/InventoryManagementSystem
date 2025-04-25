// File: src/InventorySystemTest.java

import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventorySystemTest {
    private static InventorySystem inventory;

    @BeforeAll
    public static void setup() throws SQLException {
        inventory = new InventorySystem();
    }

    @BeforeEach
    public void clearDB() throws SQLException {
        // Remove all products before each test
        for (Product p : inventory.getProducts()) {
            inventory.deleteProduct(p.getId());
        }
    }

    @Test
    public void testAddProduct() throws SQLException {
        inventory.addProduct("TestProduct", 100.0, 10, "TestCategory");
        List<Product> products = inventory.searchProducts("TestProduct");
        assertEquals(1, products.size());
        assertEquals("TestProduct", products.get(0).getName());
    }

    @Test
    public void testUpdatePrice() throws SQLException {
        inventory.addProduct("UpdateTest", 50.0, 5, "General");
        Product p = inventory.searchProducts("UpdateTest").get(0);
        inventory.updatePrice(p.getId(), 75.0);
        Product updated = inventory.searchProducts("UpdateTest").get(0);
        assertEquals(75.0, updated.getPrice());
    }

    @Test
    public void testUpdateStock() throws SQLException {
        inventory.addProduct("StockTest", 20.0, 2, "General");
        Product p = inventory.searchProducts("StockTest").get(0);
        inventory.updateStock(p.getId(), 15);
        Product updated = inventory.searchProducts("StockTest").get(0);
        assertEquals(15, updated.getQuantity());
    }

    @Test
    public void testDeleteProduct() throws SQLException {
        inventory.addProduct("DeleteTest", 30.0, 3, "General");
        Product p = inventory.searchProducts("DeleteTest").get(0);
        inventory.deleteProduct(p.getId());
        List<Product> results = inventory.searchProducts("DeleteTest");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchProducts() throws SQLException {
        inventory.addProduct("Laptop", 1000.0, 5, "Electronics");
        inventory.addProduct("Lamp",   50.0,   10, "Home");
        // Search on "La" so both "Laptop" and "Lamp" match
        List<Product> results = inventory.searchProducts("La");
        assertEquals(2, results.size());
    }
}
