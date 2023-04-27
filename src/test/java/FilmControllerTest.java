import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FilmControllerTest extends BaseControllerTest {

    @Test
    public void testCreateNewValidFilm() {
        film1 = new Film(1, "название", "описание", LocalDate.of(2014, 10, 12),
                100);
        filmController.create(film1, mockedRequest);
        assertEquals(filmController.films.size(), 1);
    }

    @Test
    public void testCreateDoubleFilmWithOneId() {
        film1 = new Film(1, "Название", "описание", LocalDate.of(2014, 10, 12),
                100);
        film2 = new Film(1, "Название", "описание", LocalDate.of(2014, 10, 12),
                100);
        filmController.create(film1, mockedRequest);
        final String invalid = "Фильм уже существует";
        FilmAlreadyExistsException thrown = assertThrows(
                FilmAlreadyExistsException.class,
                () -> {
                    filmController.create(film2, mockedRequest);
                }
        );
        assertEquals(invalid, thrown.getMessage());
    }

    @Test
    public void testCreateFilmsDifferentId() {
        film1 = new Film(1, "Название", "описание", LocalDate.of(2014, 10, 12),
                100);
        film2 = new Film(2, "Название", "описание", LocalDate.of(2014, 10, 12),
                100);
        filmController.create(film1, mockedRequest);
        filmController.create(film2, mockedRequest);
        assertEquals(filmController.films.size(), 2);
    }


    @Test
    public void testCreateFilmWithEmptyName() {
        film1 = new Film(1, "", "описание", LocalDate.of(2014, 10, 12),
                100);
        final String invalid = "Имя не должно быть пустым";
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film1, mockedRequest);
                }
        );
        assertEquals(invalid, thrown.getMessage());
    }

    @Test
    public void testCreateFilmWithSpiceInName() {
        film1 = new Film(1, " ", "описание", LocalDate.of(2014, 10, 12),
                100);
        filmController.create(film1, mockedRequest);
        assertEquals(filmController.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithDescriptionsWith200character() {
        film1 = new Film(1, "Название", "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
                LocalDate.of(2014, 10, 12),
                100);
        filmController.create(film1, mockedRequest);
        assertEquals(filmController.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithDescriptionsWith201character() {
        film1 = new Film(1, "Название", "112345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
                LocalDate.of(2014, 10, 12),
                100);
        final String invalid = "Описание не должно превышать 200 символов";
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film1, mockedRequest);
                }
        );
        assertEquals(invalid, thrown.getMessage());
    }


    @Test
    public void testCreateFilmWithReleaseDateEquallyDateFirstFilmInCinema() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        filmController.create(film1, mockedRequest);
        assertEquals(filmController.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithReleaseDateAfterDateFirstFilmInCinema() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 29),
                100);
        filmController.create(film1, mockedRequest);
        assertEquals(filmController.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithReleaseDateBeforeDateFirstFilmInCinema() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 27),
                100);
        final String invalid = "Дата не должна быть менее 28 декабря 1895 года";
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.create(film1, mockedRequest);
                }
        );
        assertEquals(invalid, thrown.getMessage());
    }


    @Test
    public void testUpdateFilmWithEquallyId() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        film2 = new Film(1, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        filmController.create(film1, mockedRequest);
        filmController.update(film2, mockedRequest);
        assertEquals(filmController.films.size(), 1);
        assertEquals(filmController.films.get(film2.getId()).getName(), "Обновленное название");
    }

    @Test
    public void testUpdateFilmWithDifferentId() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        film2 = new Film(2, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        filmController.create(film1, mockedRequest);
        final String invalid = "id фильма не найден";
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.update(film2, mockedRequest);
                }
        );
        assertEquals(invalid, thrown.getMessage());
    }

    @Test
    public void testUpdateFilmWithReleaseDateAfterDateFirstFilmInCinema() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        film2 = new Film(1, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 29),
                100);
        filmController.create(film1, mockedRequest);
        filmController.update(film2, mockedRequest);
        assertEquals(filmController.films.size(), 1);
        assertEquals(filmController.films.get(film2.getId()).getName(), "Обновленное название");
    }

    @Test
    public void testUpdateFilmWithReleaseDateEquallyDateFirstFilmInCinema() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        film2 = new Film(1, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        filmController.create(film1, mockedRequest);
        filmController.update(film2, mockedRequest);
        assertEquals(filmController.films.size(), 1);
        assertEquals(filmController.films.get(film2.getId()).getName(), "Обновленное название");
    }

    @Test
    public void testUpdateFilmWithReleaseDateBeforeDateFirstFilmInCinema() {
        film1 = new Film(1, "Название", "Описание",
                LocalDate.of(1895, 12, 28),
                100);
        film2 = new Film(1, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 27),
                100);
        filmController.create(film1, mockedRequest);
        final String invalid = "Дата не должна быть менее 28 декабря 1895 года";
        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> {
                    filmController.update(film2, mockedRequest);
                }
        );
        assertEquals(invalid, thrown.getMessage());
    }
}