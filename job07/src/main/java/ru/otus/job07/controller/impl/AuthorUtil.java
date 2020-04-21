package ru.otus.job07.controller.impl;

import ru.otus.job07.model.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательные утилиты получения объекта Автор и списка Авторов из строки.
 */
class AuthorUtil {

    static List<Author> createAuthorList(String authors) {
        List<Author> authorList = new ArrayList<>();
        String[] authorArray = authors.split(",");
        for (String authorFullName: authorArray) {
            authorList.add(createAuthor(authorFullName));
        }
        return authorList;
    }

    static Author createAuthor(String authorFullName) {
        int pos = authorFullName.lastIndexOf(" ");
        String firstName = authorFullName.substring(0, pos).trim();
        String lastName = authorFullName.substring(pos + 1);
        return new Author(null, firstName, lastName);
    }

}
