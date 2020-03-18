package ru.otus.job04.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Обработка CSV файла")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CsvSourceTest {
    private static final String FILE_NAME = "test-exam.csv";
    private static final String CHARSET = "windows-1251";
    private static final char SEPATATOR = ';';
    private static final Locale LOCALE = new Locale("ru", "RU");

    private CsvSource csvSource = new CsvSourceImpl();

    @Test
    @Order(0)
    @DisplayName("Поиск файла CSV файла")
    void searchFile() {
        InputStream is = csvSource.searchFile(FILE_NAME, LOCALE.toString());
        InputStream is1 = csvSource.searchFile(FILE_NAME, "xx_XX");
        assertAll(
                () -> assertNotNull(is)
                , () -> assertNotNull(is1)
        );
    }

    @Test
    @Order(1)
    @DisplayName("Чтение файла, проверка количества строк, кодировки")
    void readAndParseFile() {
        List<List<String>> lines = csvSource.readAndParseFile(
                csvSource.searchFile(FILE_NAME, LOCALE.toString()),
                CHARSET,
                SEPATATOR);
        assertAll(
                () -> assertNotNull(lines)
                , () -> assertEquals(6, lines.size())
                , () -> assertTrue(lines.get(0).get(0).contains("Наименование экзамена"))
        );
    }

    @Test
    @Order(2)
    @DisplayName("Парсинг строки и очистка от пустых токенов")
    void removeEmptyToken() {
        List<List<String>> tokens = csvSource.removeEmptyToken(Arrays.asList(
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

}