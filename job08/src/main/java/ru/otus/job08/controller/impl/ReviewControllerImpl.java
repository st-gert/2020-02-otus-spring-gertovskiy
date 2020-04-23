package ru.otus.job08.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job08.controller.ReviewController;
import ru.otus.job08.service.ReviewService;

@Service
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService service;
    private final ResultUtil resultUtil;

    public ReviewControllerImpl(ReviewService service, ResultUtil resultUtil) {
        this.service = service;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<String, String> addReview(String bookId, String opinion) {
        try {
            return Pair.of(service.addReview(bookId, opinion), null);
        } catch (Exception e) {
            return Pair.<String, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateReview(String reviewId, String opinion) {
        try {
            service.updateReview(reviewId, opinion);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteReview(String reviewId) {
        try {
            service.deleteReview(reviewId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
