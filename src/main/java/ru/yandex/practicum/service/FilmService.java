package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private Film findFilmOrThrow(long id) {
        Film film = filmStorage.findFilmById(id);
        if (film == null) {
            throw new NotFoundException("с таким id фильма нет");
        }
        return film;
    }

    private void checkUserExistsOrThrow(long userId) {
        if (userStorage.findUserById(userId) == null) {
            throw new NotFoundException("с таким id юзера нет");
        }
    }

    public void addLike(long id, long userId) {
        findFilmOrThrow(id);
        checkUserExistsOrThrow(userId);
        filmStorage.findFilmById(id).getLikes().add(userId);
    }

    public void removeLike(long id, long userId) {
        findFilmOrThrow(id);
        checkUserExistsOrThrow(userId);
        filmStorage.findFilmById(id).getLikes().remove(userId);
    }

    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.findAll().stream().sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}