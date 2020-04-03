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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extractor для одной книги.
 * Возвращает список с одним элементом.
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
        // сортируем авторов в коллективах авторов, чтобы придать им некий стандартный порядок
        if (book != null && book.getAuthors().size() > 1) {
            book.setAuthors(
                    book.getAuthors()
                            .stream()
                            .sorted(Comparator.comparing(Author::getAuthorId))
                            .collect(Collectors.toList())
            );
        }
        // возвращаем в виде коллекции, т.к. это Extractor
        return Collections.singletonList(book);
    }
}
