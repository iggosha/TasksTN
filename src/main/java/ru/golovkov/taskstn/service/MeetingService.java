package ru.golovkov.taskstn.service;

import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;

import java.util.List;
import java.util.UUID;

public interface MeetingService {

    List<MeetingResponseDto> getAll();

    MeetingResponseDto getById(UUID id);

    MeetingResponseDto create(MeetingRequestDto meetingRequestDto);

    MeetingResponseDto update(MeetingRequestDto meetingRequestDto);

    void deleteById(UUID id);
}


