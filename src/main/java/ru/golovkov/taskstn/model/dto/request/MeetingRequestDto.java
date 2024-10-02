package ru.golovkov.taskstn.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.golovkov.taskstn.validation.CheckMeetingPlaceNotWhitespace;
import ru.golovkov.taskstn.validation.EndDateMaxTime;
import ru.golovkov.taskstn.validation.NotWhitespace;
import ru.golovkov.taskstn.validation.StartDateMinTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@CheckMeetingPlaceNotWhitespace
public class MeetingRequestDto {

    private UUID id;

    @NotWhitespace(message = "Введено недопустимое значение поля \"Название встречи\"")
    private String name;

    private String place;

    @NotWhitespace(message = "Введено недопустимое значение поля \"Комментарий\"")
    private String comment;

    @NotNull(message = "Дата начала обязательна для заполнения.")
    @StartDateMinTime
    private LocalDateTime startDate;

    @NotNull(message = "Дата окончания обязательна для заполнения.")
    @EndDateMaxTime
    private LocalDateTime endDate;

    private Boolean isFullDay;

    private Boolean isOnline;
    private Boolean isOutlookEvent;
    private String applicantId;
    private String authorEmail;

    @NotWhitespace(message = "Введено недопустимое значение поля \"Пригласить участника\"")
    private List<String> recipientEmails;
}
