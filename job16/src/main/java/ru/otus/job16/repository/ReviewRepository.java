package ru.otus.job16.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job16.model.Review;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
