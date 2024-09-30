package ru.golovkov.taskstn.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.service.MeetingService;
import ru.golovkov.taskstn.service.UserService;

@RestController
@RequiredArgsConstructor
public class ExtraneousController {

    private final UserService userService;
    private final MeetingService meetingService;

    @GetMapping("/directories/suggestions")
    public UserSuggestionResponseDto userSuggestionResponseDto(@RequestParam String query) {
//        "APPLICANT"
        return userService.getUserSuggestions(query);
    }


    @GetMapping("/event/{eventId}")
    public MeetingResponseDto getMeetingById(@PathVariable(name = "eventId") String meetingId) {
        return meetingService.getById(meetingId);
    }
}
