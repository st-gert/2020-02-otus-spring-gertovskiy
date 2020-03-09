package ru.otus.job02.model;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Вопросник.
 * Содержит список вопросов с ответами и метод подведения итога в процентах.
 */
public class Questionnaire {

    private String examName;    // Наименование экзамена - первая строка файла

    private final List<QuestionAnswer<?>> questionsAnswerList = new ArrayList<>();

    /**
     * Доабавление объекта-вопроса.
     */
    public void addQuestionsAnswer(QuestionAnswer<?> questionsAnswer) {
        questionsAnswerList.add(questionsAnswer);
    }

    /**
     * Вычисление процента правильных ответов.
     * @return процент, округленный до целого.
     */
    public int result() {
        long correctAmount = questionsAnswerList
                .stream()
                .filter(QuestionAnswer::isCorrect)
                .count();
        float percent = correctAmount * 100.0F / questionsAnswerList.size();
        return Math.round(percent);
    }

    public List<Triple<String, String, String>> questionWithAnswersList() {
        return questionsAnswerList
                .stream()
                .map(QuestionAnswer::questionWithAnswers)
                .collect(Collectors.toList());
    }


    // generated getters & setters
    public List<QuestionAnswer<?>> getQuestionsAnswerList() {
        return questionsAnswerList;
    }
    public String getExamName() {
        return examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }
}
