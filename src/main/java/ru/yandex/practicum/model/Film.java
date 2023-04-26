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

    private int id;

    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

}