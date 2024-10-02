package ru.golovkov.taskstn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class StartDateMinTimeValidator implements ConstraintValidator<StartDateMinTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime minTime = value.toLocalDate().atTime(8, 0);
        return !value.isBefore(minTime);
    }
}
