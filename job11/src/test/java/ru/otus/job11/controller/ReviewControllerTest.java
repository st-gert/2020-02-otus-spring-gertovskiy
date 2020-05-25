package ru.otus.job11.controller;

import org.junit.jupiter.api.BeforeAll;
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
import ru.otus.job11.model.Review;
import ru.otus.job11.repository.BookRepository;
import ru.otus.job11.repository.ReviewRepository;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тест GenreController Управление отзывами")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ReviewRepository mockRepository;
    @MockBean
    private BookRepository mockBookRepository;

    private static final String SERVICE_URI = "/api/review";
    private static final String SERVICE_URI_BOOK = "/api/review/book/1000";
    private static final String SERVICE_URI_WITH_ID = "/api/review/9999";

    private static Book BOOK;
    private static Review REVIEW;
    private static String REVIEW_JSON;

    @BeforeAll
    static void initBookWithReviews() {
        BOOK = new Book("Название",
                new Genre("Жанр"),
                Collections.singletonList(new Author("Имя", "Фамилия")));
        REVIEW = new Review(BOOK, "Мура");
        BOOK.getReviews().add(REVIEW);
        BOOK.setId("1000");
        REVIEW.setId("9999");
        REVIEW_JSON = "{\"bookId\":\"1000\",\"reviewId\":\"9999\",\"opinion\":\"Мура\"}";
    }

    @DisplayName("Список отзывов")
    @Order(0)
    @Test
    void getBookWithReviews() {
        when(mockBookRepository.findById("1000")).thenReturn(Mono.just(BOOK));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_BOOK)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"bookId\":", "\"genreName\":", "\"reviews\":[", "]")
                .contains(BOOK.getTitle(), REVIEW.getOpinion());
    }

    @DisplayName("Получение отзыва")
    @Order(1)
    @Test
    void getReviewById() {
        when(mockRepository.findById("9999")).thenReturn(Mono.just(REVIEW));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"bookId\":", "\"reviewId\":", "\"opinion\":")
                .contains(REVIEW.getId(), REVIEW.getOpinion());
    }

    @DisplayName("Добавление отзыва")
    @Order(2)
    @Test
    void addReview() {
        String content = REVIEW_JSON.replace("\"9999\"", "null");
        when(mockBookRepository.findById(any(String.class))).thenReturn(Mono.just(BOOK));
        when(mockBookRepository.save(any(Book.class))).thenReturn(Mono.just(BOOK));
        when(mockRepository.insert(any(Review.class))).thenReturn(Mono.just(REVIEW));
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)      // ID == null
                .body(BodyInserters.fromValue(content))
                .exchange()
                .expectStatus().isOk();
        verify(mockBookRepository).findById(any(String.class));
        verify(mockBookRepository).save(any(Book.class));
        verify(mockRepository).insert(any(Review.class));
    }

    @DisplayName("Изменение отзыва")
    @Order(3)
    @Test
    void updateReview() {
        when(mockRepository.findById("9999")).thenReturn(Mono.just(REVIEW));
        when(mockRepository.save(any())).thenReturn(Mono.just(REVIEW));
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)      // ID != null
                .body(BodyInserters.fromValue(REVIEW_JSON))
                .exchange()
                .expectStatus().isOk();
        verify(mockRepository).save(any(Review.class));
    }

    @DisplayName("Удаление отзыва")
    @Order(4)
    @Test
    void deleteReview() {
        when(mockRepository.existsById(any(String.class))).thenReturn(Mono.just(true));
        when(mockRepository.deleteById(any(String.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk();
        verify(mockRepository).deleteById(any(String.class));
    }

    @DisplayName("Список отзывов - Error 404")
    @Order(20)
    @Test
    void getBookWithReviewsError() {
        when(mockBookRepository.findById("1000")).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_BOOK)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Получение отзыва - Error 404")
    @Order(21)
    @Test
    void getReviewByIdError() {
        when(mockRepository.findById("9999")).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Добавление отзыва - Error 404")
    @Order(22)
    @Test
    void addReviewError() {
        String content = REVIEW_JSON.replace("\"9999\"", "null");
        when(mockBookRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(content))
                .exchange()
                .expectStatus().isEqualTo(404);
        verify(mockBookRepository).findById(any(String.class));
        verify(mockBookRepository, never()).save(any(Book.class));
        verify(mockRepository, never()).insert(any(Review.class));
    }

    @DisplayName("Добавление отзыва - Error 406")
    @Order(32)
    @Test
    void addReviewError2() {
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)      // ID == null
                .body(BodyInserters.fromValue(REVIEW_JSON))
                .exchange()
                .expectStatus().isEqualTo(406);
        verify(mockBookRepository, never()).findById(any(String.class));
        verify(mockBookRepository, never()).save(any(Book.class));
        verify(mockRepository, never()).insert(any(Review.class));
    }

    @DisplayName("Изменение отзыва - Error 404")
    @Order(23)
    @Test
    void updateReviewError() {
        when(mockRepository.findById("9999")).thenReturn(Mono.empty());
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)      // ID != null
                .body(BodyInserters.fromValue(REVIEW_JSON))
                .exchange()
                .expectStatus().isEqualTo(404);
        verify(mockRepository, never()).save(any(Review.class));
    }

    @DisplayName("Удаление отзыва - Error 404")
    @Order(24)
    @Test
    void deleteReviewError() {
        when(mockRepository.existsById(any(String.class))).thenReturn(Mono.just(false));
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
        verify(mockRepository, never()).deleteById(any(String.class));
    }

}