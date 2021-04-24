package Model;

public class Items {
    String items, quantity;

    public Items(String items, String quantity) {
        this.items = items;
        this.quantity = quantity;
    }

    public Items() {
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

