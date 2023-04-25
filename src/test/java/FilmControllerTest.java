import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.model.Film;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class FilmControllerTest {


    public void test1() {
        FilmController fc = new FilmController();
        Film film = new Film(1, "Наме", "деск", LocalDate.of(2011, 11, 12), 11);

    }


}
