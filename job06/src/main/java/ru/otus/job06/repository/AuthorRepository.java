package ru.otus.job06.repository;

import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.model.Author;

import java.util.List;
import java.util.Optional;

/**
 * Repository Авторы.
 */
public interface AuthorRepository {

    List<Author> getAuthorList();
    Optional<Author> getAuthorById(Long id);
    Optional<Author> getAuthorByName(Author author);

    Long addAuthor(Author author);

    int updateAuthor(Author author) throws ApplDbConstraintException;

    int deleteAuthor(Long authorId) throws ApplDbConstraintException;

}
