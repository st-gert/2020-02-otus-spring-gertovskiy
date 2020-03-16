package ru.otus.job04.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job04.model.QuestionAnswerChoice;
import ru.otus.job04.model.QuestionAnswerString;
import ru.otus.job04.model.Questionnaire;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Формирование Вопросника")
public class QuestionnaireCsvParserTest {
    private static final String FILE_NAME = "test-exam.csv";
    private static final String CHARSET = "windows-1251";
    private static final char SEPATATOR = ';';
    private static final Locale LOCALE = new Locale("ru", "RU");

    private QuestionnaireCsvParser parser;

    @BeforeEach
    void createParcer() {
        parser = new QuestionnaireCsvParser(new CsvSourceImpl());
        parser.setBasename(FILE_NAME);
        parser.setCharset(CHARSET);
        parser.setDelimiter(SEPATATOR);
    }

    @Test
    @DisplayName("Все вместе, собственно формирование")
    public void createQuestionnairetest() {
        Questionnaire q = parser.createQuestionnaire(LOCALE);
        assertAll(
                () -> assertNotNull(q)
                , () -> assertTrue(q.getExamName().contains("Наименование экзамена"))
                , () -> assertEquals(5, q.getQuestionsAnswerList().size())
                , () -> assertTrue(q.getQuestionsAnswerList().get(0) instanceof QuestionAnswerString)
                , () -> assertEquals("Question1", q.getQuestionsAnswerList().get(0).getQuestion())
                , () -> assertEquals("answer1", q.getQuestionsAnswerList().get(0).getCorrectAnswer())
                , () -> assertTrue(q.getQuestionsAnswerList().get(3) instanceof QuestionAnswerChoice)
                , () -> assertEquals(3, ((QuestionAnswerChoice) q.getQuestionsAnswerList().get(3))
                        .getAnswerVariants().size())
        );
    }

}
