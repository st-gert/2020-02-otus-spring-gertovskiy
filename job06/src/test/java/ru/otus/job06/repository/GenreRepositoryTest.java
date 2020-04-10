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
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.impl.GenreRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест Repository Управление лит.жанрами")
@DataJpaTest
@Import(GenreRepositoryImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryTest {

    @Autowired
    GenreRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(0)
    @DisplayName("Список жанров")
    void getGenreList() {
        List<Genre> list = repository.getGenreList();
        assertThat(list)
                .isNotNull()
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
        Optional<Genre> genreOptional = repository.getGenreById(2L);
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
        Optional<Genre> genreOptional = repository.getGenreByName("сатира");
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
        Optional<Genre> genreOptional = repository.getGenreById(-2L);
        assertThat(genreOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(22)
    @DisplayName("Получение жанра по наименованию - Error")
    void getGenreByNameError() {
        Optional<Genre> genreOptional = repository.getGenreByName("женский роман");
        assertThat(genreOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(3)
    @DisplayName("Добавление жанра")
    void addGenre() {
        Genre expectedGenre = new Genre(null, "Женский роман");
        long id = repository.addGenre(expectedGenre);
        expectedGenre.setGenreId(id);
        Optional<Genre> genreOptional = repository.getGenreById(id);
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
        Genre genre = em.find(Genre.class, 2L);
        em.detach(genre);
        String oldName = genre.getGenreName();
        String newName = "Женский роман";
        genre.setGenreName(newName);
        int n = repository.updateGenre(genre);
        assertEquals(1, n);
        Genre updatedGenre = em.find(Genre.class, 2L);
        assertThat(updatedGenre.getGenreName())
                .isNotEqualTo(oldName)
                .isEqualTo(newName)
        ;
    }

    @Test
    @Order(24)
    @DisplayName("Изменение жанра - Error")
    void updateGenreError() {
        int n = repository.updateGenre(new Genre(-2L, "Мужской роман"));
        assertEquals(0, n);
    }

    @Test
    @Order(5)
    @DisplayName("Удаление жанра")
    void deleteGenre() {
        Genre genre = em.persist(new Genre(null, "Женский роман"));
        Long id = genre.getGenreId();
        em.detach(genre);
        int n = repository.deleteGenre(id);
        assertEquals(1, n);
        Genre deletedGenre = em.find(Genre.class, id);
        assertThat(deletedGenre).isNull();
    }

    @Test
    @Order(25)
    @DisplayName("Удаление жанра - Error")
    void deleteGenreError() {
        int n = repository.deleteGenre(-2L);
        assertEquals(0, n);
    }

    @Test
    @Order(35)
    @DisplayName("Удаление жанра - Exception")
    void deleteGenreErrorException() {
        assertThrows(ApplDbConstraintException.class,
                () -> repository.deleteGenre(2L));
    }

}