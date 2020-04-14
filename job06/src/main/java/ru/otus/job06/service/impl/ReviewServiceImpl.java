package ru.otus.job06.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.exception.ApplDbNoDataFoundtException;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.ReviewRepository;
import ru.otus.job06.service.ReviewService;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return Optional.ofNullable( repository.getReviewById(reviewId) );
    }

    @Override
    public long addReview(Book book) {
        Optional<Review> optionalReview = book.getReviews()
            .stream()
            .filter(x -> x.getReviewId() == null)
            .findAny();
        if (optionalReview.isPresent()) {
            return repository.addReview(optionalReview.get());
        } else {
            throw new ApplDbNoDataFoundtException();
        }
    }

    @Override
    public void updateReview(Review review) {
        Review currentReview = repository.getReviewById(review.getReviewId());
        if (currentReview == null) {
            throw new ApplDbNoDataFoundtException();
        }
        try {
            repository.updateReview(review);
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException();
        }
    }

    @Override
    public void deleteReview(long reviewId) {
        Review review = repository.getReviewById(reviewId);
        if (review == null) {
            throw new ApplDbNoDataFoundtException();
        }
        try {
            repository.deleteReview(review);
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException();
        }
    }

}
