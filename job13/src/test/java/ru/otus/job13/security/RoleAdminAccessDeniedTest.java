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
import ru.otus.job13.controller.ReviewController;
import ru.otus.job13.service.BookService;
import ru.otus.job13.service.ReviewService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Роли ADMIN запрещено работать с отзывами")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(ReviewController.class)
public class RoleAdminAccessDeniedTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewService mockService;
    @MockBean
    private BookService mockBookService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    @DisplayName("Список отзывов")
    @Order(0)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listReviews() throws Exception {
        mvc.perform(get("/review/list"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Добавление отзыва")
    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addReview() throws Exception {
        mvc.perform(get("/review/add"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Корректировка отзыва")
    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editReview() throws Exception {
        mvc.perform(get("/review/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Сохранение отзыва")
    @Order(3)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void insertReview() throws Exception {
        mvc.perform(post("/review/edit"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Удаление отзыва")
    @Order(5)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteReview() throws Exception {
        mvc.perform(get("/review/delete"))
                .andExpect(status().isForbidden());
    }
}
