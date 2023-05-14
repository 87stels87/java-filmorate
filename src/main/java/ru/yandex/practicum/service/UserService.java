package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    protected UserStorage userStorage;


    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private void checkUserExistsOrThrow(long id) {
        if (id < 1) {
            throw new NotFoundException("с таким id юзера нет");
        } else if (id > userStorage.findAll().size()) {
            throw new NotFoundException("с таким id юзера нет");
        }
    }

    private void checkFriendExistsOrThrow(long friendId) {
        if (friendId < 1) {
            throw new NotFoundException("с таким id юзера нет");
        } else if (friendId > userStorage.findAll().size()) {
            throw new NotFoundException("с таким id юзера нет");
        }
    }

    public void addFriend(long id, long friendId) {
        checkUserExistsOrThrow(id);
        checkFriendExistsOrThrow(friendId);
        if (id == friendId) {
            throw new NotFoundException("нельзя добавить себя в друзья");
        } else {
            userStorage.findUserById(id).getFriends().add(friendId);
            userStorage.findUserById(friendId).getFriends().add(id);
        }
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.findUserById(id).getFriends().remove(friendId);
        userStorage.findUserById(friendId).getFriends().remove(id);
    }

    public List<User> findFriends(long id) {
        List<User> friends = new ArrayList<>();
        if (!userStorage.findUserById(id).getFriends().isEmpty()) {
            for (long n : userStorage.findUserById(id).getFriends()) {
                friends.add(userStorage.findUserById(n));
            }
        }
        return friends;
    }

    public List<User> findCommonFriends(long id, long otherId) {
        List<User> friends = new ArrayList<>();
        if (id >= 1 || otherId >= 1) {
            Set<Long> userFriends = userStorage.findUserById(id).getFriends();
            Set<Long> otherFriends = userStorage.findUserById(otherId).getFriends();
            List<Long> commonFriends = userFriends.stream()
                    .filter(otherFriends::contains)
                    .collect(Collectors.toList());
            for (Long commonFriend : commonFriends) {
                friends.add(userStorage.findUserById(commonFriend));
            }
            return friends;
        } else {
            throw new NotFoundException("с таким id юзера нет");
        }
    }

    public User findUserById(long id) {
        if (userStorage.findUserById(id) == null) {
            throw new NotFoundException("с таким id юзера нет");
        } else {
            return userStorage.findUserById(id);
        }
    }
}