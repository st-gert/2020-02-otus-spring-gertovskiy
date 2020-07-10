package ru.otus.job16.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.job16.repository.BookRepository;

/**
 * Контроль доступности БД.
 */
@Component
public class CustomDbHealthIndicator implements HealthIndicator {

    private final BookRepository repository;

    public CustomDbHealthIndicator(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        int cnt = repository.testBookEmpty();
        if (cnt == 0) {
            return Health
                    .down()
                    .withDetail("message", "Table BOOK is empty")
                    .build();
        } else {
            return Health
                    .up()
                    .withDetail("message", "Table BOOK has some rows")
                    .build();
        }
    }
}
