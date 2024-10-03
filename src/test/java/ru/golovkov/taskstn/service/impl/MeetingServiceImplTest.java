package ru.golovkov.taskstn.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.golovkov.taskstn.exception.ResourceNotFoundException;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.model.entity.Meeting;
import ru.golovkov.taskstn.model.entity.User;
import ru.golovkov.taskstn.repository.MeetingRepository;
import ru.golovkov.taskstn.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class MeetingServiceImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17").withDatabaseName("testdb").withUsername("test").withPassword("test");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MeetingServiceImpl meetingService;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    private User applicant;
    private User author;
    private User recipient;
    private final UUID nonExistentId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        applicant = createUserObject("Applicant FIO");
        author = createUserObject("Author FIO");
        recipient = createUserObject("Recipient FIO");
        applicant = userRepository.save(applicant);
        author = userRepository.save(author);
        recipient = userRepository.save(recipient);
    }

    private User createUserObject(String fio) {
        return new User(null, "password", UUID.randomUUID() + "@example.com", fio, "photo",
                null, null, null);
    }

    private Meeting createMeetingObject() {
        return new Meeting(null, "Meeting Name", "Meeting Place",
                "Meeting Comment", LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                true, true, true, true,
                true, applicant, author, List.of(recipient));
    }

    private MeetingRequestDto createMeetingRequestDto() {
        MeetingRequestDto meetingRequestDto = new MeetingRequestDto();
        meetingRequestDto.setName("New Meeting");
        meetingRequestDto.setPlace("New Place");
        meetingRequestDto.setComment("New Comment");
        meetingRequestDto.setStartDate(LocalDateTime.now());
        meetingRequestDto.setEndDate(LocalDateTime.now().plusHours(1));
        meetingRequestDto.setIsFullDay(true);
        meetingRequestDto.setIsOnline(true);
        meetingRequestDto.setIsOutlookEvent(true);
        meetingRequestDto.setApplicantId(applicant.getId());
        meetingRequestDto.setAuthorEmail(author.getEmail());
        meetingRequestDto.setRecipientEmails(List.of(recipient.getEmail()));
        return meetingRequestDto;
    }

    @Test
    @Transactional
    void testGetAllMeetings() {
        Meeting meeting = createMeetingObject();
        meetingRepository.save(meeting);
        List<MeetingResponseDto> meetings = meetingService.getAll();
        assertFalse(meetings.isEmpty());
    }

    @Test
    @Transactional
    void testGetMeetingById() {
        Meeting meeting = createMeetingObject();
        meeting = meetingRepository.save(meeting);

        MeetingResponseDto meetingResponseDto = meetingService.getById(meeting.getId());
        assertNotNull(meetingResponseDto);
        assertEquals(meeting.getName(), meetingResponseDto.getName());
    }

    @Test
    @Transactional
    void testGetMeetingById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> meetingService.getById(nonExistentId));
    }

    @Test
    @Transactional
    void testCreateMeeting() {
        MeetingRequestDto meetingRequestDto = createMeetingRequestDto();

        MeetingResponseDto createdMeeting = meetingService.create(meetingRequestDto);
        assertNotNull(createdMeeting);
        assertEquals(meetingRequestDto.getName(), createdMeeting.getName());
    }

    @Test
    @Transactional
    void testCreateMeeting_ApplicantNotFound() {
        MeetingRequestDto meetingRequestDto = createMeetingRequestDto();
        meetingRequestDto.setApplicantId(nonExistentId);

        assertThrows(ResourceNotFoundException.class, () -> meetingService.create(meetingRequestDto));
    }

    @Test
    @Transactional
    void testUpdateMeeting() {
        Meeting meeting = createMeetingObject();
        meeting = meetingRepository.save(meeting);

        MeetingRequestDto meetingRequestDto = createMeetingRequestDto();
        meetingRequestDto.setId(meeting.getId());
        meetingRequestDto.setName("Updated Meeting");

        MeetingResponseDto updatedMeeting = meetingService.update(meetingRequestDto);
        assertNotNull(updatedMeeting);
        assertEquals(meetingRequestDto.getName(), updatedMeeting.getName());
    }

    @Test
    @Transactional
    void testUpdateMeeting_NotFound() {
        MeetingRequestDto meetingRequestDto = createMeetingRequestDto();
        meetingRequestDto.setId(nonExistentId);

        assertThrows(ResourceNotFoundException.class, () -> meetingService.update(meetingRequestDto));
    }

    @Test
    @Transactional
    void testDeleteMeetingById() {
        Meeting meeting = createMeetingObject();
        meeting = meetingRepository.save(meeting);

        meetingService.deleteById(meeting.getId());
        assertFalse(meetingRepository.existsById(meeting.getId()));
    }

    @Test
    @Transactional
    void testDeleteMeetingById_NotFound() {
        assertDoesNotThrow(() -> meetingService.deleteById(nonExistentId));
    }
}
