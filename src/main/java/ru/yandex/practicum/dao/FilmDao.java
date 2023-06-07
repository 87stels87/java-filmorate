package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmDao {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> getById(Long id);
}