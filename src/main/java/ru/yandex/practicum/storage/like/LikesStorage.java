package ru.yandex.practicum.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.dao.impl.UserDaoImpl;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;

import java.util.List;

@Component
public class LikesStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final MpaDao mpaDAO;
    private final FilmDaoImpl filmDaoImpl;
    private final UserDaoImpl userDaoImpl;

    public LikesStorage(JdbcTemplate jdbcTemplate,
                        GenreDao genreDao,
                        MpaDao mpaDAO,
                        FilmDaoImpl filmDaoImpl, UserDaoImpl userDaoImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.mpaDAO = mpaDAO;
        this.userDaoImpl = userDaoImpl;
        this.filmDaoImpl = filmDaoImpl;
    }

    public void addLike(Long id, Long userId) {
        if (!filmDaoImpl.isFilmExists(id)) throw new NotFoundException("Фильм не найден");
        if (!userDaoImpl.isUserExists(userId)) throw new NotFoundException("Юзер не найден");
        String sql = "INSERT INTO LIKES (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, id);
    }

    public void removeLike(Long id, Long userId) {
        if (!filmDaoImpl.isFilmExists(id)) throw new NotFoundException("Фильм не найден");
        if (!userDaoImpl.isUserExists(userId)) throw new NotFoundException("Юзер не найден");
        if (!isLikeExist(userId, id)) throw new NotFoundException("Юзер не добавлял лайк");
        String sql = "DELETE FROM LIKES WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, id);
    }

    private boolean isLikeExist(Long userId, Long filmId) {
        String sql = "SELECT * FROM LIKES WHERE user_id = ? AND film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        return userRows.next();
    }

    public List<Film> getPopular(int count) {
        String sql = "SELECT FILMS.FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA , " +
                "COUNT(L.USER_ID) as RATING FROM FILMS LEFT JOIN LIKES L on FILMS.FILM_ID = L.FILM_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY RATING DESC LIMIT ?";
        System.out.println(count);
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                genreDao.getFilmGenres(rs.getLong("film_id")),
                mpaDAO.getMpa(rs.getInt("mpa")),
                rs.getLong("RATING")
        ), count);
        return films;
    }
}