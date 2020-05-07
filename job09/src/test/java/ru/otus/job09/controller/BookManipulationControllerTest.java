package ru.otus.job09.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.job09.model.Author;
import ru.otus.job09.model.Book;
import ru.otus.job09.model.Genre;
import ru.otus.job09.service.AuthorService;
import ru.otus.job09.service.BookService;
import ru.otus.job09.service.GenreService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookManipulationController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookManipulationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService mockService;
    @MockBean
    private AuthorService mockAuthorService;
    @MockBean
    private GenreService mockGenreService;

    private static final List<Genre> ALL_GENRES = Arrays.asList(
            new Genre(987L, "Жанр1"),
            new Genre(654L, "Жанр2")
    );
    public static final List<Author> ALL_AUTHORS = Arrays.asList(
            new Author(963L, "Имя1", "Фамилия1"),
            new Author(852L, "Имя2", "Фамилия2")
    );
    public static final Book BOOK = new Book(1234567890L, "Название1", ALL_GENRES.get(0),
            Collections.singletonList(ALL_AUTHORS.get(0)));

    @DisplayName("Добавление книги")
    @Test
    @Order(1)
    void addBook() throws Exception {
        when(mockAuthorService.getAuthorList()).thenReturn(ALL_AUTHORS);
        when(mockGenreService.getGenreList()).thenReturn(ALL_GENRES);
        MvcResult result = mvc.perform(get("/book/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(ALL_GENRES.get(0).getGenreId().toString(), ALL_GENRES.get(0).getGenreName(),
                        ALL_GENRES.get(0).getGenreId().toString(), ALL_GENRES.get(0).getGenreName())
                .contains(ALL_AUTHORS.get(0).getAuthorId().toString(), ALL_AUTHORS.get(0).getFullName(),
                        ALL_AUTHORS.get(1).getAuthorId().toString(), ALL_AUTHORS.get(1).getFullName())
                .contains("bookId", "authorId", "genreId", "title")
                .doesNotContain("selected", "checked");
    }

    @DisplayName("Корректировка книги")
    @Test
    @Order(2)
    void editBook() throws Exception {
        when(mockAuthorService.getAuthorList()).thenReturn(ALL_AUTHORS);
        when(mockGenreService.getGenreList()).thenReturn(ALL_GENRES);
        when(mockService.getBookById(BOOK.getBookId())).thenReturn(BOOK);
        MvcResult result = mvc.perform(get("/book/edit")
                        .param("id", BOOK.getBookId().toString()))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(ALL_GENRES.get(0).getGenreId().toString(), ALL_GENRES.get(0).getGenreName(),
                        ALL_GENRES.get(0).getGenreId().toString(), ALL_GENRES.get(0).getGenreName())
                .contains(ALL_AUTHORS.get(0).getAuthorId().toString(), ALL_AUTHORS.get(0).getFullName(),
                        ALL_AUTHORS.get(1).getAuthorId().toString(), ALL_AUTHORS.get(1).getFullName())
                .contains("bookId", "authorId", "genreId", "title")
                .contains(BOOK.getBookId().toString(), BOOK.getTitle())
                .contains("selected", "checked");
    }

    @DisplayName("Сохранение книги после добавления")
    @Order(3)
    @Test
    void insertBook() throws Exception {
        mvc.perform(
                post("/book/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("title", "Новое название")           // bookId = null
                        .param("genreId", ALL_GENRES.get(1).getGenreId().toString())
                        .param("authorId", ALL_AUTHORS.get(0).getAuthorId().toString(),
                                ALL_AUTHORS.get(1).getAuthorId().toString()))
                .andExpect(status().is3xxRedirection());
        verify(mockService).addBook("Новое название", ALL_GENRES.get(1).getGenreId(),
                Arrays.asList(ALL_AUTHORS.get(0).getAuthorId(), ALL_AUTHORS.get(1).getAuthorId()));
    }

    @DisplayName("Сохранение книги после корректировки")
    @Order(4)
    @Test
    void saveBook() throws Exception {
        mvc.perform(
                post("/book/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("bookId", BOOK.getBookId().toString())
                        .param("title", "Новое название")
                        .param("genreId", ALL_GENRES.get(1).getGenreId().toString())
                        .param("authorId", ALL_AUTHORS.get(0).getAuthorId().toString(),
                                ALL_AUTHORS.get(1).getAuthorId().toString()))
                .andExpect(status().is3xxRedirection());
        verify(mockService).updateBook(BOOK.getBookId(), "Новое название", ALL_GENRES.get(1).getGenreId(),
                Arrays.asList(ALL_AUTHORS.get(0).getAuthorId(), ALL_AUTHORS.get(1).getAuthorId()));
    }

    @DisplayName("Удаление книги")
    @Order(5)
    @Test
    void deleteBook() throws Exception {
        MvcResult result = mvc.perform(
                get("/book/delete")
                        .param("id", "0"))
                .andReturn();
        assertThat(result.getResponse().getStatus())
                .isBetween(300, 399);
        verify(mockService).deleteBook(anyLong());
    }

}