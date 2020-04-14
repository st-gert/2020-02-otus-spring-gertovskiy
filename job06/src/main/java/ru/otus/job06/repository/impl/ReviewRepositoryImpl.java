package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.ReviewRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class ReviewRepositoryImpl implements ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Review getReviewById(long reviewId) {
        return em.find(Review.class, reviewId);
    }

    @Override
    public long addReview(Review review) {
        em.persist(review);
        return review.getReviewId();
    }

    @Override
    public void updateReview(Review review) {
        em.merge(review);
    }

    @Override
    public void deleteReview(Review review) {
        Review deletedReview = em.find(Review.class, review.getReviewId());
        em.remove(deletedReview);
    }

}
