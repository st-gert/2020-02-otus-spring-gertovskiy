package ru.otus.job13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job13.model.security.UserEntity;

import java.util.Optional;

/**
 * Repository Пользователи системы.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
