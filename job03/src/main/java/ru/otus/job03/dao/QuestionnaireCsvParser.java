package ru.otus.job03.dao;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.otus.job03.exception.ApplException;
import ru.otus.job03.model.QuestionAnswer;
import ru.otus.job03.model.QuestionAnswerChoice;
import ru.otus.job03.model.QuestionAnswerString;
import ru.otus.job03.model.Questionnaire;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Загрузка CSV-файла, парсинг и формирование объекта Вопросника.
 */
@Service
@ConfigurationProperties(prefix = "file")
public class QuestionnaireCsvParser implements QuestionnaireDAO {

    // Четыре параметра приложения из application.yml
    // Загружаются автоматически (@ConfigurationProperties)
    private String locale;
    private String basename;
    private String charset;
    private char delimiter;

    /**
     * Собственно формирование Вопросника
     */
    @Override
    public Questionnaire createQuestionnaire() {
        // Находим файл
        InputStream is = searchFile();
        // Читаем и парсим файл
        List<List<String>> lineTokensPre = readAndParseFile(is);
        // Очистка от пустых токенов
        List<List<String>> lineTokens = removeEmptyToken(lineTokensPre);
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

    // Читаем и парсим файл. Техгология jackson-dataformat-csv
    // видимость package - для тестирования
    List<List<String>> readAndParseFile(InputStream is) {
        CsvSchema schema = CsvSchema.emptySchema()
                .withColumnSeparator(delimiter);
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        MappingIterator<List<String>> it;
        try {
            it = mapper.readerFor(List.class)
                    .with(schema)
                    .readValues(new InputStreamReader(is, charset));
        } catch (IOException e) {
            throw new ApplException("Ошибка чтения и парсинга файла с вопросами");
        }
        List<List<String>> result = new ArrayList<>();
        StreamSupport       // См. https://stackoverflow.com/questions/24511052/how-to-convert-an-iterator-to-a-stream
                .stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false)
                .forEach(result::add);
        return result;
    }

    // Находим файл
    // видимость package - для тестирования
    InputStream searchFile() {
        // определяем имя файла
        int point = basename.lastIndexOf(".");
        String fileName = basename.substring(0, point) + "-" + locale + basename.substring(point);
        // находим файл
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        if (is == null) {
            is = classLoader.getResourceAsStream(basename);
        }
        if (is == null) {
            throw new ApplException("Файл с вопросами не найден");
        }
        return is;
    }

    // Очистка от пустых токенов, хвостовых пробелов
    // видимость package - для тестирования
    List<List<String>> removeEmptyToken(List<List<String>> tokensPre) {
        return tokensPre
                .stream()
                .map(x -> x.stream()
                        .map(String::trim)
                        .filter(t -> !t.isEmpty())
                        .collect(Collectors.toList()))
                .filter(x -> x.size() > 0)
                .collect(Collectors.toList());
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
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
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
