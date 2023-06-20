package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.service.GenreService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;


@RestController
@Validated
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAll(HttpServletRequest request) {
        log.info("Запрос на получение всех жанров", request.getMethod(), request.getRequestURI());
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id, HttpServletRequest request) {
        log.info("Запрос на получение жанра по id = {}", request.getMethod(), request.getRequestURI());
        return genreService.getGenre(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(final NotFoundException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}


