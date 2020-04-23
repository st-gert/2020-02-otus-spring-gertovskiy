package ru.otus.job08.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.job08.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тест Repository Управление лит.жанрами")
@DataMongoTest
@ComponentScan({"ru.otus.job08.changelog"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryReadTest {

    @Autowired
    GenreRepository repository;

    @Test
    @Order(2)
    @DisplayName("Получение жанра по наименованию")
    void getGenreByName() {
        Genre genre = repository.findByGenreNameIgnoreCase("сатира");
        assertAll(
                () -> assertNotNull(genre)
                , () -> assertEquals("Сатира", genre.getGenreName())
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