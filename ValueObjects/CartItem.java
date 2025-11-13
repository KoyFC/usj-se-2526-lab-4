package ValueObjects;

import Validator.Validator;

public class CartItem {
    private double price;
    private int quantity;

    public CartItem(double price, int quantity) {
        this.price = Validator.validate(price, v -> v >= 0, "Price must be greater or equal to zero");
        this.quantity = Validator.validate(quantity, v -> v > 0, "Quantity must be greater than zero");
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
