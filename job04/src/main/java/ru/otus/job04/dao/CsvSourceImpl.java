package ru.otus.job04.dao;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import ru.otus.job04.exception.ApplException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
class CsvSourceImpl implements CsvSource {

    // Поиск файла с двумя вариантами имени
    @Override
    public InputStream searchFile(String baseFileName, String suffix) {
        // определяем имя файла
        int point = baseFileName.lastIndexOf(".");
        String fileName = baseFileName.substring(0, point) + "-" + suffix + baseFileName.substring(point);
        // находим файл
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        if (is == null) {
            is = classLoader.getResourceAsStream(baseFileName);
        }
        if (is == null) {
            throw new ApplException("Файл с вопросами не найден");
        }
        return is;
    }

    // Чтение и парсиг файла.
    // Возврат - список строк, строка - список токенов.
    // Техгология jackson-dataformat-csv
    @Override
    public List<List<String>> readAndParseFile(InputStream is, String charset, char delimiter) {
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

    // Очистка строк от пустых токенов и хвостовых пробелов
    @Override
    public List<List<String>> removeEmptyToken(List<List<String>> tokens) {
        return tokens
                .stream()
                .map(x -> x.stream()
                        .map(String::trim)
                        .filter(t -> !t.isEmpty())
                        .collect(Collectors.toList()))
                .filter(x -> x.size() > 0)
                .collect(Collectors.toList());
    }

}
