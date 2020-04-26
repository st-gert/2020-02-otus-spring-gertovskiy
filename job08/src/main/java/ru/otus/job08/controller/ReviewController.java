package ru.otus.job08.controller;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Контроллер запросов по отзывам на книгу.
 */
public interface ReviewController {

    Pair<String, String> addReview(String bookId, String opinion);

    String updateReview(String reviewId, String opinion);

    String deleteReview(String reviewId);

}
