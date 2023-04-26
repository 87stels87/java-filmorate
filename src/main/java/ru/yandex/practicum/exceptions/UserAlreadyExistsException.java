package ru.yandex.practicum.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String s) {
        System.out.println(s);
    }
}
