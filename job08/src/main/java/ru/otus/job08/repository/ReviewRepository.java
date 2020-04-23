package ru.otus.job08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.job08.model.Review;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository extends MongoRepository<Review, String> {
}
