package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
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

}