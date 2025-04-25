import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class InventoryGUI {
    private JFrame frame;
    private InventorySystem inventory;
    private DefaultTableModel tableModel;
    private JTable productTable;

    public InventoryGUI() {
        try {
            inventory = new InventorySystem();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame = new JFrame("Inventory Management");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID","Name","Price","Quantity","Category"},0);
        productTable = new JTable(tableModel);
        frame.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel btnPanel=new JPanel();
        String[] names={"Add","Update Stock","Update Price","Sell","Delete","Search","Low Stock Alert","Sales Report","Export","Import","Graph"};
        for(String n:names){
            JButton b=new JButton(n);
            b.addActionListener(e->handleAction(n));
            btnPanel.add(b);
        }
        frame.add(btnPanel,BorderLayout.SOUTH);

        frame.setVisible(true);
        refreshTable();
    }

    private void handleAction(String cmd) {
        try {
            int row,id;
            switch(cmd) {
                case "Add": {
                    String name=JOptionPane.showInputDialog("Name:"); if(name==null) return;
                    double price=Double.parseDouble(JOptionPane.showInputDialog("Price:"));
                    int qty=Integer.parseInt(JOptionPane.showInputDialog("Qty:"));
                    String cat=JOptionPane.showInputDialog("Category:"); if(cat==null) cat="";
                    inventory.addProduct(name,price,qty,cat);
                    break;
                }
                case "Update Stock":
                    row=productTable.getSelectedRow(); if(row<0) return;
                    id=(int)tableModel.getValueAt(row,0);
                    inventory.updateStock(id,Integer.parseInt(JOptionPane.showInputDialog("New Qty:")));
                    break;
                case "Update Price":
                    row=productTable.getSelectedRow(); if(row<0) return;
                    id=(int)tableModel.getValueAt(row,0);
                    inventory.updatePrice(id,Double.parseDouble(JOptionPane.showInputDialog("New Price:")));
                    break;
                case "Sell":
                    row=productTable.getSelectedRow(); if(row<0) return;
                    id=(int)tableModel.getValueAt(row,0);
                    inventory.recordSale(id,Integer.parseInt(JOptionPane.showInputDialog("Sell Qty:")));
                    break;
                case "Delete":
                    row=productTable.getSelectedRow(); if(row<0) return;
                    id=(int)tableModel.getValueAt(row,0);
                    inventory.deleteProduct(id);
                    break;
                case "Search": {
                    String keyword=JOptionPane.showInputDialog("Search name:"); if(keyword==null) return;
                    double min=Double.parseDouble(JOptionPane.showInputDialog("Min Price:"));
                    double max=Double.parseDouble(JOptionPane.showInputDialog("Max Price:"));
                    tableModel.setRowCount(0);
                    for(Product p: inventory.searchProductsByNameAndPrice(keyword,min,max))
                        tableModel.addRow(new Object[]{p.getId(),p.getName(),p.getPrice(),p.getQuantity(),p.getCategory()});
                    return;
                }
                case "Low Stock Alert": {
                    List<Product> low=inventory.getLowStockProducts();
                    if(low.isEmpty()) JOptionPane.showMessageDialog(frame,"No low stock.");
                    else{
                        String msg="Low Stock:\n";
                        for(Product p:low) msg+=p.getName()+" ("+p.getQuantity()+")\n";
                        JOptionPane.showMessageDialog(frame,msg);
                    }
                    break;
                }
                case "Sales Report":{
                    String date=JOptionPane.showInputDialog("Date (YYYY-MM-DD):"); if(date==null) return;
                    List<Sale> sales=inventory.getSalesByDate(date);
                    if(sales.isEmpty()) JOptionPane.showMessageDialog(frame,"No sales on "+date);
                    else{
                        String rep="Sales on "+date+":\n";
                        for(Sale s:sales) rep+="ProductID:"+s.getProductId()+" Qty:"+s.getQuantitySold()+" Total:"+s.getSalePrice()+"\n";
                        JOptionPane.showMessageDialog(frame,rep);
                    }
                    break;
                }
                case "Export":{
                    JFileChooser fc=new JFileChooser();
                    if(fc.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION) {
                        inventory.exportToCSV(fc.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(frame,"Exported.");
                    }
                    break;
                }
                case "Import":{
                    JFileChooser fc=new JFileChooser();
                    if(fc.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
                        inventory.importFromCSV(fc.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(frame,"Imported.");
                    }
                    break;
                }
                case "Graph":{
                    DefaultCategoryDataset ds=new DefaultCategoryDataset();
                    for(Product p:inventory.getProducts()) ds.addValue(p.getQuantity(),"Qty",p.getName());
                    JFreeChart chart=ChartFactory.createBarChart("Stock","Product","Qty",ds);
                    ChartPanel cp=new ChartPanel(chart);
                    JFrame f=new JFrame("Graph"); f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); f.add(cp); f.setSize(600,400); f.setVisible(true);
                    return;
                }
            }
            refreshTable();
        }catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(frame,"Error: "+ex.getMessage()); }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try{
            for(Product p:inventory.getProducts())
                tableModel.addRow(new Object[]{p.getId(),p.getName(),p.getPrice(),p.getQuantity(),p.getCategory()});
        }catch(SQLException e){
            JOptionPane.showMessageDialog(frame,"Error refreshing: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

