package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping()
    public Collection<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех фильмов: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на создание фильма: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на апдейт фильма: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryFilmStorage.update(film);
    }
}