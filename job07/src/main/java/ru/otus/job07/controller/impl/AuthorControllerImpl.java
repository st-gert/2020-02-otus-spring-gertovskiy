package ru.otus.job07.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job07.controller.AuthorController;
import ru.otus.job07.model.Author;
import ru.otus.job07.service.AuthorService;

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
    public Pair<Long, String> addAuthor(String authorFullName) {
        try {
            return Pair.of(service.addAuthor(AuthorUtil.createAuthor(authorFullName)), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateAuthor(Long authorId, String authorFullName) {
        Author author = AuthorUtil.createAuthor(authorFullName);
        author.setAuthorId(authorId);
        try {
            service.updateAuthor(author);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteAuthor(Long authorId) {
        try {
            service.deleteAuthor(authorId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
