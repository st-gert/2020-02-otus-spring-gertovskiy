package ru.otus.job05.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job05.dao.GenreDao;
import ru.otus.job05.model.Genre;
import ru.otus.job05.service.GenreController;

import java.util.List;

@Service
public class GenreControllerImpl implements GenreController {

    private final GenreDao dao;
    private final ResultUtil resultUtil;

    public GenreControllerImpl(GenreDao dao, ResultUtil resultUtil) {
        this.dao = dao;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<List<Genre>, String> getGenreList() {
        try {
            return resultUtil.handleList(dao.getGenreList());
        } catch (Exception e) {
            return Pair.<List<Genre>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addGenre(String genre) {
        try {
            return Pair.of(dao.addGenre(new Genre(null, genre)), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateGenre(Long genreId, String genre) {
        try {
            return resultUtil.handleInt(dao.updateGenre(new Genre(genreId, genre)));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteGenre(Long genreId) {
        try {
            return resultUtil.handleInt(dao.deleteGenre(genreId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
