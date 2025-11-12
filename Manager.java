import java.util.*;

import Entities.Customer;
import Entities.Order;
import ValueObjects.Address;
import ValueObjects.OrderFlags;

public class Manager {
    private List<Order> orders = new ArrayList<>();
    private double revenue = 0;

    public void createOrder(Customer customer, Address address, double price, int quantity, OrderFlags flags) {

        if (customer == null || customer.getName() == "")
            return;
        if (customer.getEmail() == null || customer.getEmail() == "")
            return;
        if (price <= 0)
            return;

        Order order = new Order(customer, price, quantity, address, flags, true);

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

    public void save(Order order, boolean email, boolean pdf, boolean backup) {
        if (order == null)
            return;
        if (!order.isActive)
            return;

        double total = order.getTotalWithTax();

        System.out.println("INSERT INTO orders VALUES ('" + order.customer.getName() + "', " + total + ")");

        if (email) {
            System.out.println("EMAIL: Order confirmed for " + order.customer.getName());
            System.out.println("To: " + order.customer.getEmail());
            System.out.println("Total: $" + total);
        }

        if (pdf) {
            System.out.println("PDF: Generating invoice...");
            System.out.println("Customer: " + order.customer.getName());
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
                for (Order order : orders) {
                    if (order.isActive || includeInactive) {
                        report = report + "Order: " + order.customer.getName() + " - $" + order.getTotalWithTax()
                                + "\n";
                    }
                }
            } else {
                report = "Total Revenue: $" + revenue;
            }
        } else if (type == 2) {
            if (detailed) {
                for (Order order : orders) {

                    switch (order.customer.getType()) {
                        case GOLD:
                            report = report + "GOLD: " + order.customer.getName() + "\n";
                            break;
                        case SILVER:
                            report = report + "SILVER: " + order.customer.getName() + "\n";
                            break;
                        case NORMAL:
                            report = report + "NORMAL: " + order.customer.getName() + "\n";
                            break;
                        default:
                            report = report + "NORMAL: " + order.customer.getName() + "\n";
                            break;
                    }
                }
            } else {
                int goldCount = 0, silverCount = 0, normalCount = 0;
                for (Order order : orders) {

                    switch (order.customer.getType()) {
                        case GOLD:
                            goldCount++;
                            break;
                        case SILVER:
                            silverCount++;
                            break;
                        case NORMAL:
                            normalCount++;
                            break;
                        default:
                            normalCount++;
                            break;
                    }
                }
                report = "Gold: " + goldCount + ", Silver: " + silverCount + ", Normal: " + normalCount;
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