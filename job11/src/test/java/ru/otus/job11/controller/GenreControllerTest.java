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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Genre;
import ru.otus.job11.repository.GenreRepository;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Тест GenreController Управление лит.жанрами")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GenreRepository mockRepository;

    private static final String SERVICE_URI = "/api/genre";
    private static final String SERVICE_URI_WITH_ID = "/api/genre/1000";
    private static final Genre[] GENRES;

    static {
        GENRES = new Genre[]{new Genre("Интересное"),
                new Genre("Мура")};
        GENRES[0].setId("abcdef");
        GENRES[0].setBooks(Collections.singletonList(new Book()));
    }

    private String buildJson(Genre genre) {
        String id = genre.getId() == null ? "null" : "\"" + genre.getId() + "\"";
        return "{\"id\":" + id +
                ",\"genreName\":\"" + genre.getGenreName() + "\"}";
    }

    @DisplayName("Список жанров")
    @Order(0)
    @Test
    void getAllGenres() {
        when(mockRepository.findAllByOrderByGenreName()).thenReturn(Flux.just(GENRES));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"genreId\":", "\"genreName\":")
                .contains(GENRES[0].getId(), GENRES[0].getGenreName(), GENRES[1].getGenreName());
    }

    @DisplayName("Получение жанра по ID")
    @Order(1)
    @Test
    void getGenreById() {
        when(mockRepository.findById("1000")).thenReturn(Mono.just(GENRES[0]));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"genreId\":", "\"genreName\":")
                .contains(GENRES[0].getId(), GENRES[0].getGenreName());
    }

    @DisplayName("Получение жанра по ID - Error 404")
    @Order(21)
    @Test
    void getGenreByIdException() {
        when(mockRepository.findById("1000")).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Добавление жанра")
    @Order(2)
    @Test
    void addGenre() {
        when(mockRepository.insert(any(Genre.class))).thenReturn(Mono.just(GENRES[0]));
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID == null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(GENRES[1])))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Добавление жанра - Error 406")
    @Order(22)
    @Test
    void addGenreError() {
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID != null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(GENRES[0])))
                .exchange()
                .expectStatus().isEqualTo(406);
    }

    @DisplayName("Изменение жанра")
    @Order(3)
    @Test
    void updateGenre() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(GENRES[0]));
        when(mockRepository.save(any(Genre.class))).thenReturn(Mono.just(GENRES[0]));
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(GENRES[0])))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Изменение жанра - Error 404")
    @Order(23)
    @Test
    void updateGenreError() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(GENRES[0])))
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Удаление жанра")
    @Order(4)
    @Test
    void deleteGenre() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(GENRES[1]));
        when(mockRepository.delete(any(Genre.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Удаление жанра - Error 404")
    @Order(24)
    @Test
    void deleteGenreError() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Удаление жанра - Error 406")
    @Order(25)
    @Test
    void deleteGenreError2() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(GENRES[0]));
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(406);
    }

}