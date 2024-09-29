package ru.golovkov.taskstn.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.service.MeetingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public List<MeetingResponseDto> getAllMeetings() {
        return meetingService.getAll();
    }

    @PostMapping
    public MeetingResponseDto createMeeting(@RequestBody MeetingRequestDto meetingRequestDto) {
        return meetingService.create(meetingRequestDto);
    }

    @PutMapping
    public MeetingResponseDto updateMeeting(@RequestBody MeetingRequestDto meetingRequestDto) {
        return meetingService.update(meetingRequestDto);
    }

    @DeleteMapping("/{meetingId}")
    public void deleteMeeting(@PathVariable String meetingId) {
        meetingService.deleteById(meetingId);
    }

}
