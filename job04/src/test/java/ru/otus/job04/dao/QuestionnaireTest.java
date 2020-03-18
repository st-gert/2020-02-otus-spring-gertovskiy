package ru.otus.job04.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job04.model.QuestionAnswer;
import ru.otus.job04.model.QuestionAnswerChoice;
import ru.otus.job04.model.QuestionAnswerString;
import ru.otus.job04.model.Questionnaire;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuestionnaireTest {

    @Test
    @DisplayName("Тест Вопросника")
    public void questionnaireTest() {
        Questionnaire questionnaire = createQuestionnaire();
        // тип ? - тип ответа пользователя, String или Integer (extends невозможен)
        List<QuestionAnswer<?>> list = questionnaire.getQuestionsAnswerList();
        ((QuestionAnswerString)list.get(0)).setUserAnswer("CORRECTANSWER");
        ((QuestionAnswerString)list.get(1)).setUserAnswer("unCorrectAnswer");
        ((QuestionAnswerChoice)list.get(2)).setUserAnswer(2);
        ((QuestionAnswerChoice)list.get(3)).setUserAnswer(3);
        assertAll(
                () -> assertEquals(50, questionnaire.result())
                , () -> assertNotEquals(4, questionnaire.getQuestionsAnswerList())
                , () -> assertNotEquals(4, questionnaire.questionWithAnswersList())
                , () -> assertNull(questionnaire.questionWithAnswersList().get(0).getRight())
                , () -> assertNotNull(questionnaire.questionWithAnswersList().get(1).getRight())
                , () -> assertNull(questionnaire.questionWithAnswersList().get(2).getRight())
                , () -> assertNotNull(questionnaire.questionWithAnswersList().get(3).getRight())
        );
    }

    // public static - для использования в других тестах (ExamControllerTest)
    public static Questionnaire createQuestionnaire() {
        QuestionAnswer<String> qa0 = new QuestionAnswerString("question", "correctAnswer");
        QuestionAnswer<String> qa1 = new QuestionAnswerString("question", "correctAnswer");
        QuestionAnswer<Integer> qa2 = new QuestionAnswerChoice("question", 2,
                Arrays.asList("uncorrected", "correct", "uncorrected"));
        QuestionAnswer<Integer> qa3 = new QuestionAnswerChoice("question", 2,
                Arrays.asList("uncorrected", "correct", "uncorrected"));
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.addQuestionsAnswer(qa0);
        questionnaire.addQuestionsAnswer(qa1);
        questionnaire.addQuestionsAnswer(qa2);
        questionnaire.addQuestionsAnswer(qa3);
        questionnaire.setExamName("Exam name");
        return questionnaire;
    }
}
