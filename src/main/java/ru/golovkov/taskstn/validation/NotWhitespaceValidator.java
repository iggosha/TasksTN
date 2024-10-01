package ru.golovkov.taskstn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NotWhitespaceValidator implements ConstraintValidator<NotWhitespace, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String string) {
            return isValidString(string);
        } else if (value instanceof List) {
            return isValidList((List<String>) value);
        }
        return false;
    }

    private boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isValidList(List<String> value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        for (String string : value) {
            if (!isValidString(string)) {
                return false;
            }
        }
        return true;
    }
}
