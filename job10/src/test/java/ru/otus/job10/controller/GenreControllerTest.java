package ru.otus.job10.controller;

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
import ru.otus.job10.exception.ApplDbConstraintException;
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.Genre;
import ru.otus.job10.model.dto.GenreDto;
import ru.otus.job10.service.GenreService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService mockService;

    private static final String SERVICE_URI = "/api/genre";
    private static final String SERVICE_URI_WITH_ID = "/api/genre/1000";
    private static final List<GenreDto> GENRES = Arrays.asList(
            GenreDto.of(new Genre(1234567890L, "Интересное")),
            GenreDto.of(new Genre(null, "Мура"))
    );

    private String buildJson(GenreDto genre) {
        String id = genre.getGenreId() == null ? "null" : genre.getGenreId().toString();
        return "{\"genreId\":" + id +
                ",\"genreName\":\"" + genre.getGenreName() + "\"}";
    }

    @DisplayName("Список жанров")
    @Order(0)
    @Test
    void getAllGenres() throws Exception {
        when(mockService.getGenreList()).thenReturn(GENRES);
        MvcResult result = mvc.perform(get(SERVICE_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"genreId\":", "\"genreName\":")
                .contains(GENRES.get(0).getGenreId().toString(),
                        GENRES.get(0).getGenreName(),
                        GENRES.get(1).getGenreName());
    }

    @DisplayName("Получение жанра по ID")
    @Order(1)
    @Test
    void getGenreById() throws Exception {
        GenreDto genre = GENRES.get(0);
        when(mockService.getGenreById(1000L)).thenReturn(genre);
        MvcResult result = mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"genreId\":", "\"genreName\":")
                .contains(GENRES.get(0).getGenreId().toString(),
                        GENRES.get(0).getGenreName());
    }

    @DisplayName("Получение жанра по ID - Error 404")
    @Order(21)
    @Test
    void getGenreByIdException() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).getGenreById(anyLong());
        mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
    }

    @DisplayName("Добавление жанра")
    @Order(2)
    @Test
    void addGenre() throws Exception {
        mvc.perform(post(SERVICE_URI).content(buildJson(GENRES.get(1)))     // ID == null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).addGenre(any());
    }

    @DisplayName("Добавление жанра - Error 406")
    @Order(22)
    @Test
    void addGenreError() throws Exception {
        mvc.perform(post(SERVICE_URI).content(buildJson(GENRES.get(0)))     // ID != null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(406));
    }


    @DisplayName("Изменение жанра")
    @Order(3)
    @Test
    void updateGenre() throws Exception {
        mvc.perform(put(SERVICE_URI).content(buildJson(GENRES.get(0)))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).updateGenre(any());
    }

    @DisplayName("Изменение жанра - Error 404")
    @Order(23)
    @Test
    void updateGenreError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).updateGenre(any());
        mvc.perform(put(SERVICE_URI).content(buildJson(GENRES.get(1)))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(404));
        verify(mockService).updateGenre(any());
    }

    @DisplayName("Удаление жанра")
    @Order(4)
    @Test
    void deleteGenre() throws Exception {
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk());
        verify(mockService).deleteGenre(anyLong());
    }

    @DisplayName("Удаление жанра - Error 404")
    @Order(24)
    @Test
    void deleteGenreError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).deleteGenre(anyLong());
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
        verify(mockService).deleteGenre(anyLong());
    }

    @DisplayName("Удаление жанра - Error 406")
    @Order(25)
    @Test
    void deleteGenreError2() throws Exception {
        when(mockService.getGenreById(anyLong())).thenReturn(GENRES.get(0));
        doThrow(new ApplDbConstraintException()).when(mockService).deleteGenre(anyLong());
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().is(406));
        verify(mockService).deleteGenre(anyLong());
        verify(mockService).getGenreById(anyLong());
    }

}