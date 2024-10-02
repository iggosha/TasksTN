package ru.golovkov.taskstn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;

public class CheckMeetingPlaceNotWhitespaceValidator implements ConstraintValidator<CheckMeetingPlaceNotWhitespace, MeetingRequestDto> {

    @Override
    public boolean isValid(MeetingRequestDto meetingRequestDto, ConstraintValidatorContext context) {
        String place = meetingRequestDto.getPlace();
        Boolean isOnline = meetingRequestDto.getIsOnline();
        if (isOnline != null && isOnline) {
            if (place == null || !place.startsWith("http")) {
                context.buildConstraintViolationWithTemplate("Неверное значение, ссылка должна начинаться с http")
                        .addPropertyNode("place")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        } else {
            if (place == null || place.trim().isEmpty()) {
                context.buildConstraintViolationWithTemplate("Введено недопустимое значение поля \"Место встречи\"")
                        .addPropertyNode("place")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        }
        return true;
    }
}