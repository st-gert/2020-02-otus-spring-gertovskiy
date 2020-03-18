package ru.otus.job04.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.job04.dao.QuestionnaireDAO;
import ru.otus.job04.dao.QuestionnaireTest;
import ru.otus.job04.model.Person;
import ru.otus.job04.ui.UIExamScenario;

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
@SpringBootTest
@DisplayName("Сценарий экзамена с ограниченным контекстом")
public class ExamControllerExtTest {

    @Configuration                              // Ограничиваем контекст
    static class TestConfiguration {
        @Bean
        public MarksCriteria marksCriteria() {
            MarksCriteria marksCriteria = new MarksCriteria();
            marksCriteria.setExcellent(100);    // поэтому свойства загружаем явно
            marksCriteria.setGood(75);
            marksCriteria.setSatisfactory(50);
            return marksCriteria;               // а @PostConstruct, который обрабатывает свойства, должен отработать
        }
    }

    @Autowired
    private MarksCriteria marksCriteria;

    @MockBean
    private UIExamScenario mockUI;
    @MockBean
    private QuestionnaireDAO mockDao;

    @Test
    public void examTest() {
        // Mock DAO возвращает объект Questionnaire, который должен вычислить результат экзамена.
        when(mockDao.createQuestionnaire(any())).thenReturn(QuestionnaireTest.createQuestionnaire());

        // Mock UI
        // Следующие 4 ответа должны дать процент правильных ответов 50. См. ниже *** Результат экзамена ***
        when(mockUI.askQuestionString(any()))
                .thenReturn("CORRECTANSWER")        // правильно
                .thenReturn("unCorrectAnswer");     // не правильно
        when(mockUI.askQuestionChoice(any(), anyInt()))
                .thenReturn(2)                      // правильно
                .thenReturn(3);                     // не правильно

        // Выполнение сценария экзамена
        ExamController examController = new ExamControllerImpl(mockDao, mockUI, marksCriteria);
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
        inOrder.verify(mockUI).outputResult("Станислав Гертовский", 50, "criteria.satisf");
    }

}
