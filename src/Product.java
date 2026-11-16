public class Product {
    private String name;
    private String barcode;
    private double price;
    private ProductCategory category;

    public Product(String name, String barcode, double price, ProductCategory category) {
        setName(name);
        setBarcode(barcode);
        setPrice(price);
        setCategory(category);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + getName() + '\'' +
                ", barcode='" + getBarcode() + '\'' +
                ", price=" + getPrice() +
                ", category=" + getCategory() +
                '}';
    }
}
