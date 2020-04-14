package ru.otus.job06.repository;

import ru.otus.job06.model.Author;

import java.util.List;

/**
 * Repository Авторы.
 */
public interface AuthorRepository {

    List<Author> getAuthorList();

    Author getAuthorById(long id);

    // Используется в BookService для заполнения Book
    Author getAuthorByName(Author author);

    long addAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(Author author);

}
