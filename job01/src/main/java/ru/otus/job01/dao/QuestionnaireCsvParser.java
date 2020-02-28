package ru.otus.job01.dao;

import ru.otus.job01.exception.ApplException;
import ru.otus.job01.model.QuestionAnswerChoice;
import ru.otus.job01.model.QuestionAnswerString;
import ru.otus.job01.model.Questionnaire;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Загрузка CSV-файла, парсинг и формирование объекта Вопросника.
 */
public class QuestionnaireCsvParser implements QuestionnaireDAO {

    private final String fileName;
    private final String charset;
    private final char separator;

    public QuestionnaireCsvParser(String fileName, String charset, char separator) {
        this.fileName = fileName;
        this.charset = charset;
        this.separator = separator;
    }

    /**
     * Собственно формирование Вопросника
     */
    @Override
    public Questionnaire createQuestionnaire() {
        // Читаем строки файла
        List<String> lines = loadFile();
        // Преобразуем строки
        Questionnaire questionnaire = new Questionnaire();
        for (String line : lines) {
            List<String> tokens = parseLine(line);
            int size = tokens.size();
            if (lines.indexOf(line) == 0) {
                questionnaire.setExamName(tokens.get(0));
            } else if (size == 2) {
                questionnaire.addQuestionsAnswer(new QuestionAnswerString(tokens.get(0), tokens.get(1)));
            } else if (size > 2) {
                try {
                    questionnaire.addQuestionsAnswer(new QuestionAnswerChoice(tokens.get(0),
                            Integer.parseInt(tokens.get(1)), tokens.subList(2, size)));
                } catch (NumberFormatException e) {
                    throw new ApplException("Ошибка анализа файла с вопросами: нечисловой номер правильного варианта ответа");
                }
            } else if (size == 1) {
                throw new ApplException("Ошибка анализа файла с вопросами: в строке заполнено только 1 поле");
            }
        }
        return questionnaire;
    }

    // Загрузка строк файла
    List<String> loadFile() {
        // находим файл
        URL url = this.getClass().getClassLoader().getResource(fileName);
        if (url == null) {
            throw new ApplException("Файл с вопросами не найден");
        }
        // читаем строки
        try {
            URI uri = url.toURI();
            return Files.readAllLines(Paths.get(uri), Charset.forName(charset));
        } catch (IOException | URISyntaxException e) {
            throw new ApplException("Ошибка чтения файла с вопросами");
        }
    }

    // Парсим строку и очищаем от пустых токенов
    List<String> parseLine(String line) {
        return CSVUtils.parseLine(line, separator)
                .stream()
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
    }


    // generated getters & setters
    public String getFileName() {
        return fileName;
    }
    public String getCharset() {
        return charset;
    }
    public char getSeparator() {
        return separator;
    }
}
