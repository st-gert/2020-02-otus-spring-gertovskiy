package ru.otus.job05.dao.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.job05.dao.AuthorDao;
import ru.otus.job05.model.Author;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    private RowMapper<Author> rowMapper = (rs, n) -> {
        Long id = rs.getLong("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        return new Author(id, firstName, lastName);
    };



    @Override
    public List<Author> getAuthorList() {
        return jdbcOperations.query(
                "select id, first_name, last_name from author order by last_name, first_name",
                rowMapper
        );
    }

    @Override
    public Optional<Author> getAuthorById(Long id) {
        try {
            return Optional.ofNullable(
                    jdbcOperations.queryForObject(
                            "select id, first_name, last_name from author where id = :id",
                            new MapSqlParameterSource("id", id),
                            rowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> getAuthorByName(Author author) {
        try {
            return Optional.ofNullable(
                    jdbcOperations.queryForObject(
                            "select id, first_name, last_name from author" +
                                    " where lower(first_name) = lower(:first_name)" +
                                    "   and lower(last_name) = lower(:last_name)",
                            new MapSqlParameterSource()
                                    .addValue("first_name", author.getFirstName())
                                    .addValue("last_name", author.getLastName()),
                            rowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /** @return ID нового объекта.  */
    @Override
    public Long addAuthor(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(
                "insert into author(first_name, last_name) values(:first_name, :last_name)",
                new MapSqlParameterSource()
                        .addValue("first_name", author.getFirstName())
                        .addValue("last_name", author.getLastName()),
                keyHolder,
                new String[]{"id"}
        );
        return keyHolder.getKey().longValue();
    }

    /**
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      1 - OK, 0 - Данные не найдены, -1 - Операция запрещена, нарушен Constraints.
     */
    @Override
    public int updateAuthor(Author author) {
        try {
            return jdbcOperations.update(
                    "update author set first_name = :first_name, last_name = :last_name " +
                            "where id = :id",
                    new MapSqlParameterSource()
                            .addValue("id", author.getAuthorId())
                            .addValue("first_name", author.getFirstName())
                            .addValue("last_name", author.getLastName())
            );
        } catch (DataIntegrityViolationException e) {
            return -1;
        }
    }

    /**
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      1 - OK, 0 - Данные не найдены, -1 - Операция запрещена, нарушен Constraints.
     */
    @Override
    public int deleteAuthor(Long authorId) {
        try {
            return jdbcOperations.update(
                    "delete from author where id = :id",
                    new MapSqlParameterSource("id", authorId)
            );
        } catch (DataIntegrityViolationException e) {
            return -1;
        }
    }

}
