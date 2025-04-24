import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Product Class
class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public String toString() {
        return id + " - " + name + " (₹" + price + ") [Qty: " + quantity + "]";
    }
}

// Inventory Management System
class InventorySystem {
    private ArrayList<Product> products = new ArrayList<>();
    private int nextId = 1;

    public void addProduct(String name, double price, int quantity) {
        Product product = new Product(nextId++, name, price, quantity);
        products.add(product);
    }

    public void updateProduct(int id, int newQuantity) {
        for (Product product : products) {
            if (product.getId() == id) {
                product.setQuantity(newQuantity);
                return;
            }
        }
    }

    public void deleteProduct(int id) {
        products.removeIf(product -> product.getId() == id);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}

// GUI Class
class InventoryGUI {
    private JFrame frame;
    private InventorySystem inventory;
    private DefaultTableModel tableModel;
    private JTable productTable;
    private JTextField searchField;
    private JLabel totalValueLabel;

    public InventoryGUI() {
        inventory = new InventorySystem();
        frame = new JFrame("Inventory Management System");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Quantity"}, 0);
        productTable = new JTable(tableModel);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchProduct());
        topPanel.add(new JLabel("Search: "));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Stock");
        JButton deleteButton = new JButton("Delete Product");
        JButton exportButton = new JButton("Export to CSV");

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        exportButton.addActionListener(e -> exportToCSV());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        
        totalValueLabel = new JLabel("Total Inventory Value: ₹ 0.00");
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(totalValueLabel, BorderLayout.WEST);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog("Enter Product Name:");
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));
        
        inventory.addProduct(name, price, quantity);
        refreshTable();
    }

    private void updateProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Select a product to update.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int newQuantity = Integer.parseInt(JOptionPane.showInputDialog("Enter New Quantity:"));
        
        inventory.updateProduct(id, newQuantity);
        refreshTable();
    }

    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Select a product to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        inventory.deleteProduct(id);
        refreshTable();
    }

    private void searchProduct() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        for (Product product : inventory.getProducts()) {
            if (product.getName().toLowerCase().contains(searchText)) {
                tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getPrice(), product.getQuantity()});
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        double totalValue = 0;
        for (Product product : inventory.getProducts()) {
            tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getPrice(), product.getQuantity()});
            totalValue += product.getPrice() * product.getQuantity();
        }
        totalValueLabel.setText("Total Inventory Value: ₹" + String.format("%.2f", totalValue));
    }

    private void exportToCSV() {
        try (FileWriter writer = new FileWriter("inventory.csv")) {
            writer.append("ID,Name,Price,Quantity\n");
            for (Product product : inventory.getProducts()) {
                writer.append(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuantity() + "\n");
            }
            JOptionPane.showMessageDialog(frame, "Exported to inventory.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting file.");
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        new InventoryGUI();
    }
}
