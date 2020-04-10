package ru.otus.job06.ui;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.job06.service.ReviewController;

@ShellComponent
public class UIReviewShell {

    private final ReviewController controller;
    private final ShellUtil shellUtil;

    public UIReviewShell(ReviewController controller, ShellUtil shellUtil) {
        this.controller = controller;
        this.shellUtil = shellUtil;
    }

    @ShellMethod(value = "Добавить отзыв.", key = {"review-add", "ra"})
    public String addGenre(
            @ShellOption(value = "-id", help = "ID книги.") long bookId,
            @ShellOption(value = "-r", help = "Отзыв на книгу.") String opinion) {
        return shellUtil.getMessageFromLong(controller.addReview(bookId, opinion));
    }

    @ShellMethod(value = "Изменить отзыв.", key = {"review-update", "ru"})
    public String updateGenre(
            @ShellOption(value = "-id", help = "ID отзыва.") long id,
            @ShellOption(value = "-r", help = "Измененный отзыв.") String opinion) {
        return shellUtil.getMessageSimple(controller.updateReview(id, opinion));
    }

    @ShellMethod(value = "Удалить отзыв.", key = {"review-delete", "rd"})
    public String deleteGenre(
            @ShellOption(value = "-id", help = "ID отзыва.") long id) {
        return shellUtil.getMessageSimple(controller.deleteReview(id));
    }

}
