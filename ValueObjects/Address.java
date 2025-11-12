package ValueObjects;

public class Address {
    private final String streetName;
    private final String city;
    private final String country;

    public Address(String address, String city, String country) {
        this.streetName = validateString(address);
        this.city = validateString(city);
        this.country = validateString(country);
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    private String validateString(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("String cannot be null or empty");
        }
        return string;
    }
}