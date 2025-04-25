import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Relative path to the SQLite DB file; run from project root
    private static final String DB_URL = "jdbc:sqlite:/Users/mrdhruvaggarwal/Downloads/VS/InventoryManagementSystem/database/inventory.db";

    static {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Fail fast if the driver isn't on the classpath
            throw new ExceptionInInitializerError(
                "SQLite JDBC driver not found in classpath: " + e.getMessage()
            );
        }
    }

    /**
     * Returns a Connection to the SQLite database.
     * @throws SQLException if the connection cannot be established.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
