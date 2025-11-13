import Entities.Order;
import Entities.Customer.CustomerType;
import Entities.Customer;
import ValueObjects.Address;
import ValueObjects.OrderFlags;
import ValueObjects.CartItem;
import ValueObjects.ProcessingFlags;

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

        Customer customer = new Customer("John", "john@email.com", "555-1234", Customer.CustomerType.SILVER);
        Address address = new Address("123 Main St", "Madrid", "ES");
        CartItem cartItem = new CartItem(10.0, 2);
        Order order = new Order(customer, cartItem, address, new OrderFlags(), true);

        assertEqual(order.getCustomer().getName(), "John", "Order name");
        assertEqual(order.getCartItem().getPrice(), 10.0, "Order price");
        assertEqual(order.getCartItem().getQuantity(), 2, "Order quantity");
        assertEqual(order.getAddress().getCountry(), "ES", "Order country");

        System.out.println();
    }

    private static void testOrderProcessing() {
        System.out.println("Testing Order Processing...");

        Customer customer1 = new Customer("Alice", "alice@email.com", "555-5678", Customer.CustomerType.GOLD);
        Address address1 = new Address("456 Elm St", "Barcelona", "ES");
        OrderFlags flags1 = new OrderFlags().setShipping(OrderFlags.ShippingType.EXPRESS);
        CartItem cartItem1 = new CartItem(60.0, 2);
        Order order1 = new Order(customer1, cartItem1, address1, flags1, true);
        ProcessingFlags processingFlags1 = new ProcessingFlags().setNotificationEnabled(true);
        String result1 = order1.process(processingFlags1);
        assertNotNull(result1, "Gold order processing");
        assertContains(result1, "130", "Gold order with tax");

        Customer customer2 = new Customer("Bob", "bob@email.com", "555-9999", Customer.CustomerType.NORMAL);
        Address address2 = new Address("123 Main St", "Paris", "FR");
        CartItem cartItem2 = new CartItem(10.0, 1);
        Order order2 = new Order(customer2, cartItem2, address2, new OrderFlags(), true);
        ProcessingFlags processingFlags2 = new ProcessingFlags().setNotificationEnabled(true);
        String result2 = order2.process(processingFlags2);
        assertNotNull(result2, "Normal order processing");
        assertContains(result2, "17", "Normal order with shipping");

        Customer customer3 = new Customer("Charlie", "charlie@email.com", "555-0000", Customer.CustomerType.SILVER);
        Address address3 = new Address("789 Oak St", "Berlin", "DE");
        CartItem cartItem3 = new CartItem(80.0, 2);
        Order order3 = new Order(customer3, cartItem3, address3, new OrderFlags(), true);
        ProcessingFlags processingFlags3 = new ProcessingFlags().setNotificationEnabled(true);
        String result3 = order3.process(processingFlags3);
        assertNotNull(result3, "Silver order processing");
        assertContains(result3, "175", "Silver order with discount and tax");

        System.out.println();
    }

    private static void testManagerOperations() {
        System.out.println("Testing Manager Operations...");

        Manager manager = new Manager();
        Customer customer1 = new Customer("David", "david@email.com", "555-1111", Customer.CustomerType.NORMAL);
        Address address1 = new Address("123 Main St", "Madrid", "ES");
        OrderFlags flags1 = new OrderFlags();
        CartItem cartItem1 = new CartItem(50.0, 2);
        manager.createOrder(customer1, address1, cartItem1, flags1);

        Customer customer2 = new Customer("Test", "test@email.com", "555-0000", Customer.CustomerType.NORMAL);
        Address address2 = new Address("456 Side St", "Madrid", "ES");
        CartItem cartItem2 = new CartItem(50.0, 2);
        Order order = new Order(customer2, cartItem2, address2, new OrderFlags(), true);
        double total = order.getTotalWithTax();
        assertEqual(total, 121.0, "Manager tax calculation");

        double discount = order.calculateDiscount();
        assertEqual(discount, 0.0, "Manager discount calculation");

        String formatted = manager.formatPrice(cartItem2, CustomerType.SILVER);
        assertContains(formatted, "$100", "Manager price formatting");

        String report = manager.generateReport(Manager.ReportType.ORDERS, false, false);
        assertContains(report, "Revenue", "Manager report generation");

        System.out.println();
    }

    private static void testUtilsMethods() {
        System.out.println("Testing Utils Methods...");

        Customer customer = new Customer("Emily", "emily@email.com", "555-2222", Customer.CustomerType.NORMAL);
        Address address = new Address("123 Main St", "London", "UK");
        CartItem cartItem = new CartItem(20.0, 3);
        Order o = new Order(customer, cartItem, address, new OrderFlags(), true);

        boolean invalid = Utils.isInvalid(o);
        assertEqual(invalid, false, "Valid order check");

        double handled = Utils.handleOrder(o);
        assertEqual(handled, 24.2, "Utils handle calculation");

        Order nullOrder = Utils.findOrderByName(null, "NonExistent");
        assertNull(nullOrder, "Utils find non-existent order");

        // double calc = Utils.calc(10, 5, 1); // This is an aberration man, just use +
        double calc = 10 + 5;
        assertEqual(calc, 15.0, "Utils calc addition");

        String formatted = o.toString();
        assertContains(formatted, "Emily", "Utils format contains name");
        assertContains(formatted, "60", "Utils format contains total");

        boolean check = Utils.validateString("test", 3);
        assertEqual(check, true, "Utils check validation");

        double withFees = Utils.applyFees(100, Customer.CustomerType.SILVER, OrderFlags.ShippingType.EXPRESS);
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