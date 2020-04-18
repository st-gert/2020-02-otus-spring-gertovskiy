package ru.otus.job06.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbNoDataFoundtException;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.repository.ReviewRepository;
import ru.otus.job06.service.ReviewService;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final BookRepository bookRepository;

    public ReviewServiceImpl(ReviewRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return Optional.ofNullable(repository.getReviewById(reviewId));
    }

    @Override
    @Transactional
    public long addReview(long bookId, String opinion) {
        Book book = bookRepository.getBookById(bookId);
        if (book == null) {
            throw new ApplDbNoDataFoundtException();
        }
        Review review = new Review(null, book, opinion);
        return repository.addReview(review);
    }

    @Override
    @Transactional
    public void updateReview(Review review) {
        Review currentReview = repository.getReviewById(review.getReviewId());
        if (currentReview == null) {
            throw new ApplDbNoDataFoundtException();
        }
        repository.updateReview(review);
    }

    @Override
    @Transactional
    public void deleteReview(long reviewId) {
        Review review = repository.getReviewById(reviewId);
        if (review == null) {
            throw new ApplDbNoDataFoundtException();
        }
        repository.deleteReview(review);
    }

}
