package ru.otus.job12.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job12.model.Review;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
