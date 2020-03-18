package ru.otus.job04.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import ru.otus.job04.model.Person;
import ru.otus.job04.service.ExamController;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Тест команд Spring Shell")
@SpringBootTest
class UIExamShellTest {
    private static String COMMAND_LAN_DEFAULT = "lan";
    private static String COMMAND_LAN_RU = "lan ru";
    private static String COMMAND_LAN_EN = "lan en";
    private static String COMMAND_LAN_ERROR = "lan xx";
    private static String COMMAND_I_AM = "i-am ";
    private static String COMMAND_I_AM_SHORT = "i ";
    private static String COMMAND_EXAM = "exam";
    private static String COMMAND_EXAM_ALT = "start";

    private static Locale LOCALE = new Locale("ru", "RU");
    private static String LOCALE_RU = "ru_RU";
    private static String LOCALE_EN = "en_US";
    private static String LANGUAGE_EN = "English";
    private static String LANGUAGE_RU = "Русский";
    private static Person PERSON = new Person("Станислав", "Гертовский");
    private static String NAME = PERSON.toString();

    @Autowired
    private Shell shell;

    @Autowired
    private UIExamShell examShell;

    @MockBean
    private ExamController mockExamController;

    @Test
    @DisplayName("Команда i-am - ввод имени, изменяет доступность команды старта экзамена")
    void inputPerson() {
        examShell.setFlagPerson(false);
        assertAll(
                () -> assertTrue((shell.evaluate(() -> COMMAND_EXAM)) instanceof CommandNotCurrentlyAvailable)
                , () -> assertTrue(((String) shell.evaluate(() -> COMMAND_I_AM + NAME)).contains(NAME))
                , () -> assertTrue(examShell.isFlagPerson())
                , () -> assertFalse((shell.evaluate(() -> COMMAND_EXAM)) instanceof CommandNotCurrentlyAvailable)
                , () -> assertTrue(((String) shell.evaluate(() -> COMMAND_I_AM_SHORT + NAME)).contains(NAME))
                , () -> verify(mockExamController, times(2)).setPerson(any())
        );
    }

    @Test
    @DisplayName("Команда i-am - ввод имени, передает в метод сценария Person")
    void inputPersonParam() {
        shell.evaluate(() -> COMMAND_I_AM + NAME);
        // для манипуляций с параметрами метода:
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(mockExamController).setPerson(personCaptor.capture());
        assertEquals(PERSON.toString(), personCaptor.getValue().toString());
    }

    @Test
    @DisplayName("Команда lan - изменяет локали")
    void chooseLanguage() {
        examShell.setLocale(null);
        assertAll(
                () -> assertTrue(((String) shell.evaluate(() -> COMMAND_LAN_DEFAULT)).contains(LANGUAGE_EN))
                , () -> assertEquals(LOCALE_EN, examShell.getLocale().toString())
                , () -> assertTrue(((String) shell.evaluate(() -> COMMAND_LAN_RU)).contains(LANGUAGE_RU))
                , () -> assertEquals(LOCALE_RU, examShell.getLocale().toString())
                , () -> assertTrue(((String) shell.evaluate(() -> COMMAND_LAN_EN)).contains(LANGUAGE_EN))
                , () -> assertEquals(LOCALE_EN, examShell.getLocale().toString())
                , () -> assertTrue(((String) shell.evaluate(() -> COMMAND_LAN_RU)).contains(LANGUAGE_RU))
                , () -> assertTrue(((String) shell.evaluate(() -> COMMAND_LAN_ERROR)).contains(LANGUAGE_EN))
                , () -> assertEquals(LOCALE_EN, examShell.getLocale().toString())
        );
    }

    @Test
    @DisplayName("Команда start/exam запускает сценарий проведения экзамена с передачей локали")
    void startExam() {
        examShell.setFlagPerson(true);
        examShell.setLocale(LOCALE);            // Проверяем передачу параметра метод сценария
        shell.evaluate(() -> COMMAND_EXAM);
        shell.evaluate(() -> COMMAND_EXAM_ALT);
        verify(mockExamController, times(2)).execScenario(LOCALE);
    }
}