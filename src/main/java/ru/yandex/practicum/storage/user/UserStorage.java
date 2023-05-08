package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.util.Collection;

public interface UserStorage {

     Collection<User> findAll();

     User create(User user);

     User update(User user);
}
