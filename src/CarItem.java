public class CarItem {
    Product product;
    private int quantity;

    public CarItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("A termék nem lehet null!");
        }
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("A mennyiségnek pozitívnak kell lennie!");
        }
        this.quantity = quantity;
    }

    public double subtotal(){
        return product.getPrice() * getQuantity();
    }

    @Override
    public String toString() {
        return "CarItem{" +
                "product=" + getProduct() +
                ", quantity=" + getQuantity() +
                '}';
    }
}
