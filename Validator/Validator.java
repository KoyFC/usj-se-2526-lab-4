package Validator;

import java.util.function.Predicate;

public class Validator {

    public static <T> T validate(T value, Predicate<? super T> condition, String errorMessage) {
        if (!condition.test(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }
}