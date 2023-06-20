package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface UserDao {

    Optional<User> findUserById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void addFriend(Long id, Long friendId);

    Collection<User> findFriends(Long id);

    void deleteFriend(Long id, Long friendId);

    void deleteFriend(Long id);

}