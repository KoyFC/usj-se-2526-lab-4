public class Order {
    public String name;
    public String email;
    public String phone;
    public int customerType;
    public double price;
    public int quantity;
    public String country;
    public boolean isActive;
    public int days;

    public Order(String name, String email, String phone, int customerType, double price, int quantity, String country,
            boolean isActive) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.customerType = customerType;
        this.price = price;
        this.quantity = quantity;
        this.country = country;
        this.isActive = isActive;
        this.days = 0;
    }

    public String process(boolean flag, boolean email, boolean pdf) {
        String result = "";

        if (isActive == true) {
            if (name != null && name != "") {
                if (price > 0) {
                    if (quantity > 0) {
                        double total = price * quantity;

                        if (customerType == 3) {
                            if (total > 100) {
                                if (days > 30) {
                                    total = total * 0.85;
                                } else {
                                    total = total * 0.9;
                                }
                            } else if (total > 50) {
                                total = total * 0.95;
                            }
                        } else if (customerType == 2) {
                            if (total > 100) {
                                total = total * 0.92;
                            } else if (total > 75) {
                                total = total * 0.96;
                            }
                        }

                        if (country.equals("ES")) {
                            total = total * 1.21;
                        } else if (country.equals("FR")) {
                            total = total * 1.20;
                        } else if (country.equals("DE")) {
                            total = total * 1.19;
                        } else if (country.equals("UK")) {
                            total = total * 1.20;
                        } else {
                            total = total * 1.15;
                        }

                        if (total < 25) {
                            total = total + 5.99;
                        }

                        result = "Order #" + System.currentTimeMillis() + "\n";
                        result = result + "Customer: " + name + "\n";
                        result = result + "Email: " + email + "\n";
                        result = result + "Phone: " + phone + "\n";
                        result = result + "Items: " + quantity + " x $" + price + "\n";
                        result = result + "Total: $" + String.format("%.2f", total) + "\n";

                        if (flag == true) {
                            if (email == true) {
                                System.out.println("Sending email to " + email);
                            }
                            if (pdf == true) {
                                System.out.println("Generating PDF invoice");
                            }
                        }

                        System.out.println("Saving to database...");

                    } else {
                        result = "Error: Invalid quantity";
                    }
                } else {
                    result = "Error: Invalid price";
                }
            } else {
                result = "Error: Invalid name";
            }
        } else {
            result = "Error: Order not active";
        }

        return result;
    }

    public double calculateTotalPrice() {
        return price * quantity;
    }

    public void updateDays() {
        days = days + 1;
        if (days > 365) {
            isActive = false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Order Demo ===\n");

        Order order1 = new Order("John Doe", "john@email.com", "555-1234", 1, 15.0, 2, "ES", true);
        System.out.println("Normal Order:");
        System.out.println(order1.process(true, true, false));
        System.out.println();

        Order order2 = new Order("Jane Smith", "jane@email.com", "555-5678", 3, 60.0, 2, "ES", true);
        order2.days = 35;
        System.out.println("Gold Order (Old Customer):");
        System.out.println(order2.process(true, false, true));
    }
}