package ru.otus.job08.ui;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.job08.controller.GenreController;

@ShellComponent
public class UIGenreShell {

    private final GenreController controller;
    private final ShellUtil shellUtil;

    public UIGenreShell(GenreController controller, ShellUtil shellUtil) {
        this.controller = controller;
        this.shellUtil = shellUtil;
    }

    @ShellMethod(value = "Получить список всех жанров.", key = {"genre-get", "gg"})
    public String getGenreList() {
        return shellUtil.getMessageFromList(controller.getGenreList());
    }

    @ShellMethod(value = "Добавить жанр.", key = {"genre-add", "ga"})
    public String addGenre(
            @ShellOption(value = "-g", help = "Наименование жанра.") String genre) {
        return shellUtil.getMessageFromString(controller.addGenre(genre));
    }

    @ShellMethod(value = "Переименовать жанр.", key = {"genre-update", "gu"})
    public String updateGenre(
            @ShellOption(value = "-id", help = "ID жанра.") String genreId,
            @ShellOption(value = "-g", help = "Новое наименование жанра.") String genre) {
        return shellUtil.getMessageSimple(controller.updateGenre(genreId, genre));
    }

    @ShellMethod(value = "Удалить жанр.", key = {"genre-delete", "gd"})
    public String deleteGenre(
            @ShellOption(value = "-id", help = "ID жанра.") String genreId) {
        return shellUtil.getMessageSimple(controller.deleteGenre(genreId));
    }

}
