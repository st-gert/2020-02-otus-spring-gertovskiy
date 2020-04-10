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
import ru.otus.job06.model.Author;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.impl.AuthorRepositoryImpl;
import ru.otus.job06.repository.impl.BookRepositoryImpl;
import ru.otus.job06.repository.impl.GenreRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тест Repository Управление книгами")
@DataJpaTest
@Import({BookRepositoryImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookRepositoryTest {

    @Autowired
    BookRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    private List<Book> approxBooks = Arrays.asList(
            new Book(2L, "Сказка о Тройке", new Genre(1L, "Фантастика"),
                    Arrays.asList(
                            new Author(1L, "Аркадий", "Стругацкий"),
                            new Author(2L, "Борис", "Стругацкий"))),
            new Book(3L, "Комментарии к пройденному", new Genre(4L, "Дневники"),
                    Collections.singletonList(new Author(2L, "Борис", "Стругацкий"))),
            new Book(5L, "Сто лет тому вперед", new Genre(1L, "Фантастика"),
                    Collections.singletonList(new Author(5L, "Кир", "Булычев"))),
            new Book(11L, "Азазель", new Genre(3L, "Детектив"),
                    Collections.singletonList(new Author(6L, "Борис", "Акунин")))
    );

    @Test
    @Order(0)
    @DisplayName("Список книг")
    void getBookList() {
        List<Book> list = repository.getBookList();
        assertThat(list).isNotNull().hasSize(12);
        List<String> stringList = list
                .stream()
                .map(Book::toString)
                .collect(Collectors.toList());
        assertThat(stringList)
                .contains(approxBooks.get(0).toString(), approxBooks.get(1).toString(),
                        approxBooks.get(2).toString(), approxBooks.get(3).toString())
        ;
    }

    @Test
    @Order(1)
    @DisplayName("Список книг по жанру")
    void getBookListByGenre() {
        List<Book> list = repository.getBookListByGenre("фантастика");
        assertThat(list)
                .isNotNull()
                .hasSize(4);
        List<String> stringList = list
                .stream()
                .map(Book::toString)
                .collect(Collectors.toList());
        assertThat(stringList)
                .contains(approxBooks.get(0).toString(), approxBooks.get(2).toString())
                .doesNotContain(approxBooks.get(1).toString(), approxBooks.get(3).toString())
        ;
    }

    @Test
    @Order(2)
    @DisplayName("Список книг по автору")
    void getBookListByAuthor() {
        List<Book> list = repository.getBookListByAuthor("стругацкий");
        assertThat(list)
                .isNotNull()
                .hasSize(3);
        List<String> stringList = list
                .stream()
                .map(Book::toString)
                .collect(Collectors.toList());
        assertThat(stringList)
                .contains(approxBooks.get(0).toString(), approxBooks.get(1).toString())
                .doesNotContain(approxBooks.get(2).toString(), approxBooks.get(3).toString())
        ;
    }

    @Test
    @Order(10)
    @DisplayName("Получить книгу по ID")
    void getBookById() {
        Optional<Book> book = repository.getBookById(2L);
        assertThat(book).isPresent();
        assertThat(book.get().toString()).isEqualTo(approxBooks.get(0).toString());
    }

    @Test
    @Order(11)
    @DisplayName("Получить книгу по ID - Error")
    void getBookByIdError() {
        Optional<Book> book = repository.getBookById(-2L);
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
        Long bookId = repository.addBook(book);
        assertThat(bookId).isNotNull().isGreaterThan(12L);
        Optional<Book> newBook = repository.getBookById(bookId);
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
        Optional<Book> newBook = repository.updateBook(book);
        assertAll(
                () -> assertTrue(newBook.isPresent())
                , () -> assertEquals("Новое наименование", newBook.get().getTitle())
                , () -> assertEquals(book.getGenre().toString(), newBook.get().getGenre().toString())
                , () -> assertEquals(book.getAuthors().size(), newBook.get().getAuthors().size())
        );
    }

    @Test
    @Order(31)
    @DisplayName("Заменить не существующую книгу")
    void updateBookNotExists() {
        Book book = approxBooks.get(0);
        book.setBookId(-1L);
        Optional<Book> optionalBook = repository.updateBook(book);
        assertFalse(optionalBook.isPresent());
    }

    @Test
    @Order(40)
    @DisplayName("Удалить книгу")
    void deleteBook() {
        int n = repository.deleteBook(1L);
        Book book = em.find(Book.class, 1L);
        assertAll(
                () -> assertEquals(1, n)
                , () -> assertNull(book)
        );
    }

    @Test
    @Order(41)
    @DisplayName("Удалить не существующую книгу")
    void deleteBookNotExists() {
        int n = repository.deleteBook(-1L);
        assertEquals(0, n);
    }

}