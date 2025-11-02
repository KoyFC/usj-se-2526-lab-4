public class Order {
    // this is the name
    public String n; // name
    public String e; // email
    public String t; // phone
    public int c; // customer type
    public double p; // price
    public int q; // qty
    public String cc; // country
    public boolean a; // active
    public int d; // days

    public Order(String n, String e, String t, int c, double p, int q, String cc, boolean a) {
        this.n = n;
        this.e = e;
        this.t = t;
        this.c = c;
        this.p = p;
        this.q = q;
        this.cc = cc;
        this.a = a;
        this.d = 0;
    }

    public String proc(boolean flag, boolean email, boolean pdf) {
        String result = "";

        if (a == true) {
            if (n != null && n != "") {
                if (p > 0) {
                    if (q > 0) {
                        double total = p * q;

                        if (c == 3) {
                            if (total > 100) {
                                if (d > 30) {
                                    total = total * 0.85;
                                } else {
                                    total = total * 0.9;
                                }
                            } else if (total > 50) {
                                total = total * 0.95;
                            }
                        } else if (c == 2) {
                            if (total > 100) {
                                total = total * 0.92;
                            } else if (total > 75) {
                                total = total * 0.96;
                            }
                        }

                        if (cc.equals("ES")) {
                            total = total * 1.21;
                        } else if (cc.equals("FR")) {
                            total = total * 1.20;
                        } else if (cc.equals("DE")) {
                            total = total * 1.19;
                        } else if (cc.equals("UK")) {
                            total = total * 1.20;
                        } else {
                            total = total * 1.15;
                        }

                        if (total < 25) {
                            total = total + 5.99;
                        }

                        result = "Order #" + System.currentTimeMillis() + "\n";
                        result = result + "Customer: " + n + "\n";
                        result = result + "Email: " + e + "\n";
                        result = result + "Phone: " + t + "\n";
                        result = result + "Items: " + q + " x $" + p + "\n";
                        result = result + "Total: $" + String.format("%.2f", total) + "\n";

                        if (flag == true) {
                            if (email == true) {
                                System.out.println("Sending email to " + e);
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

    public double calc() {
        return p * q;
    }

    public void upd() {
        d = d + 1;
        if (d > 365) {
            a = false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Order Demo ===\n");

        Order o1 = new Order("John Doe", "john@email.com", "555-1234", 1, 15.0, 2, "ES", true);
        System.out.println("Normal Order:");
        System.out.println(o1.proc(true, true, false));
        System.out.println();

        Order o2 = new Order("Jane Smith", "jane@email.com", "555-5678", 3, 60.0, 2, "ES", true);
        o2.d = 35;
        System.out.println("Gold Order (Old Customer):");
        System.out.println(o2.proc(true, false, true));
    }
}