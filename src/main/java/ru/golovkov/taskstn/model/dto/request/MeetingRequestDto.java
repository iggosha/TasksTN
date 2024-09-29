package ru.golovkov.taskstn.model.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingRequestDto {

    private String id;
    private String name;
    private String place;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isFullDay;
    private Boolean isOnline;
    private Boolean isOutlookEvent;
    private String applicantId;
    private String authorEmail;
    private List<String> recipientEmails;
}
