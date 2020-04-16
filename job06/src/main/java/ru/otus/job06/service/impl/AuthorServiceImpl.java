package ru.otus.job06.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbNoDataFoundtException;
import ru.otus.job06.model.Author;
import ru.otus.job06.repository.AuthorRepository;
import ru.otus.job06.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Author> getAuthorList() {
        return repository.getAuthorList();
    }

    @Override
    public Optional<Author> getAuthorById(long id) {
        return Optional.ofNullable(repository.getAuthorById(id));
    }

    @Override
    @Transactional
    public long addAuthor(Author author) {
        return repository.addAuthor(author);
    }

    @Override
    @Transactional
    public void updateAuthor(Author author) {
        Author currentAuthor = repository.getAuthorById(author.getAuthorId());
        if (currentAuthor == null) {
            throw new ApplDbNoDataFoundtException();
        }
        repository.updateAuthor(author);
    }

    @Override
    @Transactional
    public void deleteAuthor(long authorId) {
        Author author = repository.getAuthorById(authorId);
        if (author == null) {
            throw new ApplDbNoDataFoundtException();
        }
        repository.deleteAuthor(author);
    }

}
