import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private List<CarItem> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public List<CarItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(CarItem item){
        items.add(item);

    }

    public void removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getName().equalsIgnoreCase(name)) {
                items.remove(i);
                return;
            }
        }
    }

    public double calculateTotal(){
        double total = 0;

        for (CarItem item : items){
            total += item.subtotal();
        }

        if (total > 25000){
            total = total * 0.75;
        } else if (total > 10000) {
            total = total * 0.9;
        }
        return total;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "items=" + items +
                '}';
    }
}
