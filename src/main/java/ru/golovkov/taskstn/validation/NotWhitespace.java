package ru.golovkov.taskstn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotWhitespaceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotWhitespace {

    String message() default "Введено недопустимое значение поля";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}