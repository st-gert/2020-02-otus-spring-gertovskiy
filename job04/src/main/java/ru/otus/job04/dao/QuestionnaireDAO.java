package ru.otus.job04.dao;

import ru.otus.job04.model.Questionnaire;

import java.util.Locale;

/**
 * Загрузка данных и формирование объекта Вопросника.
 */
public interface QuestionnaireDAO {

    Questionnaire createQuestionnaire(Locale locale);

}
