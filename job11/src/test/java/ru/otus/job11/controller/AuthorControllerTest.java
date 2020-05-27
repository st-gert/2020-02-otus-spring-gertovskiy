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
import ru.otus.job11.model.Author;
import ru.otus.job11.repository.AuthorRepository;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Тест AuthorController Управление авторами")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private AuthorRepository mockRepository;

    private static final String SERVICE_URI = "/api/author";
    private static final String SERVICE_URI_WITH_ID = "/api/author/1000";
    private static final Author[] AUTHORS;

    static {
        AUTHORS = new Author[]{new Author("Имя-0", "Фамилия-0"),
                new Author("Имя-1", "Фамилия-1")};
        AUTHORS[0].setId("abcdef");
        AUTHORS[0].setBooks(Collections.singletonList(new Book()));
    }

    private String buildJson(Author author) {
        String id = author.getId() == null ? "null" : "\"" + author.getId() + "\"";
        return "{\"id\":" + id
                + ",\"firstName\":\"" + author.getFirstName()
                + "\",\"lastName\":\"" + author.getLastName() + "\"}";
    }

    @DisplayName("Список авторов")
    @Order(0)
    @Test
    void getAllAuthors() {
        when(mockRepository.findAllByOrderByLastNameAscFirstNameAsc()).thenReturn(Flux.just(AUTHORS));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"authorId\":", "\"firstName\":", "\"lastName\":")
                .contains(AUTHORS[0].getId(), AUTHORS[0].getFirstName(), AUTHORS[0].getLastName(),
                        AUTHORS[1].getFirstName(), AUTHORS[1].getLastName());
    }

    @DisplayName("Получение автора по ID")
    @Order(1)
    @Test
    void getAuthorById() {
        when(mockRepository.findById("1000")).thenReturn(Mono.just(AUTHORS[0]));
        EntityExchangeResult<byte[]> result = webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody().returnResult();
        String content = new String(result.getResponseBody(), StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"authorId\":", "\"firstName\":", "\"lastName\":")
                .contains(AUTHORS[0].getId(), AUTHORS[0].getFirstName(), AUTHORS[0].getLastName());
    }

    @DisplayName("Получение автора по ID - Error 404")
    @Order(21)
    @Test
    void getAuthorByIdException() {
        when(mockRepository.findById("1000")).thenReturn(Mono.empty());
        webClient.get().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Добавление автора")
    @Order(2)
    @Test
    void addAuthor() {
        when(mockRepository.insert(any(Author.class))).thenReturn(Mono.just(AUTHORS[1]));
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID == null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(AUTHORS[1])))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Добавление автора - Error 406")
    @Order(22)
    @Test
    void addAuthorError() {
        webClient.post().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)            // ID != null
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(AUTHORS[0])))
                .exchange()
                .expectStatus().isEqualTo(406);
    }

    @DisplayName("Изменение автора")
    @Order(3)
    @Test
    void updateAuthor() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(AUTHORS[0]));
        when(mockRepository.save(any(Author.class))).thenReturn(Mono.just(AUTHORS[0]));
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(AUTHORS[0])))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Изменение автора - Error 404")
    @Order(23)
    @Test
    void updateAuthorError() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.put().uri(SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(BodyInserters.fromValue(buildJson(AUTHORS[0])))
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Удаление автора")
    @Order(4)
    @Test
    void deleteAuthor() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(AUTHORS[1]));
        when(mockRepository.delete(any(Author.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("Удаление автора - Error 404")
    @Order(24)
    @Test
    void deleteAuthorError() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.empty());
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @DisplayName("Удаление автора - Error 406")
    @Order(25)
    @Test
    void deleteAuthorError2() {
        when(mockRepository.findById(any(String.class))).thenReturn(Mono.just(AUTHORS[0]));
        webClient.delete().uri(SERVICE_URI_WITH_ID)
                .exchange()
                .expectStatus().isEqualTo(406);
    }

}