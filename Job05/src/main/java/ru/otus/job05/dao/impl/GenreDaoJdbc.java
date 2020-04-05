package ru.otus.job05.dao.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.job05.dao.GenreDao;
import ru.otus.job05.exception.ApplDbConstraintException;
import ru.otus.job05.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    private RowMapper<Genre> rowMapper = (rs, n) -> {
        Long id = rs.getLong("id");
        String genreName = rs.getString("genre_name");
        return new Genre(id, genreName);
    };

    @Override
    public List<Genre> getGenreList() {
        return jdbcOperations.query(
                "select id, genre_name from genre order by id",
                rowMapper
        );
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        try {
            return Optional.ofNullable(
                    jdbcOperations.queryForObject(
                            "select id, genre_name from genre where id = :id",
                            new MapSqlParameterSource("id", id),
                            rowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> getGenreByName(String genreName) {
        try {
            return Optional.ofNullable(
                    jdbcOperations.queryForObject(
                            "select id, genre_name from genre where lower(genre_name) = lower(:genre_name)",
                            new MapSqlParameterSource("genre_name", genreName),
                            rowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /** @return ID нового объекта.  */
    @Override
    public long addGenre(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(
                "insert into genre(genre_name) values(:genre_name)",
                new MapSqlParameterSource("genre_name", genre.getGenreName()),
                keyHolder,
                new String[]{"id"}
        );
        return keyHolder.getKey().longValue();
    }

    /**
     * @param genre измененный объект
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены
     * @throws ApplDbConstraintException операция запрещена, нарушен Constraints
     */
    @Override
    public int updateGenre(Genre genre) throws ApplDbConstraintException {
        try {
            return jdbcOperations.update(
                    "update genre set genre_name = :genre_name where id = :id",
                    new MapSqlParameterSource()
                            .addValue("id", genre.getGenreId())
                            .addValue("genre_name", genre.getGenreName())
            );
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

    /**
     * @param genreId ID объекта
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены
     * @throws ApplDbConstraintException операция запрещена, нарушен Constraints
     */
    @Override
    public int deleteGenre(Long genreId) throws ApplDbConstraintException {
        try {
            return jdbcOperations.update(
                    "delete from genre where id = :id",
                    new MapSqlParameterSource("id", genreId)
            );
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

}
