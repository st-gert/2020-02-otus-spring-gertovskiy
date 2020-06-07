package ru.otus.job12.controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.job12.model.Author;
import ru.otus.job12.model.Book;
import ru.otus.job12.model.Genre;
import ru.otus.job12.model.Review;
import ru.otus.job12.model.dto.BookDto;
import ru.otus.job12.security.JpaUserDetailsService;
import ru.otus.job12.service.BookService;
import ru.otus.job12.service.ReviewService;

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

@DisplayName("GenreController Управление отзывами (роль USER)")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewService mockService;
    @MockBean
    private BookService mockBookService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    public static BookDto BOOK_DTO;
    public static List<Review> REVIEWS;

    @BeforeAll
    static void initBookWithReviews() {
        Book book = new Book(1234567890L, "Название",
                new Genre(987L, "Жанр"),
                Collections.singletonList(new Author(654L, "Имя", "Фамилия")));
        REVIEWS = Arrays.asList(
                new Review(3210L, book, "Интересное"),
                new Review(null, null, "Мура")
        );
        BOOK_DTO = new BookDto(book);
        BOOK_DTO.getReviews().addAll(REVIEWS);
    }

    @DisplayName("Список отзывов")
    @Order(0)
    @Test
    @WithMockUser()     // default user/USER
    void listReviews() throws Exception {
        when(mockBookService.getBookWithReview(BOOK_DTO.getBookId())).thenReturn(BOOK_DTO);
        MvcResult result = mvc.perform(get("/review/list")
                .param("id", BOOK_DTO.getBookId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(BOOK_DTO.toString(),
                        REVIEWS.get(0).getReviewId().toString(),
                        REVIEWS.get(0).getOpinion(),
                        REVIEWS.get(1).getOpinion());
    }

    @DisplayName("Добавление отзыва")
    @Test
    @Order(1)
    @WithMockUser()
    void addReview() throws Exception {
        mvc.perform(get("/review/add")
                .param("bookId", BOOK_DTO.getBookId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @DisplayName("Корректировка отзыва")
    @Test
    @Order(2)
    @WithMockUser()
    void editReview() throws Exception {
        Review review = REVIEWS.get(0);
        when(mockService.getReviewById(review.getReviewId())).thenReturn(review);
        MvcResult result = mvc.perform(
                get("/review/edit")
                        .param("id", review.getReviewId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains(review.getReviewId().toString(),
                        review.getOpinion());
    }

    @DisplayName("Сохранение отзыва после добавления")
    @Order(3)
    @Test
    @WithMockUser()
    void insertReview() throws Exception {
        Review review = REVIEWS.get(1);
        mvc.perform(
                post("/review/edit")
                        .param("opinion", review.getOpinion())  // reviewId = null
                        .param("bookId", BOOK_DTO.getBookId().toString()))
                .andExpect(status().is3xxRedirection());
        verify(mockService).addReview(BOOK_DTO.getBookId(), review.getOpinion());
    }

    @DisplayName("Сохранение отзыва после корректировки")
    @Order(4)
    @Test
    @WithMockUser()
    void saveReview() throws Exception {
        Review review = REVIEWS.get(0);
        mvc.perform(
                post("/review/edit")
                        .param("reviewId", review.getReviewId().toString())
                        .param("opinion", review.getOpinion()))
                .andExpect(status().is3xxRedirection());
        verify(mockService).updateReview(review.getReviewId(), review.getOpinion());
    }

    @DisplayName("Удаление отзыва")
    @Order(5)
    @Test
    @WithMockUser()
    void deleteReview() throws Exception {
        mvc.perform(
                get("/review/delete")
                        .param("reviewId", "0")
                        .param("bookId", "0"))
                .andExpect(status().is3xxRedirection());
        verify(mockService).deleteReview(anyLong());
    }

}