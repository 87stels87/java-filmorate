package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;


@RestController
@Slf4j
public abstract class AbstractController {
    public static final LocalDate FIRST_DAY_OF_CINEMA = LocalDate.of(1895, 12, 28);
}
