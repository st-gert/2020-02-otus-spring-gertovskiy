package ru.otus.job03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.job03.dao.QuestionnaireDAO;
import ru.otus.job03.model.Person;
import ru.otus.job03.service.ExamController;
import ru.otus.job03.service.ExamControllerImpl;
import ru.otus.job03.service.MarksCriteria;
import ru.otus.job03.ui.UserInterface;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Тестирование класса ExamControllerImpl - сценарий экзамена.
 * <p>
 * Поскольку класс имеет только один метод void, проверяем результат его работы на стороне UI.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExamControllerTestExt {

    @MockBean
    private UserInterface mockUI;
    @MockBean
    private QuestionnaireDAO mockDao;

    @Autowired
    private MarksCriteria marksCriteria;    // MarksCriteria используем "настоящий", он работает быстро.

    @Test
    @DisplayName("Сценарий экзамена - как бы интеграционный")
    public void examTest() {
        // Mock DAO возвращает объект Questionnaire, который должен вычислить результат экзамена.
        when(mockDao.createQuestionnaire()).thenReturn(QuestionnaireTest.createQuestionnaire());

        // Mock UI
        // Проверяем класс Person и возврат его значения в UI. См. ниже *** Результат экзамена ***
        when(mockUI.inputPerson())
                .thenReturn(new Person("Станислав", "Гертовский"));
        // Следующие 4 ответа должны дать процент правильных ответов 50. См. ниже *** Результат экзамена ***
        when(mockUI.askQuestionString(any()))
                .thenReturn("CORRECTANSWER")        // правильно
                .thenReturn("unCorrectAnswer");     // не правильно
        when(mockUI.askQuestionChoice(any(), anyInt()))
                .thenReturn(2)                      // правильно
                .thenReturn(3);                     // не правильно

        // Выполнение сценария экзамена
        ExamController examController = new ExamControllerImpl(mockDao, mockUI, marksCriteria);
        examController.execScenario();

        // Проверки на стороне UI.
        // Проверяем порядок вызова методов и значения результата экзамена.
        InOrder inOrder = inOrder(mockUI);
        inOrder.verify(mockUI).inputPerson();
        inOrder.verify(mockUI).outputBeginingInfo("Exam name");     // Вопросник реальный! :)
        inOrder.verify(mockUI, times(2)).askQuestionString(any());
        inOrder.verify(mockUI, times(2)).askQuestionChoice(any(), anyInt());
        inOrder.verify(mockUI).outputAnswers(any());
        // *** Результат экзамена ***
        inOrder.verify(mockUI).outputResult("Станислав Гертовский", 50, "criteria.satisf");
        inOrder.verify(mockUI).close();
    }

}
