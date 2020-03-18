package ru.otus.job04.ui;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.job04.model.Person;
import ru.otus.job04.service.ExamController;

import javax.annotation.PostConstruct;
import java.util.Locale;

@ShellComponent
public class UIExamShell {

    private final ExamController examController;

    private Locale locale = new Locale("en", "US");     // Значение по умолчанию
    private boolean flagPerson;

    public UIExamShell(ExamController examController) {
        this.examController = examController;
    }

    // Вывод текста до первой команды
    @PostConstruct
    void execIntroduction() {
        System.out.println("Please introduce yourself. Enter your first name and last name.\n"
                + "  Use the command 'i-am'/'i'.\n"
                + "You can choose the exam language, Russian (ru) or English (en).\n"
                + "  Use the command 'lan ru' or 'lan en' (English by default).\n");
    }

    // Ввод имени экзаменующегося, вывод наименования экзамена и первого вопроса
    @ShellMethod(value = "Introduce yourself; enter your first name and last name.", key = {"i-am", "i"})
    public String inputPerson(
            @ShellOption("-fn") String firstName,
            @ShellOption("-ln") String lastName) {
        // Передаем в систему перс.данные
        Person person = new Person(firstName, lastName);
        examController.setPerson(person);
        flagPerson = true;
        return "Examinee: " + person.toString() + "\n" + localeToString();
    }

    // Выбор языка
    @ShellMethod(value = "Choose exam language, ru or en.", key = "lan")
    public String chooseLanguage(@ShellOption(value = "-l", defaultValue = "en") String language) {
        locale = "ru".equalsIgnoreCase(language)
                ? new Locale("ru", "RU")
                : new Locale("en", "US");
        return localeToString();
    }

    private String localeToString() {
        return "Exam language: " +
                ("ru".equals(locale.getLanguage())
                        ? "Русский"
                        : "English");
    }

    // Экзамен
    @ShellMethod(value = "Start exam.", key = {"exam", "start"})
    public String startExam() {
        examController.execScenario(locale);
        return "\nUse command 'exit' or 'quit' to exit.";
    }

    public Availability startExamAvailability() {
        return flagPerson
                ? Availability.available()
                : Availability.unavailable("Please introduce yourself");
    }

    // generated getters
    public ExamController getExamController() {
        return examController;
    }
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public boolean isFlagPerson() {
        return flagPerson;
    }
    public void setFlagPerson(boolean flagPerson) {
        this.flagPerson = flagPerson;
    }
}
