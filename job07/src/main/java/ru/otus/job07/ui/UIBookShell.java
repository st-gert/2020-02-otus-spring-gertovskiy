package ru.otus.job07.ui;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.job07.controller.BookController;

@ShellComponent
public class UIBookShell {

    private final BookController controller;
    private final ShellUtil shellUtil;

    public UIBookShell(BookController controller, ShellUtil shellUtil) {
        this.controller = controller;
        this.shellUtil = shellUtil;
    }

    @ShellMethod(value = "Получить список всех книг.", key = {"book-get", "bg"})
    public String getBookList () {
        return shellUtil.getMessageFromList(controller.getBookList());
    }

    @ShellMethod(value = "Получить список книг по жанру.", key = {"book-get-genre", "bgg"})
    public String getBookListByGenre (
            @ShellOption(value = "-g", help = "Наименование жанра.") String genre) {
        return shellUtil.getMessageFromList(controller.getBookListByGenre(genre));
    }

    @ShellMethod(value = "Получить список книг по автору.", key = {"book-get-author", "bga"})
    public String getBookListByAuthor (
            @ShellOption(value = "-ln", help = "Фамилия автора.") String authorLastName) {
        return shellUtil.getMessageFromList(controller.getBookListByAuthor(authorLastName));
    }

    @ShellMethod(value = "Добавить книгу.", key = {"book-add", "ba"})
    public String addBook (
            @ShellOption(value = "-t", help = "Название книги.") String title,
            @ShellOption(value = "-g", help = "Жанр.") String genre,
            @ShellOption(value = "-a", help = "Автор или список авторов в формате: \"Имя Фамилия, ...\"")
                    String authors) {
        return shellUtil.getMessageFromLong(controller.addBook(title, genre, authors));
    }

    @ShellMethod(value = "Заменить книгу.", key = {"book-update", "bu"})
    public String updateBook (
            @ShellOption(value = "-id", help = "ID книги.") long bookId,
            @ShellOption(value = "-t", help = "Новое название книги.") String title,
            @ShellOption(value = "-g", help = "Жанр.") String genre,
            @ShellOption(value = "-a", help = "Автор или список авторов в формате: \"Имя Фамилия, ...\"")
                    String authors) {
        return shellUtil.getMessageSimple(controller.updateBook(bookId, title, genre, authors));
    }

    @ShellMethod(value = "Удалить книгу.", key = {"book-delete", "bd"})
    public String deleteBook (
            @ShellOption(value = "-id", help = "ID книги.") long bookId) {
        return shellUtil.getMessageSimple(controller.deleteBook(bookId));
    }

}
