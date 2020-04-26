package ru.otus.job08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.job08.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends MongoRepository<Genre, String> {

    // Используется в BookService для заполнения Book и поиска книг по жанру
    Genre findByGenreNameIgnoreCase(String genreName);

}


