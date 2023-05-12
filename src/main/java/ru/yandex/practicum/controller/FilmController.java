package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public static final LocalDate FIRST_DAY_OF_CINEMA = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
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

    @GetMapping(value = "/{id}")
    public Film findFilmByiD(@PathVariable final long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр фильма по id: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryFilmStorage.findFilmById(id);
    }

    @GetMapping(value = "/popular")
    public List<Film> findPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр популярных фильмов: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return filmService.findPopularFilms(count);
    }


    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable long id, @PathVariable long userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на установку лайка '{} {}'",
                request.getMethod(), request.getRequestURI());
        filmService.addLike(id, userId);
    }


    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable long id, @PathVariable long userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на удаление лайка '{} {}'",
                request.getMethod(), request.getRequestURI());
        filmService.removeLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(final NotFoundException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}