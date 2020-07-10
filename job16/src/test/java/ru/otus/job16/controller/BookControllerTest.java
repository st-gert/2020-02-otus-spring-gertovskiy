package ru.otus.job16.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.job16.exception.ApplDbNoDataFoundException;
import ru.otus.job16.model.Author;
import ru.otus.job16.model.Book;
import ru.otus.job16.model.Genre;
import ru.otus.job16.model.dto.BookDto;
import ru.otus.job16.service.BookService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService mockService;

    private static final String SERVICE_URI = "/api/book";
    private static final String SERVICE_URI_WITH_ID = "/api/book/1000";

    private static final List<BookDto> ALL_BOOKS = new ArrayList<>();
    private static final Book BOOK = new Book();
    private static final String BOOK_INPUT = "{\"bookId\":999,\"title\":\"Название\",\"genreId\":20," +
            "\"authorIds\":[1,2,3]}";

    @BeforeAll
    static void init() {
        List<Genre> allGenres = Arrays.asList(
                new Genre(99901L, "Жанр0"),
                new Genre(99902L, "Жанр1")
        );
        List<Author> allAuthors = Arrays.asList(
                new Author(88801L, "Имя0", "Фамилия0"),
                new Author(88802L, "Имя1", "Фамилия1"),
                new Author(88803L, "Имя2", "Фамилия2")
        );
        List<Book> books = Arrays.asList(
                new Book(77701L, "Название0", allGenres.get(0), Collections.singletonList(allAuthors.get(0))),
                new Book(77702L, "Название1", allGenres.get(0), Arrays.asList(
                        allAuthors.get(0), allAuthors.get(1))),
                new Book(77703L, "Название2", allGenres.get(1), Collections.singletonList(allAuthors.get(2))),
                new Book(77704L, "Название3", allGenres.get(1), Arrays.asList(
                        allAuthors.get(1), allAuthors.get(2)))
        );
        ALL_BOOKS.addAll(books
                .stream()
                .map(BookDto::of)
                .collect(Collectors.toList()
                ));
        BOOK.setBookId(books.get(0).getBookId());
        BOOK.setTitle(books.get(0).getTitle());
        BOOK.setGenre(books.get(0).getGenre());
        BOOK.setAuthors(books.get(0).getAuthors());
    }

    @DisplayName("Список всех книг")
    @Order(0)
    @Test
    void getAllBooks() throws Exception {
        when(mockService.getBookList()).thenReturn(ALL_BOOKS);
        MvcResult result = mvc.perform(get(SERVICE_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"bookId\":", "\"title\":")
                .contains(ALL_BOOKS.get(0).getBookId().toString(), ALL_BOOKS.get(0).getTitle(),
                        ALL_BOOKS.get(0).getGenreName(), ALL_BOOKS.get(0).getAuthorsString())
                .contains(ALL_BOOKS.get(1).getBookId().toString(), ALL_BOOKS.get(1).getTitle(),
                        ALL_BOOKS.get(1).getGenreName(), ALL_BOOKS.get(1).getAuthorsString())
                .contains(ALL_BOOKS.get(2).getBookId().toString(), ALL_BOOKS.get(2).getTitle(),
                        ALL_BOOKS.get(2).getGenreName(), ALL_BOOKS.get(2).getAuthorsString())
                .contains(ALL_BOOKS.get(3).getBookId().toString(), ALL_BOOKS.get(3).getTitle(),
                        ALL_BOOKS.get(3).getGenreName(), ALL_BOOKS.get(3).getAuthorsString())
        ;
    }

    @DisplayName("Получение книги по ID")
    @Order(11)
    @Test
    void getBookById() throws Exception {
        when(mockService.getBookById(1000L)).thenReturn(BOOK);
        MvcResult result = mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"bookId\":", "\"title\":")
                .contains(BOOK.getBookId().toString(), BOOK.getTitle(), BOOK.getGenre().getGenreName());
    }

    @DisplayName("Получение книги по ID - Error 404")
    @Order(21)
    @Test
    void getBookByIdError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).getBookById(anyLong());
        mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
    }

    @DisplayName("Добавление книги")
    @Order(12)
    @Test
    void addBook() throws Exception {
        String request = BOOK_INPUT.replace("999", "null");
        mvc.perform(post(SERVICE_URI).content(request)  // ID == null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).addBook(any());
    }

    @DisplayName("Добавление книги - Error 406")
    @Order(22)
    @Test
    void addBookError() throws Exception {
        mvc.perform(post(SERVICE_URI).content(BOOK_INPUT)  // ID != null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(406));
        verify(mockService, never()).addBook(any());
    }

    @DisplayName("Изменение книги")
    @Order(14)
    @Test
    void updateBook() throws Exception {
        mvc.perform(put(SERVICE_URI).content(BOOK_INPUT)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).updateBook(any());
    }

    @DisplayName("Изменение книги - Error 404")
    @Order(24)
    @Test
    void updateBookError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).updateBook(any());
        mvc.perform(put(SERVICE_URI).content(BOOK_INPUT)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(404));
        verify(mockService).updateBook(any());
    }

    @DisplayName("Удаление книги")
    @Order(15)
    @Test
    void deleteBook() throws Exception {
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk());
        verify(mockService).deleteBook(anyLong());
    }

    @DisplayName("Удаление книги - Error 404")
    @Order(25)
    @Test
    void deleteBookError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).deleteBook(anyLong());
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
        verify(mockService).deleteBook(anyLong());
    }

}