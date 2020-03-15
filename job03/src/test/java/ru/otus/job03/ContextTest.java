package ru.otus.job03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.job03.dao.QuestionnaireCsvParser;
import ru.otus.job03.service.ExamControllerImpl;
import ru.otus.job03.ui.UserInterfaceScanner;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ContextTest {

    @Autowired
    private ExamControllerImpl examController;

    @Test
    @DisplayName("Проверка бинов в контексте")
    public void contextTest() {
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
