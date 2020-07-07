package ru.otus.job16.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.job16.model.Genre;

import java.util.Optional;

@RepositoryRestResource(path = "genre")
public interface GenreControllerRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByGenreName(String genreName);
}
