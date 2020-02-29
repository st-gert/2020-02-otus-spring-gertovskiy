package ru.otus.job01.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.job01.dao.QuestionnaireCsvParser;
import ru.otus.job01.ui.UserInterfaceScanner;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {
    private static final  ClassPathXmlApplicationContext CTX
            = new ClassPathXmlApplicationContext("/spring-context.xml");

    @Test
    @DisplayName("Проверка бина парсера CSV в контексте")
    public void contextCsvTest() {
        QuestionnaireCsvParser csvParser = (QuestionnaireCsvParser) CTX.getBean("questionnaireDAO");
        assertAll(
                () -> assertNotNull(csvParser)
                , () -> assertEquals("exam-seasons.csv", csvParser.getFileName())
                , () -> assertEquals("windows-1251", csvParser.getCharset())
                , () -> assertEquals(';', csvParser.getSeparator())
        );
    }

    @Test
    @DisplayName("Проверка бинов в контексте")
    public void contextTest() {
        ExamControllerImpl ec = (ExamControllerImpl) CTX.getBean("examController");
        assertAll(
                () -> assertNotNull(ec)
                , () -> assertTrue(ec.getQuestionnaireDAO() instanceof QuestionnaireCsvParser)
                , () -> assertTrue(ec.getUi() instanceof UserInterfaceScanner)
                , () -> assertNotNull(ec.getMarksCriteria())
        );
    }

}
