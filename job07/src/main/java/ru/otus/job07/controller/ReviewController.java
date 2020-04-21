package ru.otus.job07.controller;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Контроллер запросов по отзывам на книгу.
 */
public interface ReviewController {

    Pair<Long, String> addReview(long bookId, String opinion);

    String updateReview(long reviewId, String opinion);

    String deleteReview(long reviewId);

}
