package ru.yandex.practicum.exceptions;

public class FilmAlreadyExistsException extends RuntimeException  {
    public FilmAlreadyExistsException(String s) {
        System.out.println(s);
    }
}
