import Entities.Order;
import Entities.Customer;
import ValueObjects.Address;
import ValueObjects.OrderFlags;

public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Running Tests ===\n");

        testOrderCreation();
        testOrderProcessing();
        testManagerOperations();
        testUtilsMethods();

        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));
    }

    private static void testOrderCreation() {
        System.out.println("Testing Order Creation...");

        Address address = new Address("123 Main St", "Madrid", "ES");
        OrderFlags flags = new OrderFlags(OrderFlags.ShippingType.STANDARD, OrderFlags.WrapType.NONE,
                OrderFlags.InsuranceType.NONE);
        Order order = new Order(new Customer("John", "john@email.com", "555-1234", Customer.CustomerType.SILVER), 10.0,
                2, address, flags, true);

        assertEqual(order.customer.getName(), "John", "Order name");
        assertEqual(order.price, 10.0, "Order price");
        assertEqual(order.quantity, 2, "Order quantity");
        assertEqual(order.address.getCountry(), "ES", "Order country");

        System.out.println();
    }

    private static void testOrderProcessing() {
        System.out.println("Testing Order Processing...");

        Order o1 = new Order("Alice", "alice@email.com", "555-5678", 3, 60.0, 2, "ES", true);
        String result1 = o1.process(true, false, false);
        assertNotNull(result1, "Gold order processing");
        assertContains(result1, "130", "Gold order with tax");

        Order o2 = new Order("Bob", "bob@email.com", "555-9999", 1, 10.0, 1, "FR", true);
        String result2 = o2.process(true, false, false);
        assertNotNull(result2, "Normal order processing");
        assertContains(result2, "17", "Normal order with shipping");

        Order o3 = new Order("Charlie", "charlie@email.com", "555-0000", 2, 80.0, 2, "DE", true);
        String result3 = o3.process(false, false, false);
        assertNotNull(result3, "Silver order processing");
        assertContains(result3, "175", "Silver order with discount and tax");

        System.out.println();
    }

    private static void testManagerOperations() {
        System.out.println("Testing Manager Operations...");

        Manager mgr = new Manager();
        mgr.createOrder("David", "david@email.com", "555-1111",
                "123 Main St", "Madrid", "ES",
                50.0, 2, 2, false, false, false, false);

        Order o = new Order("Test", "test@email.com", "555-0000", 2, 50.0, 2, "ES", true);
        double total = mgr.getTotalWithTax(o);
        assertEqual(total, 121.0, "Manager tax calculation");

        double discount = mgr.calculateDiscount(o);
        assertEqual(discount, 0.0, "Manager discount calculation");

        String formatted = mgr.formatPrice(50.0, 2, 2);
        assertContains(formatted, "$100", "Manager price formatting");

        String report = mgr.generateReport(1, false, false);
        assertContains(report, "Revenue", "Manager report generation");

        System.out.println();
    }

    private static void testUtilsMethods() {
        System.out.println("Testing Utils Methods...");

        Order o = new Order("Emily", "emily@email.com", "555-2222", 1, 20.0, 3, "UK", true);

        boolean invalid = Utils.isInvalid(o);
        assertEqual(invalid, false, "Valid order check");

        double handled = Utils.handle(o);
        assertEqual(handled, 24.2, "Utils handle calculation");

        Order nullOrder = Utils.findOrder(null, "NonExistent");
        assertNull(nullOrder, "Utils find non-existent order");

        double calc = Utils.calc(10, 5, 1);
        assertEqual(calc, 15.0, "Utils calc addition");

        String formatted = Utils.formatOrder(o);
        assertContains(formatted, "Emily", "Utils format contains name");
        assertContains(formatted, "60", "Utils format contains total");

        boolean check = Utils.validateString("test", 3, false);
        assertEqual(check, true, "Utils check validation");

        double withFees = Utils.applyFees(100, 1, false, true);
        assertEqual(withFees, 108.98, "Utils fee calculation");

        System.out.println();
    }

    private static void assertEqual(Object actual, Object expected, String testName) {
        if (actual.equals(expected)) {
            System.out.println("✓ " + testName);
            passed++;
        } else {
            System.out.println("✗ " + testName + " - Expected: " + expected + ", Got: " + actual);
            failed++;
        }
    }

    private static void assertNotNull(Object obj, String testName) {
        if (obj != null) {
            System.out.println("✓ " + testName);
            passed++;
        } else {
            System.out.println("✗ " + testName + " - Expected non-null value");
            failed++;
        }
    }

    private static void assertNull(Object obj, String testName) {
        if (obj == null) {
            System.out.println("✓ " + testName);
            passed++;
        } else {
            System.out.println("✗ " + testName + " - Expected null value");
            failed++;
        }
    }

    private static void assertContains(String str, String substring, String testName) {
        if (str != null && str.contains(substring)) {
            System.out.println("✓ " + testName);
            passed++;
        } else {
            System.out.println("✗ " + testName + " - String doesn't contain: " + substring);
            failed++;
        }
    }
}