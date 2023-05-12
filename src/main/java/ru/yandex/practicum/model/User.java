package ru.yandex.practicum.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;
    private Set<Long> friends;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    public User(Set<Long> friends, @NotEmpty @Email String email, @NotEmpty String login, String name, @PastOrPresent LocalDate birthday) {

        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        if (friends == null) {
            this.friends = new HashSet<>();
        }

    }
}