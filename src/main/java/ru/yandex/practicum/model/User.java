package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor

public class User {
    int id;
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
