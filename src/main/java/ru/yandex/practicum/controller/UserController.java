package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    int id = 0;

    @GetMapping()
    public Collection<User> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return users.values();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'",
                request.getMethod(), request.getRequestURI());
        if (user.getEmail().isEmpty() || (!user.getEmail().contains("@"))) {
            throw new ValidationException("email не должен быть пустым, а также должен создержать @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не должен быть пустым, а также долже создержать пробел");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("день рождения не может быть в будущем");
        } else if (user.getName() == null) {
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

    @PutMapping()
    public User update(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'",
                request.getMethod(), request.getRequestURI());
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        } else {
            throw new ValidationException("нет такого id");
        }
        return user;
    }

}