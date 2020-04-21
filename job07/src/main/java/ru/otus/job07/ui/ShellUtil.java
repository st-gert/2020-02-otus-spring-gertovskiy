package ru.otus.job07.ui;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Вспомогательный сервис, утилитные методы формирования ответов команд Shell.
 */
@Service
public class ShellUtil {

    String getMessageSimple(String message) {
        return message != null
                ? "Ошибка:\n" + message
                : "OK";
    }

    String getMessageFromLong(Pair<Long, String> pair) {
        String errorMsg = pair.getRight();
        Long newId = pair.getLeft();
        return errorMsg != null
                ? "Ошибка:\n" + errorMsg
                : (newId != null ? "Новый ID: " + newId : "OK");
    }

    <T> String getMessageFromList(Pair<List<T>, String> pair) {
        String errorMsg = pair.getRight();
        return errorMsg != null
                ? "Ошибка:\n" + errorMsg
                : pair.getLeft()
                        .stream()
                        .map(T::toString)
                        .collect(Collectors.joining("\n"));
    }

}
