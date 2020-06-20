package ru.otus.job13.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job13.exception.ApplDbNoDataFoundException;
import ru.otus.job13.model.Author;
import ru.otus.job13.repository.AuthorRepository;
import ru.otus.job13.service.AuthorService;

import java.util.List;

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
    public Author getAuthorById(long id) {
        return repository.findById(id)
                .orElseThrow(ApplDbNoDataFoundException::new);
    }

    @Override
    @Transactional
    public void addAuthor(Author author) {
        repository.save(author);
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
