package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {

    private long id;
    private Set<Long> likes;

    @NotEmpty
    private String name;

    private String description;

    private LocalDate releaseDate;

    private Set<Genre> genre;

    private Rating rating;

    @Positive
    private int duration;

    public Film(Set<Long> likes, @NotEmpty String name, String description, LocalDate releaseDate, Set<Genre> genre, Rating rating, @Positive int duration) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genre = genre;
        this.rating = rating;
        if (likes == null) {
            this.likes = new HashSet<>();
        }
    }
}