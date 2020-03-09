package ru.otus.job02.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVUtilsTest {
    private static final char SEPATATOR = ';';
    private static final String LINE = "\"Exam name; \"\"Exam name\"\", Наименование экзамена\";;;;;;\n";

    @Test
    @DisplayName("Проверка парсера строки от mkyong")
    public void lineParserTest() {
        List<String> tokens = CSVUtils.parseLine(LINE, SEPATATOR);
        assertAll(
                () -> assertEquals(7, tokens.size()),
                () -> assertTrue(tokens.get(0).contains(String.valueOf(SEPATATOR))),
                () -> assertTrue(tokens.get(0).contains("\"")),
                () -> assertTrue(tokens.get(1).isEmpty()),
                () -> assertTrue(tokens.get(6).isEmpty())
        );
    }

}
