package ru.otus.job02.ui;

import org.apache.commons.lang3.tuple.Triple;
import ru.otus.job02.model.Person;

import java.util.List;

public interface UserInterface {

    Person inputPreson();

    void outputBeginingInfo(String examName);

    String askQuestionString(String question);

    int askQuestionChoice(String question, int maxNumer);

    void outputAnswers(List<Triple<String, String, String>> answers);

    void outputResult(String personName, int percent, String mark);

    void close();

}
