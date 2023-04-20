package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @GetMapping()
    public List<User> findAll() {
        return users;
    }

    @PostMapping()
    public List<User> create(@RequestBody User user){
        users.add(user);
        return users;
    }

}