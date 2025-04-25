import java.sql.*;
import java.util.*;
import java.io.*;

public class InventorySystem {
    private Connection conn;

    public InventorySystem() throws SQLException {
        conn = DBConnection.getConnection();
        try (Statement s = conn.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, price REAL, quantity INTEGER, min_stock INTEGER DEFAULT 5, category TEXT);" );
            s.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT);" );
            s.execute("CREATE TABLE IF NOT EXISTS sales (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_id INTEGER, quantity_sold INTEGER, sale_price REAL, sale_date TEXT, " +
                    "FOREIGN KEY(product_id) REFERENCES products(id));" );
        }
    }

    public List<Product> getProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category")
                ));
            }
        }
        return list;
    }

    public List<Product> getLowStockProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantity < min_stock";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("category")
                ));
            }
        }
        return list;
    }

    public List<Product> searchProductsByNameAndPrice(String name, double minPrice, double maxPrice) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? AND price BETWEEN ? AND ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, "%" + name + "%");
            p.setDouble(2, minPrice);
            p.setDouble(3, maxPrice);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("category")
                    ));
                }
            }
        }
        return list;
    }

    // ✅ Added this missing method
    public List<Product> searchProducts(String name) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, "%" + name + "%");
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("category")
                    ));
                }
            }
        }
        return list;
    }

    public List<Sale> getSalesByDate(String date) throws SQLException {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE date(sale_date)=?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, date);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    list.add(new Sale(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("sale_price"),
                        rs.getString("sale_date")
                    ));
                }
            }
        }
        return list;
    }

    public void addProduct(String name, double price, int quantity, String category) throws SQLException {
        String sql = "INSERT INTO products(name,price,quantity,category) VALUES(?,?,?,?)";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, name);
            p.setDouble(2, price);
            p.setInt(3, quantity);
            p.setString(4, category);
            p.executeUpdate();
        }
    }

    public void updateStock(int id, int quantity) throws SQLException {
        String sql = "UPDATE products SET quantity=? WHERE id=?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, quantity);
            p.setInt(2, id);
            p.executeUpdate();
        }
    }

    public void updatePrice(int id, double price) throws SQLException {
        String sql = "UPDATE products SET price=? WHERE id=?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setDouble(1, price);
            p.setInt(2, id);
            p.executeUpdate();
        }
    }

    public void recordSale(int id, int qtySold) throws SQLException {
        Product prod = null;
        for (Product p : getProducts()) {
            if (p.getId() == id) {
                prod = p;
                break;
            }
        }
        if (prod == null) return;
        double totalPrice = prod.getPrice() * qtySold;
        String sql = "INSERT INTO sales(product_id,quantity_sold,sale_price,sale_date) VALUES(?,?,?,datetime('now'))";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, id);
            p.setInt(2, qtySold);
            p.setDouble(3, totalPrice);
            p.executeUpdate();
        }
        updateStock(id, prod.getQuantity() - qtySold);
    }

    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }
    }

    public void exportToCSV(String path) throws IOException, SQLException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
            w.write("ID,Name,Price,Quantity,Category\n");
            for (Product p : getProducts()) {
                w.write(p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getQuantity() + "," + p.getCategory() + "\n");
            }
        }
    }

    public void importFromCSV(String path) throws IOException, SQLException {
        try (BufferedReader r = new BufferedReader(new FileReader(path))) {
            r.readLine(); // skip header
            String l;
            while ((l = r.readLine()) != null) {
                String[] a = l.split(",");
                addProduct(a[1], Double.parseDouble(a[2]), Integer.parseInt(a[3]), a[4]);
            }
        }
    }
}
