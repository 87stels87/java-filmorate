package ru.yandex.practicum.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserDbStorage implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {} {}", userRows.getLong("ID"),
                    userRows.getString("NAME"), userRows.getString("LOGIN"));
            User user = new User(
                    userRows.getLong("ID"),
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME"),
                    userRows.getDate("BIRTHDAY").toLocalDate());
            return Optional.of(user);
        } else {
            log.info("Пользователь с id {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users");
        ArrayList<User> userslist = new ArrayList<>();
        while (userRows.next()) {
            User user = new User(
                    userRows.getLong("ID"),
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME"),
                    userRows.getDate("BIRTHDAY").toLocalDate());
            userslist.add(user);
        }
        return userslist;
    }

    @Override
    public User create(User user) {
        if (user.getEmail().isEmpty() || (!user.getEmail().contains("@"))) {
            throw new ValidationException("email не должен быть пустым, а также должен создержать @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не должен быть пустым, а также должен создержать пробел");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("день рождения не может быть в будущем");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("USERS")
                    .usingGeneratedKeyColumns("id");
            user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        } else {
            SimpleJdbcInsert simpleJdbcInsert1 = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("USERS")
                    .usingGeneratedKeyColumns("id");
            user.setId(simpleJdbcInsert1.executeAndReturnKey(user.toMap()).longValue());
            log.info("Новый юзер добавлен {}", user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (isUserExists(user.getId())) {
            String sqlQuery = "UPDATE USERS SET " +
                    "email = ?, login = ?, name = ?, birthday = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Юзер {} обновлен", user);
            return user;
        } else {
            throw new NotFoundException("Юзер для апдейта не найден");
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        if (isUserExists(id) && isUserExists(friendId)) {
            String sql = "INSERT INTO FRIENDS (user_id, friends_id, is_approved) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, id, friendId, false);
            log.info("user {} add user {} to friends", id, friendId);
        } else {
            throw new NotFoundException("Юзер для добавления в друзья не найден");
        }
    }

    @Override
    public Collection<User> findFriends(Long id) {
        String sql = "SELECT F.FRIENDS_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY FROM FRIENDS F JOIN USERS U " +
                "on F.friends_id = U.id WHERE " +
                "F.user_id = ?";
        if (isUserExists(id)) {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                            rs.getLong("friends_id"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getDate("birthday").toLocalDate()),
                    id
            );
        } else {
            throw new NotFoundException("Юзер не найден");
        }
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        if (isUserExists(id) && isUserExists(friendId)) {
            String sql = "DELETE FROM FRIENDS WHERE user_id = ? AND friends_id = ?";
            jdbcTemplate.update(sql, id, friendId);
        } else {
            throw new NotFoundException("Юзер для удаления из друзей не найден");
        }
    }

    @Override
    public void deleteFriend(Long id) {
        if (isUserExists(id)) {
            String sql = "DELETE FROM USERS WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } else {
            throw new NotFoundException("Юзер для удаления из друзей не найден");
        }
    }

    public boolean isUserExists(Long id) {
        String sql = "SELECT * FROM USERS WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        return userRows.first();
    }
}