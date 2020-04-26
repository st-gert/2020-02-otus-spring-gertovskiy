package ru.otus.job08.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job08.controller.AuthorController;
import ru.otus.job08.model.Author;
import ru.otus.job08.service.AuthorService;

import java.util.List;

@Service
public class AuthorControllerImpl implements AuthorController {

    private final AuthorService service;
    private final ResultUtil resultUtil;

    public AuthorControllerImpl(AuthorService service, ResultUtil resultUtil) {
        this.service = service;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<List<Author>, String> getAuthorList() {
        try {
            return resultUtil.handleList(service.getAuthorList());
        } catch (Exception e) {
            return Pair.<List<Author>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<String, String> addAuthor(String authorFullName) {
        try {
            return Pair.of(service.addAuthor(AuthorUtil.createAuthor(authorFullName)), null);
        } catch (Exception e) {
            return Pair.<String, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateAuthor(String authorId, String authorFullName) {
        Author author = AuthorUtil.createAuthor(authorFullName);
        author.setId(authorId);
        try {
            service.updateAuthor(author);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteAuthor(String authorId) {
        try {
            service.deleteAuthor(authorId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
