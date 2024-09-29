package ru.golovkov.taskstn.mapper;

import org.mapstruct.Mapper;
import ru.golovkov.taskstn.model.dto.response.UserResponseDto;
import ru.golovkov.taskstn.model.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserResponseDto> toResponseDtoList(List<User> users);

}
