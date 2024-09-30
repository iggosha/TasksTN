package ru.golovkov.taskstn.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovkov.taskstn.exception.ResourceNotFoundException;
import ru.golovkov.taskstn.mapper.MeetingMapper;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.model.entity.Meeting;
import ru.golovkov.taskstn.model.entity.User;
import ru.golovkov.taskstn.repository.MeetingRepository;
import ru.golovkov.taskstn.repository.UserRepository;
import ru.golovkov.taskstn.service.MeetingService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingMapper meetingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MeetingResponseDto> getAll() {
        return meetingMapper.toResponseDtoList(meetingRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingResponseDto getById(UUID id) {
        return meetingMapper.toResponseDto(getMeetingById(id));
    }

    @Override
    public MeetingResponseDto create(MeetingRequestDto meetingRequestDto) {
        Meeting meeting = meetingMapper.toEntity(meetingRequestDto);
        setMeetingParticipantsFromRequestDto(meetingRequestDto, meeting);
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
    public void deleteById(UUID id) {
        meetingRepository.deleteById(id);
    }

    private void setMeetingParticipantsFromRequestDto(MeetingRequestDto meetingRequestDto, Meeting meeting) {
        meeting.setAuthor(userRepository
                .findByEmail(meetingRequestDto.getAuthorEmail())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("No author was found with email: " + meetingRequestDto.getAuthorEmail())
                )
        );
        meeting.setApplicant(userRepository
                .findById(UUID.fromString(meetingRequestDto.getApplicantId()))
                .orElseThrow(() -> new ResourceNotFoundException
                        ("No applicant was found with id: " + meetingRequestDto.getApplicantId())
                )
        );
        List<User> foundByEmailsRecipients = userRepository.findAllByEmailIn(meetingRequestDto.getRecipientEmails());
        if (foundByEmailsRecipients.isEmpty()) {
            throw new ResourceNotFoundException
                    ("No recipients were found with emails: " + meetingRequestDto.getRecipientEmails());
        }
        meeting.setRecipients(foundByEmailsRecipients);
    }

    private Meeting getMeetingById(UUID id) {
        return meetingRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("No meeting was found with id: " + id)
                );
    }
}
