package ru.otus.job02.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.job02.Main02;
import ru.otus.job02.config.ConfigProps;
import ru.otus.job02.dao.QuestionnaireCsvParser;
import ru.otus.job02.ui.UserInterfaceScanner;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {
    private static final AnnotationConfigApplicationContext CTX = new AnnotationConfigApplicationContext(Main02.class);

    @Test
    @DisplayName("Проверка загрузки application.properties")
    public void configPropsTest() {
        ConfigProps cp = CTX.getBean(ConfigProps.class);
        assertAll(
                () -> assertNotNull(cp)
                , () -> assertNotNull(cp.getLocale())
                , () -> assertEquals(2, cp.getLocale().getLanguage().length())
                , () -> assertNotNull(cp.getFileName())
                , () -> assertNotNull(cp.getFileCharset())
                , () -> assertTrue(cp.getFileSeparator() == ';' || cp.getFileSeparator() == ',')
                , () -> assertTrue(cp.getCriteriaExcel() > 0)
        );
    }

    @Test
    @DisplayName("Проверка бинов в контексте")
    public void contextTest() {
        ExamControllerImpl examController = (ExamControllerImpl) CTX.getBean("examController");
        assertAll(
                () -> assertNotNull(examController)
                , () -> assertTrue(examController.getQuestionnaireDAO() instanceof QuestionnaireCsvParser)
                , () -> assertTrue(examController.getUi() instanceof UserInterfaceScanner)
                , () -> assertNotNull(examController.getMarksCriteria())
                , () -> assertNotNull(((UserInterfaceScanner) examController.getUi()).getMs())
                , () -> assertNotNull(((UserInterfaceScanner) examController.getUi()).getLocale())
        );
    }

}
