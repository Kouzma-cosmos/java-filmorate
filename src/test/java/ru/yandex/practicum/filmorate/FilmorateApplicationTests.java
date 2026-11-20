package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateValidFilm() {

        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Матрица");
        film.setDescription("Культовый научно-фантастический боевик братьев Вачовски");
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 31));
        film.setDuration(136);


        Film createdFilm = controller.create(film);
        Collection<Film> allFilms = controller.findAll();

        assertNotNull(createdFilm.getId(), "Фильму должен присвоиться id");
        assertEquals(1, allFilms.size(), "В хранилище должен появиться 1 фильм");
        assertTrue(allFilms.contains(createdFilm), "Хранилище должно содержать созданный фильм");
    }

    @Test
    void shouldCreateFilmWhenDescriptionIs200Chars() {

        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Груз 200");
        film.setDescription("Мрачная реалистическая история в антураже СССР времен позднего застоя? Ответка " +
                "бессмертному" +
                " шедевру Орсона Уэллса \"Печать зла\"? Или старая-старая сказка о похищенной принцессе, Кощее и " +
                "добром молодце?");
        film.setReleaseDate(LocalDate.of(2007, Month.JUNE, 14));
        film.setDuration(90);

        Film createdFilm = controller.create(film);
        Collection<Film> allFilms = controller.findAll();

        assertNotNull(createdFilm.getId(), "Фильму должен присвоиться id");
        assertEquals(1, allFilms.size(), "В хранилище должен появиться 1 фильм");
        assertTrue(allFilms.contains(createdFilm), "Хранилище должно содержать созданный фильм");
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName(" ");
        film.setDescription("Художественный фильм в жанре лавкрафтовских ужасов");
        film.setReleaseDate(LocalDate.of(2017, Month.APRIL, 7));
        film.setDuration(90);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film),
                "Должно выброситься исключение при пустом названии"
        );

        assertEquals("Название не может быть пустым", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDesriptionIsMore200Chars() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Гравитация");
        film.setDescription("Доктор Райан Стоун, блестящий специалист в области медицинского инжиниринга, " +
                "отправляется " +
                "в свою первую космическую миссию под командованием ветерана астронавтики Мэтта Ковальски, для " +
                "которого " +
                "этот полет — последний перед отставкой. Но во время, казалось бы, рутинной работы за бортом " +
                "случается " +
                "катастрофа.");
        film.setReleaseDate(LocalDate.of(2013, Month.AUGUST, 28));
        film.setDuration(91);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film),
                "Должно выброситься исключение при избыточном описании"
        );

        assertEquals("Превышено допустимое кол-во символов в поле 'description'", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenInvalidReleaseDate() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Криминальное чтиво");
        film.setDescription("Несколько связанных историй из жизни бандитов. Шедевр Квентина Тарантино, который " +
                "изменил " +
                "мировое кино");
        film.setReleaseDate(LocalDate.of(1894, Month.MAY, 21));
        film.setDuration(154);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film),
                "Должно выброситься исключение при некорректной дате релиза"
        );

        assertEquals("Некорректная дата релиза", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldCreateFilmWhenReleaseDateEquals28_12_1895() {

        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Cinématographe Lumière");
        film.setDescription("Первые фильмы братьев Люмьер");
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 28));
        film.setDuration(20);

        Film createdFilm = controller.create(film);
        Collection<Film> allFilms = controller.findAll();

        assertNotNull(createdFilm.getId(), "Фильму должен присвоиться id");
        assertEquals(1, allFilms.size(), "В хранилище должен появиться 1 фильм");
        assertTrue(allFilms.contains(createdFilm), "Хранилище должно содержать созданный фильм");
    }

    @Test
    void shouldThrowExceptionWhenInvalidDuration() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Летят журавли");
        film.setDescription("Любовь сильнее предательства и смерти. Шедевр о Великой Отечественной войне, который " +
                "потряс" +
                " весь мир");
        film.setReleaseDate(LocalDate.of(1957, Month.OCTOBER, 12));
        film.setDuration(0);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film),
                "Должно выброситься исключение при некорректной продолжительности"
        );

        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldUpdateFilm() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Матрица");
        film.setDescription("Культовый научно-фантастический боевик братьев Вачовски");
        film.setReleaseDate(LocalDate.of(1999, Month.MARCH, 31));
        film.setDuration(136);

        Film createdFilm = controller.create(film);

        Film filmToUpdate = new Film();
        filmToUpdate.setId(createdFilm.getId());
        filmToUpdate.setName("Матрица");
        filmToUpdate.setDescription("Культовый научно-фантастический боевик сестер Вачовски");
        filmToUpdate.setReleaseDate(LocalDate.of(1999, Month.MARCH, 31));
        filmToUpdate.setDuration(136);

        controller.update(filmToUpdate);

        Film savedFilm = controller.findAll().iterator().next();
        assertEquals("Культовый научно-фантастический боевик сестер Вачовски",
                savedFilm.getDescription(), "Описание в хранилище должно измениться");
    }

    @Test
    void shouldThrowExceptionWhenIdIsEmpty() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setName("Гарри Поттер и философский камень");
        film.setDescription("Гарри поступает в школу магии Хогвартс и заводит друзей");
        film.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        film.setDuration(152);

        controller.create(film);

        Film filmToUpdate = new Film();
        filmToUpdate.setName(film.getName());
        filmToUpdate.setDescription("Первая часть большой франшизы о маленьком волшебнике");
        filmToUpdate.setReleaseDate(film.getReleaseDate());
        filmToUpdate.setDuration(film.getDuration());

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(filmToUpdate),
                "Должно выброситься исключение при отсутствии id"
        );
        Film savedFilm = controller.findAll().iterator().next();
        assertEquals("Id должен быть указан", exception.getMessage());
        assertEquals("Гарри поступает в школу магии Хогвартс и заводит друзей",
                savedFilm.getDescription(), "Описание объекта в хранилище не должно измениться");
    }

    @Test
    void shouldThrowExceptionWhenFilmNotFound() {
        FilmController controller = new FilmController();

        Film film = new Film();
        film.setId(1L);
        film.setName("Сладкая жизнь");
        film.setDescription("Он ищет смысл жизни в богемном Риме. Фильм-праздник, без которого невозможно представить" +
                "современное кино");
        film.setReleaseDate(LocalDate.of(1960, Month.FEBRUARY, 3));
        film.setDuration(179);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> controller.update(film),
                "Должно выброситься исключение при отсутствии фильма в хранилище"
        );

        assertEquals("Фильм с id = " + film.getId() + " не найден", exception.getMessage());
        assertEquals(0, controller.findAll().size());
    }

    @Test
    void shouldCreateValidUser() {

        UserController controller = new UserController();

        User user = new User();
        user.setEmail("carica77@ya.ru");
        user.setLogin("carica");
        user.setName("Julia");
        user.setBirthday(LocalDate.of(1989, Month.FEBRUARY, 16));


        User createdUser = controller.create(user);
        Collection<User> allUsers = controller.findAll();

        assertNotNull(createdUser.getId(), "Пользователю должен присвоиться id");
        assertEquals(1, allUsers.size(), "В хранилище должен появиться 1 пользователь");
        assertTrue(allUsers.contains(createdUser), "Хранилище должно содержать созданного пользователя");
    }

    @Test
    void shouldCreateUserWithLoginInsteadForName() {

        UserController controller = new UserController();

        User user = new User();
        user.setEmail("savol@mail.ru");
        user.setLogin("savvaon");
        user.setBirthday(LocalDate.of(2015, Month.APRIL, 28));

        controller.create(user);

        User savedUser = controller.findAll().iterator().next();
        assertEquals("savvaon", savedUser.getName(), "Именем должен стать логин");

    }

    @Test
    void shouldThrowExceptionWhenInvalidEmail() {
        UserController controller = new UserController();

        User user = new User();
        user.setEmail("tikhvolokh.ru");
        user.setLogin("tikhan-pakhan");
        user.setName("Тихон");
        user.setBirthday(LocalDate.of(2017, Month.JULY, 11));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user),
                "Должно выброситься исключение при некорректном email"
        );

        assertEquals("Некорректный формат email", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenInvalidLogin() {
        UserController controller = new UserController();

        User user = new User();
        user.setEmail("jhoanvolokhov@gmail.com");
        user.setLogin(null);
        user.setName("Иван");
        user.setBirthday(LocalDate.of(2023, Month.AUGUST, 8));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user),
                "Должно выброситься исключение при некорректном логине"
        );

        assertEquals("Некорректный формат логина", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenLoginContainsBlank() {
        UserController controller = new UserController();

        User user = new User();
        user.setEmail("gulya-pr@ya.ru");
        user.setLogin("Gulya Pivo");
        user.setName("Гульнара");
        user.setBirthday(LocalDate.of(1977, Month.MAY, 23));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user),
                "Должно выброситься исключение при некорректном логине"
        );

        assertEquals("Некорректный формат логина", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenInvalidBirthday() {
        UserController controller = new UserController();

        User user = new User();
        user.setEmail("postumvolokhov@rambler.ru");
        user.setLogin("postum");
        user.setName("Богдан");
        user.setBirthday(LocalDate.of(2027, Month.FEBRUARY, 22));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user),
                "Должно выброситься исключение при некорректной дате рождения"
        );

        assertEquals("Некорректная дата рождения", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void shouldUpdateUser() {
        UserController controller = new UserController();

        User user = new User();
        user.setEmail("kuzmavolohov@mail.ru");
        user.setLogin("cosmos");
        user.setName("Кузьма");
        user.setBirthday(LocalDate.of(1992, Month.OCTOBER, 29));

        controller.create(user);

        User userToUpdate = new User();
        userToUpdate.setId(user.getId());
        userToUpdate.setEmail("kuzmavolohov@gmail.com");
        userToUpdate.setLogin(user.getLogin());
        userToUpdate.setName(user.getName());
        userToUpdate.setBirthday(user.getBirthday());

        controller.update(userToUpdate);

        User savedUser = controller.findAll().iterator().next();
        assertEquals("kuzmavolohov@gmail.com",
                savedUser.getEmail(), "Email объекта в хранилище должен измениться");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserController controller = new UserController();

        User user = new User();
        user.setId(1L);
        user.setEmail("kanikylu@mail.ru");
        user.setLogin("natalya");
        user.setName("Наталья");
        user.setBirthday(LocalDate.of(1962, Month.MARCH, 5));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> controller.update(user),
                "Должно выброситься исключение при отсутствии пользователя в хранилище"
        );

        assertEquals("Пользователь с id = " + user.getId() + " не найден", exception.getMessage());
        assertEquals(0, controller.findAll().size());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        UserController controller = new UserController();

        User user = new User();
        user.setEmail("alvol56@ya.ru");
        user.setLogin("alex");
        user.setName("Александр");
        user.setBirthday(LocalDate.of(1956, Month.SEPTEMBER, 13));

        controller.create(user);

        User userToUpdate = new User();
        userToUpdate.setEmail("alvolokhov56@yandex.ru");
        userToUpdate.setLogin(user.getLogin());
        userToUpdate.setName(user.getName());
        userToUpdate.setBirthday(user.getBirthday());

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.update(userToUpdate),
                "Должно выброситься исключение при отсутствии id"
        );
        User savedUser = controller.findAll().iterator().next();
        assertEquals("Id должен быть указан", exception.getMessage());
        assertEquals("alvol56@ya.ru",
                savedUser.getEmail(), "Email объекта в хранилище не должен измениться");
    }


}
