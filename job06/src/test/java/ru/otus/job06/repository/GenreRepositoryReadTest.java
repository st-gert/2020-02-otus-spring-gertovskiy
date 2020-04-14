package ru.otus.job06.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.impl.GenreRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест Repository Управление лит.жанрами")
@DataJpaTest
@Import(GenreRepositoryImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryReadTest {

    @Autowired
    GenreRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(2)
    @DisplayName("Получение жанра по наименованию")
    void getGenreByName() {
        Genre genre = repository.getGenreByName("сатира");
        assertThat(genre)
                .isNotNull()
                .isEqualToComparingFieldByField(new Genre(2L, "Сатира"))
        ;
    }

    @Test
    @Order(22)
    @DisplayName("Получение жанра по наименованию - Error")
    void getGenreByNameError() {
        Genre genre = repository.getGenreByName("женский роман");
        assertThat(genre).isNull();
    }

}