import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryUserStorageTest {

    InMemoryUserStorage inMemoryUserStorage =  new InMemoryUserStorage();;
    protected User user1;
    protected User user2;

    @Test
    public void testCreateNewUserWithValidEmail() {
        user1 = new User(null, "1@yandex.ru", "bot", "Имя", LocalDate.of(2000, 10, 12));
        inMemoryUserStorage.create(user1);
        assertEquals(inMemoryUserStorage.users.size(), 1);
    }

    @Test
    public void testCreateNewUserWithEmptyEmail() {
        user1 = new User(null, "12ю2", "bot", "Имя", LocalDate.of(2000, 10, 12));
        final String invalid = "email не должен быть пустым, а также должен создержать @";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryUserStorage.create(user1);
                }
        );
        assertEquals(invalid, exception.getMessage());

    }

    @Test
    public void testCreateNewUserWithEmailWithoutDog() {
        user1 = new User(null, "12.ru", "bot", "Имя", LocalDate.of(2000, 10, 12));
        final String invalid = "email не должен быть пустым, а также должен создержать @";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryUserStorage.create(user1);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

    @Test
    public void testCreateNewUserWithEmtyLogin() {
        user1 = new User(null, "12@yandex.ru", "", "Имя", LocalDate.of(2000, 10, 12));
        final String invalid = "логин не должен быть пустым, а также должен создержать пробел";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryUserStorage.create(user1);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

    @Test
    public void testCreateNewUserWithSpiceInLogin() {
        user1 = new User(null, "12@yandex.ru", "a o", "Имя", LocalDate.of(2000, 10, 12));
        final String invalid = "логин не должен быть пустым, а также должен создержать пробел";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryUserStorage.create(user1);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }


    @Test
    public void testCreateNewUserWithBirthdayAfterCurentDay() {
        user1 = new User(null, "12@yandex.ru", "ao", "Имя", LocalDate.of(2100, 10, 12));
        final String invalid = "день рождения не может быть в будущем";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryUserStorage.create(user1);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

    @Test
    public void testCreateNewUserWithNameIsNull() {
        user1 = new User(null, "12@yandex.ru", "ao", null, LocalDate.of(2000, 10, 12));
        inMemoryUserStorage.create(user1);
        assertEquals(inMemoryUserStorage.users.size(), 1);
        assertEquals(inMemoryUserStorage.users.get(user1.getId()).getName(),
                inMemoryUserStorage.users.get(user1.getId()).getLogin());
    }

    @Test
    public void testUpdateUserWithDifferentId() {
        user1 = new User(null, "12@yandex.ru", "ao", "нейм", LocalDate.of(2000, 10, 12));
        user2 = new User(null, "12@yandex.ru", "Новый логин", "нейм", LocalDate.of(2000, 10, 12));
        inMemoryUserStorage.create(user1);
        final String invalid = "нет такого id";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    inMemoryUserStorage.update(user2);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

}