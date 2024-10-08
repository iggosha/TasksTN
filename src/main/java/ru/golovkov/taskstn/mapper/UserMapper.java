package ru.golovkov.taskstn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.golovkov.taskstn.model.dto.request.UserRequestDto;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.dto.response.UserSuggestionResponseDto;
import ru.golovkov.taskstn.model.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toResponseDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> users);

    @Mapping(source = "email", target = "valueId")
    @Mapping(source = "fio", target = "value")
    @Mapping(constant = "USER", target = "directoryType")
    UserSuggestionResponseDto.UserSuggestionItem toUserSuggestionItem(User user);

    List<UserSuggestionResponseDto.UserSuggestionItem> toUserSuggestionItemList(List<User> userList);

}
