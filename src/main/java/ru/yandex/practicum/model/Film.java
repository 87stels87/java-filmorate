package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Film {
    int id;

    @NotNull
    String name;
    String description;
    LocalDate releaseDate;
    int duration;

}
