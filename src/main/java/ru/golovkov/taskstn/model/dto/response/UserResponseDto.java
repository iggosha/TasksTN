package ru.golovkov.taskstn.model.dto.response;

import lombok.Data;

@Data
public class UserResponseDto {

    private String id;
    private String email;
    private String fio;
    private String photo;
}

