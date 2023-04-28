import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ru.yandex.practicum.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest extends BaseControllerTest {

    @Test
    public void testCreateNewUserWithValidEmail() {
        user1 = new User(1, "1@yandex.ru", "bot", "Имя", LocalDate.of(2000, 10, 12) );
        userController.create(user1, mockedRequest);
        assertEquals(userController.users.size(), 1);
    }

    @Test
    public void testCreateNewUserWithEmptyEmail() {
        user1 = new User(1, "12ю2", "bot", "Имя", LocalDate.of(2000, 10, 12) );
        final String invalid = "email не должен быть пустым, а также должен создержать @";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user1, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());

    }

    @Test
    public void testCreateNewUserWithEmailWithoutDog() {
        user1 = new User(1, "12.ru", "bot", "Имя", LocalDate.of(2000, 10, 12) );
        final String invalid = "email не должен быть пустым, а также должен создержать @";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user1, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }


    @Test
    public void testCreateNewUserWithEmtyLogin() {
        user1 = new User(1, "12@yandex.ru", "", "Имя", LocalDate.of(2000, 10, 12) );
        final String invalid = "логин не должен быть пустым, а также должен создержать пробел";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user1, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

    @Test
    public void testCreateNewUserWithSpiceInLogin() {
        user1 = new User(1, "12@yandex.ru", "a o", "Имя", LocalDate.of(2000, 10, 12) );
        final String invalid = "логин не должен быть пустым, а также должен создержать пробел";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user1, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }


    @Test
    public void testCreateNewUserWithBirthdayAfterCurentDay() {
        user1 = new User(1, "12@yandex.ru", "ao", "Имя", LocalDate.of(2100, 10, 12) );
        final String invalid = "день рождения не может быть в будущем";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.create(user1, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }


    @Test
    public void testCreateNewUserWithDoubleId() {
        user1 = new User(1, "12@yandex.ru", "ao", "Имя", LocalDate.of(2000, 10, 12) );
        user2 = new User(1, "12@yandex.ru", "ao", "Имя", LocalDate.of(2000, 10, 12) );
        userController.create(user1, mockedRequest);
        final String invalid = "Юзер уже существует";
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> {
                    userController.create(user2, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

    @Test
    public void testCreateNewUserWithNameIsNull() {
        user1 = new User(1, "12@yandex.ru", "ao", null, LocalDate.of(2000, 10, 12) );
        userController.create(user1, mockedRequest);
        assertEquals(userController.users.size(), 1);
        assertEquals(userController.users.get(user1.getId()).getName(),
                userController.users.get(user1.getId()).getLogin());
    }

    @Test
    public void testUpdateUserWithEquallyId() {
        user1 = new User(1, "12@yandex.ru", "ao", "нейм", LocalDate.of(2000, 10, 12) );
        user2 = new User(1, "12@yandex.ru", "Новый логин", "нейм", LocalDate.of(2000, 10, 12) );
        userController.create(user1, mockedRequest);
        userController.update(user2, mockedRequest);
        assertEquals(userController.users.size(), 1);
        assertEquals(userController.users.get(user2.getId()).getLogin(), "Новый логин");
    }


    @Test
    public void testUpdateUserWithDifferentId() {
        user1 = new User(1, "12@yandex.ru", "ao", "нейм", LocalDate.of(2000, 10, 12) );
        user2 = new User(2, "12@yandex.ru", "Новый логин", "нейм", LocalDate.of(2000, 10, 12) );
        userController.create(user1, mockedRequest);
        final String invalid = "нет такого id";
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    userController.update(user2, mockedRequest);
                }
        );
        assertEquals(invalid, exception.getMessage());
    }

    @Test
    public void testUpdateUserWithEquallyIdAndNameIsNull() {
        user1 = new User(1, "12@yandex.ru", "логин1", "нейм1", LocalDate.of(2000, 10, 12) );
        user2 = new User(1, "12@yandex.ru", "логин2", null, LocalDate.of(2000, 10, 12) );
        userController.create(user1, mockedRequest);
        userController.update(user2, mockedRequest);
        assertEquals(userController.users.get(user2.getId()).getName(), "логин2");
    }
}