import java.util.*;

import Entities.Order;
import Entities.Customer;
import ValueObjects.CartItem;
import ValueObjects.OrderFlags;

public class Utils {
    public static double handleOrder(Order order) {
        double total = order.getCartItem().getPrice() * 1.21;
        order.updateDays();
        System.out.println("Processing order for " + order.getCustomer().getName());
        return total;
    }

    public static boolean isInvalid(Order order) {
        if (order == null || !order.isActive())
            return true;
        return false;
    }

    public static boolean isNotReady(Order order) {
        return isInvalid(order) || order.getDays() < 1;
    }

    public static Order findOrderByName(List<Order> orders, String name) {
        if (orders == null)
            return null;
        Order result = orders.stream().filter(order -> name.equals(order.getCustomer().getName())).findAny()
                .orElse(null);
        return result;
    }

    public static void processValue(Order order) {
        if (order == null)
            return;

        Customer customer = order.getCustomer();
        if (customer == null)
            return;

        CartItem cartItem = order.getCartItem();
        if (cartItem == null)
            return;

        System.out.println("Processing: " + customer.getName());
        double value = cartItem.getPrice();

        if (customer.getType() == Customer.CustomerType.NORMAL) {
            System.out.println("Type 1 processing");
            value = value * 1.1;
        } else if (customer.getType() == Customer.CustomerType.SILVER) {
            System.out.println("Type 2 processing");
            value = value * 1.2;
        }

        if (value > 100) {
            System.out.println("High value alert!");
        }
    }

    public static boolean validateString(String text, int length) {
        if (text == null || text == "" || text.length() < length) {
            return false;
        }
        return true;
    }

    public static boolean validateStringNoSpaces(String text, int length) {
        if (validateString(text, length) || text.contains(" ")) {
            return false;
        }
        return true;
    }

    public enum NotificationType {
        EMAIL,
        SMS,
        PUSH
    }

    public static void sendNotification(Order order, NotificationType type, boolean isUrgent) {
        if (type == NotificationType.EMAIL) {
            System.out.println("Email sent");
            if (isUrgent) {
                System.out.println("URGENT!");
            }
        } else if (type == NotificationType.SMS) {
            System.out.println("SMS sent");
            if (isUrgent) {
                System.out.println("HIGH PRIORITY");
            }
        } else if (type == NotificationType.PUSH) {
            System.out.println("Push notification sent");
        }
    }

    public static double applyFees(double amount, Customer.CustomerType type, OrderFlags.ShippingType shipping) {
        double fee = 0.0;

        switch (type) {
            case SILVER:
                fee = 2.99;
                if (shipping == OrderFlags.ShippingType.EXPRESS) {
                    fee += 5.99;
                }
                break;
            case NORMAL:
                fee = 1.99;
                if (shipping == OrderFlags.ShippingType.EXPRESS) {
                    fee += 4.99;
                }
                break;
            case GOLD:
                fee = 0.0;
                if (shipping == OrderFlags.ShippingType.EXPRESS) {
                    fee += 3.99;
                }
                break;
        }

        return amount + fee;
    }
}