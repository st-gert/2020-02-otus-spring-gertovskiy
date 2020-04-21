package ru.otus.job07.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job07.exception.ApplDbConstraintException;
import ru.otus.job07.exception.ApplDbNoDataFoundException;

import java.util.List;

/**
 * Общие утилиты обработки полученного от Repository результата.
 */
@Service
class ResultUtil {

    private static final String NO_DATA_FOUND =  "Данные не найдены";
    private static final String CONSTRAINT_ERROR = "Операция запрещена, нарушается целостность данных";
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
        if (e instanceof ApplDbNoDataFoundException) {
            return NO_DATA_FOUND;
        } else if (e instanceof ApplDbConstraintException) {
            return CONSTRAINT_ERROR;
        } else {
            return DB_ERROR + e.getMessage();
        }
    }

}
