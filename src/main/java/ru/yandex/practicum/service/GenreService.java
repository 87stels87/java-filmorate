package ru.yandex.practicum.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Genre;

import java.util.Collection;

@Component
public class GenreService {
    private final JdbcTemplate jdbcTemplate;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES",
                ((rs, rowNum) -> new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre"))
                ));
    }

    public Genre getGenre(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT GENRE FROM GENRES WHERE GENRE_ID = ?", id);
        if (userRows.next()) {
            Genre genre = new Genre(
                    id,
                    userRows.getString("genre")
            );
            return genre;
        } else throw new NotFoundException("Жанр по id не найден");
    }
}