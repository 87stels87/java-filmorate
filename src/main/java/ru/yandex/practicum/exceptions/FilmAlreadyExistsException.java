package ru.yandex.practicum.exceptions;

public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String s) {
        super(s);
    }
}