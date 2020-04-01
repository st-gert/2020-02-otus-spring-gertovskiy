package ru.otus.job05.dao.ext;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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
 *
 * Внимание! Имеет состояния, использовать только new объекты.
 */
public class BookListResultSetExtractor implements ResultSetExtractor<List<Book>> {

    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    private Map<Long, Author> authorMap;
    private Map<Long, Genre> genreMap;

    public BookListResultSetExtractor(AuthorDao authorDao, GenreDao genreDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        initDictData();
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
        return map.values()
                .stream()
                .sorted(Comparator.<Book, String>comparing(x -> x.getGenre().getGenreName())
                        .thenComparing(x -> x.getAuthors().get(0).getLastName())
                )
                .collect(Collectors.toList())
                ;
    }

    private void initDictData() {
        List<Author> authorList = authorDao.getAuthorList();
        authorMap = authorList
                .stream()
                .collect(Collectors.toMap(Author::getAuthorId, x -> x));
        List<Genre> genreList = genreDao.getGenreList();
        genreMap = genreList
                .stream()
                .collect(Collectors.toMap(Genre::getGenreId, x -> x));
    }

}
