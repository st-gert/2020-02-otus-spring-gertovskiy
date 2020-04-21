package ru.otus.job07.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job07.exception.ApplDbNoDataFoundException;
import ru.otus.job07.model.Book;
import ru.otus.job07.model.Review;
import ru.otus.job07.repository.BookRepository;
import ru.otus.job07.repository.ReviewRepository;
import ru.otus.job07.service.ReviewService;

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
        return repository.findById(reviewId);
    }

    @Override
    @Transactional
    public long addReview(long bookId, String opinion) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new ApplDbNoDataFoundException();
        }
        Review review = new Review(null, optionalBook.get(), opinion);
        return repository.save(review).getReviewId();
    }

    @Override
    @Transactional
    public void updateReview(Review review) {
        if (!repository.existsById(review.getReviewId())) {
            throw new ApplDbNoDataFoundException();
        }
        repository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(long reviewId) {
        if (!repository.existsById(reviewId)) {
            throw new ApplDbNoDataFoundException();
        }
        repository.deleteById(reviewId);
    }

}
