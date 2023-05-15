package ru.yandex.practicum.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;

import java.util.Collection;

@Component
public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User findUserById(long id);
}