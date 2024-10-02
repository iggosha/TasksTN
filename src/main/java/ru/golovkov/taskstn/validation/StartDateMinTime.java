package ru.golovkov.taskstn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartDateMinTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartDateMinTime {

    String message() default "Минимальное время начала встречи 08:00";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
