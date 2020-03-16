package ru.otus.job04.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.job04.dao.QuestionnaireDAO;
import ru.otus.job04.dao.QuestionnaireTest;
import ru.otus.job04.model.Person;
import ru.otus.job04.ui.UIExamScenario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Тестирование класса ExamControllerImpl - сценарий экзамена.
 *
 * Поскольку класс имеет только один метод void, проверяем результат его работы на стороне UI.
 */
@DisplayName("Сценарий экзамена")
public class ExamControllerTest {

    @Test
    public void examTest() {
        // Mock DAO. Возвращает объект Questionnaire, который должен вычислить результат экзамена.
        QuestionnaireDAO mockDao = mock(QuestionnaireDAO.class);
        when(mockDao.createQuestionnaire(any())).thenReturn(QuestionnaireTest.createQuestionnaire());

        // Mock MarksCriteria
        MarksCriteria mockMC = mock(MarksCriteria.class);
        // Проверяем вызов метода calcMark и передачу результата в UI. См. ниже *** Результат экзамена ***
        when(mockMC.calcMark(anyInt())).thenReturn("OK!");

        // Mock UI
        UIExamScenario mockUI = mock(UIExamScenario.class);
        // Следующие 4 ответа должны дать процент правильных ответов 50. См. ниже *** Результат экзамена ***
        when(mockUI.askQuestionString(any()))
                .thenReturn("CORRECTANSWER")        // правильно
                .thenReturn("unCorrectAnswer");     // не правильно
        when(mockUI.askQuestionChoice(any(), anyInt()))
                .thenReturn(2)                      // правильно
                .thenReturn(3);                     // не правильно

        // Выполнение сценария экзамена
        ExamController examController = new ExamControllerImpl(mockDao, mockUI, mockMC);
        // Проверяем возврат значения Person в UI. См. ниже *** Результат экзамена ***
        examController.setPerson(new Person("Станислав", "Гертовский"));
        examController.execScenario(any());

        // Проверки на стороне UI.
        // Проверяем порядок вызова методов и значения результата экзамена.
        InOrder inOrder = inOrder(mockUI);
        inOrder.verify(mockUI).outputBeginingInfo("Exam name");     // Вопросник реальный! :)
        inOrder.verify(mockUI, times(2)).askQuestionString(any());
        inOrder.verify(mockUI, times(2)).askQuestionChoice(any(), anyInt());
        inOrder.verify(mockUI).outputAnswers(any());
        // *** Результат экзамена ***
        inOrder.verify(mockUI).outputResult("Станислав Гертовский", 50, "OK!");
    }

}
