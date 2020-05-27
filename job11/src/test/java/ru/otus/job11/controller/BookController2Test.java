package ru.otus.job11.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Author;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Genre;
import ru.otus.job11.repository.AuthorRepository;
import ru.otus.job11.repository.BookRepository;
import ru.otus.job11.repository.GenreRepository;
import ru.otus.job11.repository.ReviewRepository;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Тест BookController Управление книгами")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest(BookController.class)
public class BookController2Test {

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
    private static final String SERVICE_URI_WITH_ID = "/api/book/1000";

    private static final Book BOOK = new Book("Название", new Genre("Жанр"),
            Collections.singletonList(new Author("Имя", "Фамилия")));
    private static final String BOOK_INPUT = "{\"bookId\":\"999\",\"title\":\"Название\",\"genreId\":20," +
            "\"authorsIds\":[\"1\",\"2\",\"3\"]}";

    @DisplayName("Получение книги по ID")
    @Order(1)
    @Test
    void getBookById() {
        when(mockRepository.findById("1000")).thenReturn(Mono.just(BOOK));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"bookId\":", "\"title\":", "\"genreName\":")
                .contains(BOOK.getTitle(), BOOK.getGenre().getGenreName(),
                        BOOK.getAuthors().get(0).getFirstName(), BOOK.getAuthors().get(0).getLastName());
    }

    @DisplayName("Получение книги по ID - Error 404")
    @Order(21)
    @Test
    void getBookByIdError() {
        when(mockRepository.findById("1000")).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Добавление книги")
    @Order(2)
        @Test
    void addBook() {
        String bookInput = BOOK_INPUT.replace("\"999\"", "null");
        when(mockRepository.insert(any(Book.class))).thenReturn(Mono.just(BOOK));
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID == null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(bookInput))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Добавление книги - Error 406")
    @Order(22)
        @Test
    void addBookError() {
        when(mockRepository.insert(any(Book.class))).thenReturn(Mono.just(BOOK));
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID != null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(BOOK_INPUT))
                .exchange()
                .expectStatus().isEqualTo(406);
    }

    @DisplayName("Изменение книги")
    @Order(3)
    @Test
    void updateBook() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(BOOK));
        when(mockRepository.save(any(Book.class))).thenReturn(Mono.just(BOOK));
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID == null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(BOOK_INPUT))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Изменение книги - Error 404")
    @Order(23)
    @Test
    void updateBookError() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID == null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(BOOK_INPUT))
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Удаление книги")
    @Order(4)
    @Test
    void deleteBook() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(BOOK));
        when(mockRepository.delete(any(Book.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Удаление книги - Error 404")
    @Order(24)
    @Test
    void deleteGenreError() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

}
