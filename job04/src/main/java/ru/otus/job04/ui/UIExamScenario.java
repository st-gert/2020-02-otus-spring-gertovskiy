package ru.otus.job04.ui;

import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Locale;

/**
 * Пользовательский интерфейс.
 */
public interface UIExamScenario {

    void setLocale(Locale locale);

    void outputBeginingInfo(String examName);

    String askQuestionString(String question);

    int askQuestionChoice(String question, int maxNumer);

    void outputAnswers(List<Triple<String, String, String>> answers);

    void outputResult(String personName, int percent, String mark);

}
