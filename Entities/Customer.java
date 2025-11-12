package Entities;

import ValueObjects.Email;
import ValueObjects.Phone;

public class Customer {
    String name;
    Email email;
    Phone phone;
    CustomerType type;

    public Customer(String name, String email, String phone, CustomerType type) {
        this.name = validateName(name);
        this.email = new Email(email);
        this.phone = new Phone(phone);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPhone() {
        return phone.getValue();
    }

    public CustomerType getType() {
        return type;
    }

    String validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return name;
    }

    public enum CustomerType {
        GOLD,
        SILVER,
        NORMAL
    }
}
