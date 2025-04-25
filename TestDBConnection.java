import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            // Load the SQLite JDBC driver explicitly
            Class.forName("org.sqlite.JDBC");

            // Database URL (use absolute path)
            String url = "jdbc:sqlite:/Users/mrdhruvaggarwal/Downloads/VS/InventoryManagementSystem/database/inventory.db";
            
            // Establish connection
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Connected to the database!");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
