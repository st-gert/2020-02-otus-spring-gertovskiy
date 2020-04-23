package ru.otus.job08.service;

import ru.otus.job08.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> getAuthorList();

    Optional<Author> getAuthorById(String id);

    String addAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(String authorId);

}
