package ru.yandex.practicum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.like.LikesStorage;

import java.util.Collection;
import java.util.List;


@Service
public class FilmService {

    private final FilmDao filmDao;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(FilmDao filmDao, LikesStorage likesStorage) {
        this.filmDao = filmDao;
        this.likesStorage = likesStorage;
    }

    public Collection<Film> findAll() {
        return filmDao.findAll();
    }

    public Film create(Film film) {
        return filmDao.create(film);
    }

    public Film updateFilm(Film film) {
        return filmDao.update(film);
    }

    public Film getFilmById(Long id) {
        return filmDao.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм по id не найден"));
    }

    public List<Film> getFilmsByRating(int count) {
        return likesStorage.getPopular(count);
    }

    public void addLike(Long id, Long userId) {
        likesStorage.addLike(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        likesStorage.removeLike(id, userId);
    }
}