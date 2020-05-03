package ru.otus.job09.controller;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.job09.exception.ApplDbConstraintException;
import ru.otus.job09.model.Author;
import ru.otus.job09.service.AuthorService;

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

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService mockService;

    public static final List<Author> AUTHORS = Arrays.asList(
            new Author(1234567890L, "Иван", "Иванов"),
            new Author(null, "Петр", "Петров")
    );

    @DisplayName("Список авторов")
    @Order(0)
    @Test
    void listAuthors() throws Exception {
        when(mockService.getAuthorList()).thenReturn(AUTHORS);
        MvcResult result = mvc.perform(get("/author/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(AUTHORS.get(0).getAuthorId().toString(),
                        AUTHORS.get(0).getLastName(),
                        AUTHORS.get(1).getLastName());
    }

    @DisplayName("Добавление автора")
    @Test
    @Order(1)
    void addAuthor() throws Exception {
        mvc.perform(get("/author/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @DisplayName("Корректировка автора")
    @Test
    @Order(2)
    void editAuthor() throws Exception {
        Author author = AUTHORS.get(0);
        when(mockService.getAuthorById(author.getAuthorId())).thenReturn(author);
        MvcResult result = mvc.perform(
                get("/author/edit")
                        .param("id", author.getAuthorId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(author.getAuthorId().toString(),
                        author.getFirstName(),
                        author.getLastName());
    }

    @DisplayName("Сохранение автора после добавления")
    @Order(3)
    @Test
    void insertAuthor() throws Exception {
        Author author = AUTHORS.get(1);
        mvc.perform(
                post("/author/edit")
                        .param("firstName", author.getFirstName()) // authorId = null
                        .param("lastName", author.getLastName()))
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockService).addAuthor(captor.capture());
        assertEquals(author, captor.getValue());
    }

    @DisplayName("Сохранение автора после корректировки")
    @Order(4)
    @Test
    void saveAuthor() throws Exception {
        Author author = AUTHORS.get(0);
        mvc.perform(
                post("/author/edit")
                        .param("authorId", author.getAuthorId().toString())
                        .param("firstName", author.getFirstName())
                        .param("lastName", author.getLastName()))
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockService).updateAuthor(captor.capture());
        assertEquals(author, captor.getValue());
    }

    @DisplayName("Удаление автора")
    @Order(5)
    @Test
    void deleteAuthor() throws Exception {
        mvc.perform(
                get("/author/delete")
                        .param("id", "0"))
                .andExpect(status().is3xxRedirection());
        verify(mockService).deleteAuthor(anyLong());
    }

    @DisplayName("Неудачное удаление автора")
    @Order(6)
    @Test
    void deleteAuthorError() throws Exception {
        Author author = AUTHORS.get(0);
        doThrow(new ApplDbConstraintException()).when(mockService).deleteAuthor(anyLong());
        when(mockService.getAuthorById(author.getAuthorId())).thenReturn(author);
        MvcResult result = mvc.perform(
                get("/author/delete")
                        .param("id", author.getAuthorId().toString()))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains("Ошибка",
                        author.getFullName())
                .doesNotContain(author.getAuthorId().toString());
    }

}