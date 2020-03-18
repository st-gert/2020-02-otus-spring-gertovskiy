package ru.otus.job04.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionAnswerTest {

    @Test
    @DisplayName("Вопрос с текстовым ответом")
    public void questionAnswerStringTest() {
        QuestionAnswer<String> qa = new QuestionAnswerString("question", "correctAnswer");
        qa.setUserAnswer("CORRECTANSWER");
        assertAll(
                () -> assertEquals("question", qa.getQuestion())
                , () -> assertEquals("question", qa.questionToString().getLeft())
                , () -> assertNull(qa.questionToString().getRight())
                , () -> assertEquals("correctAnswer", qa.getCorrectAnswer())
                , () -> assertEquals("correctAnswer", qa.getCorrectAnswerString())
                , () -> assertEquals("CORRECTANSWER", qa.getUserAnswer())
                , () -> assertEquals("CORRECTANSWER", qa.getUserAnswerString())
                , () -> assertTrue(qa.isCorrect())
                , () -> assertEquals("question", qa.questionWithAnswers().getLeft())
                , () -> assertEquals("CORRECTANSWER", qa.questionWithAnswers().getMiddle())
                , () -> assertNull(qa.questionWithAnswers().getRight())
        );
        qa.setUserAnswer("unCorrectAnswer");
        assertAll(
                () -> assertFalse(qa.isCorrect())
                , () -> assertEquals("question", qa.questionWithAnswers().getLeft())
                , () -> assertEquals("unCorrectAnswer", qa.questionWithAnswers().getMiddle())
                , () -> assertEquals("correctAnswer", qa.questionWithAnswers().getRight())
        );
    }

    @Test
    @DisplayName("Вопрос с выбором ответа")
    public void questionAnswerChoiceTest() {
        QuestionAnswer<Integer> qa = new QuestionAnswerChoice("question", 2,
                Arrays.asList("uncorrected", "correct", "uncorrected"));
        qa.setUserAnswer(2);
        assertAll(
                () -> assertEquals("question", qa.getQuestion())
                , () -> assertTrue(qa.questionToString().getLeft().startsWith("question"))
                , () -> assertTrue(qa.questionToString().getLeft().contains("correct"))
                , () -> assertTrue(qa.questionToString().getLeft().contains("uncorrect"))
                , () -> assertEquals(3, qa.questionToString().getRight())
                , () -> assertEquals(2, qa.getCorrectAnswer())
                , () -> assertEquals("correct", qa.getCorrectAnswerString())
                , () -> assertEquals(2, qa.getUserAnswer())
                , () -> assertEquals("correct", qa.getUserAnswerString())
                , () -> assertTrue(qa.isCorrect())
                , () -> assertEquals("question", qa.questionWithAnswers().getLeft())
                , () -> assertEquals("correct", qa.questionWithAnswers().getMiddle())
                , () -> assertNull(qa.questionWithAnswers().getRight())
        );
        qa.setUserAnswer(3);
        assertAll(
                () -> assertFalse(qa.isCorrect())
                , () -> assertEquals("question", qa.questionWithAnswers().getLeft())
                , () -> assertEquals("uncorrected", qa.questionWithAnswers().getMiddle())
                , () -> assertEquals("correct", qa.questionWithAnswers().getRight())
        );
    }

}
