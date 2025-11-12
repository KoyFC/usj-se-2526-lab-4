package ValueObjects;

public class Phone {
    private final String value;

    public Phone(String number) {
        if (!isValidPhoneNumber(number)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.value = number;
    }

    private boolean isValidPhoneNumber(String number) {
        return number != null && number.matches(
                // Pretty basic regex for phone number validation
                "[+]?(?:\\(\\d+(?:\\.\\d+)?\\)|\\d+(?:\\.\\d+)?)(?:[ -]?(?:\\(\\d+(?:\\.\\d+)?\\)|\\d+(?:\\.\\d+)?))*(?:[ ]?(?:x|ext)\\.?[ ]?\\d{1,5})?");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}