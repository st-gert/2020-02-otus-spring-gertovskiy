package ru.otus.job08.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job08.controller.GenreController;
import ru.otus.job08.model.Genre;
import ru.otus.job08.service.GenreService;

import java.util.List;

@Service
public class GenreControllerImpl implements GenreController {

    private final GenreService service;
    private final ResultUtil resultUtil;

    public GenreControllerImpl(GenreService service, ResultUtil resultUtil) {
        this.service = service;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<List<Genre>, String> getGenreList() {
        try {
            return resultUtil.handleList(service.getGenreList());
        } catch (Exception e) {
            return Pair.<List<Genre>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<String, String> addGenre(String genre) {
        try {
            return Pair.of(service.addGenre(new Genre(genre)), null);
        } catch (Exception e) {
            return Pair.<String, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateGenre(String genreId, String genreName) {
        try {
            service.updateGenre(genreId, genreName);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteGenre(String genreId) {
        try {
            service.deleteGenre(genreId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
