package ru.otus.job06.service;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Контроллер запросов по отзывам на книгу.
 */
public interface ReviewController {

    Pair<Long, String> addReview(long bookId, String opnion);

    String updateReview(Long reviewId, String opnion);

    String deleteReview(Long reviewId);

}
