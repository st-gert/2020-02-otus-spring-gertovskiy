package ru.otus.job04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.job04.dao.QuestionnaireCsvParser;
import ru.otus.job04.service.ExamControllerImpl;
import ru.otus.job04.ui.UIExamScenarioScanner;
import ru.otus.job04.ui.UIExamShell;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("Проверка бинов в контексте:")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContextTest {

    @Autowired
    private UIExamShell uiExamShell;

    @Test
    @Order(0)
    @DisplayName("начинаем с Shell, который начинает работу, ")
    public void shellTest() {
        assertAll(
                () -> assertNotNull(uiExamShell)
                , () -> assertNotNull(uiExamShell.getExamController())
                , () -> assertTrue(uiExamShell.getExamController() instanceof ExamControllerImpl)
        );
    }

    @Test
    @Order(1)
    @DisplayName("а теперь ExamController")
    public void examControllerTest() {
        ExamControllerImpl examController = (ExamControllerImpl) uiExamShell.getExamController();
        assertAll(
                () -> assertTrue(examController.getQuestionnaireDAO() instanceof QuestionnaireCsvParser)
                , () -> assertTrue(examController.getUiScenario() instanceof UIExamScenarioScanner)
                , () -> assertNotNull(examController.getMarksCriteria())
                , () -> assertNotNull(((UIExamScenarioScanner) examController.getUiScenario()).getMs())
        );
    }

    @Test
    @Order(2)
    @DisplayName("и DAO")
    public void csvParserTest() {
        ExamControllerImpl examController = (ExamControllerImpl) uiExamShell.getExamController();
        QuestionnaireCsvParser parser = (QuestionnaireCsvParser) examController.getQuestionnaireDAO();
        assertAll(
                () -> assertNotNull(parser.getBasename())
                , () -> assertNotNull(parser.getCharset())
                , () -> assertTrue(parser.getDelimiter() > '\u0000')
        );
    }

}
