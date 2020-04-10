package ru.otus.job06.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.repository.AuthorRepository;
import ru.otus.job06.model.Author;
import ru.otus.job06.service.AuthorController;

import java.util.List;

@Service
public class AuthorControllerImpl implements AuthorController {

    private final AuthorRepository repository;
    private final ru.otus.job06.service.impl.AuthorUtil authorUtil;
    private final ResultUtil resultUtil;

    public AuthorControllerImpl(AuthorRepository repository, ru.otus.job06.service.impl.AuthorUtil authorUtil, ResultUtil resultUtil) {
        this.repository = repository;
        this.authorUtil = authorUtil;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<List<Author>, String> getAuthorList() {
        try {
            return resultUtil.handleList(repository.getAuthorList());
        } catch (Exception e) {
            return Pair.<List<Author>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addAuthor(String authorFullName) {
        try {
            return Pair.of(repository.addAuthor(authorUtil.createAuthor(authorFullName)), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateAuthor(Long authorId, String authorFullName) {
        Author author = authorUtil.createAuthor(authorFullName);
        author.setAuthorId(authorId);
        try {
            return resultUtil.handleInt(repository.updateAuthor(author));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteAuthor(Long authorId) {
        try {
            return resultUtil.handleInt(repository.deleteAuthor(authorId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
