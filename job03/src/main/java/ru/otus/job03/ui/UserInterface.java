package ru.otus.job03.ui;

import org.apache.commons.lang3.tuple.Triple;
import ru.otus.job03.model.Person;

import java.util.List;

/**
 * Пользовательский интерфейс.
 */
public interface UserInterface {

    Person inputPerson();

    void outputBeginingInfo(String examName);

    String askQuestionString(String question);

    int askQuestionChoice(String question, int maxNumer);

    void outputAnswers(List<Triple<String, String, String>> answers);

    void outputResult(String personName, int percent, String mark);

    void close();

}
