package ru.otus.job16.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.job16.exception.ApplDbNoDataFoundException;
import ru.otus.job16.model.dto.BookDto;
import ru.otus.job16.model.dto.ReviewDto;
import ru.otus.job16.service.BookService;
import ru.otus.job16.service.ReviewService;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReviewService service;
    private final BookService bookService;

    public ReviewController(ReviewService service, BookService bookService) {
        this.service = service;
        this.bookService = bookService;
    }

    @GetMapping("/book/{bookId}")
    public BookDto getBookWithReviews(@PathVariable("bookId") Long bookId) {
        try {
            return bookService.getBookWithReview(bookId);
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("Отзывы. Книга ID {} не найдена", bookId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + bookId + " не найдена");
        }
    }

    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable("id") Long id) {
        try {
            return service.getReviewById(id);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв ID " + id + " не найден");
        }
    }

    @PostMapping
    public void addReview(@RequestBody ReviewDto review) {
        if (review.getReviewId() != null) {
            LOGGER.error("Добавление отзыва с имеющимся ID {}", review.getReviewId());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно добавить отзыв с имеющимся ID");
        }
        LOGGER.info("Добавление отзыва");
        try {
            long id = service.addReview(review.getBookId(), review.getOpinion());
            LOGGER.info("новый отзыв ID {}", id);
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("книга ID {} не найдена", review.getBookId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + review.getBookId() + " не найдена");
        }
    }

    @PutMapping
    public void updateReview(@RequestBody ReviewDto review) {
        try {
            service.updateReview(review.getReviewId(), review.getOpinion());
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв ID " + review.getReviewId() + " не найден");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Удаление отзыва ID {}", id);
            service.deleteReview(id);
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("Ошибка удаления отзыва. ID {} не найден", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв ID " + id + " не найден");
        }
    }

}
