package ru.otus.job13.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.job13.controller.AuthorController;
import ru.otus.job13.controller.GenreController;
import ru.otus.job13.controller.ReviewController;
import ru.otus.job13.service.AuthorService;
import ru.otus.job13.service.BookService;
import ru.otus.job13.service.GenreService;
import ru.otus.job13.service.ReviewService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Роли Anonymous запрещено все кроме чтения списков книг")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest({AuthorController.class, GenreController.class, ReviewController.class})
public class RoleAnonymousAccessDeniedTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService mockAuthorService;
    @MockBean
    private GenreService mockGenreService;
    @MockBean
    private ReviewService mockReviewService;
    @MockBean
    private BookService mockBookService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    @DisplayName("Добавление книги")
    @Test
    @Order(1)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void addBook() throws Exception {
        mvc.perform(get("/book/add"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Корректировка книги")
    @Test
    @Order(2)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void editBook() throws Exception {
        mvc.perform(get("/book/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Сохранение книги")
    @Order(4)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void insertBook() throws Exception {
        mvc.perform(post("/book/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Удаление книги")
    @Order(5)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void deleteBook() throws Exception {
        mvc.perform(get("/book/delete"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Список авторов")
    @Test
    @Order(10)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void listAuthors() throws Exception {
        mvc.perform(get("/author/list"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Добавление автора")
    @Test
    @Order(11)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void addAuthor() throws Exception {
        mvc.perform(get("/author/add"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Корректировка автора")
    @Test
    @Order(12)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void editAuthor() throws Exception {
        mvc.perform(get("/author/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Сохранение автора")
    @Order(14)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void insertAuthor() throws Exception {
        mvc.perform(post("/author/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Удаление автора")
    @Order(15)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void deleteAuthor() throws Exception {
        mvc.perform(get("/author/delete"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Список жанров")
    @Test
    @Order(20)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void listGenre() throws Exception {
        mvc.perform(get("/genre/list"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Добавление жанра")
    @Test
    @Order(21)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void addGenre() throws Exception {
        mvc.perform(get("/genre/add"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Корректировка жанра")
    @Test
    @Order(22)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void editGenre() throws Exception {
        mvc.perform(get("/genre/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Сохранение жанра")
    @Order(24)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void insertGenre() throws Exception {
        mvc.perform(post("/genre/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Удаление жанра")
    @Order(25)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void deleteGenre() throws Exception {
        mvc.perform(get("/genre/delete"))
                .andExpect(status().isForbidden());
    }


    @DisplayName("Список отзывов")
    @Order(30)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void listReviews() throws Exception {
        mvc.perform(get("/review/list"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Добавление отзыва")
    @Test
    @Order(31)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void addReview() throws Exception {
        mvc.perform(get("/review/add"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Корректировка отзыва")
    @Test
    @Order(32)
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void editReview() throws Exception {
        mvc.perform(get("/review/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Сохранение отзыва")
    @Order(33)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void insertReview() throws Exception {
        mvc.perform(post("/review/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Удаление отзыва")
    @Order(34)
    @Test
    @WithMockUser(username = "anonymous", roles = {"ANONYMOUS"})
    void deleteReview() throws Exception {
        mvc.perform(get("/review/delete"))
                .andExpect(status().isForbidden());
    }

}
