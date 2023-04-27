package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.util.HashMap;


@RestController
@Slf4j
public abstract class AbstractController {

    public HashMap<Integer, Film> films = new HashMap<>();
    public HashMap<Integer, User> users = new HashMap<>();
    protected int id = 0;

}
