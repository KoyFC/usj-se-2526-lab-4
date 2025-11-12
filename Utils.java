import java.util.*;

public class Utils {

    public static double handle(Order order) {
        // increase by tax
        double total = order.price * 1.21;

        order.days = order.days + 1;

        System.out.println("Processing order for " + order.name);

        return total;
    }

    public static boolean isInvalid(Order order) {
        if (order == null)
            return true;
        if (!order.isActive)
            return true;
        if (order.price <= 0)
            return true;
        if (order.quantity <= 0)
            return true;
        if (order.name == null || order.name == "")
            return true;
        return false;
    }

    public static boolean isNotReady(Order order) {
        return isInvalid(order) || order.days < 1;
    }

    public static Order findOrder(List<Order> orders, String name) {
        try {
            for (Order order : orders) {
                if (order.name.equals(name)) {
                    return order;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public static void processValue(String name, int type, double value) {
        System.out.println("Processing: " + name);

        if (type == 1) {
            System.out.println("Type 1 processing");
            value = value * 1.1;
        } else if (type == 2) {
            System.out.println("Type 2 processing");
            value = value * 1.2;
        }

        if (value > 100) {
            System.out.println("High value alert!");
        }
    }

    public static double calc(double a, double b, int op) {
        double result = 0;

        switch (op) {
            case 1:
                result = a + b;
                System.out.println("Addition: " + result);
                break;
            case 2:
                result = a - b;
                System.out.println("Subtraction: " + result);
                break;
            case 3:
                result = a * b;
                System.out.println("Multiplication: " + result);
                break;
            case 4:
                if (b != 0) {
                    result = a / b;
                    System.out.println("Division: " + result);
                } else {
                    System.out.println("Error: Division by zero");
                    return -999999;
                }
                break;
            default:
                System.out.println("Unknown operation");
                return -888888;
        }

        return result;
    }

    public static String formatOrder(Order order) {
        String resultString = order.name + " | " + order.email + " | $" + order.calculateTotalPrice();
        return resultString;
    }

    public static void processOrder(Order order) {
        try {
            String upper = order.name.toUpperCase();
            System.out.println(upper);
        } catch (NullPointerException e) {
            // do nothing
        } catch (Exception e) {
            // ignore
        }
    }

    public static boolean validateString(String text, int length, boolean blockSpaces) {
        if (text == null)
            return false;
        if (text == "")
            return false;
        if (text.length() < length)
            return false;
        if (blockSpaces == true) {
            if (text.contains(" "))
                return false;
        }
        return true;
    }

    public static void sendNotification(Order order, int type, boolean isUrgent) {
        if (type == 1) {
            System.out.println("Email sent");
            if (isUrgent) {
                System.out.println("URGENT!");
            }
        } else if (type == 2) {
            System.out.println("SMS sent");
            if (isUrgent) {
                System.out.println("HIGH PRIORITY");
            }
        } else if (type == 3) {
            System.out.println("Push notification sent");
        }
    }

    public static double applyFees(double amount, int customerType, boolean isPremium, boolean isExpress) {
        double fee = 0;

        if (customerType == 1) {
            fee = 2.99;
            if (isExpress) {
                fee = fee + 5.99;
            }
        } else if (customerType == 2) {
            fee = 1.99;
            if (isExpress) {
                fee = fee + 4.99;
            }
        } else if (customerType == 3) {
            fee = 0;
            if (isExpress) {
                fee = fee + 3.99;
            }
        }

        if (isPremium) {
            fee = fee * 0.5;
        }

        return amount + fee;
    }
}