package ru.otus.job08.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.otus.job08.model.Author;
import ru.otus.job08.model.Book;
import ru.otus.job08.model.Genre;
import ru.otus.job08.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест Service Чтение списков книг")
@DataMongoTest
@ComponentScan({"ru.otus.job08.changelog"})
@Import({BookServiceImpl.class} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceReadTest {

    @Autowired
    BookServiceImpl bookService;

    private List<Book> approxBooks = Arrays.asList(
            new Book("Сказка о Тройке", new Genre("Фантастика"),
                    Arrays.asList(
                            new Author("Аркадий", "Стругацкий"),
                            new Author("Борис", "Стругацкий"))),
            new Book("Комментарии к пройденному", new Genre("Дневники"),
                    Collections.singletonList(new Author("Борис", "Стругацкий"))),
            new Book("Сто лет тому вперед", new Genre("Фантастика"),
                    Collections.singletonList(new Author("Кир", "Булычев"))),
            new Book("Азазель", new Genre("Детектив"),
                    Collections.singletonList(new Author("Борис", "Акунин")))
    );

    @Test
    @Order(0)
    @DisplayName("Список книг")
    void getBookList() {
        List<Book> list = bookService.getBookList();
        assertThat(list).isNotNull().hasSize(12);
        list.forEach(x -> x.setId(null));
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
        list.forEach(x -> x.setId(null));
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
        list.forEach(x -> x.setId(null));
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