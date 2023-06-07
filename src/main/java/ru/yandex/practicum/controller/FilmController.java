package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    public static final LocalDate FIRST_DAY_OF_CINEMA = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех фильмов: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return filmService.findAll();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на создание фильма: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на апдейт фильма: '{} {}",
                request.getMethod(), request.getRequestURI());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос на получение фильма по id: '{} {}",
                request.getMethod(), request.getRequestURI());
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(required = false) Integer count) {
        log.info("Лучшие фильмы, count = {}", count);
        if (count == null) count = 10;
        return filmService.getFilmsByRating(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Запрос на добавление лайка, user id = {} ставит лайк film id = {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteMapping(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Запрос на удаление лайка, user id = {} удаляет лайк с film id = {}", userId, id);
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