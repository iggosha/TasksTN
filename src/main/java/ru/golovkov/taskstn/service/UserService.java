package ru.golovkov.taskstn.service;

import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll();

    UserSuggestionResponseDto getUserSuggestions(String query);
}
