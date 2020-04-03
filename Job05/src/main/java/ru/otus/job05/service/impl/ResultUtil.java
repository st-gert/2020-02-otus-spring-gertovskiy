package ru.otus.job05.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job05.exception.ApplException;

import java.util.List;

/**
 * Общие утилиты обработки полученного от DAO результата.
 */
@Service
class ResultUtil {

    private static final String NO_DATA_FOUND =  "Данные не найдены";
    private static final String DB_ERROR =  "Ошибка базы данных:\n";

    <T> Pair<List<T>, String> handleList(List<T> list) {
        return list.isEmpty()
                ? Pair.of(null, NO_DATA_FOUND)
                : Pair.of(list, null);
    }

    String handleInt(int result) {
        return result == 0 ? NO_DATA_FOUND : null;
    }

    public String handleException(Exception e) {
        return DB_ERROR + e.getMessage();
    }

}
