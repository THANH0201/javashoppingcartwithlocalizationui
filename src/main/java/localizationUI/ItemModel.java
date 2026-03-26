package localizationUI;

public class ItemModel {
    String name;
    double price;
    double quantity;

    public ItemModel(String name, double price, double quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotal() {
        return price * quantity;
    }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public double getQuantity() {
        return quantity;
    }
}
