package ru.otus.job07.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job07.controller.GenreController;
import ru.otus.job07.model.Genre;
import ru.otus.job07.service.GenreService;

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
    public Pair<Long, String> addGenre(String genre) {
        try {
            return Pair.of(service.addGenre(new Genre(null, genre)), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateGenre(Long genreId, String genre) {
        try {
            service.updateGenre(new Genre(genreId, genre));
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteGenre(Long genreId) {
        try {
            service.deleteGenre(genreId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
