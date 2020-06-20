package ru.otus.job13.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.job13.exception.ApplDbConstraintException;
import ru.otus.job13.model.Genre;
import ru.otus.job13.security.JpaUserDetailsService;
import ru.otus.job13.service.GenreService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GenreController Управление лит.жанрами (роль ADMIN)")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService mockService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    public static final List<Genre> GENRES = Arrays.asList(
            new Genre(1234567890L, "Интересное"),
            new Genre(null, "Мура")
    );

    @DisplayName("Список жанров")
    @Order(0)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listGenres() throws Exception {
        when(mockService.getGenreList()).thenReturn(GENRES);
        MvcResult result = mvc.perform(get("/genre/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(GENRES.get(0).getGenreId().toString(),
                        GENRES.get(0).getGenreName(),
                        GENRES.get(1).getGenreName());
    }

    @DisplayName("Добавление жанра")
    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addGenre() throws Exception {
        mvc.perform(get("/genre/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @DisplayName("Корректировка жанра")
    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editGenre() throws Exception {
        Genre genre = GENRES.get(0);
        when(mockService.getGenreById(genre.getGenreId())).thenReturn(genre);
        MvcResult result = mvc.perform(
                get("/genre/edit")
                        .param("id", genre.getGenreId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(genre.getGenreName(),
                        genre.getGenreId().toString());
    }

    @DisplayName("Сохранение жанра после добавления")
    @Order(3)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void insertGenre() throws Exception {
        Genre genre = GENRES.get(1);
        mvc.perform(
                post("/genre/edit")
                        .param("genreName", genre.getGenreName()))  // genreId = null
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);
        verify(mockService).addGenre(captor.capture());
        assertEquals(genre, captor.getValue());
    }

    @DisplayName("Сохранение жанра после корректировки")
    @Order(4)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveGenre() throws Exception {
        Genre genre = GENRES.get(0);
        mvc.perform(
                post("/genre/edit")
                        .param("genreId", genre.getGenreId().toString())
                        .param("genreName", genre.getGenreName()))
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);
        verify(mockService).updateGenre(captor.capture());
        assertEquals(genre, captor.getValue());
    }

    @DisplayName("Удаление жанра")
    @Order(5)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteGenre() throws Exception {
        mvc.perform(
                get("/genre/delete")
                        .param("id", "0"))
                .andExpect(status().is3xxRedirection());
        verify(mockService).deleteGenre(anyLong());
    }

    @DisplayName("Неудачное удаление жанра")
    @Order(6)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteGenreError() throws Exception {
        Genre genre = GENRES.get(0);
        doThrow(new ApplDbConstraintException()).when(mockService).deleteGenre(anyLong());
        when(mockService.getGenreById(genre.getGenreId())).thenReturn(genre);
        MvcResult result = mvc.perform(
                get("/genre/delete")
                        .param("id", genre.getGenreId().toString()))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains("Ошибка",
                        genre.getGenreName())
                .doesNotContain(genre.getGenreId().toString());
    }

}