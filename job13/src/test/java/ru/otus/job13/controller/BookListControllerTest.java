package ru.otus.job13.controller;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.job13.model.Author;
import ru.otus.job13.model.Book;
import ru.otus.job13.model.Genre;
import ru.otus.job13.model.dto.BookDto;
import ru.otus.job13.security.JpaUserDetailsService;
import ru.otus.job13.service.AuthorService;
import ru.otus.job13.service.BookService;
import ru.otus.job13.service.GenreService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookListController Чтение списков книг (роль USER)")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(BookListController.class)
class BookListControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService mockService;
    @MockBean
    private AuthorService mockAuthorService;
    @MockBean
    private GenreService mockGenreService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    public static List<Genre> ALL_GENRES;
    public static List<Author> ALL_AUTHORS;
    public static List<BookDto> ALL_BOOKS;

    @BeforeAll
    static void init() {
        ALL_GENRES = Arrays.asList(
                new Genre(99901L, "Жанр1"),
                new Genre(99902L, "Жанр2")
        );
        ALL_AUTHORS = Arrays.asList(
                new Author(88801L, "Имя1", "Фамилия1"),
                new Author(88802L, "Имя2", "Фамилия2"),
                new Author(88803L, "Имя3", "Фамилия3")
        );
        List<Book> books = Arrays.asList(
                new Book(77701L, "Название1", ALL_GENRES.get(0), Collections.singletonList(ALL_AUTHORS.get(0))),
                new Book(77702L, "Название2", ALL_GENRES.get(0), Arrays.asList(
                        ALL_AUTHORS.get(0), ALL_AUTHORS.get(1))),
                new Book(77703L, "Название3", ALL_GENRES.get(1), Collections.singletonList(ALL_AUTHORS.get(2))),
                new Book(77704L, "Название4", ALL_GENRES.get(1), Arrays.asList(
                        ALL_AUTHORS.get(1), ALL_AUTHORS.get(2)))
        );
        ALL_BOOKS = books
                .stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @DisplayName("Список всех книг")
    @Order(0)
    @Test
    @WithMockUser()     // default user/USER
    void listBooks() throws Exception {
        when(mockService.getBookList()).thenReturn(ALL_BOOKS);
        MvcResult result = mvc.perform(get("/book/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
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

    @DisplayName("Выбор жанра для списка книг")
    @Order(1)
    @Test
    @WithMockUser()
    void getGenreForBookList() throws Exception {
        when(mockGenreService.getGenreList()).thenReturn(ALL_GENRES);
        MvcResult result = mvc.perform(get("/book/list/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(ALL_GENRES.get(0).getGenreId().toString(), ALL_GENRES.get(0).getGenreName(),
                        ALL_GENRES.get(1).getGenreId().toString(), ALL_GENRES.get(1).getGenreName());
    }

    @DisplayName("Список книг по жанру")
    @Order(2)
    @Test
    @WithMockUser()
    void listBooksByGenre() throws Exception {
        when(mockService.getBookListByGenre(ALL_GENRES.get(0).getGenreId())).thenReturn(ALL_BOOKS.subList(0, 2));
        MvcResult result = mvc.perform(post("/book/list/genre")
                        .param("id", ALL_GENRES.get(0).getGenreId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(ALL_BOOKS.get(0).getBookId().toString(), ALL_BOOKS.get(0).getTitle(),
                        ALL_BOOKS.get(0).getGenreName(), ALL_BOOKS.get(0).getAuthorsString())
                .contains(ALL_BOOKS.get(1).getBookId().toString(), ALL_BOOKS.get(1).getTitle(),
                        ALL_BOOKS.get(1).getGenreName(), ALL_BOOKS.get(1).getAuthorsString())
                .doesNotContain(ALL_BOOKS.get(2).getTitle(), ALL_BOOKS.get(3).getTitle())
        ;
    }

    @DisplayName("Выбор авторов для списка книг")
    @Order(3)
    @Test
    @WithMockUser()
    void getAuthorsForBookList() throws Exception {
        when(mockAuthorService.getAuthorList()).thenReturn(ALL_AUTHORS);
        MvcResult result = mvc.perform(get("/book/list/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(ALL_AUTHORS.get(0).getAuthorId().toString(), ALL_AUTHORS.get(0).getFullName(),
                        ALL_AUTHORS.get(1).getAuthorId().toString(), ALL_AUTHORS.get(1).getFullName(),
                        ALL_AUTHORS.get(2).getAuthorId().toString(), ALL_AUTHORS.get(2).getFullName());
    }

    @DisplayName("Список книг по авторам")
    @Order(4)
    @Test
    @WithMockUser()
    void listBooksByAuthors() throws Exception {
        Long[] authorIds = {ALL_AUTHORS.get(0).getAuthorId(),ALL_AUTHORS.get(1).getAuthorId()};
        when(mockService.getBookListByAuthors(Arrays.asList(authorIds))).thenReturn(ALL_BOOKS.subList(0, 2));
        MvcResult result = mvc.perform(post("/book/list/authors")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("selectedId", authorIds[0].toString(), authorIds[1].toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(ALL_BOOKS.get(0).getBookId().toString(), ALL_BOOKS.get(0).getTitle(),
                        ALL_BOOKS.get(0).getGenreName(), ALL_BOOKS.get(0).getAuthorsString())
                .contains(ALL_BOOKS.get(1).getBookId().toString(), ALL_BOOKS.get(1).getTitle(),
                        ALL_BOOKS.get(1).getGenreName(), ALL_BOOKS.get(1).getAuthorsString())
                .doesNotContain(ALL_BOOKS.get(2).getTitle(), ALL_BOOKS.get(3).getTitle())
        ;
    }

}