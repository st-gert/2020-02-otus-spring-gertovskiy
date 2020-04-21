package ru.otus.job07.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job07.model.Author;
import ru.otus.job07.model.Book;
import ru.otus.job07.model.Genre;
import ru.otus.job07.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест Repository Управление книгами")
@DataJpaTest
@Import({BookServiceImpl.class} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookRepositoryReadTest {

    @Autowired
    BookServiceImpl bookService;

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
        List<Book> list = bookService.getBookList();
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
        List<Book> list = bookService.getBookListByGenre("фантастика");
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
        List<Book> list = bookService.getBookListByAuthor("стругацкий");
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

}