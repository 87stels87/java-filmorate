package ru.yandex.practicum.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        System.out.println(s);
    }
}