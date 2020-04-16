package ru.otus.job06.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.controller.ReviewController;
import ru.otus.job06.model.Review;
import ru.otus.job06.service.ReviewService;

import java.util.Optional;

@Service
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService service;
    private final ResultUtil resultUtil;

    public ReviewControllerImpl(ReviewService service, ResultUtil resultUtil) {
        this.service = service;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<Long, String> addReview(long bookId, String opinion) {
        try {
            return Pair.of(service.addReview(bookId, opinion), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateReview(long reviewId, String opinion) {
        try {
            Optional<Review> optionalReview = service.getReviewById(reviewId);
            if (optionalReview.isPresent()) {
                Review review = optionalReview.get();
                review.setOpinion(opinion);
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
