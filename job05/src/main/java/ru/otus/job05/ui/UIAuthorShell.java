package ru.otus.job05.ui;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.job05.service.AuthorController;

@ShellComponent
public class UIAuthorShell {

    private final AuthorController controller;
    private final ShellUtil shellUtil;

    public UIAuthorShell(AuthorController controller, ShellUtil shellUtil) {
        this.controller = controller;
        this.shellUtil = shellUtil;
    }

    @ShellMethod(value = "Получить список всех авторов.", key = {"author-get", "ag"})
    public String getAuthorList() {
        return shellUtil.getMessageFromList(controller.getAuthorList());
    }

    @ShellMethod(value = "Добавить автора.", key = {"author-add", "aa"})
    public String addAuthor(
            @ShellOption(value = "-a", help = "Автор в формате: Имя Фамилия.") String author) {
        return shellUtil.getMessageFromLong(controller.addAuthor(author));
    }

    @ShellMethod(value = "Изменить имя автора.", key = {"author-update", "au"})
    public String updateAuthor(
            @ShellOption(value = "-id", help = "ID автора.") long authorId,
            @ShellOption(value = "-a", help = "Автор в формате: Имя Фамилия.") String author) {
        return shellUtil.getMessageSimple(controller.updateAuthor(authorId, author));
    }

    @ShellMethod(value = "Удалить автора.", key = {"author-delete", "ad"})
    public String deleteAuthor(
            @ShellOption(value = "-id", help = "ID автора.") long authorId) {
        return shellUtil.getMessageSimple(controller.deleteAuthor(authorId));
    }

}
