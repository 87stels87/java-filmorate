package ru.yandex.practicum.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.dao.MpaDao;

import java.util.*;

import static ru.yandex.practicum.controller.FilmController.FIRST_DAY_OF_CINEMA;

@Slf4j
@Component
public class FilmDbStorage implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private MpaDao mpaDao;
    private GenreDao genreDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM FILMS ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                genreDao.getFilmGenres(rs.getLong("film_id")),
                mpaDao.getMpa(rs.getInt("mpa"))
        ));
    }

    @Override
    public Film create(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Имя не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(FIRST_DAY_OF_CINEMA)) {
            throw new ValidationException("Дата не должна быть менее 28 декабря 1895 года");
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("FILMS")
                    .usingGeneratedKeyColumns("film_id");
            film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?)",
                            film.getId(), genre.getId());
                }
            }
            jdbcTemplate.update("UPDATE FILMS SET MPA = ? WHERE FILM_ID = ?",
                    film.getMpa().getId(),
                    film.getId()
            );
            log.info("Новый фильм успешно добавлен {}", film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isFilmExists(film.getId())) {
            String sqlQuery = "UPDATE FILMS SET " +
                    "name = ?, description = ?, release_Date = ?, duration = ?, " +
                    "mpa = ? WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            genreDao.updateGenresOfFilm(film);
            log.info("Фильм {} успешно обновлен. id фильма {}", film, film.getId());
            return film;
        } else {
            throw new NotFoundException(String.format("Фильм с таким id не найден ", film.getId()));
        }
    }

    @Override
    public Optional<Film> getById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE film_id = ?", id);
        if (userRows.next()) {
            Film film = new Film(
                    userRows.getLong("film_id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("release_Date").toLocalDate(),
                    userRows.getInt("duration")
            );
            Integer rateMpa = userRows.getInt("mpa");
            film.setMpa(mpaDao.getMpa(rateMpa));
            Set<Genre> genres = genreDao.getFilmGenres(id);
            if (genres.size() != 0) {
                film.setGenres(genreDao.getFilmGenres(id));
            } else {
                film.setGenres(new HashSet<>());
            }
            log.info("Фильм с данным id найден", film);
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    public boolean isFilmExists(Long id) {
        String sql = "SELECT * FROM FILMS WHERE film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        return userRows.next();
    }
}