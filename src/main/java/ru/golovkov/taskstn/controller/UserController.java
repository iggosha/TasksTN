package ru.golovkov.taskstn.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import ru.golovkov.taskstn.model.dto.request.SignInRequestDto;
import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/sign-up")
    public UserResponseDto signUp(@RequestBody UserRequestDto userRequestDto) {
        return userService.create(userRequestDto);
    }

    @PostMapping("/sign-in")
    public UserResponseDto signIn(@ParameterObject SignInRequestDto signInRequestDto, HttpServletRequest request) {
        return userService.authenticateAndGetUserResponseDto(signInRequestDto, request);
    }

    @GetMapping("/suggestions")
    public UserSuggestionResponseDto getUserSuggestions(@RequestParam String query) {
        return userService.getUserSuggestions(query);
    }
}
