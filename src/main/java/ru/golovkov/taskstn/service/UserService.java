package ru.golovkov.taskstn.service;

import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.security.CustomUserDetails;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll();

    UserSuggestionResponseDto getUserSuggestions(String query);

    UserResponseDto create(UserRequestDto userRequestDto);

    UserResponseDto getUserResponseDtoFromUserDetails(CustomUserDetails user);
}
