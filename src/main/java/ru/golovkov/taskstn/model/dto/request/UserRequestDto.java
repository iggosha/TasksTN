package ru.golovkov.taskstn.model.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {

    private String email;
    private String password;
    private String fio;
    private String photo;
}
