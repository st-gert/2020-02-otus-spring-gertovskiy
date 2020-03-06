package ru.otus.job02.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.job02.config.ConfigProps;
import ru.otus.job02.model.Questionnaire;
import ru.otus.job02.model.QuestionAnswerChoice;
import ru.otus.job02.model.QuestionAnswerString;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireCsvParserTest {
    private static final String FILE_NAME = "test-exam.csv";
    private static final String CHARSET = "windows-1251";
    private static final char SEPATATOR = ';';
    private static final Locale LOCALE = new Locale("ru", "RU");

    private QuestionnaireCsvParser parser;

    @BeforeEach
    void createParcer() {
        ConfigProps configProps = new ConfigProps();
        configProps.setFileName(FILE_NAME);
        configProps.setFileCharset(CHARSET);
        configProps.setFileSeparator(SEPATATOR);
        configProps.setLocale(LOCALE);
        parser = new QuestionnaireCsvParser(configProps);
    }

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
