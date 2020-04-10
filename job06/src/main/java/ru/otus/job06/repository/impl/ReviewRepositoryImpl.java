package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.exception.ApplException;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.ReviewRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return Optional.ofNullable( em.find(Review.class, reviewId) );
    }

    @Transactional
    @Override
    public long addReview(Book book) {
        Optional<Review> optionalReview = book.getReviews()
                .stream()
                .filter(x -> x.getReviewId() == null)
                .findAny()
                ;
        if (optionalReview.isPresent()) {
            Review addedReview = em.merge(optionalReview.get());
            return addedReview.getReviewId();
        } else {
            throw new ApplException("Книга не содержит новых отзывов");
        }
    }

    @Transactional
    @Override
    public void updateReview(Review review) {
        try {
            em.merge(review);
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

    @Transactional
    @Override
    public int deleteReview(Long reviewId) {
        Query query = em.createQuery("delete from Review r where r.reviewId = :reviewId");
        query.setParameter("reviewId", reviewId);
        try {
            return query.executeUpdate();
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

}
