package ru.otus.job07.ui;

import org.springframework.stereotype.Service;
import ru.otus.job07.model.Author;
import ru.otus.job07.model.Book;
import ru.otus.job07.model.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Формирование примеров объектов для тестирования: Книга, Автор, Жанр.
 */
@Service
public class TestShellUtil {

    public List<Genre> createGenreList() {
        List<Genre> list = new ArrayList<>();
        list.add(new Genre(1L, "Фантастика"));
        list.add(new Genre(2L, "Детектив"));
        return list;
    }

    public List<Author> createAuthorList() {
        List<Author> list = new ArrayList<>();
        list.add(new Author(1L, "Аркадий", "Стругацкий"));
        list.add(new Author(2L, "Борис", "Стругацкий"));
        list.add(new Author(3L, "Борис", "Акунин"));
        return list;
    }

    public List<Book> createBookList() {
        List<Genre> genreList = createGenreList();
        List<Author> authorList = createAuthorList();
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, "Понедельник начинается в субботу", genreList.get(0),
                Arrays.asList(authorList.get(0), authorList.get(1))));
        bookList.add(new Book(2L, "Азазель", genreList.get(1),
                Collections.singletonList(authorList.get(2))));
        return bookList;
    }

}
