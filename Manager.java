import java.util.*;

public class Manager {
    private List<Order> orders = new ArrayList<>();
    private double revenue = 0;

    public void createOrder(String name, String email, String phone,
            String address, String city, String country,
            double price, int quantity, int customerType,
            boolean isPremium, boolean expressShipping,
            boolean giftWrap, boolean insurance) {

        if (name == null || name == "")
            return;
        if (email == null || email == "")
            return;
        if (price <= 0)
            return;

        Order order = new Order(name, email, phone, customerType, price, quantity, country, true);

        double discount = 0;
        if (customerType == 3 && price * quantity > 100) {
            discount = 0.1;
        } else if (customerType == 2 && price * quantity > 100) {
            discount = 0.08;
        }

        order.price = order.price * (1 - discount);

        if (expressShipping) {
            order.price = order.price + 9.99;
        }
        if (giftWrap) {
            order.price = order.price + 2.99;
        }
        if (insurance) {
            order.price = order.price + 4.99;
        }

        orders.add(order);

        revenue = revenue + (order.price * order.quantity);
    }

    public String formatPrice(double p, int tier, int qty) {
        double total = p * qty;

        if (tier == 3 && total > 100) {
            total = total * 0.9;
        } else if (tier == 2 && total > 100) {
            total = total * 0.92;
        }

        return "$" + String.format("%.2f", total);
    }

    public double getTotalWithTax(Order o) {
        double subtotal = o.price * o.quantity;

        if (o.country.equals("ES")) {
            subtotal = subtotal * 1.21;
        } else if (o.country.equals("FR")) {
            subtotal = subtotal * 1.20;
        } else if (o.country.equals("DE")) {
            subtotal = subtotal * 1.19;
        } else {
            subtotal = subtotal * 1.15;
        }

        return subtotal;
    }

    public double calculateDiscount(Order order) {
        double total = order.price * order.quantity;
        double discount = 0;

        if (order.customerType == 3 && total > 100) {
            discount = total * 0.1;
        } else if (order.customerType == 2 && total > 100) {
            discount = total * 0.08;
        }

        return discount;
    }

    public void save(Order order, boolean email, boolean pdf, boolean backup) {
        if (order == null)
            return;
        if (!order.isActive)
            return;

        double total = getTotalWithTax(order);

        System.out.println("INSERT INTO orders VALUES ('" + order.name + "', " + total + ")");

        if (email) {
            System.out.println("EMAIL: Order confirmed for " + order.name);
            System.out.println("To: " + order.email);
            System.out.println("Total: $" + total);
        }

        if (pdf) {
            System.out.println("PDF: Generating invoice...");
            System.out.println("Customer: " + order.name);
            System.out.println("Amount: $" + total);
        }

        if (backup) {
            System.out.println("BACKUP: Saving to backup server...");
        }

        revenue = revenue + total;
    }

    public String generateReport(int type, boolean detailed, boolean includeInactive) {
        String report = "";

        if (type == 1) {
            if (detailed) {
                for (Order o : orders) {
                    if (o.isActive || includeInactive) {
                        report = report + "Order: " + o.name + " - $" + getTotalWithTax(o) + "\n";
                    }
                }
            } else {
                report = "Total Revenue: $" + revenue;
            }
        } else if (type == 2) {
            if (detailed) {
                for (Order o : orders) {
                    if (o.customerType == 3) {
                        report = report + "GOLD: " + o.name + "\n";
                    } else if (o.customerType == 2) {
                        report = report + "SILVER: " + o.name + "\n";
                    } else {
                        report = report + "NORMAL: " + o.name + "\n";
                    }
                }
            } else {
                int gold = 0, silver = 0, normal = 0;
                for (Order o : orders) {
                    if (o.customerType == 3)
                        gold++;
                    else if (o.customerType == 2)
                        silver++;
                    else
                        normal++;
                }
                report = "Gold: " + gold + ", Silver: " + silver + ", Normal: " + normal;
            }
        }

        return report;
    }

    public void stats(boolean print, boolean save, boolean email) {
        double average = 0;
        double max = 0;
        double min = 999999;

        for (Order order : orders) {
            double orderTotal = order.price * order.quantity;
            average = average + orderTotal;
            if (orderTotal > max)
                max = orderTotal;
            if (orderTotal < min)
                min = orderTotal;
        }

        if (orders.size() > 0) {
            average = average / orders.size();
        }

        if (print) {
            System.out.println("=== STATISTICS ===");
            System.out.println("Average: $" + average);
            System.out.println("Max: $" + max);
            System.out.println("Min: $" + min);
            System.out.println("Total Orders: " + orders.size());
        }

        if (save) {
            System.out.println("Saving statistics to file...");
        }

        if (email) {
            System.out.println("Emailing statistics to admin@company.com");
        }
    }

    public boolean validateAndProcess(Order order, int action) {
        if (order == null)
            return false;
        if (order.name == null || order.name == "")
            return false;
        if (order.email == null || order.email == "")
            return false;
        if (order.price <= 0)
            return false;
        if (order.quantity <= 0)
            return false;

        if (action == 1) {
            orders.add(order);
            revenue = revenue + order.price * order.quantity;
            System.out.println("Order processed normally");
            return true;
        } else if (action == 2) {
            orders.add(0, order);
            revenue = revenue + order.price * order.quantity * 1.5;
            System.out.println("URGENT order processed");
            return true;
        } else if (action == 3) {
            System.out.println("Order sent for review");
            if (order.price * order.quantity > 500) {
                System.out.println("High value - needs approval");
                return false;
            }
            orders.add(order);
            revenue = revenue + order.price * order.quantity;
            return true;
        }

        return false;
    }
}