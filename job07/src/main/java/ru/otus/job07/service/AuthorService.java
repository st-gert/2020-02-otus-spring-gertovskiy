package ru.otus.job07.service;

import ru.otus.job07.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> getAuthorList();

    Optional<Author> getAuthorById(long id);

    long addAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(long authorId);

}
