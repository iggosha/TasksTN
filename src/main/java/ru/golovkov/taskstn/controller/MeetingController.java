package ru.golovkov.taskstn.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.service.MeetingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
@Validated
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public List<MeetingResponseDto> getAllMeetings() {
        return meetingService.getAll();
    }

    @PostMapping
    public MeetingResponseDto createMeeting(@Valid @RequestBody MeetingRequestDto meetingRequestDto) {
        return meetingService.create(meetingRequestDto);
    }

    @PutMapping
    public MeetingResponseDto updateMeeting(@Valid @RequestBody MeetingRequestDto meetingRequestDto) {
        return meetingService.update(meetingRequestDto);
    }

    @DeleteMapping("/{meetingId}")
    public void deleteMeeting(@PathVariable UUID meetingId) {
        meetingService.deleteById(meetingId);
    }
}
