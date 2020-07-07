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
import ru.otus.job16.model.Review;
import ru.otus.job16.model.dto.BookDto;
import ru.otus.job16.model.dto.ReviewDto;
import ru.otus.job16.service.BookService;
import ru.otus.job16.service.ReviewService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
@WebMvcTest(ReviewController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewService mockService;
    @MockBean
    private BookService mockBookService;

    private static final String SERVICE_URI = "/api/review";
    private static final String SERVICE_URI_BOOK = "/api/review/book/1000";
    private static final String SERVICE_URI_WITH_ID = "/api/review/9999";

    private static BookDto BOOK_DTO;
    private static ReviewDto REVIEW;
    private static String REVIEW_JSON;

    @BeforeAll
    static void initBookWithReviews() {
        Book book = new Book(1234567890L, "Название",
                new Genre(987L, "Жанр"),
                Collections.singletonList(new Author(1000L, "Имя", "Фамилия")));
        REVIEW = ReviewDto.of(new Review(9999L, book, "Мура"));
        BOOK_DTO = BookDto.of(book);
        BOOK_DTO.getReviews().add(REVIEW);
        REVIEW_JSON = "{\"bookId\":1000,\"reviewId\":9999,\"opinion\":\"Мура\"}";
    }

    @DisplayName("Список отзывов")
    @Order(0)
    @Test
    void getBookWithReviews() throws Exception {
        when(mockBookService.getBookWithReview(anyLong())).thenReturn(BOOK_DTO);
        MvcResult result = mvc.perform(get(SERVICE_URI_BOOK))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"bookId\":", "\"genreName\":", "\"reviews\":[", "]")
                .contains(BOOK_DTO.getBookId().toString(),
                        BOOK_DTO.getTitle(),
                        BOOK_DTO.getReviews().get(0).getOpinion());
        verify(mockBookService).getBookWithReview(anyLong());
    }

    @DisplayName("Получение отзыва")
    @Order(1)
    @Test
    void getReviewById() throws Exception {
        when(mockService.getReviewById(anyLong())).thenReturn(REVIEW);
        MvcResult result = mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(content)
                .startsWith("{").endsWith("}")
                .contains("\"bookId\":", "\"reviewId\":", "\"opinion\":")
                .contains(REVIEW.getBookId().toString(),
                        REVIEW.getOpinion(),
                        REVIEW.getReviewId().toString());
        verify(mockService).getReviewById(anyLong());
    }

    @DisplayName("Добавление отзыва")
    @Order(2)
    @Test
    void addReview() throws Exception {
        String content = REVIEW_JSON.replace("9999", "null");
        mvc.perform(post(SERVICE_URI).content(content)     // ID == null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).addReview(anyLong(), anyString());
    }

    @DisplayName("Изменение отзыва")
    @Order(3)
    @Test
    void updateReview() throws Exception {
        mvc.perform(put(SERVICE_URI).content(REVIEW_JSON)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(mockService).updateReview(anyLong(), anyString());
    }

    @DisplayName("Удаление отзыва")
    @Order(4)
    @Test
    void deleteReview() throws Exception {
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().isOk());
        verify(mockService).deleteReview(anyLong());
    }

    @DisplayName("Список отзывов - Error 404")
    @Order(20)
    @Test
    void getBookWithReviewsError() throws Exception {
        when(mockBookService.getBookWithReview(anyLong())).thenThrow(new ApplDbNoDataFoundException());
        mvc.perform(get(SERVICE_URI_BOOK))
                .andExpect(status().is(404));
    }

    @DisplayName("Получение отзыва - Error 404")
    @Order(21)
    @Test
    void getReviewByIdError() throws Exception {
        when(mockService.getReviewById(anyLong())).thenThrow(new ApplDbNoDataFoundException());
        mvc.perform(get(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
    }

    @DisplayName("Добавление отзыва - Error 404")
    @Order(22)
    @Test
    void addReviewError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).addReview(anyLong(), anyString());
        String content = REVIEW_JSON.replace("9999", "null");
        mvc.perform(post(SERVICE_URI).content(content)     // ID == null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(404));
    }

    @DisplayName("Добавление отзыва - Error 406")
    @Order(32)
    @Test
    void addReviewError2() throws Exception {
        mvc.perform(post(SERVICE_URI).content(REVIEW_JSON)  // ID != null
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(406));
    }

    @DisplayName("Изменение отзыва - Error 404")
    @Order(23)
    @Test
    void updateReviewError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).updateReview(anyLong(), anyString());
        mvc.perform(put(SERVICE_URI).content(REVIEW_JSON)
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is(404));
    }

    @DisplayName("Удаление отзыва - Error 404")
    @Order(24)
    @Test
    void deleteReviewError() throws Exception {
        doThrow(new ApplDbNoDataFoundException()).when(mockService).deleteReview(anyLong());
        mvc.perform(delete(SERVICE_URI_WITH_ID))
                .andExpect(status().is(404));
    }
}