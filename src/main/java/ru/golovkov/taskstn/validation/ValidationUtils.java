package ru.golovkov.taskstn.validation;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean matchesEmailPattern(String string) {
        return Pattern.matches(ValidationUtils.EMAIL_PATTERN, string);
    }
}
