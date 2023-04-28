package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.HashMap;


@RestController
@Slf4j
public abstract class AbstractController {

    public final HashMap<Integer, Film> films = new HashMap<>();
    public final HashMap<Integer, User> users = new HashMap<>();
    protected static final LocalDate FIRST_DAY_OF_CINEMA = LocalDate.of(1895, 12, 28);
    protected int id = 0;

}
