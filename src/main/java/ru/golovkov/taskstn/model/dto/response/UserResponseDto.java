package ru.golovkov.taskstn.model.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDto {

    private UUID id;
    private String email;
    private String fio;
    private String photo;
}

