package ru.otus.job10.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.Book;
import ru.otus.job10.model.Review;
import ru.otus.job10.model.dto.ReviewDto;
import ru.otus.job10.repository.BookRepository;
import ru.otus.job10.repository.ReviewRepository;
import ru.otus.job10.service.ReviewService;

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
    public ReviewDto getReviewById(Long reviewId) {
        return ReviewDto.of(repository.findById(reviewId)
                .orElseThrow(ApplDbNoDataFoundException::new));
    }

    @Override
    @Transactional
    public void addReview(long bookId, String opinion) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new ApplDbNoDataFoundException();
        }
        Review review = new Review(null, optionalBook.get(), opinion);
        repository.save(review);
    }

    @Override
    @Transactional
    public void updateReview(long reviewId, String opinion) {
        Optional<Review> optionalReview = repository.findById(reviewId);
        Review review = optionalReview.orElseThrow(ApplDbNoDataFoundException::new);
        review.setOpinion(opinion);
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
