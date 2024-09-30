package ru.golovkov.taskstn.model.dto.request;

import lombok.Data;

@Data
public class SignInRequestDto {

    private String email;
    private String password;
}
