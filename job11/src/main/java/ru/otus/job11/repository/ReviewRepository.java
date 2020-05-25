package ru.otus.job11.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.job11.model.Review;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
}
