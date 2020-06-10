package ru.otus.job13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.job13.model.Review;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")  // WRITE - читаем для корректировки
    Review getById(Long id);

    @PreAuthorize("hasPermission(#review, 'WRITE')")
    Review save(@Param("review") Review review);

    @Override
    @PreAuthorize("hasPermission(#reviewId, 'ru.otus.job13.model.Review', 'DELETE')")
    void deleteById(@Param("reviewId") Long reviewId);
}
