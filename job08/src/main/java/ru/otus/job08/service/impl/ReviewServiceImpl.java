package ru.otus.job08.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.job08.exception.ApplDbNoDataFoundException;
import ru.otus.job08.model.Book;
import ru.otus.job08.model.Review;
import ru.otus.job08.repository.BookRepository;
import ru.otus.job08.repository.ReviewRepository;
import ru.otus.job08.service.ReviewService;

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
    public Optional<Review> getReviewById(String reviewId) {
        return repository.findById(reviewId);
    }

    @Override
    public String addReview(String bookId, String opinion) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Book book = optionalBook
                .orElseThrow(ApplDbNoDataFoundException::new);
        Review review = new Review(book, opinion);
        Review addedReview = repository.save(review);
        book.getReviews().add(addedReview);
        bookRepository.save(book);
        return addedReview.getId();
    }

    @Override
    public void updateReview(String reviewId, String opinion) {
        Optional<Review> optionalReview = repository.findById(reviewId);
        Review review = optionalReview
                .orElseThrow(ApplDbNoDataFoundException::new);
        review.setOpinion(opinion);
        repository.save(review);
    }

    @Override
    public void deleteReview(String reviewId) {
        Optional<Review> optionalReview = repository.findById(reviewId);
        // Сначала удаляем связи,
        Book book = optionalReview
                .map(Review::getBook)
                .orElseThrow(ApplDbNoDataFoundException::new);
        book.getReviews().remove(optionalReview.get());
        bookRepository.save(book);
        // потом саму сущность
        repository.deleteById(reviewId);
    }

}
