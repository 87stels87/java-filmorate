package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {

    private int id;

    @NotEmpty
    private String name;

    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

}