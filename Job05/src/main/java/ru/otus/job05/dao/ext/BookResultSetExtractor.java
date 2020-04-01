package ru.otus.job05.dao.ext;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.job05.model.Author;
import ru.otus.job05.model.Book;
import ru.otus.job05.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Extractor для одной книги.
 * Возвращает список с одним элементом.
 *
 * Не имеет состояний.
 *
 */
public class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Book book = null;
        while (rs.next()) {
            if (book == null) {
                book = new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        new Genre(rs.getLong("genre_id"), rs.getString("genre_name")),
                        new ArrayList<>()
                );
            }
            book.getAuthors().add(new Author(rs.getLong("author_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")));
        }
        return Collections.singletonList(book);
    }
}
