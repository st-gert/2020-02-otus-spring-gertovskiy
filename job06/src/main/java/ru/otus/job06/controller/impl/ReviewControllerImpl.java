package ru.otus.job06.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.controller.ReviewController;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.service.ReviewService;

import java.util.Optional;

@Service
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService service;
    private final BookRepository bookRepository;
    private final ResultUtil resultUtil;

    public ReviewControllerImpl(ReviewService service, BookRepository bookRepository, ResultUtil resultUtil) {
        this.service = service;
        this.bookRepository = bookRepository;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<Long, String> addReview(long bookId, String opnion) {
        Book book = bookRepository.getBookById(bookId);
        if (book == null) {
            return Pair.<Long, String>of(null, "Книга не найдена. ID " + bookId);
        }
        book.addReview(opnion);
        try {
            return Pair.of(service.addReview(book), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateReview(long reviewId, String opnion) {
        try {
            Optional<Review> optionalReview = service.getReviewById(reviewId);
            if (optionalReview.isPresent()) {
                Review review = optionalReview.get();
                review.setOpinion(opnion);
                service.updateReview(review);
                return null;
            } else {
                return "Отзыв ID " + reviewId + " не найден";
            }
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteReview(long reviewId) {
        try {
            service.deleteReview(reviewId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
