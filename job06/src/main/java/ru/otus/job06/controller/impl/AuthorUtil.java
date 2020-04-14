package ru.otus.job06.controller.impl;

import org.springframework.stereotype.Service;
import ru.otus.job06.model.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательный сервис получения объекта Автор и списка Авторов из строки.
 */
@Service
class AuthorUtil {

    List<Author> createAuthorList(String authors) {
        List<Author> authorList = new ArrayList<>();
        String[] authorArray = authors.split(",");
        for (String authorFullName: authorArray) {
            authorList.add(createAuthor(authorFullName));
        }
        return authorList;
    }

    Author createAuthor(String authorFullName) {
        int pos = authorFullName.lastIndexOf(" ");
        String firstName = authorFullName.substring(0, pos).trim();
        String lastName = authorFullName.substring(pos + 1);
        return new Author(null, firstName, lastName);
    }

}
