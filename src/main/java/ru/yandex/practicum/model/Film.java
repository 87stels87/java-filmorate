package ru.yandex.practicum.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;

}
