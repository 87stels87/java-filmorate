package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public Optional<User> getUserByID(@PathVariable final long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр юзера по id: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.getUserByID(id);
    }


    @GetMapping()
    public List<User> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех юзеров: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.findAll();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на создание юзера: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.create(user);

    }

    @PutMapping()
    public User update(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на апдейт юзера: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.update(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable long id, @PathVariable long friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на добавление в друзья: '{} {}'",
                request.getMethod(), request.getRequestURI());
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@Valid @PathVariable long id, @PathVariable long friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на удаление из друзей: '{} {}'",
                request.getMethod(), request.getRequestURI());
        userService.deleteUsers(id, friendId);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUsers(@Valid @PathVariable long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на удаление юзера: '{} {}'",
                request.getMethod(), request.getRequestURI());
        userService.deleteUsers(id);
    }

    @GetMapping(value = "/{id}/friends")
    public Collection<User> getFriends(@Valid @PathVariable long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр всех друзей: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.getFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@Valid @PathVariable Long id, @PathVariable Long otherId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту на просмотр общих друзей: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return userService.getCommonFriends(id, otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(final NotFoundException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}