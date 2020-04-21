package ru.otus.job07.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job07.exception.ApplDbNoDataFoundException;
import ru.otus.job07.model.Author;
import ru.otus.job07.repository.AuthorRepository;
import ru.otus.job07.service.AuthorService;

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
        return repository.findAll();
    }

    @Override
    public Optional<Author> getAuthorById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public long addAuthor(Author author) {
        Author addedAuthor = repository.save(author);
        return addedAuthor.getAuthorId();
    }

    @Override
    @Transactional
    public void updateAuthor(Author author) {
        if (!repository.existsById(author.getAuthorId())) {
            throw new ApplDbNoDataFoundException();
        }
        repository.save(author);
    }

    @Override
    @Transactional
    public void deleteAuthor(long authorId) {
        if (!repository.existsById(authorId)) {
            throw new ApplDbNoDataFoundException();
        }
        repository.deleteById(authorId);
    }

}
