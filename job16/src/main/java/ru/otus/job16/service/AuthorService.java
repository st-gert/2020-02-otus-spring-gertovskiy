package ru.otus.job16.service;

import ru.otus.job16.model.Author;
import ru.otus.job16.model.dto.AuthorDto;

import java.util.List;

public interface AuthorService {

    List<AuthorDto> getAuthorList();

    AuthorDto getAuthorById(long id);

    void addAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(long authorId);

}
