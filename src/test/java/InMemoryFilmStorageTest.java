import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Rating;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class InMemoryFilmStorageTest {
    private InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    protected Film film1;
    protected Film film2;

    @BeforeEach
    public void clean() {
        inMemoryFilmStorage.films.clear();
    }

    @Test
    public void testCreateNewValidFilm() {
        film1 = new Film(null, "название", "описание", LocalDate.of(2014, 10, 12),
                null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        assertEquals(inMemoryFilmStorage.films.size(), 1);
    }

    @Test
    public void testCreateFilmsDifferentId() {
        film1 = new Film(null, "Название", "описание", LocalDate.of(2014, 10, 12), null, Rating.PG,
                100);
        film2 = new Film(null, "Название", "описание", LocalDate.of(2014, 10, 12), null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        inMemoryFilmStorage.create(film2);
        assertEquals(inMemoryFilmStorage.films.size(), 2);
    }

    @Test
    public void testCreateFilmWithEmptyName() {
        film1 = new Film(null, "", "описание", LocalDate.of(2014, 10, 12), null, Rating.PG,
                100);
        final String expectedExceptionMessage = "Имя не должно быть пустым";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryFilmStorage.create(film1);
                }
        );
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    public void testCreateFilmWithSpiceInName() {
        film1 = new Film(null, " ", "описание", LocalDate.of(2014, 10, 12), null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        assertEquals(inMemoryFilmStorage.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithDescriptionsWith200character() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(2014, 10, 12), null, Rating.PG,
                100);
        film1.setDescription("f".repeat(200));
        inMemoryFilmStorage.create(film1);
        assertEquals(inMemoryFilmStorage.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithDescriptionsWith201character() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(2014, 10, 12), null, Rating.PG,
                100);
        film1.setDescription("f".repeat(201));
        final String expectedExceptionMessage = "Описание не должно превышать 200 символов";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryFilmStorage.create(film1);
                }
        );
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    public void testCreateFilmWithReleaseDateEquallyDateFirstFilmInCinema() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(1895, 12, 28), null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        assertEquals(inMemoryFilmStorage.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithReleaseDateAfterDateFirstFilmInCinema() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(1895, 12, 29), null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        assertEquals(inMemoryFilmStorage.films.size(), 1);
    }

    @Test
    public void testCreateFilmWithReleaseDateBeforeDateFirstFilmInCinema() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(1895, 12, 27), null, Rating.PG,
                100);
        final String expectedExceptionMessage = "Дата не должна быть менее 28 декабря 1895 года";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryFilmStorage.create(film1);
                }
        );
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    public void testUpdateFilmWithDifferentId() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(1895, 12, 28), null, Rating.PG,
                100);
        film2 = new Film(null, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 28), null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        final String expectedExceptionMessage = "id фильма не найден";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryFilmStorage.update(film2);
                }
        );
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    public void testUpdateFilmWithReleaseDateBeforeDateFirstFilmInCinema() {
        film1 = new Film(null, "Название", "Описание",
                LocalDate.of(1895, 12, 28), null, Rating.PG,
                100);
        film2 = new Film(null, "Обновленное название", "Описание",
                LocalDate.of(1895, 12, 27), null, Rating.PG,
                100);
        inMemoryFilmStorage.create(film1);
        final String expectedExceptionMessage = "Дата не должна быть менее 28 декабря 1895 года";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryFilmStorage.update(film2);
                }
        );
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }
}