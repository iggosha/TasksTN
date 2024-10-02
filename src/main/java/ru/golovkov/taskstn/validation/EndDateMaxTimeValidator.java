package ru.golovkov.taskstn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EndDateMaxTimeValidator implements ConstraintValidator<EndDateMaxTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime maxTime = value.toLocalDate().atTime(19, 0);
        return !value.isAfter(maxTime);
    }
}
