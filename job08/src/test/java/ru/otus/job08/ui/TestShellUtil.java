package ru.otus.job08.ui;

import org.springframework.stereotype.Service;
import ru.otus.job08.model.Author;
import ru.otus.job08.model.Book;
import ru.otus.job08.model.Genre;

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
        list.add(new Genre("Фантастика"));
        list.add(new Genre("Детектив"));
        list.get(0).setId("1");
        list.get(1).setId("2");
        return list;
    }

    public List<Author> createAuthorList() {
        List<Author> list = new ArrayList<>();
        list.add(new Author("Аркадий", "Стругацкий"));
        list.add(new Author("Борис", "Стругацкий"));
        list.add(new Author("Борис", "Акунин"));
        list.get(0).setId("1");
        list.get(1).setId("2");
        list.get(2).setId("3");
        return list;
    }

    public List<Book> createBookList() {
        List<Genre> genreList = createGenreList();
        List<Author> authorList = createAuthorList();
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("Понедельник начинается в субботу", genreList.get(0),
                Arrays.asList(authorList.get(0), authorList.get(1))));
        bookList.add(new Book("Азазель", genreList.get(1),
                Collections.singletonList(authorList.get(2))));
        bookList.get(0).setId("1");
        bookList.get(1).setId("2");
        return bookList;
    }

}
