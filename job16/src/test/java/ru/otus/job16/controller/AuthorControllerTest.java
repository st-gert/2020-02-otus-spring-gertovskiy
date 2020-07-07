package ru.otus.job16.controller;

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
import ru.otus.job16.exception.ApplDbConstraintException;
import ru.otus.job16.exception.ApplDbNoDataFoundException;
import ru.otus.job16.model.Author;
import ru.otus.job16.model.dto.AuthorDto;
import ru.otus.job16.service.AuthorService;

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
@WebMvcTest(AuthorController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService mockService;

    private static final String SERVICE_URI = "/api/author";
    private static final String SERVICE_URI_WITH_ID = "/api/author/1000";
    private static final List<AuthorDto> AUTHORS = Arrays.asList(
            AuthorDto.of(new Author(1234567890L, "Имя-0", "Фамилия-0")),
            AuthorDto.of(new Author(null, "Имя-1", "Фамилия-1"))
    );

    private String buildJson(AuthorDto author) {
        String id = author.getAuthorId() == null ? "null" : author.getAuthorId().toString();
        return "{\"authorId\":" + id
                + ",\"firstName\":\"" + author.getFirstName()
                + "\",\"lastName\":\"" + author.getLastName() + "\"}";
    }

    @DisplayName("Список авторов")
    @Order(0)
    @Test
    void getAllAuthors() throws Exception {
        when(mockService.getAuthorList()).thenReturn(AUTHORS);
        MvcResult result = mvc.perform(get(SERVICE_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("[{").endsWith("}]")
                .contains("\"authorId\":", "\"firstName\":", "\"lastName\":")
                .contains(AUTHORS.get(0).getAuthorId().toString(),
                        AUTHORS.get(0).getFirstName(), AUTHORS.get(0).getLastName(),
                        AUTHORS.get(1).getFirstName(), AUTHORS.get(1).getLastName());
    }

    @DisplayName("Получение автора по ID")
    @Order(1)
    @Test
    void getAuthorById() throws Exception {
        AuthorDto author = AUTHORS.get(0);
        when(mockService.getAuthorById(1000L)).thenReturn(author);
        MvcResult result = mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"authorId\":", "\"firstName\":", "\"lastName\":")
                .contains(AUTHORS.get(0).getAuthorId().toString(),
                        AUTHORS.get(0).getFirstName(), AUTHORS.get(0).getLastName());
    }

    @DisplayName("Получение автора по ID - Error 404")
    @Order(21)
    @Test
    void getAuthorByIdException() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).getAuthorById(anyLong());
        mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
    }

    @DisplayName("Добавление автора")
    @Order(2)
    @Test
    void addAuthor() throws Exception {
        mvc.perform(post(SERVICE_URI).content(buildJson(AUTHORS.get(1)))     // ID == null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).addAuthor(any());
    }

    @DisplayName("Добавление автора - Error 406")
    @Order(22)
    @Test
    void addAuthorError() throws Exception {
        mvc.perform(post(SERVICE_URI).content(buildJson(AUTHORS.get(0)))     // ID != null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(406));
    }


    @DisplayName("Изменение автора")
    @Order(3)
    @Test
    void updateAuthor() throws Exception {
        mvc.perform(put(SERVICE_URI).content(buildJson(AUTHORS.get(0)))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).updateAuthor(any());
    }

    @DisplayName("Изменение автора - Error 404")
    @Order(23)
    @Test
    void updateAuthorError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).updateAuthor(any());
        mvc.perform(put(SERVICE_URI).content(buildJson(AUTHORS.get(1)))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(404));
        verify(mockService).updateAuthor(any());
    }

    @DisplayName("Удаление автора")
    @Order(4)
    @Test
    void deleteAuthor() throws Exception {
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk());
        verify(mockService).deleteAuthor(anyLong());
    }

    @DisplayName("Удаление автора - Error 404")
    @Order(24)
    @Test
    void deleteAuthorError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).deleteAuthor(anyLong());
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
        verify(mockService).deleteAuthor(anyLong());
    }

    @DisplayName("Удаление автора - Error 406")
    @Order(25)
    @Test
    void deleteAuthorError2() throws Exception {
        when(mockService.getAuthorById(anyLong())).thenReturn(AUTHORS.get(0));
        doThrow(new ApplDbConstraintException()).when(mockService).deleteAuthor(anyLong());
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().is(406));
        verify(mockService).deleteAuthor(anyLong());
        verify(mockService).getAuthorById(anyLong());
    }

}