package ru.otus.job04.service;

import ru.otus.job04.model.Person;

import java.util.Locale;

/**
 * Сценарий проведения экзамена.
 */
public interface ExamController {

    void setPerson(Person person);

    void execScenario(Locale locale);

}
