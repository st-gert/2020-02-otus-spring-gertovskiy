package ru.otus.job12.service;

import ru.otus.job12.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAuthorList();

    Author getAuthorById(long id);

    void addAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(long authorId);

}
