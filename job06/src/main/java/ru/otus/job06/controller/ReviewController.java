package ru.otus.job06.controller;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Контроллер запросов по отзывам на книгу.
 */
public interface ReviewController {

    Pair<Long, String> addReview(long bookId, String opnion);

    String updateReview(long reviewId, String opnion);

    String deleteReview(long reviewId);

}
