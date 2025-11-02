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

        Order o = new Order(name, email, phone, customerType, price, quantity, country, true);

        double discount = 0;
        if (customerType == 3 && price * quantity > 100) {
            discount = 0.1;
        } else if (customerType == 2 && price * quantity > 100) {
            discount = 0.08;
        }

        o.p = o.p * (1 - discount);

        if (expressShipping) {
            o.p = o.p + 9.99;
        }
        if (giftWrap) {
            o.p = o.p + 2.99;
        }
        if (insurance) {
            o.p = o.p + 4.99;
        }

        orders.add(o);

        revenue = revenue + (o.p * o.q);
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
        double sub = o.p * o.q;

        if (o.cc.equals("ES")) {
            sub = sub * 1.21;
        } else if (o.cc.equals("FR")) {
            sub = sub * 1.20;
        } else if (o.cc.equals("DE")) {
            sub = sub * 1.19;
        } else {
            sub = sub * 1.15;
        }

        return sub;
    }

    public double calculateDiscount(Order o) {
        double total = o.p * o.q;
        double discount = 0;

        if (o.c == 3 && total > 100) {
            discount = total * 0.1;
        } else if (o.c == 2 && total > 100) {
            discount = total * 0.08;
        }

        return discount;
    }

    public void save(Order o, boolean email, boolean pdf, boolean backup) {
        if (o == null)
            return;
        if (!o.a)
            return;

        double total = getTotalWithTax(o);

        System.out.println("INSERT INTO orders VALUES ('" + o.n + "', " + total + ")");

        if (email) {
            System.out.println("EMAIL: Order confirmed for " + o.n);
            System.out.println("To: " + o.e);
            System.out.println("Total: $" + total);
        }

        if (pdf) {
            System.out.println("PDF: Generating invoice...");
            System.out.println("Customer: " + o.n);
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
                    if (o.a || includeInactive) {
                        report = report + "Order: " + o.n + " - $" + getTotalWithTax(o) + "\n";
                    }
                }
            } else {
                report = "Total Revenue: $" + revenue;
            }
        } else if (type == 2) {
            if (detailed) {
                for (Order o : orders) {
                    if (o.c == 3) {
                        report = report + "GOLD: " + o.n + "\n";
                    } else if (o.c == 2) {
                        report = report + "SILVER: " + o.n + "\n";
                    } else {
                        report = report + "NORMAL: " + o.n + "\n";
                    }
                }
            } else {
                int gold = 0, silver = 0, normal = 0;
                for (Order o : orders) {
                    if (o.c == 3)
                        gold++;
                    else if (o.c == 2)
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
        double avg = 0;
        double max = 0;
        double min = 999999;

        for (Order o : orders) {
            double t = o.p * o.q;
            avg = avg + t;
            if (t > max)
                max = t;
            if (t < min)
                min = t;
        }

        if (orders.size() > 0) {
            avg = avg / orders.size();
        }

        if (print) {
            System.out.println("=== STATISTICS ===");
            System.out.println("Average: $" + avg);
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

    public boolean validateAndProcess(Order o, int action) {
        if (o == null)
            return false;
        if (o.n == null || o.n == "")
            return false;
        if (o.e == null || o.e == "")
            return false;
        if (o.p <= 0)
            return false;
        if (o.q <= 0)
            return false;

        if (action == 1) {
            orders.add(o);
            revenue = revenue + o.p * o.q;
            System.out.println("Order processed normally");
            return true;
        } else if (action == 2) {
            orders.add(0, o);
            revenue = revenue + o.p * o.q * 1.5;
            System.out.println("URGENT order processed");
            return true;
        } else if (action == 3) {
            System.out.println("Order sent for review");
            if (o.p * o.q > 500) {
                System.out.println("High value - needs approval");
                return false;
            }
            orders.add(o);
            revenue = revenue + o.p * o.q;
            return true;
        }

        return false;
    }
}