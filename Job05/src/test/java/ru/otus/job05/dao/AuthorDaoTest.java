package ru.otus.job05.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.job05.dao.impl.AuthorDaoJdbc;
import ru.otus.job05.model.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тест DAO Управление авторами")
@JdbcTest
@Import({AuthorDaoJdbc.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorDaoTest {

    @Autowired
    AuthorDaoJdbc dao;

    @Test
    @Order(0)
    @DisplayName("Список авторов")
    void getAuthorList() {
        List<Author> list = dao.getAuthorList();
        assertThat(list)
                .isNotNull()
                .filteredOn(x -> x.getAuthorId() < 100L)
                .hasSize(7)
                .contains(
                        new Author(1L, "Аркадий", "Стругацкий")
                        , new Author(3L, "Илья", "Ильф")
                        , new Author(7L, "Агата", "Кристи")
                )
        ;
    }

    @Test
    @Order(1)
    @DisplayName("Получение автора по ID")
    void getAuthorById() {
        Optional<Author> authorOptional = dao.getAuthorById(5L);
        assertThat(authorOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(new Author(5L, "Кир", "Булычев"))
        ;
    }

    @Test
    @Order(2)
    @DisplayName("Получение автора по имени")
    void getAuthorByName() {
        Author author = new Author(null, "Кир", "Булычев");
        Optional<Author> AuthorOptional = dao.getAuthorByName(author);
        author.setAuthorId(5L);
        assertThat(AuthorOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(author)
        ;
    }

    @Test
    @Order(21)
    @DisplayName("Получение автора по ID - Error")
    void getAuthorByIdError() {
        Optional<Author> AuthorOptional = dao.getAuthorById(-2L);
        assertThat(AuthorOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(22)
    @DisplayName("Получение автора по имени - Error")
    void getAuthorByNameError() {
        Author author = new Author(null, "Дарья", "Донцова");
        Optional<Author> AuthorOptional = dao.getAuthorByName(author);
        assertThat(AuthorOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(3)
    @DisplayName("Добавление автора")
    void addAuthor() {
        Author expectedAuthor = new Author(null, "А. С.", "Пушкин");
        long id = dao.addAuthor(expectedAuthor);
        expectedAuthor.setAuthorId(id);
        Optional<Author> AuthorOptional = dao.getAuthorById(id);
        assertThat(AuthorOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(expectedAuthor)
        ;
    }

    @Test
    @Order(4)
    @DisplayName("Изменение автора")
    void updateAuthor() {
        Author expectedAuthor = new Author(3L, "Илья Арнольдович", "Ильф");
        int n = dao.updateAuthor(expectedAuthor);
        assertTrue(n > 0);
        Optional<Author> AuthorOptional = dao.getAuthorById(3L);
        assertThat(AuthorOptional)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(expectedAuthor)
        ;
    }

    @Test
    @Order(24)
    @DisplayName("Изменение автора - Error")
    void updateAuthorError() {
        int n = dao.updateAuthor(new Author(-1L, "Дарья", "Донцова"));
        assertEquals(0, n);
    }

    @Test
    @Order(5)
    @DisplayName("Удаление автора")
    void deleteAuthor() {
        Author Author = new Author(null, "Дарья", "Донцова");
        long id = dao.addAuthor(Author);
        Optional<Author> presentOptional = dao.getAuthorById(id);
        int n = dao.deleteAuthor(id);
        Optional<Author> notPresentOptional = dao.getAuthorById(id);
        assertAll(
                () -> assertTrue(id >= 100L)
                , () -> assertTrue(n > 0)
                , () -> assertTrue(presentOptional.isPresent())
                , () -> assertFalse(notPresentOptional.isPresent())
        );
    }

    @Test
    @Order(25)
    @DisplayName("Удаление автора - Error")
    void deleteAuthorError() {
        int n = dao.deleteAuthor(-2L);
        assertEquals(0, n);
    }

    @Test
    @Order(35)
    @DisplayName("Удаление автора - Exception")
    void deleteAuthorErrorException() {
        int n = dao.deleteAuthor(2L);
        assertEquals(-1, n);
    }

}
