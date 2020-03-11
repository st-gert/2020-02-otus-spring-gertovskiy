package ru.otus.job03.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job03.model.QuestionAnswerChoice;
import ru.otus.job03.model.QuestionAnswerString;
import ru.otus.job03.model.Questionnaire;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireCsvParserTest {
    private static final String FILE_NAME = "test-exam.csv";
    private static final String CHARSET = "windows-1251";
    private static final char SEPATATOR = ';';
    private static final String LOCALE = "ru_RU";

    private QuestionnaireCsvParser parser;

    @BeforeEach
    void createParcer() {
        parser = new QuestionnaireCsvParser();
        parser.setLocale(LOCALE);
        parser.setBasename(FILE_NAME);
        parser.setCharset(CHARSET);
        parser.setDelimiter(SEPATATOR);
    }

    @Test
    @DisplayName("Поиск файла")
    public void searchFile() {
        InputStream is = parser.searchFile();
        parser.setLocale("xx_XX");
        InputStream is1 = parser.searchFile();
        assertAll(
                () -> assertNotNull(is)
                , () -> assertNotNull(is1)
        );
    }

    @Test
    @DisplayName("Чтение CSV файла, проверка количества строк, кодировки")
    public void loadFileTest() {
        List<List<String>> lines = parser.readAndParseFile(parser.searchFile());
        assertAll(
                () -> assertNotNull(lines)
                , () -> assertEquals(6, lines.size())
                , () -> assertTrue(lines.get(0).get(0).contains("Наименование экзамена"))
        );
    }

    @Test
    @DisplayName("Парсинг строки и очистка от пустых токенов")
    public void removeEmptyTokenTest() {
        List<List<String>> tokens = parser.removeEmptyToken(Arrays.asList(
                Arrays.asList("qwerty ", " 3", " раз", "два", " три", "", "", "", "", ""),
                Arrays.asList("asddfg ", " 3", " раз", "два", " три", "", "", "", "", "")
                ));
        assertAll(
                () -> assertNotNull(tokens)
                , () -> assertEquals(2, tokens.size())
                , () -> assertEquals(5, tokens.get(0).size())
                , () -> assertEquals("qwerty", tokens.get(0).get(0))
                , () -> assertEquals("3", tokens.get(0).get(1))
                , () -> assertEquals("три", tokens.get(0).get(4))
                , () -> assertEquals("asddfg", tokens.get(1).get(0))
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
                , () -> assertEquals("Question1", q.getQuestionsAnswerList().get(0).getQuestion())
                , () -> assertEquals("answer1", q.getQuestionsAnswerList().get(0).getCorrectAnswer())
                , () -> assertTrue(q.getQuestionsAnswerList().get(3) instanceof QuestionAnswerChoice)
                , () -> assertEquals(3, ((QuestionAnswerChoice) q.getQuestionsAnswerList().get(3))
                        .getAnswerVariants().size())
        );
    }

}
