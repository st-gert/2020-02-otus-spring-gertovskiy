package ru.otus.job11.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Author;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Genre;
import ru.otus.job11.repository.AuthorRepository;
import ru.otus.job11.repository.BookRepository;
import ru.otus.job11.repository.GenreRepository;
import ru.otus.job11.repository.ReviewRepository;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тест BookController Чтение списков книг")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest(BookController.class)
class BookController1Test {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookRepository mockRepository;
    @MockBean
    private AuthorRepository mockAuthorRepository;
    @MockBean
    private GenreRepository mockGenreRepository;
    @MockBean
    private ReviewRepository mockReviewRepository;

    private static final String SERVICE_URI = "/api/book";
    private static final String SERVICE_URI_GENRE = "/api/book/genre/1000";
    private static final String SERVICE_URI_AUTHORS = "/api/book/authors/abc,def";

    private static final List<Book> BOOKS;
    private static final List<Genre> GENRES;
    private static final List<Author> AUTHORS;

    static {
        GENRES = Arrays.asList(
                new Genre("Жанр0"),
                new Genre("Жанр1")
        );
        AUTHORS = Arrays.asList(
                new Author("Имя0", "Фамилия0"),
                new Author("Имя1", "Фамилия1"),
                new Author("Имя2", "Фамилия2")
        );
        BOOKS = Arrays.asList(
                new Book("Название0", GENRES.get(0), Collections.singletonList(AUTHORS.get(0))),
                new Book("Название1", GENRES.get(0), Arrays.asList(AUTHORS.get(0), AUTHORS.get(1))),
                new Book("Название2", GENRES.get(1), Collections.singletonList(AUTHORS.get(2))),
                new Book("Название3", GENRES.get(1), Arrays.asList(AUTHORS.get(1), AUTHORS.get(2)))
        );
        GENRES.get(0).setBooks(BOOKS.subList(0, 2));
        AUTHORS.get(0).setBooks(BOOKS.subList(0, 2));
        AUTHORS.get(1).setBooks(BOOKS.subList(3, 4));
    }

    @DisplayName("Список всех книг")
    @Order(0)
    @Test
    void getAllBooks() {
        when(mockRepository.findAll()).thenReturn(Flux.fromIterable(BOOKS));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"bookId\":", "\"title\":")
                .contains("Жанр0", "Жанр1", "Имя0", "Фамилия0", "Имя1", "Фамилия1", "Имя2", "Фамилия2",
                        "Название0", "Название1", "Название2", "Название3")
        ;
    }

    @DisplayName("Список книг по жанру")
    @Order(1)
    @Test
    void getBooksByGenre() {
        when(mockGenreRepository.findById("1000")).thenReturn(Mono.just(GENRES.get(0)));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_GENRE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"bookId\":", "\"title\":")
                .contains("Жанр0", "Название0", "Название1")
                .doesNotContain("Жанр1", "Название2", "Название3")
        ;
    }

    @DisplayName("Список книг по жанру - Error 404")
    @Order(41)
    @Test
    void getBooksByGenreError() {
        when(mockGenreRepository.findById("1000")).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_GENRE)
                .exchange()
                .expectStatus().isEqualTo(404)
        ;
    }

    @DisplayName("Список книг по авторам")
    @Order(2)
    @Test
    void getBooksByAuthors() {
        when(mockAuthorRepository.findById(any(String.class))).thenReturn(Mono.just(AUTHORS.get(0)))
                .thenReturn(Mono.just(AUTHORS.get(1)));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_AUTHORS)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"bookId\":", "\"title\":")
                .contains("Жанр0", "Жанр1", "Название0", "Название1", "Название3",
                        "Имя0", "Фамилия0", "Имя1", "Фамилия1")
                .doesNotContain("Название2")
        ;
        verify(mockAuthorRepository, times(2)).findById(any(String.class));
    }

    @DisplayName("Список книг по авторам - Error 404")
    @Order(42)
    @Test
    void getBooksByAuthorsError() {
        when(mockAuthorRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_AUTHORS)
                .exchange()
                .expectStatus().isEqualTo(404);
        verify(mockAuthorRepository, times(2)).findById(any(String.class));
    }

}