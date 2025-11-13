import java.util.*;

import Entities.Customer;
import Entities.Order;
import ValueObjects.Address;
import ValueObjects.CartItem;
import ValueObjects.OrderFlags;
import ValueObjects.SaveFlags;
import ValueObjects.StatsFlags;

public class Manager {
    private List<Order> orders = new ArrayList<>();
    private double revenue = 0;

    private HashMap<Customer.CustomerType, Double> discountRates = new HashMap<>() {
        {
            put(Customer.CustomerType.GOLD, 0.1);
            put(Customer.CustomerType.SILVER, 0.08);
            put(Customer.CustomerType.NORMAL, 0.0);
        }
    };

    public void createOrder(Customer customer, Address address, CartItem cartItem, OrderFlags flags) {
        Order order = new Order(customer, cartItem, address, flags, true);

        orders.add(order);

        revenue = revenue + cartItem.getPrice() * cartItem.getQuantity();
    }

    public String formatPrice(CartItem cartItem, Customer.CustomerType tier) {
        double total = cartItem.getPrice() * cartItem.getQuantity();
        if (total > 100) {
            double discountFactor = 1 - discountRates.getOrDefault(tier, 0.0);
            total = total * discountFactor;
        }

        return "$" + String.format("%.2f", total);
    }

    public void save(Order order, SaveFlags flags) {
        if (order == null)
            return;
        if (!order.isActive())
            return;

        double total = order.getTotalWithTax();

        System.out.println("INSERT INTO orders VALUES ('" + order.getCustomer().getName() + "', " + total + ")");

        if (flags.isEmail()) {
            System.out.println("EMAIL: Order confirmed for " + order.getCustomer().getName());
            System.out.println("To: " + order.getCustomer().getEmail());
            System.out.println("Total: $" + total);
        }

        if (flags.isPdf()) {
            System.out.println("PDF: Generating invoice...");
            System.out.println("Customer: " + order.getCustomer().getName());
            System.out.println("Amount: $" + total);
        }

        if (flags.isBackup()) {
            System.out.println("BACKUP: Saving to backup server...");
        }

        revenue = revenue + total;
    }

    public enum ReportType {
        ORDERS,
        CUSTOMER_TYPES
    }

    public String generateReport(ReportType type, boolean detailed, boolean includeInactive) {
        String report = "";

        switch (type) {
            case ORDERS:
                report = detailed ? buildOrdersReportDetailed(includeInactive) : buildOrdersReportSummary();
                break;
            case CUSTOMER_TYPES:
                report = detailed ? buildCustomerTypesReportDetailed() : buildCustomerTypesReportSummary();
                break;
            default:
                break;
        }

        return report;
    }

    private String buildOrdersReportDetailed(boolean includeInactive) {
        String report = "";
        for (Order order : orders) {
            if (order.isActive() || includeInactive) {
                report = report + "Order: " + order.getCustomer().getName() + " - $"
                        + String.format("%.2f", order.getTotalWithTax()) + "\n";
            }
        }
        return report;
    }

    private String buildOrdersReportSummary() {
        return String.format("Total Revenue: $%.2f", revenue);
    }

    private String buildCustomerTypesReportDetailed() {
        String report = "";
        for (Order order : orders) {
            report = report + order.getCustomer().getType().name() + ": " + order.getCustomer().getName() + "\n";
        }
        return report;
    }

    private String buildCustomerTypesReportSummary() {
        EnumMap<Customer.CustomerType, Integer> counts = new EnumMap<>(Customer.CustomerType.class);
        for (Order order : orders) {
            Customer.CustomerType typeKey = order.getCustomer().getType();
            counts.put(typeKey, counts.getOrDefault(typeKey, 0) + 1);
        }
        return "Gold: " + counts.getOrDefault(Customer.CustomerType.GOLD, 0) + ", Silver: "
                + counts.getOrDefault(Customer.CustomerType.SILVER, 0) + ", Normal: "
                + counts.getOrDefault(Customer.CustomerType.NORMAL, 0);
    }

    private double[] computeStats() {
        double sum = 0;
        double max = 0;
        double min = Double.MAX_VALUE;

        for (Order order : orders) {
            double orderTotal = order.getCartItem().getPrice() * order.getCartItem().getQuantity();
            sum += orderTotal;
            if (orderTotal > max)
                max = orderTotal;
            if (orderTotal < min)
                min = orderTotal;
        }

        double average = 0;
        if (orders.size() > 0) {
            average = sum / orders.size();
        } else {
            min = 0;
        }

        return new double[] { average, max, min };
    }

    public void stats(StatsFlags flags) {
        double average = 0;
        double max = 0;
        double min = 999999;

        for (Order order : orders) {
            double orderTotal = order.getCartItem().getPrice() * order.getCartItem().getQuantity();
            average = average + orderTotal;
            if (orderTotal > max)
                max = orderTotal;
            if (orderTotal < min)
                min = orderTotal;
        }

        if (orders.size() > 0) {
            average = average / orders.size();
        }

        if (flags.shouldPrint()) {
            System.out.println("=== STATISTICS ===");
            System.out.println("Average: $" + average);
            System.out.println("Max: $" + max);
            System.out.println("Min: $" + min);
            System.out.println("Total Orders: " + orders.size());
        }

        if (flags.shouldSave()) {
            System.out.println("Saving statistics to file...");
        }

        if (flags.shouldSendEmail()) {
            System.out.println("Emailing statistics to admin@company.com");
        }
    }

    public enum ActionType {
        NORMAL,
        URGENT,
        REVIEW
    }

    public boolean validateAndProcess(Order order, ActionType action) {
        if (order == null)
            return false;

        CartItem cartItem = order.getCartItem();
        double price = cartItem.getPrice();
        int qty = cartItem.getQuantity();

        if (price <= 0 || qty <= 0)
            return false;

        switch (action) {
            case NORMAL:
                return processNormal(order, price, qty);
            case URGENT:
                return processUrgent(order, price, qty);
            case REVIEW:
                return processReview(order, price, qty);
            default:
                return false;
        }
    }

    private boolean processNormal(Order order, double price, int qty) {
        orders.add(order);
        revenue = revenue + price * qty;
        System.out.println("Order processed normally");
        return true;
    }

    private boolean processUrgent(Order order, double price, int qty) {
        orders.add(0, order);
        revenue = revenue + price * qty * 1.5;
        System.out.println("URGENT order processed");
        return true;
    }

    private boolean processReview(Order order, double price, int qty) {
        System.out.println("Order sent for review");
        if (price * qty > 500) {
            System.out.println("High value - needs approval");
            return false;
        }
        orders.add(order);
        revenue = revenue + price * qty;
        return true;
    }
}