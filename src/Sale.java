public class Sale {
    private int id;
    private int productId;
    private int quantitySold;
    private double salePrice;
    private String saleDate;

    public Sale(int id, int productId, int quantitySold, double salePrice, String saleDate) {
        this.id = id;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
    }

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public int getQuantitySold() { return quantitySold; }
    public double getSalePrice() { return salePrice; }
    public String getSaleDate() { return saleDate; }
}
