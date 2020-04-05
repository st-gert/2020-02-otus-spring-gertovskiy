package ru.otus.job05.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.job05.dao.ext.BookListResultSetExtractor;
import ru.otus.job05.dao.impl.AuthorDaoJdbc;
import ru.otus.job05.dao.impl.BookDaoJdbc;
import ru.otus.job05.dao.impl.GenreDaoJdbc;
import ru.otus.job05.model.Author;
import ru.otus.job05.model.Book;
import ru.otus.job05.model.Genre;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тест DAO Управление книгами")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class, BookListResultSetExtractor.class} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookDaoTest {

    @Autowired
    BookDaoJdbc dao;

    private List<Book> approxBooks = Arrays.asList(
            new Book(2L, "Сказка о Тройке", new Genre(1L, "Фантастика"),
                    Arrays.asList(
                            new Author(1L, "Аркадий", "Стругацкий"),
                            new Author(2L, "Борис", "Стругацкий"))),
            new Book(3L, "Комментарии к пройденному", new Genre(4L, "Дневники"),
                    Collections.singletonList(new Author(2L, "Борис", "Стругацкий"))),
            new Book(4L, "Путешествие Алисы", new Genre(1L, "Фантастика"),
                    Collections.singletonList(new Author(5L, "Кир", "Булычев"))),
            new Book(11L, "Азазель", new Genre(3L, "Детектив"),
                    Collections.singletonList(new Author(6L, "Борис", "Акунин")))
    );

    @Test
    @Order(0)
    @DisplayName("Список книг")
    void getBookList() {
        List<Book> list = dao.getBookList();
        assertThat(list)
                .isNotNull()
                .filteredOn(x -> x.getBookId() < 100L)
                .hasSize(12)
                .contains(approxBooks.get(0), approxBooks.get(1), approxBooks.get(2), approxBooks.get(3))
        ;
    }

    @Test
    @Order(1)
    @DisplayName("Список книг по жанру")
    void getBookListByGenre() {
        List<Book> list = dao.getBookListByGenre("фантастика");
        assertThat(list)
                .isNotNull()
                .filteredOn(x -> x.getBookId() < 100L)
                .hasSize(4)
                .contains(approxBooks.get(0), approxBooks.get(2))
                .doesNotContain(approxBooks.get(1), approxBooks.get(3))
        ;
    }

    @Test
    @Order(2)
    @DisplayName("Список книг по автору")
    void getBookListByAuthor() {
        List<Book> list = dao.getBookListByAuthor("стругацкий");
        assertThat(list)
                .isNotNull()
                .filteredOn(x -> x.getBookId() < 100L)
                .hasSize(3)
                .contains(approxBooks.get(0), approxBooks.get(1))
                .doesNotContain(approxBooks.get(2), approxBooks.get(3))
        ;
    }

    @Test
    @Order(10)
    @DisplayName("Получить книгу по ID")
    void getBookById() {
        Optional<Book> book = dao.getBookById(2L);
        assertThat(book)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(approxBooks.get(0))
        ;
    }

    @Test
    @Order(11)
    @DisplayName("Получить книгу по ID - Error")
    void getBookByIdError() {
        Optional<Book> book = dao.getBookById(-2L);
        assertThat(book)
                .isNotPresent()
        ;
    }

    @Test
    @Order(20)
    @DisplayName("Добавить книгу")
    void addBook() {
        Book book = new Book(null, "Новая книга",
                new Genre(3L, "Детектив"),
                Collections.singletonList(new Author(6L, "Борис", "Акунин")));
        Long bookId = dao.addBook(book);
        assertThat(bookId).isNotNull().isGreaterThan(99L);
        Optional<Book> newBook = dao.getBookById(bookId);
        assertAll(
                () -> assertTrue(newBook.isPresent())
                , () -> assertEquals("Новая книга", newBook.get().getTitle())
                , () -> assertEquals(approxBooks.get(3).getGenre(), newBook.get().getGenre())
                , () -> assertEquals(1, newBook.get().getAuthors().size())
                , () -> assertEquals(approxBooks.get(3).getAuthors().get(0), newBook.get().getAuthors().get(0))
        );
    }

    @Test
    @Order(30)
    @DisplayName("Заменить книгу")
    void updateBook() {
        Book book = approxBooks.get(0);
        book.setTitle("Новое наименование");
        Optional<Book> newBook = dao.updateBook(book);
        assertAll(
                () -> assertTrue(newBook.isPresent())
                , () -> assertTrue(newBook.get().getBookId() >= 100L)
                , () -> assertEquals("Новое наименование", newBook.get().getTitle())
                , () -> assertEquals(book.getGenre(), newBook.get().getGenre())
                , () -> assertEquals(book.getAuthors(), newBook.get().getAuthors())
        );
    }

    @Test
    @Order(31)
    @DisplayName("Заменить не существующую книгу")
    void updateBookNotExists() {
        Book book = approxBooks.get(0);
        book.setBookId(-1L);
        Optional<Book> optionalBook = dao.updateBook(book);
        assertFalse(optionalBook.isPresent());
    }

    @Test
    @Order(40)
    @DisplayName("Удалить книгу")
    void deleteBook() {
        int n = dao.deleteBook(1L);
        Optional<Book> optionalBook = dao.getBookById(1L);
        assertAll(
                () -> assertEquals(1, n)
                , () -> assertFalse(optionalBook.isPresent())
        );
    }

    @Test
    @Order(41)
    @DisplayName("Удалить не существующую книгу")
    void deleteBookNotExists() {
        int n = dao.deleteBook(-1L);
        assertEquals(0, n);
    }

}