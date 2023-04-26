package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;


@RestController
@Slf4j
public abstract class AbstractController {

    protected HashMap<Integer, Film> films = new HashMap<>();
    protected HashMap<Integer, User> users = new HashMap<>();
    protected int id = 0;

}
