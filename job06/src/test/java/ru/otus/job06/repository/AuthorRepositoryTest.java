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
import ru.otus.job06.model.Author;
import ru.otus.job06.repository.impl.AuthorRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест Repository Управление авторами")
@DataJpaTest
@Import({AuthorRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(0)
    @DisplayName("Список авторов")
    void getAuthorList() {
        List<Author> list = repository.getAuthorList();
        assertThat(list)
                .isNotNull()
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
        Optional<Author> authorOptional = repository.getAuthorById(5L);
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
        Optional<Author> AuthorOptional = repository.getAuthorByName(author);
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
        Optional<Author> AuthorOptional = repository.getAuthorById(-2L);
        assertThat(AuthorOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(22)
    @DisplayName("Получение автора по имени - Error")
    void getAuthorByNameError() {
        Author author = new Author(null, "Дарья", "Донцова");
        Optional<Author> AuthorOptional = repository.getAuthorByName(author);
        assertThat(AuthorOptional)
                .isNotPresent()
        ;
    }

    @Test
    @Order(3)
    @DisplayName("Добавление автора")
    void addAuthor() {
        Author expectedAuthor = new Author(null, "А. С.", "Пушкин");
        long id = repository.addAuthor(expectedAuthor);
        expectedAuthor.setAuthorId(id);
        Author actualAuthor = em.find(Author.class, id);
        assertThat(actualAuthor)
                .isNotNull()
                .isEqualToComparingFieldByField(expectedAuthor)
        ;
    }

    @Test
    @Order(4)
    @DisplayName("Изменение автора")
    void updateAuthor() {
        long id = 3L;
        Author expectedAuthor = new Author(id, "Илья Арнольдович", "Ильф");
        int n = repository.updateAuthor(expectedAuthor);
        assertEquals(1, n);
        Author actualAuthor = em.find(Author.class, id);
        assertThat(actualAuthor)
                .isNotNull()
                .isEqualToComparingFieldByField(expectedAuthor)
        ;
    }

    @Test
    @Order(24)
    @DisplayName("Изменение автора - Error")
    void updateAuthorError() {
        assertDoesNotThrow(() -> {
            int n = repository.updateAuthor(new Author(-1L, "Дарья", "Донцова"));
            assertEquals(0, n);
        });
    }

    @Test
    @Order(5)
    @DisplayName("Удаление автора")
    void deleteAuthor() {
        Author author = em.persist(new  Author(null, "Дарья", "Донцова"));
        long id = author.getAuthorId();
        em.detach(author);
        int n = repository.deleteAuthor(id);
        assertEquals(1, n);
        Author deletedAuthor = em.find(Author.class, id);
        assertThat(deletedAuthor).isNull();
    }

    @Test
    @Order(25)
    @DisplayName("Удаление автора - Error")
    void deleteAuthorError() {
        assertDoesNotThrow(() -> {
            int n = repository.deleteAuthor(-2L);
            assertEquals(0, n);
        });
    }

    @Test
    @Order(35)
    @DisplayName("Удаление автора - Exception")
    void deleteAuthorErrorException() {
        assertThrows(ApplDbConstraintException.class,
                () -> repository.deleteAuthor(2L));
    }

}
