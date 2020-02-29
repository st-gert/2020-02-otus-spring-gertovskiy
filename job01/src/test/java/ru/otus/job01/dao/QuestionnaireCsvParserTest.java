package ru.otus.job01.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job01.model.Questionnaire;
import ru.otus.job01.model.QuestionAnswerChoice;
import ru.otus.job01.model.QuestionAnswerString;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireCsvParserTest {
    private static final String FILE_NAME = "test-exam.csv";
    private static final String CHARSET = "windows-1251";
    private static final char SEPATATOR = ';';

    private final QuestionnaireCsvParser parser = new QuestionnaireCsvParser(FILE_NAME, CHARSET, SEPATATOR);

    @Test
    @DisplayName("Чтение CSV файла, проверка количества строк, кодировки")
    public void loadFileTest() {
        List<String> lines = parser.loadFile();
        assertAll(
                () -> assertNotNull(lines)
                , () -> assertEquals(6, lines.size())
                , () -> assertTrue(lines.get(0).contains("Наименование экзамена"))
        );
    }

    @Test
    @DisplayName("Парсинг строки и очистка от пустых токенов")
    public void parseLineTest() {
        List<String> tokens = parser.parseLine("qwerty ; 3; раз; два; три;;;;;;;");
        assertAll(
                () -> assertNotNull(tokens)
                , () -> assertEquals(5, tokens.size())
                , () -> assertEquals("qwerty", tokens.get(0))
                , () -> assertEquals("3", tokens.get(1))
                , () -> assertEquals("три", tokens.get(4))
        );
    }

    @Test
    @DisplayName("Формирование Вопросника")
    public void createQuestionnairetest() {
        Questionnaire q = parser.createQuestionnaire();
        assertAll(
                () -> assertNotNull(q)
                , () -> assertTrue(q.getExamName().contains("Наименование экзамена"))
                , () -> assertEquals(5, q.getQuestionsAnswerList().size())
                , () -> assertTrue(q.getQuestionsAnswerList().get(0) instanceof QuestionAnswerString)
//                , () -> assertEquals(QuestionTypeEnum.STRING, q.getQuestionsAnswerList().get(0).getType())
                , () -> assertEquals("Question1", q.getQuestionsAnswerList().get(0).getQuestion())
                , () -> assertEquals("answer1", q.getQuestionsAnswerList().get(0).getCorrectAnswer())
                , () -> assertTrue(q.getQuestionsAnswerList().get(3) instanceof QuestionAnswerChoice)
//                , () -> assertEquals(QuestionTypeEnum.CHOICE, q.getQuestionsAnswerList().get(3).getType())
                , () -> assertEquals(3, ((QuestionAnswerChoice) q.getQuestionsAnswerList().get(3))
                        .getAnswerVariants().size())
        );
    }

}
