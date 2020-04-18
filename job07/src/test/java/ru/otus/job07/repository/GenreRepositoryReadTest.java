package ru.otus.job07.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.job07.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тест Repository Управление лит.жанрами")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryReadTest {

    @Autowired
    GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(2)
    @DisplayName("Получение жанра по наименованию")
    void getGenreByName() {
        Genre genre = repository.findByGenreNameIgnoreCase("сатира");
        assertAll(
                () -> assertNotNull(genre)
                , () -> assertEquals(new Genre(2L, "Сатира").toString(), genre.toString())
        );
    }

    @Test
    @Order(22)
    @DisplayName("Получение жанра по наименованию - Error")
    void getGenreByNameError() {
        Genre genre = repository.findByGenreNameIgnoreCase("женский роман");
        assertThat(genre).isNull();
    }

}