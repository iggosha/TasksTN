package ru.golovkov.taskstn.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovkov.taskstn.exception.ResourceNotFoundException;
import ru.golovkov.taskstn.mapper.MeetingMapper;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.model.entity.Meeting;
import ru.golovkov.taskstn.repository.MeetingRepository;
import ru.golovkov.taskstn.repository.UserRepository;
import ru.golovkov.taskstn.service.MeetingService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingMapper meetingMapper;

    @Override
    public List<MeetingResponseDto> getAll() {
        return meetingMapper.toResponseDtoList(meetingRepository.findAll());
    }

    @Override
    public MeetingResponseDto getById(String id) {
        return meetingMapper.toResponseDto(getMeetingById(id));
    }

    @Override
    public MeetingResponseDto create(MeetingRequestDto meetingRequestDto) {
        Meeting meeting = meetingMapper.toEntity(meetingRequestDto);
        meeting.setAuthor(userRepository.findByEmail(meetingRequestDto.getAuthorEmail()));
        meeting.setApplicant(userRepository.findById(meetingRequestDto.getApplicantId()).orElseThrow());
        meeting.setRecipients(userRepository.findAllById(meetingRequestDto.getRecipientEmails()));
        Meeting savedMeeting = meetingRepository.save(meeting);
        return meetingMapper.toResponseDto(savedMeeting);
    }

    @Override
    public MeetingResponseDto update(MeetingRequestDto meetingRequestDto) {
        Meeting meetingEntity = getMeetingById(meetingRequestDto.getId());
        meetingMapper.updateEntityFromRequestDto(meetingEntity, meetingRequestDto);
        Meeting updatedMeeting = meetingRepository.save(meetingEntity);
        return meetingMapper.toResponseDto(updatedMeeting);
    }

    @Override
    public void deleteById(String id) {
        meetingRepository.deleteById(id);
    }

    private Meeting getMeetingById(String id) {
        return meetingRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found"));
    }
}
