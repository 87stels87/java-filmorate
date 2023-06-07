package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;

import java.util.Set;

public interface GenreDao {

    Set<Genre> getFilmGenres(Long filmId);

    void updateGenresOfFilm(Film film);
}