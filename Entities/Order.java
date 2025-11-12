package Entities;

import ValueObjects.Address;
import ValueObjects.OrderFlags;
import Validator.Validator;

public class Order {
    public Customer customer;
    public double price;
    public int quantity;
    public Address address;
    public OrderFlags flags;
    public boolean isActive;
    public int days;

    public Order(Customer customer, double price, int quantity, Address address, OrderFlags flags, boolean isActive) {
        this.customer = customer;
        this.price = Validator.validate(price, v -> v > 0, "Price must be greater than zero.");
        this.quantity = Validator.validate(quantity, v -> v > 0, "Quantity must be greater than zero.");
        this.address = address;
        this.flags = flags;
        this.isActive = isActive;
        this.days = 0;

        double discount = calculateDiscount();
        if (discount > 0) {
            this.price = this.price * (1 - discount);
        }

        double additionalCharges = calculateAdditionalCharges(flags);
        if (additionalCharges > 0) {
            this.price = this.price + additionalCharges;
        }
    }

    public double calculateDiscount() {
        double discount = 0;

        if (price * quantity > 100) {
            switch (customer.getType()) {
                case Customer.CustomerType.GOLD:
                    discount = 0.1;
                    break;
                case Customer.CustomerType.SILVER:
                    discount = 0.08;
                    break;
                default:
                    discount = 0;
            }
        }
        return discount;
    }

    private double calculateAdditionalCharges(OrderFlags flags) {
        double additionalCharges = 0;

        if (flags.getShipping() == OrderFlags.ShippingType.EXPRESS) {
            additionalCharges += 9.99;
        }
        if (flags.getWrap() == OrderFlags.WrapType.GIFT) {
            additionalCharges += 2.99;
        }
        if (flags.getInsurance() == OrderFlags.InsuranceType.BASIC) {
            additionalCharges += 4.99;
        }
        return additionalCharges;
    }

    public String process(boolean flag, boolean email, boolean pdf) {
        String result = "";

        if (!isActive) {
            return "Error: Order not active";
        }

        double total = calculateTotalPrice();
        System.out.println("Total before discount and tax: $" + String.format("%.2f", total));
        total *= calculateDiscount(total);
        System.out.println("Total after discount: $" + String.format("%.2f", total));
        total *= getCountryTaxRate();
        System.out.println("Total after tax: $" + String.format("%.2f", total));

        if (total < 25) {
            total += 5.99;
        }

        result = "Order #" + System.currentTimeMillis() + "\n";
        result += "Customer: " + customer.getName() + "\n";
        result += "Email: " + customer.getEmail() + "\n";
        result += "Phone: " + customer.getPhone() + "\n";
        result += "Items: " + quantity + " x $" + price + "\n";
        result += "Total: $" + String.format("%.2f", total) + "\n";

        if (flag == true) { // TODO: Refactor this
            if (email == true) {
                System.out.println("Sending email to " + email);
            }
            if (pdf == true) {
                System.out.println("Generating PDF invoice");
            }
        }

        System.out.println("Saving to database...");

        return result;
    }

    public double calculateTotalPrice() {
        return price * quantity;
    }

    private double calculateDiscount(double total) {
        double discount = 0;
        switch (customer.getType()) {
            case Customer.CustomerType.SILVER:
                if (total > 100) {
                    discount = 0.08;
                } else if (total > 75) {
                    discount = 0.04;
                }
                break;
            case Customer.CustomerType.GOLD:
                if (total > 100) {
                    if (days > 30) {
                        discount = 0.15;
                    } else {
                        discount = 0.1;
                    }
                } else if (total > 50) {
                    discount = 0.05;
                }
                break;
            default:
                // No discount
                break;
        }
        return total * (1 - discount);
    }

    private double getCountryTaxRate() {
        double taxRate = 0;
        switch (address.getCountry()) {
            case "ES":
                taxRate = 1.21;
                break;
            case "FR":
                taxRate = 1.20;
                break;
            case "DE":
                taxRate = 1.19;
                break;
            case "UK":
                taxRate = 1.20;
                break;
            default:
                taxRate = 1.15;
        }
        return taxRate;
    }

    public void updateDays() {
        days = days + 1;
        if (days > 365) {
            isActive = false;
        }
    }

    public double getTotalWithTax() {
        double total = calculateTotalPrice();
        total *= getCountryTaxRate();
        return total;
    }

    /*
     * public static void main(String[] args) {
     * System.out.println("=== Order Demo ===\n");
     * 
     * Order order1 = new Order("John Doe", "john@email.com", "555-1234", 1, 15.0,
     * 2, "ES", true);
     * System.out.println("Normal Order:");
     * System.out.println(order1.process(true, true, false));
     * System.out.println();
     * 
     * Order order2 = new Order("Jane Smith", "jane@email.com", "555-5678", 3, 60.0,
     * 2, "ES", true);
     * order2.days = 35;
     * System.out.println("Gold Order (Old Customer):");
     * System.out.println(order2.process(true, false, true));
     * }
     */
}