package Entities;

import ValueObjects.Email;
import ValueObjects.Phone;

import Validator.Validator;

public class Customer {
    String name;
    Email email;
    Phone phone;
    CustomerType type;

    public Customer(String name, String email, String phone, CustomerType type) {
        this.name = Validator.validate(name, v -> v != null && !v.isEmpty(), "Name cannot be null or empty");
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

    public enum CustomerType {
        GOLD,
        SILVER,
        NORMAL
    }
}
