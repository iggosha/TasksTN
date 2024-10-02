package ru.golovkov.taskstn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CheckMeetingPlaceNotWhitespaceValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckMeetingPlaceNotWhitespace {

    String message() default "Неверное значение поля \"Место\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}