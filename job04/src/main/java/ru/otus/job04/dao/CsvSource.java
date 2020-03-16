package ru.otus.job04.dao;

import java.io.InputStream;
import java.util.List;

public interface CsvSource {

    // Поиск файла с двумя вариантами имени
    InputStream searchFile(String baseFileName, String suffix);

    // Чтение и парсиг файла.
    // Возврат - список строк, строка - список токенов.
    List<List<String>> readAndParseFile(InputStream is, String charset, char delimiter);

    // Очистка строк от пустых токенов и хвостовых пробелов
    List<List<String>> removeEmptyToken(List<List<String>> tokens);

}
