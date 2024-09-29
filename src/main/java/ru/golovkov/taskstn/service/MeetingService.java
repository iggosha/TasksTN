package ru.golovkov.taskstn.service;

import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;

import java.util.List;

public interface MeetingService {

    List<MeetingResponseDto> getAll();

    MeetingResponseDto getById(String id);

    MeetingResponseDto create(MeetingRequestDto meetingRequestDto);

    MeetingResponseDto update(MeetingRequestDto meetingRequestDto);

    void deleteById(String id);
}


