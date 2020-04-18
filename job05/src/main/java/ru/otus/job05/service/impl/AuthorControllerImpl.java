package ru.otus.job05.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job05.dao.AuthorDao;
import ru.otus.job05.model.Author;
import ru.otus.job05.service.AuthorController;

import java.util.List;

@Service
public class AuthorControllerImpl implements AuthorController {

    private final AuthorDao dao;
    private final AuthorUtil authorUtil;
    private final ResultUtil resultUtil;

    public AuthorControllerImpl(AuthorDao dao, AuthorUtil authorUtil, ResultUtil resultUtil) {
        this.dao = dao;
        this.authorUtil = authorUtil;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<List<Author>, String> getAuthorList() {
        try {
            return resultUtil.handleList(dao.getAuthorList());
        } catch (Exception e) {
            return Pair.<List<Author>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addAuthor(String authorFullName) {
        try {
            return Pair.of(dao.addAuthor(authorUtil.createAuthor(authorFullName)), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateAuthor(Long authorId, String authorFullName) {
        Author author = authorUtil.createAuthor(authorFullName);
        author.setAuthorId(authorId);
        try {
            return resultUtil.handleInt(dao.updateAuthor(author));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteAuthor(Long authorId) {
        try {
            return resultUtil.handleInt(dao.deleteAuthor(authorId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
