package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех юзеров: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryUserStorage.findAll();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на создание юзера: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryUserStorage.create(user);
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на апдейт юзера: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return inMemoryUserStorage.update(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable long id, @PathVariable long friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на добавление в друзья: '{} {}'",
                request.getMethod(), request.getRequestURI());
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriend(@Valid @PathVariable long id, @PathVariable long friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на удаление из друзей: '{} {}'",
                request.getMethod(), request.getRequestURI());
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> findFriends(@Valid @PathVariable long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех друзей: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.findFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@Valid @PathVariable long id, @PathVariable long otherId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр общих друзей: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.findCommonFriends(id, otherId);
    }

    @GetMapping(value = "/{id}")
    public User findUserByiD(@PathVariable final long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр юзера по id: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.findUserById(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(final NotFoundException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}