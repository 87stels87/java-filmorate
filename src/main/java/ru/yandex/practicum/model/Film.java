package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    int id;

    @NotEmpty
    String name;

    @Size(max = 200)
    String description;

    LocalDate releaseDate;

    @Positive
    int duration;

}