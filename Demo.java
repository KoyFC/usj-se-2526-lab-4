import Entities.Order;
import Entities.Customer;

public class Demo {

        public static void main(String[] args) {
                System.out.println("=== RESTAURANT ORDER SYSTEM DEMO ===\n");

                // Create and process a normal order
                Customer customerOrder1 = new Customer("John Smith", "john@email.com", "555-1234",
                                Customer.CustomerType.SILVER);

                Order order1 = new Order("John Smith", "john@email.com", "555-1234",
                                1, 25.0, 2, "ES", true);
                System.out.println(order1.process(true, true, false));

                // Gold customer order with discount
                Order order2 = new Order("Mary VIP", "mary@email.com", "555-5678",
                                3, 60.0, 2, "ES", true);
                order2.days = 35; // Old customer
                System.out.println(order2.process(false, false, true));

                // Use Manager to handle orders
                Manager manager = new Manager();

                manager.createOrder("Customer 1", "customer1@email.com", "555-1111",
                                "Main Street 1", "Madrid", "ES",
                                30.0, 2, 1, false, false, false, false);

                manager.createOrder("VIP Customer", "vip@email.com", "555-2222",
                                "Central Avenue", "Barcelona", "ES",
                                150.0, 3, 3, true, true, true, false);

                // Generate statistics and reports
                manager.stats(true, false, false);
                System.out.println(manager.generateReport(2, false, false));

                // Validate and process order
                Order order3 = new Order("Test Customer", "test@email.com", "555-9999",
                                1, 100.0, 2, "ES", true);
                manager.validateAndProcess(order3, 1);

                // Use some utilities
                Utils.handle(order3);
                Utils.formatOrder(order3);
                Utils.calc(100, 21, 3);
                Utils.applyFees(100, 1, false, true);

                System.out.println("\n=== END OF DEMO ===");
        }
}