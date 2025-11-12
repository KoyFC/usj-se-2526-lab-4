import java.util.*;

public class Utils {

    public static double handle(Order o) {
        // increase by tax
        double t = o.price * 1.21;

        o.days = o.days + 1;

        System.out.println("Processing order for " + o.name);

        return t;
    }

    public static boolean isInvalid(Order o) {
        if (o == null)
            return true;
        if (!o.isActive)
            return true;
        if (o.price <= 0)
            return true;
        if (o.quantity <= 0)
            return true;
        if (o.name == null || o.name == "")
            return true;
        return false;
    }

    public static boolean isNotReady(Order o) {
        return isInvalid(o) || o.days < 1;
    }

    public static Order findOrder(List<Order> orders, String name) {
        try {
            for (Order o : orders) {
                if (o.name.equals(name)) {
                    return o;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public static void doStuff(String n, String e, String p, int t, double v) {
        System.out.println("Processing: " + n);

        if (t == 1) {
            System.out.println("Type 1 processing");
            v = v * 1.1;
        } else if (t == 2) {
            System.out.println("Type 2 processing");
            v = v * 1.2;
        }

        if (v > 100) {
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

    public static String fmt(Order o) {
        // we need to format the order for display
        // first get the data
        String s = ""; // string for result

        // add the name
        s = s + o.name;

        // add separator
        s = s + " | ";

        // add email
        s = s + o.email;

        // add another separator
        s = s + " | ";

        // calculate total
        double t = o.price * o.quantity; // t is total

        // add total
        s = s + "$" + t;

        return s;
    }

    public static void process(Order o) {
        try {
            String upper = o.name.toUpperCase();
            System.out.println(upper);
        } catch (NullPointerException e) {
            // do nothing
        } catch (Exception e) {
            // ignore
        }
    }

    public static boolean check(String s, int l, boolean f) {
        if (s == null)
            return false;
        if (s == "")
            return false;
        if (s.length() < l)
            return false;
        if (f == true) {
            if (s.contains(" "))
                return false;
        }
        return true;
    }

    public static void sendNotification(Order o, int type, boolean urgent) {
        if (type == 1) {
            System.out.println("Email sent");
            if (urgent) {
                System.out.println("URGENT!");
            }
        } else if (type == 2) {
            System.out.println("SMS sent");
            if (urgent) {
                System.out.println("HIGH PRIORITY");
            }
        } else if (type == 3) {
            System.out.println("Push notification sent");
        }
    }

    public static double applyFees(double amount, int customerType, boolean premium, boolean express) {
        double fee = 0;

        if (customerType == 1) {
            fee = 2.99;
            if (express) {
                fee = fee + 5.99;
            }
        } else if (customerType == 2) {
            fee = 1.99;
            if (express) {
                fee = fee + 4.99;
            }
        } else if (customerType == 3) {
            fee = 0;
            if (express) {
                fee = fee + 3.99;
            }
        }

        if (premium) {
            fee = fee * 0.5;
        }

        return amount + fee;
    }
}