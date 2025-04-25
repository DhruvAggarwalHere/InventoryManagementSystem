import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog(null, "Enter Username:");
            String password = JOptionPane.showInputDialog(null, "Enter Password:");

            if ("admin".equals(username) && "admin123".equals(password)) {
                try {
                    System.out.println("Login successful. Launching Inventory GUI...");
                    new InventoryGUI(); // Ensure InventoryGUI sets the JFrame visible
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Startup Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Access Denied", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
