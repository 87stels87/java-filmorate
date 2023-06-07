package ru.yandex.practicum.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Mpa;

import java.util.Collection;


@Component
public class MpaService {

    private final JdbcTemplate jdbcTemplate;

    public MpaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getMpa() {
        return jdbcTemplate.query("SELECT * FROM MPA",
                ((rs, rowNum) -> new Mpa(
                        rs.getInt("mpa_id"),
                        rs.getString("mpa_name"))
                ));
    }

    public Mpa getMpaById(int id) {
        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet("SELECT MPA_NAME FROM MPA WHERE MPA_ID = ?", id);

        if (userRows.next()) {
            Mpa mpa = new Mpa(
                    id,
                    userRows.getString("mpa_name")
            );
            return mpa;
        } else {
            throw new NotFoundException("Attempt to get mpa with absent id");
        }
    }
}