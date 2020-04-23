package ru.otus.job08.ui;

import org.springframework.shell.Input;
import ru.otus.job08.exception.ApplException;

import java.util.Arrays;
import java.util.List;

/**
 * Вынужденная замена реализации Input.
 *
 * Метод
 * default List<String> words() {
 *     return "".equals(rawText()) ? Collections.emptyList() : Arrays.asList(rawText().split(" "));
 * }
 * не учитывает, что параметры могут быть с пробелом внутри.
 *
 * А господину @author Eric Bottard - двойка с минусом!
 */
class FixedInput implements Input {

    private final String[] wordArray;

    private FixedInput(String[] wordArray) {
        this.wordArray = wordArray;
    }

    @Override
    public String rawText() {
        throw new ApplException("Нереализованный метод Input.rawText");
    }

    @Override
    public List<String> words() {
        return Arrays.asList(wordArray);
    }

    static Input of(String ... word) {
        return new FixedInput(word);
    }

}
