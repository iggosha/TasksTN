package ru.golovkov.taskstn.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.golovkov.taskstn.model.dto.request.SignInRequestDto;
import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.model.entity.User;
import ru.golovkov.taskstn.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Slf4j
public class UserServiceImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = createUserObject();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
    }

    private User createUserObject() {
        return new User(null, "password", UUID.randomUUID() + "@example.com", "Test FIO", "photo",
                null, null, null);
    }

    @Test
    @Transactional
    void testGetAll() {
        List<UserResponseDto> users = userService.getAll();
        assertFalse(users.isEmpty());
    }

    @Test
    @Transactional
    void testCreate() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("newuser@example.com");
        userRequestDto.setPassword("newpassword");
        userRequestDto.setFio("New User");
        userRequestDto.setPhoto("newphoto");

        UserResponseDto createdUser = userService.create(userRequestDto);
        assertNotNull(createdUser);
        assertEquals(userRequestDto.getEmail(), createdUser.getEmail());
    }

    @Test
    void testCreate_UserAlreadyExists() {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(existingUser.getEmail());
        userRequestDto.setPassword("newpassword");
        userRequestDto.setFio("New User");
        userRequestDto.setPhoto("newphoto");
        assertThrows(DataIntegrityViolationException.class, () -> userService.create(userRequestDto));
    }

    @Test
    @Transactional
    void testGetUserSuggestions() {
        String query = "Test";
        UserSuggestionResponseDto userSuggestions = userService.getUserSuggestions(query);
        assertNotNull(userSuggestions);
        assertFalse(userSuggestions.getUserSuggestionItemList().isEmpty());
    }

    @Test
    @Transactional
    void testAuthenticateAndGetUserResponseDto() {
        SignInRequestDto signInRequestDto = new SignInRequestDto();
        signInRequestDto.setEmail(user.getEmail());
        signInRequestDto.setPassword("password");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));

        UserResponseDto authenticatedUser = userService.authenticateAndGetUserResponseDto(signInRequestDto, request);
        assertNotNull(authenticatedUser);
        assertEquals(user.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    @Transactional
    void testAuthenticateAndGetUserResponseDto_InvalidCredentials() {
        SignInRequestDto signInRequestDto = new SignInRequestDto();
        signInRequestDto.setEmail(user.getEmail());
        signInRequestDto.setPassword("wrongpassword");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));

        assertThrows(BadCredentialsException.class, () -> userService.authenticateAndGetUserResponseDto(signInRequestDto, request));
    }
}
