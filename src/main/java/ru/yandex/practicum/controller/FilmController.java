package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends AbstractController {


    @GetMapping()
    public Collection<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех фильмов: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return films.values();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на создание фильма: '{} {}'",
                request.getMethod(), request.getRequestURI());
        if (film.getName().isEmpty()) {
            throw new ValidationException("Имя не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата не должна быть менее 28 декабря 1895 года");
        } else if (!films.containsKey(film.getId())) {
            films.put(++id, film);
            film.setId(id);
        } else {
            throw new FilmAlreadyExistsException("Фильм уже существует");
        }
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на апдейт фильма: '{} {}'",
                request.getMethod(), request.getRequestURI());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата не должна быть менее 28 декабря 1895 года");
        } else if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
        } else {
            throw new ValidationException("id фильма не найден");
        }
        return film;
    }
}