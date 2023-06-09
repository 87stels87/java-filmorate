package ru.yandex.practicum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;

import java.util.Set;
import java.util.TreeSet;

@Component
public class GenreDaoImpl implements GenreDao {

    JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT GENRES.GENRE_ID, GENRE FROM FILM_GENRES JOIN GENRES " +
                "ON FILM_GENRES.GENRE_ID = GENRES.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return new TreeSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre")),
                filmId
        ));
    }

    @Override
    public void updateGenresOfFilm(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE film_id = ?", film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }
}