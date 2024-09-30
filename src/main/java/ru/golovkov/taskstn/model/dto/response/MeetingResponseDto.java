package ru.golovkov.taskstn.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MeetingResponseDto {

    private UUID id;
    private String name;
    private String place;
    private String comment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isFullDay;
    private boolean isOnline;
    private boolean isOutlookEvent;
    private boolean isViewMeeting;
    private boolean isStartMeeting;
    private String applicantId;
    private String authorEmail;
    private List<String> recipientEmails;
}
