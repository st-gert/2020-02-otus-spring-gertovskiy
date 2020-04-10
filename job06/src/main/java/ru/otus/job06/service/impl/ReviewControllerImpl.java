package ru.otus.job06.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.repository.ReviewRepository;
import ru.otus.job06.service.ReviewController;

import java.util.Optional;

@Service
public class ReviewControllerImpl implements ReviewController {

    private final ReviewRepository repository;
    private final BookRepository bookRepository;
    private final ResultUtil resultUtil;

    public ReviewControllerImpl(ReviewRepository repository, BookRepository bookRepository, ResultUtil resultUtil) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<Long, String> addReview(long bookId, String opnion) {
        Optional<Book> optionalBook = bookRepository.getBookById(bookId);
        if (!optionalBook.isPresent()) {
            return Pair.<Long, String>of(null, "Книга не найдена. ID " + bookId);
        }
        Book book = optionalBook.get();
        book.addReview(opnion);
        try {
            return Pair.of(repository.addReview(book), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateReview(Long reviewId, String opnion) {
        try {
            Optional<Review> optionalReview = repository.getReviewById(reviewId);
            if (optionalReview.isPresent()) {
                Review review = optionalReview.get();
                review.setOpinion(opnion);
                repository.updateReview(review);
                return null;
            } else {
                return "Отзыв ID " + reviewId + " не найден";
            }
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteReview(Long reviewId) {
        try {
            return resultUtil.handleInt(repository.deleteReview(reviewId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
