package ru.yandex.practicum.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import static ru.yandex.practicum.controller.AbstractController.FIRST_DAY_OF_CINEMA;


@Component
public class InMemoryFilmStorage implements FilmStorage {

    public final HashMap<Integer, Film> films = new HashMap<>();
    private static int id = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Имя не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(FIRST_DAY_OF_CINEMA)) {
            throw new ValidationException("Дата не должна быть менее 28 декабря 1895 года");
        } else if (!films.containsKey(film.getId())) {
            films.put(++id, film);
            film.setId(id);
        } else {
            throw new FilmAlreadyExistsException("Фильм уже существует");
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата не должна быть менее 28 декабря 1895 года");
        }
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
        } else {
            throw new ValidationException("id фильма не найден");
        }
        return film;
    }
}
