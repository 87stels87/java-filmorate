package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> getUserByID(long id) {
        if (userDao.findUserById(id).isEmpty()) {
            throw new NotFoundException("с таким id юзера нет");
        } else {
            return userDao.findUserById(id);
        }
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User create(User user) {
        return userDao.create(user);

    }

    public User update(User user) {
        return userDao.update(user);
    }

    public void addFriend(long id, long friendId) {
        userDao.addFriend(id, friendId);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        return getFriends(id).stream()
                .filter(x -> getFriends(otherId).contains(x))
                .collect(Collectors.toList());
    }

    public Collection<User> getFriends(Long id) {
        return userDao.findFriends(id);
    }

    public void deleteUsers(long id, long friendId) {
        userDao.deleteFriend(id, friendId);
    }

    public void deleteUsers(Long id) {
        userDao.deleteFriend(id);
    }
}