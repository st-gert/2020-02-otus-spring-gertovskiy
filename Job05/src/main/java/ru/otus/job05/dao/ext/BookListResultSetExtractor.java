package ru.otus.job05.dao.ext;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.otus.job05.dao.AuthorDao;
import ru.otus.job05.dao.GenreDao;
import ru.otus.job05.model.Author;
import ru.otus.job05.model.Book;
import ru.otus.job05.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extractor для списка книг.
 */
@Component
public class BookListResultSetExtractor implements ResultSetExtractor<List<Book>> {

    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookListResultSetExtractor(AuthorDao authorDao, GenreDao genreDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Author> authorMap = authorDao.getAuthorList()
                .stream()
                .collect(Collectors.toMap(Author::getAuthorId, x -> x));
        Map<Long, Genre> genreMap = genreDao.getGenreList()
                .stream()
                .collect(Collectors.toMap(Genre::getGenreId, x -> x));
        Map<Long, Book> map = new HashMap<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            Book book = map.get(id);
            if (book == null) {
                book = new Book(id,
                        rs.getString("title"),
                        genreMap.get(rs.getLong("genre_id")),
                        new ArrayList<>());
                map.put(id, book);
            }
            book.getAuthors().add(authorMap.get(rs.getLong("author_id")));
        }
        // сортируем авторов в коллективах авторов, чтобы придать им некий стандартный порядок
        map.values()
                .stream()
                .filter(x -> x.getAuthors().size() > 1)
                .forEach(x -> x.setAuthors(
                        x.getAuthors()
                                .stream()
                                .sorted(Comparator.comparing(Author::getAuthorId))
                                .collect(Collectors.toList())
                ));
        return map.values()
                .stream()
                .sorted(Comparator.<Book, String>comparing(x -> x.getGenre().getGenreName())
                        .thenComparing(x -> x.getAuthors().get(0).getLastName())
                )
                .collect(Collectors.toList())
                ;
    }

}
