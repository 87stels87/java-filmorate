package ru.yandex.practicum.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    public final HashMap<Long, User> users = new HashMap<>();
    private static long id = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
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
            users.put(++id, user);
            user.setId(id);
        } else if (!users.containsKey(user.getId())) {
            users.put(++id, user);
            user.setId(id);
        } else {
            throw new UserAlreadyExistsException("Юзер уже существует");
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.replace(user.getId(), user);
        } else {
            throw new ValidationException("нет такого id");
        }
        return user;
    }

    @Override
    public User findUserById(long id) {
        return users.get(id);
    }
}