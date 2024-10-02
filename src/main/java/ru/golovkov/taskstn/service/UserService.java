package ru.golovkov.taskstn.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.golovkov.taskstn.model.dto.request.SignInRequestDto;
import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll();

    UserSuggestionResponseDto getUserSuggestions(String query);

    UserResponseDto create(UserRequestDto userRequestDto);

    UserResponseDto authenticateAndGetUserResponseDto(SignInRequestDto signInRequestDto, HttpServletRequest request);
}
