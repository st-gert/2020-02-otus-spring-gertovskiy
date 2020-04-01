package ru.otus.job05.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.job05.dao.impl.GenreDaoJdbc;
import ru.otus.job05.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тест DAO Управление лит.жанрами")
@JdbcTest
@Import({GenreDaoJdbc.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreDaoTest {

    @Autowired
    GenreDaoJdbc dao;

    @Test
    @Order(0)
    @DisplayName("Список жанров")
    void getGenreList() {
        List<Genre> list = dao.getGenreList();
        assertThat(list)
                .isNotNull()
                .filteredOn(x -> x.getGenreId() < 100L)
                .hasSize(4)
                .containsOnly(
                        new Genre(1L, "Фантастика")
                        , new Genre(2L, "Сатира")
                        , new Genre(3L, "Детектив")
                        , new Genre(4L, "Дневники")
                )
        ;
    }

    @Test
    @Order(1)
    @DisplayName("Получение жанра по ID")
    void getGenreById() {
        Optional<Genre> genreOptional = dao.getGenreById(2L);
        assertThat(genreOptional)
                .isPresent()
                .get()
                .isEqualTo(new Genre(2L, "Сатира"))
        ;
    }

    @Test
    @Order(2)
    @DisplayName("Получение жанра по наименованию")
    void getGenreByName() {
        Optional<Genre> genreOptional = dao.getGenreByName("сатира");
        assertThat(genreOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(new Genre(2L, "Сатира"))
        ;
    }

    @Test
    @Order(21)
    @DisplayName("Получение жанра по ID - Error")
    void getGenreByIdError() {
        Optional<Genre> genreOptional = dao.getGenreById(-2L);
        assertThat(genreOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(22)
    @DisplayName("Получение жанра по наименованию - Error")
    void getGenreByNameError() {
        Optional<Genre> genreOptional = dao.getGenreByName("женский роман");
        assertThat(genreOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(3)
    @DisplayName("Добавление жанра")
    void addGenre() {
        Genre expectedGenre = new Genre(null, "Женский роман");
        long id = dao.addGenre(expectedGenre);
        expectedGenre.setGenreId(id);
        Optional<Genre> genreOptional = dao.getGenreById(id);
        assertThat(genreOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(expectedGenre)
                ;
    }

    @Test
    @Order(4)
    @DisplayName("Изменение жанра")
    void updateGenre() {
        Genre expectedGenre = new Genre(2L, "Женский роман");
        int n = dao.updateGenre(expectedGenre);
        assertTrue(n > 0);
        Optional<Genre> genreOptional = dao.getGenreById(2L);
        assertThat(genreOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(expectedGenre)
        ;
    }

    @Test
    @Order(24)
    @DisplayName("Изменение жанра - Error")
    void updateGenreError() {
        int n = dao.updateGenre(new Genre(-2L, "Мужской роман"));
        assertEquals(0, n);
    }

    @Test
    @Order(5)
    @DisplayName("Удаление жанра")
    void deleteGenre() {
        Genre genre = new Genre(null, "Женский роман");
        long id = dao.addGenre(genre);
        Optional<Genre> presentOptional = dao.getGenreById(id);
        int n = dao.deleteGenre(id);
        Optional<Genre> notPresentOptional = dao.getGenreById(id);
        assertAll(
                () -> assertTrue(id >= 100L)
                , () -> assertTrue(n > 0)
                , () -> assertTrue(presentOptional.isPresent())
                , () -> assertFalse(notPresentOptional.isPresent())
        );
    }

    @Test
    @Order(25)
    @DisplayName("Удаление жанра - Error")
    void deleteGenreError() {
        int n = dao.deleteGenre(-2L);
        assertEquals(0, n);
    }

    @Test
    @Order(35)
    @DisplayName("Удаление жанра - Exception")
    void deleteGenreErrorException() {
        int n = dao.deleteGenre(2L);
        assertEquals(-1, n);
    }

}