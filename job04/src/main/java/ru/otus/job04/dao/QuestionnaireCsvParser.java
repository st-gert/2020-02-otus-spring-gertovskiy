package ru.otus.job04.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.otus.job04.exception.ApplException;
import ru.otus.job04.model.QuestionAnswer;
import ru.otus.job04.model.QuestionAnswerChoice;
import ru.otus.job04.model.QuestionAnswerString;
import ru.otus.job04.model.Questionnaire;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Загрузка CSV-файла, парсинг и формирование объекта Вопросника.
 */
@Service
@ConfigurationProperties(prefix = "file")
public class QuestionnaireCsvParser implements QuestionnaireDAO {

    // Три параметра приложения из application.yml
    // Загружаются автоматически (@ConfigurationProperties)
    private String basename;
    private String charset;
    private char delimiter;

    private final CsvSource csvSource;

    public QuestionnaireCsvParser(CsvSource csvSource) {
        this.csvSource = csvSource;
    }

    /**
     * Собственно формирование Вопросника
     * @param locale локаль
     */
    @Override
    public Questionnaire createQuestionnaire(Locale locale) {
        // Находим файл
        InputStream is = csvSource.searchFile(basename, locale.toString());
        // Читаем и парсим файл
        List<List<String>> lineTokensPre = csvSource.readAndParseFile(is, charset, delimiter);
        // Очистка от пустых токенов
        List<List<String>> lineTokens = csvSource.removeEmptyToken(lineTokensPre);
        // Формируем вопросник
        Questionnaire questionnaire = new Questionnaire();
        for (List<String> tokens : lineTokens) {
            if (lineTokens.indexOf(tokens) == 0) {
                questionnaire.setExamName(tokens.get(0));
            } else {
                questionnaire.addQuestionsAnswer(createQuestionAnswer(tokens));
            }
        }
        return questionnaire;
    }

    // Формирование объекта с вопросом
    // тип ? - тип ответа пользователя, String или Integer (extends невозможен)
    private QuestionAnswer<?> createQuestionAnswer(List<String> tokens) {
        int size = tokens.size();
        if (size == 2) {
            return new QuestionAnswerString(tokens.get(0), tokens.get(1));
        } else if (size > 2) {
            try {
                return new QuestionAnswerChoice(tokens.get(0),
                        Integer.parseInt(tokens.get(1)), tokens.subList(2, size));
            } catch (NumberFormatException e) {
                throw new ApplException("Ошибка анализа файла с вопросами: нечисловой номер правильного варианта ответа");
            }
        } else {
            throw new ApplException("Ошибка анализа файла с вопросами: в строке заполнено меньше 2 полей");
        }
    }

    // generated getters & setters
    public String getBasename() {
        return basename;
    }
    public void setBasename(String basename) {
        this.basename = basename;
    }
    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public char getDelimiter() {
        return delimiter;
    }
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }
}
